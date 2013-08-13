package de.akquinet.jbosscc.needle.junit.testrule;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import de.akquinet.jbosscc.needle.configuration.NeedleConfiguration;
import de.akquinet.jbosscc.needle.db.DatabaseTestcase;
import de.akquinet.jbosscc.needle.db.operation.DBOperation;

public class DatabaseTestRule extends DatabaseTestcase implements TestRule {

    /**
     * @see de.akquinet.jbosscc.needle.db.DatabaseTestcase#DatabaseTestcase()
     */
    public DatabaseTestRule() {
        super();
    }

    /**
     * @see de.akquinet.jbosscc.needle.db.DatabaseTestcase#DatabaseTestcase(DBOperation)
     */
    public DatabaseTestRule(final DBOperation dbOperation) {
        super(dbOperation);
    }

    /**
     * @see de.akquinet.jbosscc.needle.db.DatabaseTestcase#DatabaseTestcase(String,
     *      DBOperation)
     */
    public DatabaseTestRule(final String persistenceUnitName, final DBOperation dbOperation) {
        super(persistenceUnitName, dbOperation);
    }

    /**
     * @see de.akquinet.jbosscc.needle.db.DatabaseTestcase#DatabaseTestcase(String)
     */
    public DatabaseTestRule(final String persistenceUnitName) {
        super(persistenceUnitName);
    }
    
    DatabaseTestRule(final NeedleConfiguration configuration) {
        super(configuration);
    }
    

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    before();
                    base.evaluate();
                } finally {
                    after();
                }
            }
        };
    }

}
