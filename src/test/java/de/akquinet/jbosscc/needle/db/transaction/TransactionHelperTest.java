package de.akquinet.jbosscc.needle.db.transaction;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.akquinet.jbosscc.needle.db.Address;
import de.akquinet.jbosscc.needle.db.Person;
import de.akquinet.jbosscc.needle.db.PersonTestdataBuilder;
import de.akquinet.jbosscc.needle.db.User;

public class TransactionHelperTest {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    private TransactionHelper objectUnderTest;

    @Before
    public void setup() {
        entityManagerFactory = Persistence.createEntityManagerFactory("TestDataModel");
        entityManager = entityManagerFactory.createEntityManager();
        objectUnderTest = new TransactionHelper(entityManager);
    }

    @Test
    public void testLoadAllObjects_WithEntityName() throws Exception {
        Person entity = objectUnderTest.persist(new PersonTestdataBuilder().build());
        Assert.assertNotNull(entity.getId());

        List<Person> loadAllObjects = objectUnderTest.loadAllObjects(Person.class);
        Assert.assertEquals(1, loadAllObjects.size());

    }

    @Test
    public void testLoadAllObjects_WithDefaultEntityName() throws Exception {
        User entity = objectUnderTest.persist(new User());
        Assert.assertNotNull(entity.getId());

        List<User> loadAllObjects = objectUnderTest.loadAllObjects(User.class);
        Assert.assertEquals(1, loadAllObjects.size());
    }
    
    @Test
    public void testLoadAllObjects_EmptyResultList() throws Exception {
        List<Address> loadAllObjects = objectUnderTest.loadAllObjects(Address.class);
        Assert.assertEquals(0, loadAllObjects.size());
    }


    @Test(expected = IllegalArgumentException.class)
    public void testLoadAllObjects_WithUnknownEntity() throws Exception {
        objectUnderTest.persist(new Object());
    }

    @After
    public void tearDown() {
        if (entityManager != null) {
            entityManager.close();
        }

        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

}
