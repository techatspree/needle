...................................................... .........................
.88888.....888..888888888O..888888888..O888888888O.....888........888888888.....
.888888....888..8888........888:.......O888...I8888Z...888........888...........
.888=888...888..8888........888........O888.....Z888...888........888...........
.888.8888..888..888888888...88888888O..O888......888O..888........88888888......
.888,.888O.888..888888888...888888888..O888......8888..888........88888888......
.888,..888,888..8888........888........O888......888=..888........888...........
.888,...888888..8888........888........O888.....8888...888........888...........
.888,....88888..8888888888..888888888$.O88888888888....888888888..888888888.....
.888,.....8888..8888888888..888888888$.O88888888.......888888888..888888888.....
................................................................................
================================================================================
Effective Unit Testing of Java EE
================================================================================

Needle is a lightweight framework for testing Java EE components outside of the
container. It reduce the test setup code by analysing dependencies and
automatically injection of mock objects.

================================================================================
Core Features:

* Instantiation of @ObjectUnderTest Components
* Constructor, Method and Field based dependency injection
* Default injection are Mock objects
* Extensible by providing custom injection provider
* Wiring of object graphs

* Database testing via JPA Provider, e.g. EclipseLink or Hibernate
* EntityManager creation and injection
* Execute optional database operation on test setup and tear down
* Transaction Utilities

* Provide Utilities for Reflection, e.g. for private method invocation or field access

* Needle can used with JUnit or TestNG.
* It supports out of the box EasyMock and Mockito as mock framework.

================================================================================
Contents of distribution

./
	Needle artifacts (bin, source and javadoc jars)

./docs
	API documentation and reference guide.

================================================================================
Licensing

Needle is licensed under GNU Lesser General Public License (LGPL) version 2.1 or later.

Needle URLs
================================================================================

Needle Home Page:		http://needle.spree.de/
Downloads:              https://sourceforge.net/projects/jbosscc-needle/
Forums:                 https://sourceforge.net/projects/jbosscc-needle/forums
Source Code:            https://jbosscc-needle.svn.sourceforge.net/svnroot/jbosscc-needle/
Issue Tracking:         https://sourceforge.net/tracker/?group_id=306915

================================================================================