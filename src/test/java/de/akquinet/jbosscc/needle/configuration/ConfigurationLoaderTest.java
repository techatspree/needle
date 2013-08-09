package de.akquinet.jbosscc.needle.configuration;

import static de.akquinet.jbosscc.needle.configuration.ConfigurationProperties.HIBERNATE_CFG_FILENAME_KEY;
import static de.akquinet.jbosscc.needle.configuration.ConfigurationProperties.JDBC_URL_KEY;
import static de.akquinet.jbosscc.needle.configuration.ConfigurationProperties.PERSISTENCEUNIT_NAME_KEY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import org.junit.Test;

public class ConfigurationLoaderTest {

    private final ConfigurationLoader configurationLoader = new ConfigurationLoader();

    @Test
    public void testContainsKey() throws Exception {
        assertFalse(configurationLoader.containsKey("anykey"));
        assertTrue(configurationLoader.containsKey(PERSISTENCEUNIT_NAME_KEY));
    }

    @Test
    public void testGetProperty() throws Exception {
        assertEquals("TestDataModel", configurationLoader.getProperty(PERSISTENCEUNIT_NAME_KEY));
        assertNull(configurationLoader.getProperty("any key"));
    }

    @Test
    public void canLoadCustomBundle() throws Exception {
        final Map<String, String> loadResourceAndDefault = ConfigurationLoader.loadResourceAndDefault("needle-custom");
        assertNotNull(loadResourceAndDefault);
        assertEquals("jdbc-custom", loadResourceAndDefault.get(JDBC_URL_KEY));
        assertEquals("needle-hsql-hibernate.cfg.xml", loadResourceAndDefault.get(HIBERNATE_CFG_FILENAME_KEY));
    }

    @Test
    public void defaultResourceBundleIsFetched() throws Exception {
        final Map<String, String> loadResourceAndDefault = ConfigurationLoader.loadResourceAndDefault("not-existing");
        assertNotNull(loadResourceAndDefault);
        assertEquals("needle-hsql-hibernate.cfg.xml", loadResourceAndDefault.get(HIBERNATE_CFG_FILENAME_KEY));
    }

    @Test
    public void testLoadResource() throws Exception {
        final InputStream loadResource = ConfigurationLoader.loadResource("needle.properties");
        assertNotNull(loadResource);

        final InputStream loadResourceWithLeadingSlash = ConfigurationLoader.loadResource("/needle.properties");
        assertNotNull(loadResourceWithLeadingSlash);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadResource_NotFound() throws Exception {
        ConfigurationLoader.loadResource("notfound.properties");
    }

}
