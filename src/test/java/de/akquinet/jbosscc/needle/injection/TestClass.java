package de.akquinet.jbosscc.needle.injection;

import javax.inject.Inject;

public class TestClass {

	@Inject
	private String string;

	public String getString() {
    	return string;
    }
}
