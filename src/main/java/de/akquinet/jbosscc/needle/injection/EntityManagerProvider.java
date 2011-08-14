package de.akquinet.jbosscc.needle.injection;

import java.lang.reflect.Field;

import javax.persistence.EntityManager;

import de.akquinet.jbosscc.needle.db.DatabaseTestcase;

public class EntityManagerProvider implements InjectionProvider {

	private final DatabaseTestcase databaseTestcase;

	private final InjectionVerifier verifyer;

	public EntityManagerProvider(final DatabaseTestcase databaseTestcase) {
		super();
		this.databaseTestcase = databaseTestcase;
		verifyer = new InjectionVerifier() {

			@Override
			public boolean verify(Field field) {
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

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> type) {
		return (T) databaseTestcase.getEntityManager();
	}

	@Override
	public boolean verify(Field field) {
		return verifyer.verify(field);
	}

	@Override
    public Object getKey(final Field field) {
	    return EntityManager.class;
    }

}
