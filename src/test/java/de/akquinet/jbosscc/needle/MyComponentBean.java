package de.akquinet.jbosscc.needle;

import java.util.Queue;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

public class MyComponentBean implements MyComponent {

	@PersistenceContext
	private EntityManager entityManager;

	@Inject
	private EntityManagerFactory entityManagerFactory;

	@EJB
	private MyEjbComponent myEjbComponent;

	@Resource
	private SessionContext sessionContext;

	@Resource(mappedName = "queue1")
	private Queue queue1;

	@Resource(mappedName = "queue2")
	private Queue queue2;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public MyEjbComponent getMyEjbComponent() {
		return myEjbComponent;
	}

	@Override
	public String testMock() {
		queue2.clear();
		return myEjbComponent.doSomething();
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	public SessionContext getSessionContext() {
    	return sessionContext;
    }

	public Queue getQueue1() {
    	return queue1;
    }

	public Queue getQueue2() {
    	return queue2;
    }
	
	

}
