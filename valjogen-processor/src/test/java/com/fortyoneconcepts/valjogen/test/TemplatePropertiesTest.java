/*
* Copyright (C) 2014 41concepts Aps
*/
package com.fortyoneconcepts.valjogen.test;

import org.junit.Test;

import com.fortyoneconcepts.valjogen.model.ConfigurationDefaults;
import com.fortyoneconcepts.valjogen.model.ConfigurationOptionKeys;
import com.fortyoneconcepts.valjogen.test.input.*;
import com.fortyoneconcepts.valjogen.test.util.TemplateTestBase;

import static com.fortyoneconcepts.valjogen.test.util.TestSupport.*;

/**
 * Stubbed integration test of StringTemplate generation of code related to the properties (methods) inside the class. See {@link TemplateTestBase} for general
 * words about template tests.
 *
 * @author mmc
 */
public class TemplatePropertiesTest extends TemplateTestBase
{
	@Test
	public void testPublicGetter() throws Exception
	{
		Output output = produceOutput(ImmutableInterface.class);
		assertContainsWithWildcards("public * int getIntValue() { return intValue; }", output.code);
	}

	@Test
	public void testPublicImmutableSetter() throws Exception
	{
		Output output = produceOutput(ImmutableInterface.class);

		assertContainsWithWildcards("public * ImmutableInterface setObjectValue(final Object objectValue) { return new TestImpl(this.intValue, objectValue); }", output.code);
	}

	@Test
	public void testCustomSetterAndGetter() throws Exception
	{
		Output output = produceOutput(CustomNamedPropertiesInterface.class,
				                      generateAnnotationBuilder.build(),
				                      configureAnnotationBuilder.add(ConfigurationOptionKeys.getterPrefixes, new String[] { "should"})
				                      .add(ConfigurationOptionKeys.setterPrefixes, new String[] { "with"})
				                      .build());

		assertContainsWithWildcards("boolean shouldRequire() { return require; }", output.code);
		assertContainsWithWildcards(CustomNamedPropertiesInterface.class.getSimpleName()+" withRequire(final boolean require) { return new CustomNamedPropertiesImpl(require); }", output.code);
	}

	@Test
	public void testPublicMutableSetter() throws Exception
	{
		configurationOptions.put(ConfigurationDefaults.OPTION_QUALIFIER+ConfigurationOptionKeys.ensureNotNullEnabled, "false");

		Output output = produceOutput(MutableInterface.class);

		assertContainsWithWildcards("public * setObjectValue(final Object objectValue) { this.objectValue=objectValue; }", output.code);
	}

	@Test
	public void testMutableGuardedSetter() throws Exception
	{
		configurationOptions.put(ConfigurationDefaults.OPTION_QUALIFIER+ConfigurationOptionKeys.ensureNotNullEnabled, "true");
		Output output = produceOutput(MutableInterface.class);

		assertContainsWithWildcards("setObjectValue(final Object objectValue) { this.objectValue=Objects.requireNonNull(objectValue); }", output.code);
	}

	@Test
	public void testMutableNotGuardedSetter() throws Exception
	{
		configurationOptions.put(ConfigurationDefaults.OPTION_QUALIFIER+ConfigurationOptionKeys.ensureNotNullEnabled, "false");
		Output output = produceOutput(MutableInterface.class);

		assertContainsWithWildcards("setObjectValue(final Object objectValue) { this.objectValue=objectValue; }", output.code);
	}

	@Test
	public void testGenricImmutableSetter() throws Exception
	{
		Output output = produceOutput(GenericInterface.class);

		assertContainsWithWildcards("public * GenericInterface<GT,ST,OT> setSt(final ST st) { return new TestImpl(this.gt, st, this.ot); }", output.code);
	}

	@Test
	public void testGenricMutableSetter() throws Exception
	{
		Output output = produceOutput(GenericInterface.class);

		assertContainsWithWildcards("public final void setGt(final GT gt) { this.gt=Objects.requireNonNull(gt); }", output.code);
	}

	@Test
	public void testGenricGetter() throws Exception
	{
		Output output = produceOutput(GenericInterface.class);

		assertContainsWithWildcards("public final GT getGt() { return gt; }", output.code);
	}

	@Test
	public void testLocalGenricImmutableSetter() throws Exception
	{
		Output output = produceOutput(InterfaceWithGenericMembers.class);

		assertContainsWithWildcards("public final InterfaceWithGenericMembers setSet(final java.util.Set<String> set) { return new TestImpl(this.map, set); }", output.code);
	}

	@Test
	public void testLocalGenricMutableSetter() throws Exception
	{
		Output output = produceOutput(InterfaceWithGenericMembers.class);

		assertContainsWithWildcards("public final void setMap(final java.util.Map<String,Object> map) { this.map=Objects.requireNonNull(map); }", output.code);
	}

	@Test
	public void testLocalGenricGetter() throws Exception
	{
		Output output = produceOutput(InterfaceWithGenericMembers.class);

		assertContainsWithWildcards("public final java.util.Set<String> getSet() { return set; }", output.code);
	}
}
