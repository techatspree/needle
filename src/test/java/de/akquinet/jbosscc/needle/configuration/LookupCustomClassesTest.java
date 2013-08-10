package de.akquinet.jbosscc.needle.configuration;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class LookupCustomClassesTest {

    private static final String KEY = "FOO";

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenConfigurationIsNull() throws Exception {
        new LookupCustomClasses(null);
    }

    @Test
    public void shouldLoadInjectionProviders() throws ClassNotFoundException {
        final Map<String, String> configurationProperties = createConfigurationProperties(KEY, String.class.getCanonicalName(), "foo.Bar", " ", null, Integer.class.getCanonicalName());

        final LookupCustomClasses lookupCustomClasses = new LookupCustomClasses(configurationProperties);
        final Set<Class<Object>> providers = lookupCustomClasses.lookup(KEY);

        assertThat(providers.size(), is(2));
        assertTrue(providers.contains(String.class));
        assertTrue(providers.contains(Integer.class));

    }

    @SuppressWarnings("serial")
    private Map<String, String> createConfigurationProperties(final String key, final String... fqns) {
        return new HashMap<String, String>() {
            {
                final StringBuilder b = new StringBuilder();
                for (String fqn : fqns) {
                    b.append(fqn);
                    b.append(", ");
                }
                put(key, b.toString());
            }
        };
    }
}
