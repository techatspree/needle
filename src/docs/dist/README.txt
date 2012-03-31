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
Effective Unit Testing for Java EE
================================================================================

Needle is a lightweight framework for testing Java EE components outside of the
container in isolation. It reduces the test setup code by analysing dependencies and
automatic injection of mock objects. It will thus maximize the speed of development
as well as the execution of unit tests.

================================================================================
Core Features:

* Instantiation of @ObjectUnderTest Components
* Constructor, Method and Field based dependency injection
* Injection of Mock objects by default
* Extensible by providing custom injection providers
* Wiring of object graphs

* Database testing via JPA Provider, e.g. EclipseLink or Hibernate
* EntityManager creation and injection
* Execute optional database operations during test setup and tear down
* Transaction Utilities

* Provide Utilities for Reflection, e.g. for private method invocation or field access

* Needle can be used with JUnit or TestNG.
* It supports EasyMock and Mockito out-of-the-box.

================================================================================
Contents of distribution

./
	Needle artifacts (bin, source and javadoc jars)

./docs
	API documentation and reference guide.

./examples
	Maven-based example projects.

================================================================================
Licensing

Needle is licensed under GNU Lesser General Public License (LGPL) version 2.1 or later.

Needle URLs
================================================================================

Needle Home Page:		http://needle.spree.de/
Downloads:              https://sourceforge.net/projects/jbosscc-needle/
Forums:                 https://sourceforge.net/projects/jbosscc-needle/forums
Source Code:            https://github.com/akquinet/needle
Issue Tracking:         https://sourceforge.net/tracker/?group_id=306915

================================================================================