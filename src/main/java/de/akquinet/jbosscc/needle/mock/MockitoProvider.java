package de.akquinet.jbosscc.needle.mock;

import org.mockito.Mockito;

public class MockitoProvider implements MockProvider {

	@Override
    public <T> T createMock(Class<T> type) {
	    return Mockito.mock(type);
    }

}
