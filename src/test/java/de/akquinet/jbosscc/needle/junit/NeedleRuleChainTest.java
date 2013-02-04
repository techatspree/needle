package de.akquinet.jbosscc.needle.junit;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.Mock;

public class NeedleRuleChainTest {

    @Rule
    public NeedleRule needleRule = new NeedleRule().withOuter(new DatabaseRule());

    @Inject
    private EntityManager entityManager;

    @Mock
    private Runnable runnableMock;

    @Test
    public void testDtabaseRuleInjection() throws Exception {
        assertNotNull(entityManager);
    }
    
    @Test
    public void testMockCreation() throws Exception {
        assertNotNull(runnableMock);
    }

}
