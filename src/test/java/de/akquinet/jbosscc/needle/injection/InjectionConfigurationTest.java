package de.akquinet.jbosscc.needle.injection;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.akquinet.jbosscc.needle.mock.EasyMockProvider;
import de.akquinet.jbosscc.needle.mock.MockProvider;
import de.akquinet.jbosscc.needle.mock.MockitoProvider;

public class InjectionConfigurationTest {

	
	@Test
	public void testCreateMockProvider() throws Exception {
	    Class<? extends MockProvider> lookupMockProviderClass = EasyMockProvider.class;
	    InjectionConfiguration injectionConfiguration = new InjectionConfiguration();
		MockProvider mockProvider = injectionConfiguration.createMockProvider(lookupMockProviderClass);
		assertTrue(mockProvider instanceof EasyMockProvider);
	}
	
	@Test
    public void testLookupMockProviderClass() throws Exception {
        assertNotNull(InjectionConfiguration.lookupMockProviderClass(MockitoProvider.class.getName()));
    }

    
    @Test(expected = RuntimeException.class)
    public void testLookupMockProviderClass_Null() throws Exception {
        assertNull(InjectionConfiguration.lookupMockProviderClass(null));
    }

}
