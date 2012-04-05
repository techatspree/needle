package de.akquinet.jbosscc.needle.db.transaction;

import javax.persistence.EntityManager;

/**
 * Default implementation of {@link Runnable}. Does nothing.
 */
public abstract class VoidRunnable implements Runnable<Object> {
  /**
   * {@inheritDoc}
   */
  @Override
  public final Object run(final EntityManager entityManager) throws Exception {
    doRun(entityManager);
    return null;
  }

  /**
   * Hook method inside run().
   * 
   * @param entityManager
   *          entityManager
   * 
   * @throws Exception
   *           when something failed
   */
  public abstract void doRun(EntityManager entityManager) throws Exception;
}