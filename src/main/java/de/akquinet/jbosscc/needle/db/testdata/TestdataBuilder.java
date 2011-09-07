package de.akquinet.jbosscc.needle.db.testdata;

public interface TestdataBuilder<T> {

	T build();

	T buildAndSave();

}
