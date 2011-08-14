package de.akquinet.jbosscc.needle;

public class MyEjbComponentBean implements MyEjbComponent {

	@Override
    public String doSomething() {
	    return "Hello World";
    }

}
