package de.akquinet.jbosscc.needle.injection;

import javax.persistence.EntityManager;

import de.akquinet.jbosscc.needle.db.DatabaseRule;

public class EntityManagerProvider implements InjectionProvider<EntityManager> {

	private final DatabaseRule databaseRule;



	public EntityManagerProvider(final DatabaseRule databaseRule) {
		super();
		this.databaseRule = databaseRule;
	}

	@Override
	public EntityManager get() {
		return databaseRule.getEntityManager();
	}

	@Override
	public Class<EntityManager> getType() {
		return EntityManager.class;
	}

}
