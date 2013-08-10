package de.akquinet.jbosscc.needle.junit;

import javax.persistence.PersistenceException;

import org.junit.Test;

public class DatabaseRuleBuilderTest {
    
    @Test(expected = PersistenceException.class)
    public void testCustomConfiguration_WithNoPersistenceProvider() throws Exception {
        DatabaseRule databaseRule = new DatabaseRuleBuilder().with("database-custom").build();
        databaseRule.getEntityManagerFactory();
        
        new DatabaseRule();
    }

}
