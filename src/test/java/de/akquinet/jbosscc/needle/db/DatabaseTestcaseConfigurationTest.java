package de.akquinet.jbosscc.needle.db;

import javax.persistence.EntityManager;

import junit.framework.Assert;

import org.junit.Test;

import de.akquinet.jbosscc.needle.db.operation.AbstractDBOperation;
import de.akquinet.jbosscc.needle.db.operation.ExecuteScriptOperation;
import de.akquinet.jbosscc.needle.db.operation.JdbcConfiguration;
import de.akquinet.jbosscc.needle.db.operation.hsql.HSQLDeleteOperation;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

public class DatabaseTestcaseConfigurationTest {

	@Test(expected = RuntimeException.class)
	public void testEntityManager_Close() throws Exception {
		DatabaseTestcaseConfiguration databaseRuleConfiguration = new DatabaseTestcaseConfiguration("TestDataModel");
		EntityManager entityManager = databaseRuleConfiguration.getEntityManager();
		Assert.assertNotNull(entityManager);

		entityManager.close();
	}

	@Test
	public void testGetEntityManagerFactoryProperties() throws Exception {
		DatabaseTestcaseConfiguration databaseRuleConfiguration = new DatabaseTestcaseConfiguration();

		JdbcConfiguration jdbcConfiguration = (JdbcConfiguration) ReflectionUtil.invokeMethod(
		        databaseRuleConfiguration, "getEntityManagerFactoryProperties");

		Assert.assertNotNull(jdbcConfiguration);
	}


	@Test
	public void testLookupDBOperationClassClass_HSQLDeleteOperation() throws Exception {
		Class<? extends AbstractDBOperation> dbDialectClass = DatabaseTestcaseConfiguration.lookupDBOperationClass(HSQLDeleteOperation.class.getName());
		Assert.assertEquals(HSQLDeleteOperation.class, dbDialectClass);
	}

	@Test
	public void testLookupDBOperationClassClass_UnknownClass() throws Exception {
		Class<? extends AbstractDBOperation> dbDialectClass = DatabaseTestcaseConfiguration.lookupDBOperationClass("unknowm");
		Assert.assertNull(dbDialectClass);
	}

	@Test
	public void testLookupDBOperationClassClass_Null() throws Exception {
		Class<? extends AbstractDBOperation> dbDialectClass = DatabaseTestcaseConfiguration.lookupDBOperationClass(null);
		Assert.assertNull(dbDialectClass);
	}


	@Test
	public void testCreateDBOperation() throws Exception {
		DatabaseTestcaseConfiguration configuration = new DatabaseTestcaseConfiguration();
		AbstractDBOperation operation = configuration.createDBOperation(ExecuteScriptOperation.class);

		Assert.assertTrue(operation instanceof ExecuteScriptOperation);
	}

	@Test
	public void testCreateDBOperation_Null() throws Exception {
		DatabaseTestcaseConfiguration configuration = new DatabaseTestcaseConfiguration();
		Assert.assertNull(configuration.createDBOperation(null));
	}
}
