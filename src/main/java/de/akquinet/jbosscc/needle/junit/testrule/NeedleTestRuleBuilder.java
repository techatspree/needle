package de.akquinet.jbosscc.needle.junit.testrule;

import de.akquinet.jbosscc.needle.injection.InjectionConfiguration;
import de.akquinet.jbosscc.needle.injection.InjectionProvider;
import de.akquinet.jbosscc.needle.junit.AbstractNeedleRuleBuilder;

public class NeedleTestRuleBuilder extends AbstractNeedleRuleBuilder<NeedleTestRuleBuilder, NeedleTestRule> {

    private final Object objectUnderTest;

    public NeedleTestRuleBuilder(Object objectUnderTest) {
        super();
        this.objectUnderTest = objectUnderTest;
    }

    @Override
    protected NeedleTestRule build(final InjectionConfiguration injectionConfiguration,
            InjectionProvider<?>... injectionProvider) {

        return new NeedleTestRule(objectUnderTest, injectionProvider);
    }

}
