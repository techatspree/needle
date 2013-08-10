package de.akquinet.jbosscc.needle.db.operation.hsql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.db.Address;
import de.akquinet.jbosscc.needle.db.operation.JdbcConfiguration;
import de.akquinet.jbosscc.needle.db.transaction.VoidRunnable;
import de.akquinet.jbosscc.needle.junit.DatabaseRule;

public class HSQLDeleteOperationTest {

	private static final JdbcConfiguration HSQL_DB_CONFIGURATION = new JdbcConfiguration("jdbc:hsqldb:mem:memoryDB",
	        "org.hsqldb.jdbcDriver", "sa", "");

	@Rule
	public DatabaseRule databaseRule = new DatabaseRule();



	private HSQLDeleteOperationForTest hsqlDeleteOperation = new HSQLDeleteOperationForTest(HSQL_DB_CONFIGURATION) {
		public java.sql.Connection getConnection() throws SQLException {
			return super.getConnection();
		};
	};


	@Test
	public void testDisableReferentialIntegrity() throws Exception {
		Statement statement = hsqlDeleteOperation.getConnection().createStatement();
		hsqlDeleteOperation.disableReferentialIntegrity(statement);
		insertAddressWithInvalidFk();

		statement.close();

	}

	@Test(expected = SQLException.class)
	public void testEnableReferentialIntegrity() throws Exception {
		Statement statement = null;
		try {
			statement = hsqlDeleteOperation.getConnection().createStatement();
			hsqlDeleteOperation.enableReferentialIntegrity(statement);
			insertAddressWithInvalidFk();
		} finally {
			statement.close();
		}

	}

	private void insertAddressWithInvalidFk() throws Exception {
		Statement statement = null;
		try {

			statement = hsqlDeleteOperation.getConnection().createStatement();
			List<String> table = new ArrayList<String>();
			table.add(Address.TABLE_NAME);
			final Address address = new Address();

			databaseRule.getTransactionHelper().executeInTransaction(new VoidRunnable() {

				@Override
				public void doRun(EntityManager entityManager) throws Exception {
					entityManager.persist(address);
					entityManager.flush();

				}
			});
			hsqlDeleteOperation.commit();

			Statement st = hsqlDeleteOperation.getConnection().createStatement();
			int executeUpdate = st.executeUpdate("update " + Address.TABLE_NAME + " set person_id = 2");
			Assert.assertEquals(1, executeUpdate);
			st.close();
			hsqlDeleteOperation.commit();
		} finally {
			statement.close();
		}
	}

	@Test
	public void testDeleteContent() throws Exception {
		Statement statement = null;
		try {
			statement = hsqlDeleteOperation.getConnection().createStatement();
			databaseRule.getTransactionHelper().executeInTransaction(new VoidRunnable() {

				@Override
				public void doRun(EntityManager entityManager) throws Exception {
					entityManager.persist(new Address());

				}
			});

			Statement st = hsqlDeleteOperation.getConnection().createStatement();
			ResultSet rs = st.executeQuery("select * from " + Address.TABLE_NAME);
			Assert.assertTrue(rs.next());


			List<String> tableNames = new ArrayList<String>();
			tableNames.add(Address.TABLE_NAME);
			hsqlDeleteOperation.deleteContent(tableNames, statement);

			st = hsqlDeleteOperation.getConnection().createStatement();
			st.executeQuery("select * from " + Address.TABLE_NAME);
			Assert.assertFalse(rs.next());
			rs.close();
			st.close();


		} finally {
			statement.close();
		}

	}

	@After
	public void tearDown() throws Exception {
		hsqlDeleteOperation.closeConnection();
	}

}

class HSQLDeleteOperationForTest extends HSQLDeleteOperation {

	public HSQLDeleteOperationForTest(JdbcConfiguration configuration) {
		super(configuration);
	}

	@Override
	protected Connection getConnection() throws SQLException {
		return super.getConnection();
	}

	@Override
	protected void closeConnection() throws SQLException {
		super.closeConnection();
	}

	@Override
	protected void commit() throws SQLException {
		super.commit();
	}

	@Override
	protected void rollback() throws SQLException {
		super.rollback();
	}

}
