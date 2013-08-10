package de.akquinet.jbosscc.needle.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.junit.Test;

import de.akquinet.jbosscc.needle.db.operation.hsql.HSQLDeleteOperation;
import de.akquinet.jbosscc.needle.injection.CustomInjectionAnnotation1;
import de.akquinet.jbosscc.needle.injection.CustomInjectionAnnotation2;
import de.akquinet.jbosscc.needle.mock.EasyMockProvider;

public class NeedleConfigurationTest {

    private final NeedleConfiguration needleConfiguration = NeedleConfiguration.get();

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenConfigurationIsInitializedMoreThenOnce() throws Exception {
        NeedleConfiguration.init();
        NeedleConfiguration.init();
    }

    @Test
    public void testGetMockProviderClass_Default() throws Exception {
        assertEquals(EasyMockProvider.class.getName(), needleConfiguration.getMockProviderClassName());
    }

    @Test
    public void testDBOperationClassName_NoDefaults() throws Exception {
        assertEquals(HSQLDeleteOperation.class.getName(), needleConfiguration.getDBOperationClassName());
    }

    @Test
    public void testGetCustomInjectionAnnotations() throws Exception {
        final Set<Class<Annotation>> customInjectionAnnotations = needleConfiguration.getCustomInjectionAnnotations();
        assertEquals(2, customInjectionAnnotations.size());
        assertTrue(customInjectionAnnotations.contains(CustomInjectionAnnotation1.class));
        assertTrue(customInjectionAnnotations.contains(CustomInjectionAnnotation2.class));
    }

}
