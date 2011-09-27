package de.akquinet.jbosscc.needle.injection;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

public class CustomInjectionAnnotationTest {

	@Rule
	public NeedleRule needleRule = new NeedleRule();

	@ObjectUnderTest
	private CustomInjectionTestComponent component;

	@Test
	public void testCustomeInjection() throws Exception {
		Assert.assertNotNull(component.getQueue1());
		Assert.assertNotNull(component.getQueue2());
	}

}
