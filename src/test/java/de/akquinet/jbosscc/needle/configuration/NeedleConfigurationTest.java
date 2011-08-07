package de.akquinet.jbosscc.needle.configuration;




import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ResourceBundle;

import org.junit.Test;


public class NeedleConfigurationTest {

    @Test
    public void defaultResourceBundleIsFetched() throws Exception {
        final ResourceBundle resourceBundle = NeedleConfiguration.loadResourceOrDefault("not-existing");
        assertNotNull(resourceBundle);
        assertEquals("needle-hsql-hibernate.cfg.xml", resourceBundle.getString(NeedleConfiguration.HIBERNATE_CFG_FILENAME_KEY));
    }

    @Test
    public void canLoadCustomBundle() throws Exception {
        final ResourceBundle resourceBundle = NeedleConfiguration.loadResourceOrDefault("needle-custom");
        assertNotNull(resourceBundle);
        assertEquals("hibernate-custom", resourceBundle.getString(NeedleConfiguration.HIBERNATE_CFG_FILENAME_KEY));
    }
}

