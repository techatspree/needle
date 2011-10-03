package de.akquinet.jbosscc.needle.junit;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import de.akquinet.jbosscc.needle.db.DatabaseTestcase;
import de.akquinet.jbosscc.needle.db.operation.DBOperation;

/**
 * The {@link DatabaseRule} provides access to the configured Database and
 * execute optional configured {@link DBOperation} before and after a test.
 *
 * <pre>
 * public class EntityTestcase {
 * 	&#064;Rule
 * 	public DatabaseRule databaseRule = new DatabaseRule();
 *
 * 	&#064;Test
 * 	public void testPersist() throws Exception {
 * 		User user = new User();
 * 		// ...
 * 		databaseRule.getEntityMnager().persist(user);
 * 	}
 * }
 * </pre>
 *
 */
public class DatabaseRule extends DatabaseTestcase implements MethodRule {

	public DatabaseRule() {
		super();
	}

	public DatabaseRule(final Class<?>... clazzes) {
		super(clazzes);
	}

	public DatabaseRule(final DBOperation dbOperation, final Class<?>... clazzes) {
		super(dbOperation, clazzes);
	}

	public DatabaseRule(final DBOperation dbOperation) {
		super(dbOperation);
	}

	public DatabaseRule(final String puName, final DBOperation dbOperation) {
		super(puName, dbOperation);
	}

	public DatabaseRule(final String puName) {
		super(puName);
	}

	@Override
	public final Statement apply(final Statement base, FrameworkMethod method, Object target) {
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
