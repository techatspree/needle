package de.akquinet.jbosscc.needle.junit.testrule;

import static org.junit.Assert.assertNotNull;

import org.junit.Rule;
import org.junit.Test;

public class DatabaseTestRuleTest {
    
    @Rule
    public DatabaseTestRule databaseTestRule = new DatabaseTestRule();
    
    @Test
    public void testEntityManager(){
        assertNotNull(databaseTestRule.getEntityManager());
    }
}
