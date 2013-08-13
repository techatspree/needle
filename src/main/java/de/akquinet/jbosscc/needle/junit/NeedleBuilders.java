package de.akquinet.jbosscc.needle.junit;

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
     * Do not instantiate.
     */
    private NeedleBuilders() {
        super();
    }
}
