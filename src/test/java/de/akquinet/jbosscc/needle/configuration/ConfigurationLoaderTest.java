package de.akquinet.jbosscc.needle.configuration;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class ConfigurationLoaderTest {

	private ConfigurationLoader configurationLoader = new ConfigurationLoader();

	@Test
	public void testContainsKey() throws Exception {
		Assert.assertFalse(configurationLoader.containsKey("anykey"));
		Assert.assertTrue(configurationLoader.containsKey(NeedleConfiguration.JDBC_PASSWORD_KEY));
	}

	@Test
	public void testGetProperty() throws Exception {
		Assert.assertEquals("sa", configurationLoader.getPropertie(NeedleConfiguration.JDBC_USER_KEY));
		Assert.assertNull(configurationLoader.getPropertie("any key"));
	}

	@Test
	public void canLoadCustomBundle() throws Exception {
		Map<String, String> loadResourceAndDefault = configurationLoader.loadResourceAndDefault("needle-custom");
		Assert.assertNotNull(loadResourceAndDefault);
		Assert.assertEquals("jdbc-custom", loadResourceAndDefault.get(NeedleConfiguration.JDBC_URL_KEY));
		Assert.assertEquals("needle-hsql-hibernate.cfg.xml",
		        loadResourceAndDefault.get(NeedleConfiguration.HIBERNATE_CFG_FILENAME_KEY));
	}

	@Test
	public void defaultResourceBundleIsFetched() throws Exception {
		Map<String, String> loadResourceAndDefault = configurationLoader.loadResourceAndDefault("not-existing");
		Assert.assertNotNull(loadResourceAndDefault);
		Assert.assertEquals("needle-hsql-hibernate.cfg.xml",
		        loadResourceAndDefault.get(NeedleConfiguration.HIBERNATE_CFG_FILENAME_KEY));
	}

}
