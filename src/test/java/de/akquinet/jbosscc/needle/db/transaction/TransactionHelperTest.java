package de.akquinet.jbosscc.needle.db.transaction;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.akquinet.jbosscc.needle.db.Address;
import de.akquinet.jbosscc.needle.db.Person;
import de.akquinet.jbosscc.needle.db.PersonTestdataBuilder;
import de.akquinet.jbosscc.needle.db.User;
import de.akquinet.jbosscc.needle.db.configuration.PersistenceConfigurationFactory;

public class TransactionHelperTest {

    private EntityManager entityManager;

    private TransactionHelper objectUnderTest;

    @Before
    public void setup() {
        PersistenceConfigurationFactory persistence = new PersistenceConfigurationFactory("TestDataModel");
        entityManager = persistence.getEntityManager();
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
}
