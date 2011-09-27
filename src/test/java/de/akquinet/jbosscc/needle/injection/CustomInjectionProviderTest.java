package de.akquinet.jbosscc.needle.injection;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

public class CustomInjectionProviderTest {

	@Rule
	public NeedleRule needleRule = new NeedleRule();

	@ObjectUnderTest
	private CustomInjectionTestComponent component;

	@Test
	public void testCustomeInjectionProvider() throws Exception {
		Assert.assertSame(CustomMapInjectionProvider.MAP, component.getMap());
	}

}
