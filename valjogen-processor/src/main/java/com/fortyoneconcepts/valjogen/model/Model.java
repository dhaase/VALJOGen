/*
* Copyright (C) 2014 41concepts Aps
*/
package com.fortyoneconcepts.valjogen.model;

/**
 * Common interface for all model elements.
 *
 * @author mmc
 */
public interface Model
{
	/**
	 * The configuration object for this model.
	 *
	 * @return The configuration object used for codegeneration with this model.
	 */
	public Configuration getConfiguration();

	/**
	 * Some helper types for this model.
	 *
	 * @return Helper types for this model.
	 */
	public HelperTypes getHelperTypes();

	/**
	 * The package that the output belongs to.
	 *
	 * @return The package name that should be used when generating code for this model.
	 */
	public String getPackageName();

	/**
	 * True if the output has no package.
	 *
	 * @return true if the package that shoulde be used when generating code for this model is a default pacakge.
	 */
	public default boolean isInDefaultPackage()
	{
		return getPackageName().isEmpty();
	}

	/**
	 * Get the class that this model is used in.
	 *
	 * @return The class that this model is a part of.
	 */
	public BasicClazz getClazz();

	/**
	 * Prefix for (auto-generated) local variables.
	 *
	 * @return The prefix that should be used for local variables in methods.
	 */
	public default String getVariablesPrefix()
	{
		return getConfiguration().getSuggestedVariablesPrefix();
	}

	/**
	 * Special (safe) version of toString that guards against infinite loops.
	 * Called by default toString() method so there is no need to call this
	 * directly by clients.
	 *
	 * @param level The internal recursion level.
	 *
	 * @return String representation of model.
	 */
	 String toString(int level);
}
