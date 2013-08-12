package de.akquinet.jbosscc.needle;

import de.akquinet.jbosscc.needle.junit.AbstractNeedleRuleBuilder;
import de.akquinet.jbosscc.needle.junit.NeedleRule;
import de.akquinet.jbosscc.needle.junit.NeedleRuleBuilder;

/**
 * Allows static factory method access to selected needle components.
 */
public final class NeedleBuilders {

    /**
     * @return a new builder for {@link NeedleRule}.
     */
    public static AbstractNeedleRuleBuilder<NeedleRuleBuilder, NeedleRule> needleRule() {
        return new NeedleRuleBuilder();
    }

    /**
     * @param configFile
     *            the config file resource to use (filename without
     *            ".properties" suffix)
     * @return a new, preconfigured builder for {@link NeedleRule}.
     */
    public static AbstractNeedleRuleBuilder<NeedleRuleBuilder, NeedleRule> needleRule(final String configFile) {
        return needleRule().with(configFile);
    }

    /**
     * Do not instantiate.
     */
    private NeedleBuilders() {
        // hide
    }
}
