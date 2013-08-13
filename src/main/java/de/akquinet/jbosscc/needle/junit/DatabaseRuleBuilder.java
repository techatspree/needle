package de.akquinet.jbosscc.needle.junit;

import de.akquinet.jbosscc.needle.configuration.NeedleConfiguration;

public class DatabaseRuleBuilder extends AbstractDatabaseRuleBuilder<DatabaseRuleBuilder, DatabaseRule> {

    @Override
    protected DatabaseRule createRule(final NeedleConfiguration needleConfiguration) {
        return new DatabaseRule(needleConfiguration);
    }

}
