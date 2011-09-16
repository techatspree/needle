package de.akquinet.jbosscc.needle.db.dialect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.db.Address;
import de.akquinet.jbosscc.needle.db.Person;
import de.akquinet.jbosscc.needle.db.transaction.VoidRunnable;
import de.akquinet.jbosscc.needle.junit.DatabaseRule;

public class HSQLDialectTest {

	@Rule
	public DatabaseRule databaseRule = new DatabaseRule();

	private HSQLDialect dialect = new HSQLDialect(new DBDialectConfiguration("jdbc:hsqldb:mem:memoryDB",
	        "org.hsqldb.jdbcDriver", "sa", ""));

	@Before
	public void setUp() throws Exception {
		// Hibernate mapping
		databaseRule.getEntityManager();

		Connection connection = dialect.getConnection();

		Assert.assertNotNull(connection);

	}

	@Test
	public void testGetTableNames() throws Exception {
		List<String> tableNames = dialect.getTableNames();

		Assert.assertEquals(2, tableNames.size());
		Assert.assertTrue(tableNames.contains(Person.TABLE_NAME));
		Assert.assertTrue(tableNames.contains(Address.TABLE_NAME));

	}

	@Test
	public void testDisableReferentialIntegrity() throws Exception {

		dialect.disableReferentialIntegrity();
		insertAddressWithInvalidFk();

	}

	@Test(expected = SQLException.class)
	public void testEnableReferentialIntegrity() throws Exception {

		dialect.disableReferentialIntegrity();
		dialect.enableReferentialIntegrity();
		insertAddressWithInvalidFk();
	}

	private void insertAddressWithInvalidFk() throws Exception {
		List<String> table = new ArrayList<String>();
		table.add(Address.TABLE_NAME);
		dialect.deleteContent(table);
		final Address address = new Address();

		databaseRule.getTransactionHelper().executeInTransaction(new VoidRunnable() {

			@Override
			public void doRun(EntityManager entityManager) throws Exception {
				entityManager.persist(address);
				entityManager.flush();

			}
		});
		dialect.commit();

		Statement st = dialect.getConnection().createStatement();
		int executeUpdate = st.executeUpdate("update " + Address.TABLE_NAME + " set person_id = 2");
		Assert.assertEquals(executeUpdate, 1);
		st.close();
		dialect.commit();
	}

	@Test
	public void testDeleteContent() throws Exception {
		databaseRule.getTransactionHelper().executeInTransaction(new VoidRunnable() {

			@Override
			public void doRun(EntityManager entityManager) throws Exception {
				entityManager.persist(new Address());

			}
		});

		Statement st = dialect.getConnection().createStatement();
		ResultSet rs = st.executeQuery("select * from " + Address.TABLE_NAME);
		Assert.assertTrue(rs.next());
		rs.close();
		st.close();

		List<String> tableNames = new ArrayList<String>();
		tableNames.add(Address.TABLE_NAME);
		dialect.deleteContent(tableNames);

		st = dialect.getConnection().createStatement();
		st.executeQuery("select * from " + Address.TABLE_NAME);
		Assert.assertFalse(rs.next());
		rs.close();
		st.close();

	}

	@After
	public void tearDown() throws Exception {
		dialect.closeConnection();
	}

}
