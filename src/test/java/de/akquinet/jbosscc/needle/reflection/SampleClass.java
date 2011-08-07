package de.akquinet.jbosscc.needle.reflection;

public class SampleClass {

	@MyAnnotation
	private String aPrivateField;

	public String getPrivateField() {
		return aPrivateField;
	}
}
