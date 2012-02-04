package de.akquinet.jbosscc.needle.injection.testinjection;

import java.net.Authenticator;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.MyEjbComponent;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

public class TestMockInjectionTest {

	@Rule
	public NeedleRule needleRule = new NeedleRule();

	@ObjectUnderTest
	private TestInjectionComponent objectUnderTest;

	@Inject
	private Authenticator authenticator;

	@EJB
	private MyEjbComponent ejbComponent;

	@Test
	public void testInjection() throws Exception {
		Assert.assertNotNull(authenticator);
		Assert.assertEquals(objectUnderTest.getAuthenticator(), authenticator);

		Assert.assertNotNull(ejbComponent);
		Assert.assertEquals(objectUnderTest.getEjbComponent(), ejbComponent);
	}

}
