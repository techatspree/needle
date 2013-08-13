package de.akquinet.jbosscc.needle.junit.testrule;

import de.akquinet.jbosscc.needle.configuration.NeedleConfiguration;
import de.akquinet.jbosscc.needle.junit.AbstractDatabaseRuleBuilder;

public class DatabaseTestRuleBuilder extends AbstractDatabaseRuleBuilder<DatabaseTestRuleBuilder, DatabaseTestRule> {

    @Override
    protected DatabaseTestRule createRule(final NeedleConfiguration needleConfiguration) {
        return new DatabaseTestRule(needleConfiguration);
    }

}
