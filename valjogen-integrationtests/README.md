<a name="jumbotron-start"/>

# VALJOGen - Integration Tests

# About this module
This module contains a number of known annotated source files that the annotation processor is run against in order to produce new generated source files. These generated source files then compared with pre-verified expected results that acts as a "golden master".

When a generated source file is different from the master it is assumed there is an regression error. If this is not the case the golden master must be updated manually.

In addition this project also checks some of the generated source files for additonal correctness using the nl.jqno.equalsverifier tool.

<a name="jumbotron-end"/>

/ [Morten M. Christensen](http://www.linkedin.com/in/mortench), [41concepts](http://www.41concepts.com)
