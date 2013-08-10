package de.akquinet.jbosscc.needle.junit.builder;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.junit.Rule;
import org.junit.Test;
import org.testng.Assert;

import de.akquinet.jbosscc.needle.junit.DatabaseRule;
import de.akquinet.jbosscc.needle.junit.NeedleRule;
import de.akquinet.jbosscc.needle.junit.NeedleRuleBuilder;

public class NeedleBuilderWithOuterRulesTest {
    
    @Rule
    public NeedleRule rule = new NeedleRuleBuilder().withOuter(new DatabaseRule()).build();
    
    @Inject
    private EntityManager entityManager;
    
    @Test
    public void testDatabaseOuterRule() throws Exception {
        Assert.assertSame(new DatabaseRule().getEntityManager(), entityManager);
    }
}
