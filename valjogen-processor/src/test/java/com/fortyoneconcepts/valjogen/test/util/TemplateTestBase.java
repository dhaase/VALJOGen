/*
* Copyright (C) 2014 41concepts Aps
*/
package com.fortyoneconcepts.valjogen.test.util;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;

import com.fortyoneconcepts.valjogen.annotations.VALJOConfigure;
import com.fortyoneconcepts.valjogen.annotations.VALJOGenerate;
import com.fortyoneconcepts.valjogen.model.Clazz;
import com.fortyoneconcepts.valjogen.model.Configuration;
import com.fortyoneconcepts.valjogen.model.ConfigurationDefaults;
import com.fortyoneconcepts.valjogen.model.ConfigurationOptionKeys;
import com.fortyoneconcepts.valjogen.model.util.AnnotationProxyBuilder;
import com.fortyoneconcepts.valjogen.processor.ClazzFactory;
import com.fortyoneconcepts.valjogen.processor.ResourceLoader;
import com.fortyoneconcepts.valjogen.processor.STCodeWriter;
import com.google.testing.compile.CompilationRule;

/**
 * Common superclass for template-based tests.Derived tests are not supposed to check for compilation errors or that names are qualified correctly. This should be covered by the seperate tests that
 * checks if source will compile.
 *
 * As we use javax.lang.model which we do not normally have access to at runtime in Java 1.8 we use javax.lang.model magic proxies supplied by com.google.testing.compile library.
 *
 * Known problems:
 * No tests for javaDocs as reading javaDocs from source in tests not working due to limitation in google com.google.testing library v0.5.
 *
 * @author mmc
 */
public abstract class TemplateTestBase
{
	private final static Logger LOGGER = Logger.getLogger(TemplateTestBase.class.getName());

	@Rule
	public CompilationRule compilationRule = new CompilationRule();

	private Types types;
	private Elements elements;
	private ClazzFactory clazzFactory;

	protected AnnotationProxyBuilder<VALJOGenerate> generateAnnotationBuilder;
	protected AnnotationProxyBuilder<VALJOConfigure> configureAnnotationBuilder;
	protected Map<String,String> configurationOptions;
	protected static final String generatedPackageName = "testPackage";
	protected static final String generatedClassName = "TestImpl";

	private final Logger parentLogger;

	public TemplateTestBase() {
		super();
		parentLogger = Logger.getLogger(ConfigurationDefaults.TOP_PACKAGE_NAME);

		// Set a tempoary default log level until configuration has been read.
   	   parentLogger.setLevel(Level.INFO);
	}

	@Before
	public void init() throws URISyntaxException {
		types = Objects.requireNonNull(compilationRule.getTypes());
		elements = Objects.requireNonNull(compilationRule.getElements());

		generateAnnotationBuilder = new AnnotationProxyBuilder<VALJOGenerate>(VALJOGenerate.class);
		configureAnnotationBuilder = new AnnotationProxyBuilder<VALJOConfigure>(VALJOConfigure.class);

		clazzFactory = Objects.requireNonNull(ClazzFactory.getInstance());

		configurationOptions = new HashMap<String,String>();
	}

	public final class Output
	{
		public final String code;
		public final List<String> warnings;
		public final List<String> errors;

		public Output(String code, List<String> warnings, List<String> errors)
		{
			this.code=Objects.requireNonNull(code);
			this.warnings=Objects.requireNonNull(warnings);
			this.errors=Objects.requireNonNull(errors);
		}
	}

	protected Output produceOutput(Class<?> sourceClass) throws Exception {
		return produceOutput(sourceClass, generateAnnotationBuilder.add(ConfigurationOptionKeys.name, generatedPackageName+"."+generatedClassName).build(), configureAnnotationBuilder.build(), false);
	}

	protected Output produceOutput(Class<?> sourceClass, VALJOConfigure configureAnnotation) throws Exception {
		return produceOutput(sourceClass, generateAnnotationBuilder.add(ConfigurationOptionKeys.name, generatedPackageName+"."+generatedClassName).build(), configureAnnotation, false);
	}

	protected Output produceOutput(Class<?> sourceClass, VALJOGenerate generateAnnotation, VALJOConfigure configureAnnotation) throws Exception
	{
		return produceOutput(sourceClass, generateAnnotation, configureAnnotation, false);
	}

	protected Output produceOutput(Class<?> sourceClass, VALJOGenerate generateAnnotation, VALJOConfigure configureAnnotation, boolean allowErrors) throws Exception
	{
		Configuration configuration = new Configuration(generateAnnotation, configureAnnotation, Locale.ENGLISH, configurationOptions);

		 // Know that we know what proper log level to set, do set it correctly.
	    parentLogger.setLevel(configuration.getLogLevel());

		TypeElement interfaceElement = elements.getTypeElement(sourceClass.getCanonicalName());
		PackageElement packageElement = (PackageElement)(interfaceElement.getEnclosingElement());

		String sourcePackageElementPath = packageElement.toString().replace('.', '/');
		ResourceLoader resourceLoader = new ResourceLoader(TestClassConstants.relSourcePath, sourcePackageElementPath);

		List<String> warnings = new ArrayList<String>();
		List<String> errors = new ArrayList<String>();

		Clazz clazz = clazzFactory.createClazz(types, elements, interfaceElement, configuration, (megElement, kind, message) ->
		  {
			if (kind==Kind.ERROR) {
			  errors.add(message);
			  if (!allowErrors) {
	  			  LOGGER.severe(message);
				  Assert.fail(message);
			  }
			} else if (kind==Kind.WARNING || kind==Kind.MANDATORY_WARNING) {
			  warnings.add(message);
			  LOGGER.warning(message);
			} else LOGGER.info(message);
		  },
		  resourceLoader
		);

		LOGGER.info(() -> "VALJOGen ClazzFactory GENERATED CLAZZ MODEL INSTANCE: "+System.lineSeparator()+clazz.toString());

		STCodeWriter codeWriter = new STCodeWriter(resourceLoader);

		String output = codeWriter.outputClass(clazz, configuration);

		Assert.assertNotNull("template output should not be null", output);

		// Since we are generating output without the annotation processor, let's do our own debug output if enabled.
    	LOGGER.info("VALJOGen STCodeWriter GENERATED CONTENT: "+System.lineSeparator()+output);

	    output=output.replace('\r', ' ').replace('\n', ' ');
	    output=output.replaceAll("\\s+", " ");
	    output=output.trim();

		return new Output(output, warnings, errors);
	}
}