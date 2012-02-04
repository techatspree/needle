package de.akquinet.jbosscc.needle.injection;

import junit.framework.Assert;

import org.junit.Test;

import de.akquinet.jbosscc.needle.mock.EasyMockProvider;
import de.akquinet.jbosscc.needle.mock.MockProvider;
import de.akquinet.jbosscc.needle.mock.MockitoProvider;

public class InjectionConfigurationTest {

	@Test
	public void testLookupMockProviderClass() throws Exception {
		Assert.assertNotNull(MockitoProvider.class.getName());
	}

	@Test(expected = RuntimeException.class)
	public void testLookupMockProviderClass_WithUnknownClass() throws Exception {
		Assert.assertNull(InjectionConfiguration.lookupMockProviderClass("unknown"));
	}

	@Test(expected = RuntimeException.class)
	public void testLookupMockProviderClass_Null() throws Exception {
		Assert.assertNull(InjectionConfiguration.lookupMockProviderClass(null));
	}

	@Test
	public void testCreateMockProvider() throws Exception {
		InjectionConfiguration injectionConfiguration = new InjectionConfiguration();
		MockProvider mockProvider = injectionConfiguration.createMockProvider();
		Assert.assertTrue(mockProvider instanceof EasyMockProvider);
	}
}
