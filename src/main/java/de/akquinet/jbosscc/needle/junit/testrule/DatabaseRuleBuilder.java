package de.akquinet.jbosscc.needle.junit.testrule;

import de.akquinet.jbosscc.needle.configuration.NeedleConfiguration;
import de.akquinet.jbosscc.needle.junit.AbstractDatabaseRuleBuilder;

public class DatabaseRuleBuilder extends AbstractDatabaseRuleBuilder<DatabaseRuleBuilder, DatabaseTestRule> {

    @Override
    protected DatabaseTestRule createRule(final NeedleConfiguration needleConfiguration) {
        return new DatabaseTestRule(needleConfiguration);
    }

}
