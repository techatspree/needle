package de.akquinet.jbosscc.needle.junit;

import de.akquinet.jbosscc.needle.common.Builder;
import de.akquinet.jbosscc.needle.configuration.NeedleConfiguration;
import de.akquinet.jbosscc.needle.configuration.PropertyBasedConfigurationFactory;

/**
 * 
 * 
 * @param <B>
 *            type of builder, needed for type-safe "return this"
 * @param <R>
 *            type of rule to build
 */
@SuppressWarnings("unchecked")
public abstract class AbstractRuleBuilder<B, R> implements Builder<R> {

    protected String configFile;

    /**
     * @param configFile
     *            the config file resource to use (filename without
     *            ".properties" suffix)
     */
    public B with(final String configFile) {
        this.configFile = configFile;
        return (B) this;
    }

    private NeedleConfiguration getNeedleConfiguration() {
        try {
            return configFile == null ? PropertyBasedConfigurationFactory.get().clone()
                    : PropertyBasedConfigurationFactory.get(configFile);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("could not create needle configuration", e);
        }
    }

    public final R build() {
        return build(getNeedleConfiguration());
    }

    protected abstract R build(final NeedleConfiguration needleConfiguration);

}
