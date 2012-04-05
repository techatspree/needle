package de.akquinet.jbosscc.needle.db.transaction;

import javax.persistence.EntityManager;

/**
 * Interface for passing algorithms to executeInTransaction().
 * 
 * @param <T>
 *          - return type of run()
 */
public interface Runnable<T> {
  /**
   * Operation to be automatically called inside executeInTransaction().
   * 
   * @param entityManager
   *          an {@link javax.persistence.EntityManager}
   * @return return value of the operation
   * @throws Exception
   *           thrown when something failed
   */
  T run(EntityManager entityManager) throws Exception;
}