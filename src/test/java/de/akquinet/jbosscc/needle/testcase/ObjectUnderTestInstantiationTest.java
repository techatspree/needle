package de.akquinet.jbosscc.needle.testcase;

import org.junit.Test;

import de.akquinet.jbosscc.needle.NeedleTestcase;
import de.akquinet.jbosscc.needle.ObjectUnderTestInstantiationException;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;

public class ObjectUnderTestInstantiationTest extends NeedleTestcase {

	@SuppressWarnings("unused")
	@ObjectUnderTest
	private ObjectUnderTestBean objectUnderTest;

	@Test(expected = ObjectUnderTestInstantiationException.class)
	public void testInstantiationWithNoArgConstructor() throws Exception {
		initTestcase(this);
	}

	class ObjectUnderTestBean {

		private ObjectUnderTestBean() {
			super();
		}

	}

}
