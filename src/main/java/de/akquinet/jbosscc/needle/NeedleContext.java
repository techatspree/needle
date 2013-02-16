package de.akquinet.jbosscc.needle;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

public class NeedleContext {

    private final Object test;
    private final Map<String, Object> objectUnderTestMap = new HashMap<String, Object>();
    private final Map<Object, Object> injectedObjectMap = new HashMap<Object, Object>();

    private final Map<Class<? extends Annotation>, List<Field>> annotatedTestcaseFieldMap;

    public NeedleContext(final Object test) {
        this.test = test;
        annotatedTestcaseFieldMap = ReflectionUtil.getAllAnnotatedFields(test.getClass());
    }

    public Object getTest() {
        return test;
    }

    @SuppressWarnings("unchecked")
    public <X> X getInjectedObject(final Object key) {
        return (X)injectedObjectMap.get(key);
    }

    public Collection<Object> getInjectedObjects() {
        return injectedObjectMap.values();
    }

    public void addInjectedObject(final Object key, final Object instance) {
        injectedObjectMap.put(key, instance);
    }

    public Object getObjectUnderTest(final String id) {
        return objectUnderTestMap.get(id);
    }

    public void addObjectUnderTest(final String id, final Object instance) {
        objectUnderTestMap.put(id, instance);
    }

    public Collection<Object> getObjectsUnderTest() {
        return objectUnderTestMap.values();
    }

    public List<Field> getAnnotatedTestcaseFields(final Class<? extends Annotation> annotationClass) {
        final List<Field> value = annotatedTestcaseFieldMap.get(annotationClass);
        return value != null ? value : new ArrayList<Field>();
    }

}
