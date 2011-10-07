package de.akquinet.jbosscc.needle.db;

import javax.persistence.EntityManager;

import junit.framework.Assert;

import org.junit.Test;

import de.akquinet.jbosscc.needle.db.operation.JdbcConfiguration;
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

}
