package de.akquinet.jbosscc.needle;

import java.util.Queue;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class MyComponentBean implements MyComponent {

	@PersistenceContext
	private EntityManager entityManager;

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
	public String testMock(){
		return myEjbComponent.doSomething();
	}


}
