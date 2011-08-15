package de.akquinet.jbosscc.needle.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Test;


public class NeedleConfigurationTest {

    @Test
    public void defaultResourceBundleIsFetched() throws Exception {
    	Map<String, String> loadResourceAndDefault = NeedleConfiguration.loadResourceAndDefault("not-existing");
        assertNotNull(loadResourceAndDefault);
        assertEquals("needle-hsql-hibernate.cfg.xml", loadResourceAndDefault.get(NeedleConfiguration.HIBERNATE_CFG_FILENAME_KEY));
    }

    @Test
    public void canLoadCustomBundle() throws Exception {
        Map<String, String> loadResourceAndDefault = NeedleConfiguration.loadResourceAndDefault("needle-custom");
        assertNotNull(loadResourceAndDefault);
        assertEquals("jdbc-custom", loadResourceAndDefault.get(NeedleConfiguration.JDBC_URL_KEY));
        assertEquals("needle-hsql-hibernate.cfg.xml", loadResourceAndDefault.get(NeedleConfiguration.HIBERNATE_CFG_FILENAME_KEY));
    }
}

