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

    static final String CUSTOM_CONFIGURATION_FILENAME = "needle";
    private static final String DEFAULT_CONFIGURATION_FILENAME = "needle-defaults";

    /**
     * @see #ConfigurationLoader(String)
     */
    ConfigurationLoader() {
        this(CUSTOM_CONFIGURATION_FILENAME);
    }

    ConfigurationLoader(final String resourceName) {
        configProperties = loadResourceAndDefault(resourceName);
    }

    /**
     * @deprecated values are accessed in NeedleConfiguration
     * @param key
     * @return
     */
    @Deprecated
    String getProperty(final String key) {
        return configProperties.get(key);
    }

    /**
     * @deprecated values are accessed in NeedleConfiguration
     * @param key
     * @return
     */
    @Deprecated
    boolean containsKey(final String key) {
        return configProperties.containsKey(key);
    }

    static Map<String, String> loadResourceAndDefault(final String name) {
        final Map<String, String> result = new HashMap<String, String>();

        try {
            final ResourceBundle customBundle = ResourceBundle.getBundle(name);

            for (final Enumeration<String> keys = customBundle.getKeys(); keys.hasMoreElements();) {
                final String key = keys.nextElement();
                addKeyValuePair(result, key, customBundle.getString(key));
            }

            final URL url = NeedleConfiguration.class.getResource("/" + name + ".properties");
            LOG.info("loaded Needle config named {} from {}", name, url);
        }
        catch (final Exception e) {
            LOG.debug("found no custom configuration");
        }

        try {

            final ResourceBundle defaultResourceBundle = ResourceBundle.getBundle(DEFAULT_CONFIGURATION_FILENAME);

            for (final Enumeration<String> keys = defaultResourceBundle.getKeys(); keys.hasMoreElements();) {
                final String key = keys.nextElement();
                if (!result.containsKey(key)) {
                    addKeyValuePair(result, key, defaultResourceBundle.getString(key));
                }
            }

            final URL url = NeedleConfiguration.class.getResource("/" + DEFAULT_CONFIGURATION_FILENAME + ".properties");
            LOG.debug("loaded default Needle config from: {}", url);
        }
        catch (final Exception e1) {
            LOG.error("should never happen", e1);

            throw new RuntimeException("should never happen", e1);
        }

        return result;
    }

    private static void addKeyValuePair(final Map<String, String> target, final String key, final String value) {
        final String trimmedValue = value.trim();

        if (!trimmedValue.equals(value)) {
            LOG.warn("trimmed value " + value + " to " + trimmedValue + " (key was " + key + ")");
        }

        target.put(key, trimmedValue);
    }

    public Map<String, String> getConfigProperties() {
        return configProperties;
    }

    /**
     * Returns an input stream for reading the specified resource.
     * 
     * @param resource
     *        the resource name
     * @return an input stream for reading the resource.
     * @throws FileNotFoundException
     *         if the resource could not be found
     */
    public static InputStream loadResource(final String resource) throws FileNotFoundException {
        final boolean hasLeadingSlash = resource.startsWith("/");
        final String stripped = hasLeadingSlash ? resource.substring(1) : resource;

        InputStream stream = null;

        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
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
