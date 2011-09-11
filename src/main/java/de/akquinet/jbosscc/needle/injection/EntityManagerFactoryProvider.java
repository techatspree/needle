package de.akquinet.jbosscc.needle.injection;

import javax.persistence.EntityManagerFactory;

import de.akquinet.jbosscc.needle.db.DatabaseTestcase;

public class EntityManagerFactoryProvider implements InjectionProvider<EntityManagerFactory> {

	private final DatabaseTestcase databaseTestcase;
	private InjectionVerifier verifier;

	public EntityManagerFactoryProvider(final DatabaseTestcase databaseTestcase) {
		super();
		this.databaseTestcase = databaseTestcase;
		verifier = new InjectionVerifier() {

			@Override
			public boolean verify(final InjectionTargetInformation information) {
				if (information.getType() == EntityManagerFactory.class) {
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

	@Override
	public EntityManagerFactory getInjectedObject(final Class<?> type) {
		return databaseTestcase.getEntityManagerFactory();
	}

	@Override
	public boolean verify(final InjectionTargetInformation injectionTargetInformation) {
		return verifier.verify(injectionTargetInformation);
	}

	@Override
	public Object getKey(final InjectionTargetInformation injectionTargetInformation) {
		return EntityManagerFactory.class;
	}
}
