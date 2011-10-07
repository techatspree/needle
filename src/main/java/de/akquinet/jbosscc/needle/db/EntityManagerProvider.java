package de.akquinet.jbosscc.needle.db;

import javax.persistence.EntityManager;

import de.akquinet.jbosscc.needle.injection.InjectionProvider;
import de.akquinet.jbosscc.needle.injection.InjectionTargetInformation;
import de.akquinet.jbosscc.needle.injection.InjectionVerifier;

class EntityManagerProvider implements InjectionProvider<EntityManager> {

	private final DatabaseTestcase databaseTestcase;

	private final InjectionVerifier verifyer;

	public EntityManagerProvider(final DatabaseTestcase databaseTestcase) {
		super();
		this.databaseTestcase = databaseTestcase;
		verifyer = new InjectionVerifier() {

			@Override
			public boolean verify(final InjectionTargetInformation information) {
				if (information.getType() == EntityManager.class) {
					return true;
				}
				return false;
			}
		};

	}


	@Override
	public EntityManager getInjectedObject(final Class<?> type) {
		return databaseTestcase.getEntityManager();
	}

	@Override
	public boolean verify(final InjectionTargetInformation injectionTargetInformation) {
		return verifyer.verify(injectionTargetInformation);
	}

	@Override
	public Object getKey(final InjectionTargetInformation injectionTargetInformation) {
		return EntityManager.class;
	}
}
