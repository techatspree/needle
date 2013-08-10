package de.akquinet.jbosscc.needle.db;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import org.junit.Test;

import de.akquinet.jbosscc.needle.configuration.PropertyBasedConfigurationFactory;
import de.akquinet.jbosscc.needle.db.operation.AbstractDBOperation;
import de.akquinet.jbosscc.needle.db.operation.ExecuteScriptOperation;
import de.akquinet.jbosscc.needle.db.operation.JdbcConfiguration;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

public class DatabaseTestcaseConfigurationTest {

    @Test(expected = RuntimeException.class)
    public void testEntityManager_Close() throws Exception {
        DatabaseTestcaseConfiguration databaseRuleConfiguration = new DatabaseTestcaseConfiguration(
                PropertyBasedConfigurationFactory.get(), "TestDataModel");
        EntityManager entityManager = databaseRuleConfiguration.getEntityManager();
        assertNotNull(entityManager);

        entityManager.close();
    }

    @Test
    public void testGetEntityManagerFactoryProperties() throws Exception {
        DatabaseTestcaseConfiguration databaseRuleConfiguration = new DatabaseTestcaseConfiguration(PropertyBasedConfigurationFactory.get());

        JdbcConfiguration jdbcConfiguration = (JdbcConfiguration) ReflectionUtil.invokeMethod(
                databaseRuleConfiguration, "getEntityManagerFactoryProperties");

        assertNotNull(jdbcConfiguration);
    }

    @Test
    public void testCreateDBOperation() throws Exception {
        DatabaseTestcaseConfiguration configuration = new DatabaseTestcaseConfiguration(PropertyBasedConfigurationFactory.get());
        AbstractDBOperation operation = configuration.createDBOperation(ExecuteScriptOperation.class);

        assertTrue(operation instanceof ExecuteScriptOperation);
    }

    @Test
    public void testCreateDBOperation_Null() throws Exception {
        DatabaseTestcaseConfiguration configuration = new DatabaseTestcaseConfiguration(PropertyBasedConfigurationFactory.get());
        assertNull(configuration.createDBOperation(null));
    }
}
