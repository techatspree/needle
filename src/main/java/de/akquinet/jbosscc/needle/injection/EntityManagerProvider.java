package de.akquinet.jbosscc.needle.injection;

import java.lang.reflect.Field;

import javax.persistence.EntityManager;

import de.akquinet.jbosscc.needle.db.DatabaseTestcase;

public class EntityManagerProvider implements InjectionProvider<EntityManager> {

  private final DatabaseTestcase databaseTestcase;

  private final InjectionVerifier verifyer;

  public EntityManagerProvider(final DatabaseTestcase databaseTestcase) {
    super();
    this.databaseTestcase = databaseTestcase;
    verifyer = new InjectionVerifier() {

      @Override
      public boolean verify(final Field field) {
        if (field.getType() == EntityManager.class) {
          return true;
        }
        return false;
      }
    };

  }

  public EntityManagerProvider(final InjectionVerifier verifyer, final DatabaseTestcase databaseRule) {
    super();
    this.databaseTestcase = databaseRule;
    this.verifyer = verifyer;
  }

  @Override
  public EntityManager getInjectedObject(final Class<?> type) {
    return databaseTestcase.getEntityManager();
  }

  @Override
  public boolean verify(final Field field) {
    return verifyer.verify(field);
  }

  @Override
  public Object getKey(final Field field) {
    return EntityManager.class;
  }
}
