package de.akquinet.jbosscc.needle.db;

import javax.persistence.EntityManagerFactory;

import de.akquinet.jbosscc.needle.injection.InjectionProvider;
import de.akquinet.jbosscc.needle.injection.InjectionTargetInformation;
import de.akquinet.jbosscc.needle.injection.InjectionVerifier;

class EntityManagerFactoryProvider implements InjectionProvider<EntityManagerFactory> {

	private final DatabaseTestcase databaseTestcase;
	private InjectionVerifier verifier;

	EntityManagerFactoryProvider(final DatabaseTestcase databaseTestcase) {
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
