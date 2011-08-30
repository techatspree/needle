package de.akquinet.jbosscc.needle.db;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.db.transaction.TransactionHelper;
import de.akquinet.jbosscc.needle.junit.DatabaseRule;

public class DBPersistenceUnitTest {

  @Rule
  public DatabaseRule db = new DatabaseRule("TestDataModel");

  @Test
  public void testDB_withPersistenceUnit() throws Exception {
    final MyEntity myEntity = new MyEntity();
    final EntityManager entityManager = db.getEntityManager();

    myEntity.setMyName("My Name");

    assertNotNull(db);
    assertNotNull(entityManager);

    final EntityTransaction tx = entityManager.getTransaction();
    tx.begin();
    entityManager.persist(myEntity);

    final MyEntity fromDB = entityManager.find(MyEntity.class, myEntity.getId());

    assertSame(myEntity, fromDB);
    tx.commit();
  }

  @Test
  public void testTransactions() throws Exception {
    final MyEntity myEntity = new MyEntity();
    final EntityManager entityManager = db.getEntityManager();

    myEntity.setMyName("My Name");

    final TransactionHelper transactionHelper = new TransactionHelper(entityManager);

    transactionHelper.saveObject(myEntity);
    final MyEntity fromDb = transactionHelper.loadObject(MyEntity.class, myEntity.getId());

    assertFalse(myEntity == fromDb);
  }
}
