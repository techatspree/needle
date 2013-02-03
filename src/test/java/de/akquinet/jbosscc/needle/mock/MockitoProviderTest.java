package de.akquinet.jbosscc.needle.mock;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.Mockito;

public class MockitoProviderTest {

	private MockitoProvider mockitoProvider = new MockitoProvider();

	@Test
	public void testCreateMockComponent() throws Exception {
		@SuppressWarnings("unchecked")
		Map<String, String> mapMock = mockitoProvider.createMockComponent(Map.class);

		String key = "key";
		String value = "value";

		Mockito.when(mapMock.get(key)).thenReturn(value);

		assertEquals(value, mapMock.get(key));

	}

	@Test
	public void testCreateMockComponent_Final() throws Exception {
		assertNull(mockitoProvider.createMockComponent(String.class));
	}

	@Test
	public void testCreateMockComponent_isPrimitive() throws Exception {
		assertNull(mockitoProvider.createMockComponent(int.class));
	}

}
