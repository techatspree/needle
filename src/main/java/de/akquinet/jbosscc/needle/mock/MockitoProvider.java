package de.akquinet.jbosscc.needle.mock;

import org.mockito.Mockito;

public class MockitoProvider implements MockProvider {

	@Override
    public <T> T createMockComponent(Class<T> type) {
	    return Mockito.mock(type);
    }

}
