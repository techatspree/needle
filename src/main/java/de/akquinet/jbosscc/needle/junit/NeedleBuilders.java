package de.akquinet.jbosscc.needle.junit;

import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

import de.akquinet.jbosscc.needle.junit.testrule.DatabaseTestRule;
import de.akquinet.jbosscc.needle.junit.testrule.DatabaseTestRuleBuilder;
import de.akquinet.jbosscc.needle.junit.testrule.NeedleTestRuleBuilder;

/**
 * Allows static factory method access to selected needle components.
 */
public final class NeedleBuilders {

    /**
     * @return a new builder for {@link NeedleRule}.
     */
    public static NeedleRuleBuilder needleRule() {
        return new NeedleRuleBuilder();
    }
    
    /**
     * @return a new builder for {@link NeedleTestRule}.
     */
    public static NeedleTestRuleBuilder needleTestRule(final Object testInstance) {
        return new NeedleTestRuleBuilder(testInstance);
    }

    /**
     * @return a new builder for {@link DatabaseTestRule}.
     */
    public static DatabaseTestRuleBuilder databaseTestRule() {
        return new DatabaseTestRuleBuilder();
    }

    /**
     * @return a new builder for {@link DatabaseTestRule}.
     */
    public static DatabaseRuleBuilder databaseRule() {
        return new DatabaseRuleBuilder();
    }

    /**
     * Returns a {@code RuleChain} with a single {@link TestRule}. This method
     * is the usual starting point of a {@code RuleChain}.
     * 
     * @param outerRule
     *            the outer rule of the {@code RuleChain}.
     * @return a {@code RuleChain} with a single {@link TestRule}.
     */
    public static RuleChain outerRule(final TestRule outerRule) {
        return RuleChain.outerRule(outerRule);
    }

    /**
     * Returns a {@code RuleChain} without a {@link TestRule}. This method may
     * be the starting point of a {@code RuleChain}.
     *
     * @return a {@code RuleChain} without a {@link TestRule}.
     */
    public static RuleChain emptyRuleChain() {
        return RuleChain.emptyRuleChain();
    }

    private NeedleBuilders() {
        super();
    }
}
