package de.akquinet.jbosscc.needle.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.junit.DatabaseRule;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

public class DatabaseRuleEntityTransactionInjectionTest {

    @Rule
    public NeedleRule needleRule = new NeedleRule().withOuter(new DatabaseRule());

    @Inject
    private EntityManager entityManager;

    @Inject
    private EntityTransaction entityTransaction;

    @Test
    public void testEntityTransaction() {
        entityTransaction.begin();
        User user1 = new User();
        entityManager.persist(user1);
        entityTransaction.commit();
        entityManager.clear();

        entityTransaction.begin();
        User user2 = new User();
        entityManager.persist(user2);
        entityTransaction.commit();
        entityManager.clear();

        User user1FromDb = entityManager.find(User.class, user1.getId());
        assertEquals(user1.getId(), user1FromDb.getId());
        assertNotEquals(user1, user1FromDb);

        User user2FromDb = entityManager.find(User.class, user2.getId());
        assertEquals(user2.getId(), user2FromDb.getId());
        assertNotEquals(user2, user2FromDb);
    }

}
