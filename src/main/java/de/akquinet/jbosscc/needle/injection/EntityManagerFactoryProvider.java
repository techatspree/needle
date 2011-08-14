package de.akquinet.jbosscc.needle.injection;

import java.lang.reflect.Field;

import javax.persistence.EntityManagerFactory;

import de.akquinet.jbosscc.needle.db.DatabaseTestcase;

public class EntityManagerFactoryProvider implements InjectionProvider {

	private final DatabaseTestcase databaseTestcase;

	private final InjectionVerifier verifier;

	public EntityManagerFactoryProvider(final DatabaseTestcase databaseTestcase) {
		super();
		this.databaseTestcase = databaseTestcase;
		verifier = new InjectionVerifier() {

			@Override
			public boolean verify(Field field) {
				if (field.getType() == EntityManagerFactory.class) {
					return true;
				}
				return false;
			}
		};

	}

	public EntityManagerFactoryProvider(final InjectionVerifier verifyer, final DatabaseTestcase databaseTestcase) {
		super();
		this.databaseTestcase = databaseTestcase;
		this.verifier = verifyer;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> type) {
		return (T) databaseTestcase.getEntityManagerFactory();
	}

	@Override
	public boolean verify(Field field) {
		return verifier.verify(field);
	}

	@Override
	public Object getKey(final Field field) {
		return EntityManagerFactory.class;
	}

}
