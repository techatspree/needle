package de.akquinet.jbosscc.needle.reflection;

import java.util.Collection;

public class DerivedClass extends SampleClass {

	private String field;

	private Boolean booleanField;

	private Collection<?> collectionField2;

	public Boolean getBooleanField() {
		return booleanField;
	}

	@Override
	public void toOverride() {
		super.toOverride();
	}

	@SuppressWarnings("unused")
	private boolean testInvokeWithPrimitive(final int intValue, final float floatValue, final char charValue,
	        final boolean booleanValue, final long longValue, final byte byteValue, final short shortValue,
	        final double doubleValue) {
		return true;
	}

	@SuppressWarnings("unused")
	private boolean testInvokeWithObjects(final Integer intValue, final Float floatValue, final Character charValue,
	        final Boolean booleanValue, final Long longValue, final Byte byteValue, final Short shortValue,
	        final Double doubleValue) {
		return true;
	}

}
