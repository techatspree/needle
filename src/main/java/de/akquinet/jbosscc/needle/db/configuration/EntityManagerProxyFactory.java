package de.akquinet.jbosscc.needle.db.configuration;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.persistence.EntityManager;

class EntityManagerProxyFactory {
	static EntityManager createProxy(final EntityManager real) {
		return (EntityManager) Proxy.newProxyInstance(PersistenceConfigurationFactory.class.getClassLoader(),
		        new Class[] { EntityManager.class }, new InvocationHandler() {
			        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				        if (method.getName().equals("close")) {
					        throw new RuntimeException("you are not allowed to explicitely close this EntityManager");
				        }

				        try {
					        return method.invoke(real, args);
				        } catch (InvocationTargetException e) {
					        throw e.getCause();
				        }
			        }
		        });
	}
}
