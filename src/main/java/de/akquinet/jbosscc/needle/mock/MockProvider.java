package de.akquinet.jbosscc.needle.mock;

public interface MockProvider {

	<T> T createMockComponent(Class<T> type);

}
