package de.akquinet.jbosscc.needle.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.junit.Test;

import de.akquinet.jbosscc.needle.db.operation.AbstractDBOperation;
import de.akquinet.jbosscc.needle.db.operation.hsql.HSQLDeleteOperation;
import de.akquinet.jbosscc.needle.injection.CustomInjectionAnnotation1;
import de.akquinet.jbosscc.needle.injection.CustomInjectionAnnotation2;
import de.akquinet.jbosscc.needle.mock.EasyMockProvider;
import de.akquinet.jbosscc.needle.mock.MockitoProvider;

public class PropertyBasedConfigurationFactoryTest {

    private final NeedleConfiguration needleConfiguration = PropertyBasedConfigurationFactory.get();

    @Test
    public void testGetMockProviderClass_Default() throws Exception {
        assertEquals(EasyMockProvider.class, needleConfiguration.getMockProviderClass());
    }

    @Test
    public void testDBOperationClassName_NoDefaults() throws Exception {
        assertEquals(HSQLDeleteOperation.class, needleConfiguration.getDBOperationClass());
    }

    @Test
    public void testGetCustomInjectionAnnotations() throws Exception {
        final Set<Class<Annotation>> customInjectionAnnotations = needleConfiguration.getCustomInjectionAnnotations();
        assertEquals(2, customInjectionAnnotations.size());
        assertTrue(customInjectionAnnotations.contains(CustomInjectionAnnotation1.class));
        assertTrue(customInjectionAnnotations.contains(CustomInjectionAnnotation2.class));
    }
    
    @Test
    public void testLookupMockProviderClass() throws Exception {
        assertNotNull(PropertyBasedConfigurationFactory.lookupMockProviderClass(MockitoProvider.class.getName()));
    }

    @Test(expected = RuntimeException.class)
    public void testLookupMockProviderClass_WithUnknownClass() throws Exception {
        assertNull(PropertyBasedConfigurationFactory.lookupMockProviderClass("unknown"));
    }

    @Test(expected = RuntimeException.class)
    public void testLookupMockProviderClass_Null() throws Exception {
        assertNull(PropertyBasedConfigurationFactory.lookupMockProviderClass(null));
    }
    
    @Test
    public void testLookupDBOperationClassClass_HSQLDeleteOperation() throws Exception {
        Class<? extends AbstractDBOperation> dbDialectClass = PropertyBasedConfigurationFactory
                .lookupDBOperationClass(HSQLDeleteOperation.class.getName());
        assertEquals(HSQLDeleteOperation.class, dbDialectClass);
    }

    @Test
    public void testLookupDBOperationClassClass_UnknownClass() throws Exception {
        Class<? extends AbstractDBOperation> dbDialectClass = PropertyBasedConfigurationFactory
                .lookupDBOperationClass("unknowm");
        assertNull(dbDialectClass);
    }

    @Test
    public void testLookupDBOperationClassClass_Null() throws Exception {
        Class<? extends AbstractDBOperation> dbDialectClass = PropertyBasedConfigurationFactory
                .lookupDBOperationClass(null);
        assertNull(dbDialectClass);
    }


}
