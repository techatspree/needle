package de.akquinet.jbosscc.needle.mock;

public interface MockProvider {

	<T> T createMock(Class<T> type);

}
