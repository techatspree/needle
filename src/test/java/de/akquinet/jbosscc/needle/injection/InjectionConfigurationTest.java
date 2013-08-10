package de.akquinet.jbosscc.needle.injection;


import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.akquinet.jbosscc.needle.mock.EasyMockProvider;
import de.akquinet.jbosscc.needle.mock.MockProvider;

public class InjectionConfigurationTest {

	
	@Test
	public void testCreateMockProvider() throws Exception {
	    Class<? extends MockProvider> lookupMockProviderClass = EasyMockProvider.class;
	    InjectionConfiguration injectionConfiguration = new InjectionConfiguration();
		MockProvider mockProvider = injectionConfiguration.createMockProvider(lookupMockProviderClass);
		assertTrue(mockProvider instanceof EasyMockProvider);
	}
}
