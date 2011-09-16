package de.akquinet.jbosscc.needle.configuration;

import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class ConfigurationLoader {

	private static final Logger LOG = LoggerFactory.getLogger(ConfigurationLoader.class);

	private final Map<String, String> configProperties;

	private static final String DEFAULT_CONFIGURATION_FILENAME = "needle-defaults";

	ConfigurationLoader() {
		super();
		configProperties = loadResourceAndDefault("needle");
	}

	String getPropertie(final String key) {
		return configProperties.get(key);
	}

	boolean containsKey(String key) {
		return configProperties.containsKey(key);
	}

	Map<String, String> loadResourceAndDefault(String name) {
		final Map<String, String> result = new HashMap<String, String>();

		try {
			ResourceBundle customBundle = loadResourceBundle(name);

			for (Enumeration<String> keys = customBundle.getKeys(); keys.hasMoreElements();) {
				String key = keys.nextElement();
				result.put(key, customBundle.getString(key));
			}

			URL url = NeedleConfiguration.class.getResource("/" + name + ".properties");
			LOG.info("loaded Needle config named {} from {}", name, url);
		} catch (Exception e) {
			LOG.debug("found no custom configuration");
		}

		try {

			ResourceBundle defaultResourceBundle = loadResourceBundle(DEFAULT_CONFIGURATION_FILENAME);

			for (Enumeration<String> keys = defaultResourceBundle.getKeys(); keys.hasMoreElements();) {
				String key = keys.nextElement();
				if (!result.containsKey(key)) {
					result.put(key, defaultResourceBundle.getString(key));
				}

			}

			URL url = NeedleConfiguration.class.getResource("/" + DEFAULT_CONFIGURATION_FILENAME + ".properties");
			LOG.debug("loaded default Needle config from: {}", url);
		} catch (Exception e1) {
			LOG.error("should never happen", e1);

			throw new RuntimeException("should never happen");
		}

		return result;
	}

	private ResourceBundle loadResourceBundle(String name) {
		return ResourceBundle.getBundle(name);
	}
}
