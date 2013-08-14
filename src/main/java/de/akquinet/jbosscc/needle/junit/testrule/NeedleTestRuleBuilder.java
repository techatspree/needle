package de.akquinet.jbosscc.needle.junit.testrule;

import de.akquinet.jbosscc.needle.injection.InjectionConfiguration;
import de.akquinet.jbosscc.needle.injection.InjectionProvider;
import de.akquinet.jbosscc.needle.junit.AbstractNeedleRuleBuilder;

/**
 * Builder that created a new {@link NeedleTestRule}.
 */
public class NeedleTestRuleBuilder extends AbstractNeedleRuleBuilder<NeedleTestRuleBuilder, NeedleTestRule> {

    private final Object testInstance;

    /**
     * Create new instance.
     * 
     * @param testInstance
     *            the test instance containing the rule, normally "this"
     */
    public NeedleTestRuleBuilder(final Object testInstance) {
        this.testInstance = testInstance;
    }

    @Override
    protected NeedleTestRule build(final InjectionConfiguration injectionConfiguration,
            final InjectionProvider<?>... injectionProvider) {

        return new NeedleTestRule(testInstance, injectionConfiguration, injectionProvider);
    }

}
