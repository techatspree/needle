package de.akquinet.jbosscc.needle.injection.testinjection;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.junit.NeedleRule;
import de.akquinet.jbosscc.needle.mock.EasyMockProvider;

public class MockProviderInjectionTest {

	@Rule
	public NeedleRule needleRule = new NeedleRule();

	@Inject
	private EasyMockProvider mockProvider;

	@Test
	public void testMockProviderInjection() throws Exception {
		Assert.assertNotNull(mockProvider);
    }

}
