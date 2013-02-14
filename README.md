[![Build Status](https://needle.ci.cloudbees.com/job/Needle/badge/icon)](https://needle.ci.cloudbees.com/job/Needle/)

Need(le) for Speed - Effective Unit Testing for Java EE
================================
[@NeedleProject](https://twitter.com/NeedleProject)

**Needle is a lightweight framework for testing Java EE components outside of the
container in isolation. It reduces the test setup code by analysing dependencies and
automatic injection of mock objects. It will thus maximize the speed of development
as well as the execution of unit tests.**

![Needle Coffee Cups](http://needle.spree.de/public/images/needle-coffeecups-380px.jpg)


**Core Features:**

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

* Needle can be used with [JUnit](http://www.junit.org/) or [TestNG](http://testng.org/).
* It supports [EasyMock](http://www.easymock.org/) and [Mockito](http://code.google.com/p/mockito/) out-of-the-box.



**Licensing**

Needle is licensed under GNU Lesser General Public License (LGPL) version 2.1 or later.


**Needle URLs**

* Needle Home Page:	http://needle.spree.de/
* Downloads:				https://sourceforge.net/projects/jbosscc-needle/
* Source Code:			https://github.com/akquinet/needle
* Issue Tracking:			https://github.com/akquinet/needle/issues

**Release Nodes**

Version 2.2 - https://github.com/akquinet/needle/issues?milestone=1&state=closed

Previous Versions - https://github.com/akquinet/needle/blob/master/src/docs/dist/changelog.txt

