package de.akquinet.jbosscc.needle.configuration;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;

import org.junit.rules.ExternalResource;

/**
 * Allows changing the NeedleConfiguration on a per test level. Usage:
 * 
 * <pre>
 * 
 * &#064;ClassRule
 * public static NeedleConfigurationResetRule configurationReset = new NeedleConfigurationResetRule(&quot;myResource&quot;);
 * </pre>
 * 
 * @author Jan Galinski, Holisticon AG
 */
public final class NeedleConfigurationResetRule extends ExternalResource {

    private final String resourceName;

    public NeedleConfigurationResetRule(final String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    protected void before() throws Throwable {
        super.before();
        getAndResetInstance(resourceName);
    }

    @Override
    protected void after() {
        super.after();
        getAndResetInstance();
    }

    public static NeedleConfiguration getAndResetInstance() {
        return getAndResetInstance(ConfigurationLoader.CUSTOM_CONFIGURATION_FILENAME);
    }

    public static NeedleConfiguration getAndResetInstance(final String resourceName) {
        try {
            final Field instanceField = NeedleConfiguration.class.getDeclaredField("INSTANCE");
            instanceField.setAccessible(true);
            instanceField.set(NeedleConfiguration.class, null);
            assertThat(instanceField.get(NeedleConfiguration.class), nullValue());

            return NeedleConfiguration.init(resourceName);
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
