package de.akquinet.jbosscc.needle.db.testdata;

import javax.persistence.EntityManager;

import de.akquinet.jbosscc.needle.db.transaction.TransactionHelper;

public abstract class AbstractTestdataBuilder<T> implements TestdataBuilder<T> {
	private static int count = 0;

	private EntityManager entityManager;

	private TransactionHelper transactionHelper;

	public AbstractTestdataBuilder(final EntityManager entityManager) {
        this.entityManager = entityManager;
        this.transactionHelper = new TransactionHelper(entityManager);
    }

    public AbstractTestdataBuilder() {
    }

    protected final EntityManager getEntityManager() {
        return entityManager;
    }

    protected final boolean hasEntityManager() {
        return entityManager != null;
    }

    protected final void ensureEntityManager() {
        if (entityManager == null) {
            throw new IllegalStateException("cannot persist w/o entity manager!");
        }
    }

    @Override
    public final T buildAndSave() {
    	ensureEntityManager();

        try {
        	return transactionHelper.saveObject(build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected final int getId() {
        return count++;
    }
}
