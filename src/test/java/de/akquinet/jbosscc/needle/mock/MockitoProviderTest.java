package de.akquinet.jbosscc.needle.mock;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;

public class MockitoProviderTest {

    private final MockitoProvider mockitoProvider = new MockitoProvider();

    @Test
    public void shouldCreateMockComponent() throws Exception {
        @SuppressWarnings("unchecked")
        final Map<String, String> mapMock = mockitoProvider.createMockComponent(Map.class);

        final String key = "key";
        final String value = "value";

        Mockito.when(mapMock.get(key)).thenReturn(value);

        assertEquals(value, mapMock.get(key));

    }

    @Test
    public void shouldCreateSpyComponent() throws Exception {
        Map<String, String> mapSpy = new HashMap<String, String>();
        mapSpy = mockitoProvider.createSpyComponent(mapSpy);

        mapSpy.put("foo", "a");

        when(mapSpy.get("bar")).thenReturn("b");

        assertThat(mapSpy.get("foo"), is("a"));
        assertThat(mapSpy.get("bar"), is("b"));

        verify(mapSpy).get("foo");
        verify(mapSpy).get("bar");
        verify(mapSpy).put("foo", "a");
        verifyNoMoreInteractions(mapSpy);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToCreateSpyWhenInstanceIsNull() throws Exception {
        mockitoProvider.createSpyComponent(null);
    }

    @Test
    public void shouldSkipCreateMockComponentForFinalType() throws Exception {
        assertNull(mockitoProvider.createMockComponent(String.class));
    }

    @Test
    public void shouldSkipCreateMockComponentForPrimitiveType() throws Exception {
        assertNull(mockitoProvider.createMockComponent(int.class));
    }

    @Test
    public void shouldSkipCreateSpyComponentForFinal() throws Exception {
        assertNull(mockitoProvider.createSpyComponent("foo"));
    }

    @Test
    public void shouldSkipCreateSpyComponentForPrimitive() throws Exception {
        assertNull(mockitoProvider.createSpyComponent(1));
    }

}
