package de.akquinet.jbosscc.needle;

public class MyEjbComponentBean implements MyEjbComponent {

	private String testInjection;

	@Override
	public String doSomething() {
		return "Hello World";
	}

	public String getTestInjection() {
		return testInjection;
	}

}
