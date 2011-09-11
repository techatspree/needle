package de.akquinet.jbosscc.needle.reflection;

import java.util.Collection;

public class SampleClass {

	@MyAnnotation
	private String aPrivateField;

	private Collection<?> collectionField2;

	public String getPrivateField() {
		return aPrivateField;
	}
}
