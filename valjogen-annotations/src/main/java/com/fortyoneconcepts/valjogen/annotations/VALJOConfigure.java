/*
* Copyright (C) 2014 41concepts Aps
*/
package com.fortyoneconcepts.valjogen.annotations;

import java.lang.annotation.*;

/**
 * Specifies details about the code that should be generated. May be specified on a package (package-info.java) or on a inteface alongside
 * the {@link VALJOGenerate} annotation. If specified on both a package and an interface then the package specification is ignored. Has no effect
 * unless affected interfaces also has a {@link VALJOGenerate} annotation. All details may be overruled by setting indentically named key/values
 * in the annotation processor. Indeed some details like "debugXXX" are usally better specified as a runtime option instead of hardcoding in the source using
 * this annotation.
 *
 * <p><b>Usage example (package-info.java):</b></p>
 * <pre>
 * <code>
 *<span class="identifier">{@literal @}VALJOConfigure</span>(<span class="identifier">outputPackage</span>=<span class="string">"test.impl"</span>, <span class="identifier">baseClazzName</span>=<span class="string">"test.CommonBaseClass"</span>)
 * <span class="keyword">package</span> <span class="identifier">test</span>;
 * </code>
 * </pre>
 *
 * The above code will instruct the VALJOGen annotation processor to have generated value object classes from interfaces in this package belong to
 * the "test.impl" package and to inherit from a common base class with qualified name "test.CommonBaseClass". Note that generation requires
 * seperate {@link VALJOGenerate} annotations.
 *
 * All string properties recognize the following macro: <code>$(This)</code> which resolves to the fully qualified name of the generated class.
 *
 * @author mmc
 */
@Retention(RetentionPolicy.SOURCE)
@Target(value={ElementType.TYPE, ElementType.PACKAGE})
public @interface VALJOConfigure
{
	/**
	* Package name of generated class. May be overruled by a fully qualified name on VALJOGenerate or by equivalent annotation processor key.
	*
	* If not specified (set to N/A), the output package will be the same as the interface used to generate the class.
	*
	* @return Package name.
	*/
    String outputPackage() default "N/A";

	/**
	* Access modifier for generated class. May be overruled by equivalent annotation processor key.
	*
	* @return Access modifier.
	*/
    String clazzScope() default "public";

	/**
	*  Experimental IETF BCP 47 language tag string descripting internal locale to use for annotation processor. May be overruled by equivalent annotation processor key.
	*
	*  @return Internal language tag describing locale to use for annotation processor.
	*
	*  @see java.util.Locale#forLanguageTag
	*/
    String localeTag() default "en-US";

	/**
	* Linewidth for generated code. 0 if unlimited. May be overruled by equivalent annotation processor key.
	*
	* @return line width to use when generating output.
	*/
    int lineWidth() default -1;

	/**
	* Specifies if generated members should be final if possible. May be overruled by equivalent annotation processor key.
	*
	* @return True if generated members are prefered to be final.
	*/
    boolean finalMembersEnabled() default true;

	/**
	* Specifies if generated classes should be final if possible. May be overruled by equivalent annotation processor key.
	*
	* @return True if generated classes are prefered to be final.
	*/
    boolean finalClassEnabled() default true;

	/**
	* Specifies if generated property methods should be final if possible. May be overruled by equivalent annotation processor key.
	*
	* @return True if generated properties are prefered to be final
	*/
	boolean finalPropertiesEnabled() default true;

	/**
	* Specifies assignments to local variables should guard against null. May be overruled by equivalent annotation processor key.
	*
	* @return True if local variables should be guarded against null assignments
	*/
	boolean ensureNotNullEnabled() default true;

	/**
	* Specifies if generated properties/methods for mutable members should be synchronized. May be overruled by equivalent annotation processor key.
	*
	* @return True if generated properties/methods are prefered to be synchronized.
	*/
	boolean synchronizedAccessEnabled() default false;

	/**
	* Specifies prefix to use by temporary variables in method in order to avoid clashing with members. May be overruled by equivalent annotation processor key.
	*
	* @return Prefix to use by local variables.
	*/
	String suggestedVariablesPrefix() default "_";

	/**
	* Specifies the serialization ID to use for generated classes or ZERO if not set. Any non-zero value will automatically be inserted into classes that
	* directly or indirectly implement the {@link java.io.Serializable} interface.
	*
	* May be overruled by equivalent annotation processor key.
	*
	* @return The serialization ID to use or '0' if not set.
	*/
	long serialVersionUID() default 1L;

	/**
	* Specifies if equals method should be generated for the class. May be overruled by equivalent annotation processor key.
	*
	* Remember to supply a hash method when generating equals methods.
	*
	* @see VALJOConfigure#hashEnabled
	*
	* @return True if a equals method should be generated for the class.
	*/
	boolean equalsEnabled() default true;

	/**
	* Specifies if hash method should be generated for the class. May be overruled by equivalent annotation processor key.
	*
	* The generated hash method will be consistent with equals if this method is generated as well.
	*
	* @see VALJOConfigure#equalsEnabled
	*
	* @return True if a hash method should be generated for the class.
	*/
	boolean hashEnabled() default true;

	/**
	* Specifies if compareTo implementation should be generated for classes that directly or indirectly implement the {@link Comparable} interface. May be overruled by equivalent annotation processor key.
	*
	* @see VALJOConfigure#extraInterfaceNames for how to make the implementation class implement Comparable with regard to itself.
	* @see VALJOConfigure#comparableMembers For how to specify which members to check for in the implemenation.
	* @return True if a comparable method should be generated for classes that implement the {@link Comparable} interface.
	*/
	boolean comparableEnabled() default true;

	/**
	* Ordered names of members to use in a compareTo implementation and in which order. If left unspecified all members in declaration order is assumed. May be overruled by equivalent annotation processor key.
	*
	* @return Array of names of all members to use in compareTo method and in specified order.
	*/
    String[] comparableMembers() default { };

	/**
	* Specifies if a toString method should be generated the class. May be overruled by equivalent annotation processor key.
	*
	* @return True if a toString method should be generated for the class.
	*/
	boolean toStringEnabled() default true;

	/**
	* Specifies if javaDoc should be added to the generated class. May be overruled by equivalent annotation processor key.
	*
	* NB: In progress - NOT IMPLEMENTED YET!
	*
	* @return True if javaDoc should be generated for the class.
	*/
	boolean javadDocEnabled() default false;

	/**
	* Specifies if errors should be issued for malformed getter and setter methods. May be overruled by equivalent annotation processor key.
	*
	* @return True if malformed getter/setter methods should be ignored. False if they should give errors.
	*/
	boolean ignoreMalformedProperties() default false;

	/**
	* Fully qualified extra classes to import into the generated code (in addition to implemented interfaces
    * and baseclass which is imported as needed).
	*
	* @return Array of all qualified classes to import.
	*/
    String[] importClasses() default { "java.util.Arrays", "java.util.Objects", "javax.annotation.Generated" };

    /**
	* Specifies the prefixes of javaBean-style getter methods. Governs which memebers are inserted into the target class and which property method are
	* implemented. By default only standard javaBean prefixes are included but additional custom prefixes can be added.
	* Prefixes may be overruled by equivalent annotation processor key.
	*
	* @return Array of prefixes for getter methods.
	*/
    String[] getterPrefixes() default { "is", "get" };

    /**
	* Specifies the prefixes of javaBean-style getter methods. Governs which memebers are inserted into the target class and which property method are
	* implemented. By default only the standard javaBean prefix is included but additional custom prefixes can be added. For instance "withXXX" methods
	* for immutable properties might be useful. Prefixes may be overruled by equivalent annotation processor key.
	*
	* @return Array of prefixes for setter methods.
	*/
    String[] setterPrefixes() default { "set" };

	/**
	* Specifies if additional interfaces should be implemented to the generated class. May be overruled by equivalent annotation processor key.
	*
	* Hint: For generic interfaces using the macro <code>"$(This)"</code> for the generic qualifier is useful to refer to the name of the generated class. F.x.
	* to implement Comparable write "<code>java.lang.Comparable&lt;(This)&gt;</code>".
	*
	* @return Array of all additional interfaces
	*/
    String[] extraInterfaceNames() default {};

    /**
	* Specifies names of methods that will be implemented by the generated class. May be overruled by equivalent annotation processor key.
	*
	* NOTE: Do not set this (yet). Presently used internally only. Reserved for future use.
	*
	* @return Array of names of implemented methods.
	*/
    String[] implementedMethodNames() default {};

	/**
	* Specifies the base class of the generated class. May be overruled by equivalent annotation processor key.
	*
	* @return Name of the base class for the generated class.
	*/
    String baseClazzName() default "java.lang.Object";

    /**
	* UTF-8 formatted file that contains a header that should be added at the top of the generated output.
	*
	* @return Filename of text file containing header.
	*/
    String headerFileName() default "N/A";

    /**
	* UTF-8 formatted string template file that should be used.
	*
	* @return Filename of string template file.
	*/
    String customTemplateFileName() default "N/A";

    /**
	* Specifies the {@link java.util.logging.Level} log level to use inside the annotation processor. Set this to INFO or FINE to inspect model instances,
	* inspect output or to help track errors inside the processor
	*
	* Note that normally java.util.logging.ConsoleHandler.level needs to be set as well for log levels below INFO to be shown.
	*
	* @return Log level
	*/
    String logLevel() default "INFO"; // "WARNING";

    /**
	* Experimental feature that specifies if the annotation processor should open the STViz GUI Inspector for debugging the internal stringtemplates. You should not need to enable this unless you are
	* adding/modifying code templates and run into problems. Be aware that code generation will pause while the generator is shown - you properly do not want to do that when using an IDE.
	* May be overruled by equivalent annotation processor key.
	*
	* @return True if annotation processor should open StringTemplagte's STViz Gui explorer during code generation phase.
	*/
    boolean debugStringTemplates() default false;
}
