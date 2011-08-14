package de.akquinet.jbosscc.needle.injection;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

public class InjectionFinalClassTest {

	@Rule
	public NeedleRule needleRule = new NeedleRule();

	@ObjectUnderTest
	private TestClass testClass;


	@Test
	public void testFinal() throws Exception {
		Assert.assertNull(testClass.getString());

    }



}
