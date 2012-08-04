package de.akquinet.jbosscc.needle.configuration;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ConfigurationLoader {

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
                addKeyValuePair(result, key, customBundle.getString(key));
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
                    addKeyValuePair(result, key, defaultResourceBundle.getString(key));
				}
			}

			URL url = NeedleConfiguration.class.getResource("/" + DEFAULT_CONFIGURATION_FILENAME + ".properties");
			LOG.debug("loaded default Needle config from: {}", url);
		} catch (Exception e1) {
			LOG.error("should never happen", e1);

			throw new RuntimeException("should never happen", e1);
		}

		return result;
	}

    private void addKeyValuePair(Map<String, String> target, String key, String value) {
        String trimmedValue = value.trim();

        if (!trimmedValue.equals(value)) {
            LOG.warn("trimmed value " + value + " to " + trimmedValue + " (key was " + key + ")");
        }

        target.put(key, trimmedValue);
    }

	private ResourceBundle loadResourceBundle(String name) {
		return ResourceBundle.getBundle(name);
	}

	/**
	 * Returns an input stream for reading the specified resource.
	 *
	 * @param resource
	 *            the resource name
	 * @return an input stream for reading the resource.
	 * @throws FileNotFoundException
	 *             if the resource could not be found
	 */
	public static InputStream loadResource(final String resource) throws FileNotFoundException {
		boolean hasLeadingSlash = resource.startsWith("/");
		String stripped = hasLeadingSlash ? resource.substring(1) : resource;

		InputStream stream = null;

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader != null) {
			stream = classLoader.getResourceAsStream(resource);
			if (stream == null && hasLeadingSlash) {
				stream = classLoader.getResourceAsStream(stripped);
			}
		}

		if (stream == null) {
			throw new FileNotFoundException("resource " + resource + " not found");
		}

		return stream;
	}
}
