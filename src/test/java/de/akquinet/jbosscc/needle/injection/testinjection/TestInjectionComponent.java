package de.akquinet.jbosscc.needle.injection.testinjection;

import java.net.Authenticator;

import javax.ejb.EJB;
import javax.inject.Inject;

import de.akquinet.jbosscc.needle.MyEjbComponent;

public class TestInjectionComponent {

	@Inject
	private Authenticator authenticator;

	@EJB
	private MyEjbComponent ejbComponent;

	public Authenticator getAuthenticator() {
		return authenticator;
	}

	public MyEjbComponent getEjbComponent() {
		return ejbComponent;
	}

}
