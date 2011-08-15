package de.akquinet.jbosscc.needle.mock;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.MyComponent;
import de.akquinet.jbosscc.needle.MyComponentBean;
import de.akquinet.jbosscc.needle.MyEjbComponent;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

public class EasyMockTest {

	@Rule
	public final NeedleRule needleRule = new NeedleRule();

	@ObjectUnderTest(implementation = MyComponentBean.class)
	private MyComponent component;

	private EasyMockProvider mockProvider = (EasyMockProvider) needleRule.getMockProvider();


	@Test
	public void testNiceMock() throws Exception {
		String testMock = component.testMock();
		Assert.assertNull(testMock);
	}

	@Test
	public void testStricMock() throws Exception {
		MyEjbComponent myEjbComponentMock = (MyEjbComponent) needleRule.getInjectedObject(MyEjbComponent.class);
		Assert.assertNotNull(myEjbComponentMock);

		EasyMock.resetToStrict(myEjbComponentMock);

		EasyMock.expect(myEjbComponentMock.doSomething()).andReturn("Hello World");

		mockProvider.replayAll();
		String testMock = component.testMock();

		Assert.assertEquals("Hello World", testMock);

		mockProvider.verifyAll();
	}

}
