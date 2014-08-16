package com.fortyoneconcepts.valjogen.test;

import org.junit.Test;

import com.fortyoneconcepts.valjogen.model.*;
import com.fortyoneconcepts.valjogen.model.util.NamesUtil;
import com.fortyoneconcepts.valjogen.test.input.*;
import com.fortyoneconcepts.valjogen.test.util.TemplateTestBase;

import static com.fortyoneconcepts.valjogen.test.util.TestSupport.*;

/**
 * Stubbed integration test of StringTemplate generation of code related to the overall class itself (not the methods/members/properties inside). See {@link TemplateTestBase} for general
 * words about template tests.
 *
 * @author mmc
 */
public class TemplateClassTest extends TemplateTestBase
{
	@Test
	public void testImmutableAsFinal() throws Exception
	{
		String output = produceOutput(ImmutableInterface.class);
	    assertContains("final class "+generatedClassName, output);
	}

	@Test
	public void testImmutableAsNonAbstract() throws Exception
	{
		String output = produceOutput(ImmutableInterface.class);
		assertNotContainsWithWildcards("abstract class "+generatedClassName, output);
	}

	@Test
	public void testUnknownNonPropertyMethodsAsAbstractClass() throws Exception
	{
		String output = produceOutput(InterfaceWithNonPropertyMethods.class);
		assertContainsWithWildcards("abstract class "+generatedClassName, output);
	}

	@Test
	public void testPublicClassByDefault() throws Exception
	{
		String output = produceOutput(ImmutableInterface.class);
		assertContainsWithWildcards("public * class "+generatedClassName, output);
	}

	@Test
	public void testPrivateClassCanBeSet() throws Exception
	{
		configurationOptions.put(ConfigurationOptionKeys.clazzScope, "private");
		String output = produceOutput(ImmutableInterface.class);
		assertContainsWithWildcards("private * class "+generatedClassName, output);
	}

	@Test
	public void testGenerateClassNameWhenNotSet() throws Exception
	{
		String output = produceOutput(ImmutableInterface.class, generateAnnotationBuilder.build(), configureAnnotationBuilder.build());
		assertContainsWithWildcards("class "+ImmutableInterface.class.getSimpleName().replace("Interface", "")+NamesUtil.ImplClassSuffix, output);
	}

	@Test
	public void testImplementsInterface() throws Exception
	{
		String output = produceOutput(ImmutableInterface.class);
		assertContainsWithWildcards("class "+generatedClassName+" implements *"+ImmutableInterface.class.getSimpleName(), output);
	}

	@Test
	public void testImplementsGenericInterface() throws Exception
	{
		String output = produceOutput(GenericInterface.class);
		assertContainsWithWildcards("class "+generatedClassName+" implements *GenericInterface<T>", output);
	}
}
