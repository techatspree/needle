package de.akquinet.jbosscc.needle.junit;

import static de.akquinet.jbosscc.needle.junit.NeedleBuilders.databaseTestRule;
import static de.akquinet.jbosscc.needle.junit.NeedleBuilders.needleTestRule;
import static de.akquinet.jbosscc.needle.junit.NeedleBuilders.outerRule;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.mock.MockitoProvider;

public class NeedleBuildersTest {

    @Rule
    public TestRule rule = outerRule(needleTestRule(this).build()).around(databaseTestRule().build());

    @ObjectUnderTest()
    private Runnable runnable = new Runnable() {

        @Inject
        private Instance<Runnable> instance;

        @Override
        public void run() {
            instance.get();

        }
    };

    @Inject
    private MockitoProvider mockitoProvider;

    @Inject
    private EntityManager entityManager;

    @Test
    public void testRuleChain() throws Exception {
        Assert.assertNotNull(mockitoProvider);
        Assert.assertNotNull(runnable);
        Assert.assertNotNull(entityManager);
    }

}
