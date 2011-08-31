package de.akquinet.jbosscc.needle;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.db.DatabaseTestcase;
import de.akquinet.jbosscc.needle.injection.EntityManagerFactoryProvider;
import de.akquinet.jbosscc.needle.injection.EntityManagerProvider;
import de.akquinet.jbosscc.needle.injection.InjectionConfiguration;
import de.akquinet.jbosscc.needle.injection.InjectionIntoAnnotationProcessor;
import de.akquinet.jbosscc.needle.injection.InjectionProvider;
import de.akquinet.jbosscc.needle.mock.MockProvider;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

public class NeedleTestcase {
  private static final Logger LOG = LoggerFactory.getLogger(NeedleTestcase.class);

  private static final InjectionIntoAnnotationProcessor INJECTION_INTO_ANNOTATION_PROCESSOR = new InjectionIntoAnnotationProcessor();

  private final List<InjectionProvider> injectionProviderList = new ArrayList<InjectionProvider>();

  private final InjectionConfiguration configuration = new InjectionConfiguration();
  private final Map<Object, Object> injectedObjectMap = new HashMap<Object, Object>();

  public NeedleTestcase(final InjectionProvider... injectionProvider) {
    assignInejctionProvider(injectionProvider);
  }

  public NeedleTestcase(final DatabaseTestcase databaseTestcase, final InjectionProvider... injectionProvider) {
    this(injectionProvider);
    addEntityManagerProvider(databaseTestcase);
  }

  private void addEntityManagerProvider(final DatabaseTestcase databaseTestcase) {
    final InjectionProvider entityManagerProvider = new EntityManagerProvider(databaseTestcase);
    final InjectionProvider entityManagerFactoryProvider = new EntityManagerFactoryProvider(databaseTestcase);

    assignInejctionProvider(entityManagerProvider, entityManagerFactoryProvider);
  }

  protected final void assignInejctionProvider(final InjectionProvider... injectionProvider) {
    for (final InjectionProvider provider : injectionProvider) {
      injectionProviderList.add(0, provider);
    }
  }

  protected final void initTestcase(final Object test) throws Exception {
    LOG.info("init testcase");

    injectedObjectMap.clear();

    final List<Field> fields = ReflectionUtil.getAllFieldsWithAnnotation(test, ObjectUnderTest.class);

    LOG.debug("found fields {}", fields);

    for (final Field field : fields) {
      LOG.info("found field {}", field.getName());
      try {
        final Object instance = setInstanceIfNotNull(field, test);
        initInstance(instance);
      } catch (final InstantiationException e) {
        LOG.error(e.getMessage(), e);
      } catch (final IllegalAccessException e) {
        LOG.error(e.getMessage(), e);
      }
    }

    handleInjectInto(test);
  }

  private void handleInjectInto(final Object test) {
    INJECTION_INTO_ANNOTATION_PROCESSOR.process(test);
  }

  protected final void initInstance(final Object instance) {
    final List<Field> fields = ReflectionUtil.getAllFields(instance.getClass());
    fieldIteration: for (final Field field : fields) {

      // Custom provider
      for (final InjectionProvider provider : injectionProviderList) {
        if (handleInjection(instance, field, provider)) {
          continue fieldIteration;
        }
      }

      // Default provider from configuration
      for (final InjectionProvider provider : configuration.getInjectionProvider()) {
        if (handleInjection(instance, field, provider)) {
          continue fieldIteration;
        }
      }
    }
  }

  private boolean handleInjection(final Object instance, final Field field, final InjectionProvider provider) {
    if (provider.verify(field)) {
      final Object object = provider.get(field.getType());
      setFieldValue(instance, field, object);
      injectedObjectMap.put(provider.getKey(field), object);
      return true;
    }

    return false;
  }

  private void setFieldValue(final Object instance, final Field field, final Object fieldValue) {
    try {
      ReflectionUtil.setField(field, instance, fieldValue);
    } catch (final Exception e) {
      LOG.error(e.getMessage(), e);
    }
  }

  private Object setInstanceIfNotNull(final Field field, final Object test) throws Exception {

    Object instance = ReflectionUtil.getFieldValue(test, field);

    if (instance == null) {
      final ObjectUnderTest annotation = field.getAnnotation(ObjectUnderTest.class);

      final Class<?> implementation = annotation.implementation() != Void.class ? annotation.implementation() : field.getType();

      if (implementation.isInterface()) {
        throw new ObjectUnderTestInstantiationException("could not create an instance of object under test " + implementation
            + ", no implementation class configured");
      }

      try {
        implementation.getConstructor();
      } catch (final NoSuchMethodException e) {
        throw new ObjectUnderTestInstantiationException("could not create an instance of object under test " + implementation
            + ",implementation has no public no-arguments constructor", e);
      }

      instance = implementation.newInstance();

      try {
        ReflectionUtil.setField(field, test, instance);
      } catch (final Exception e) {
        throw new ObjectUnderTestInstantiationException(e);
      }
    }

    return instance;

  }

  @SuppressWarnings("unchecked")
  public <X> X getInjectedObject(final Object key) {
    return (X) injectedObjectMap.get(key);
  }

  public <X extends MockProvider> X getMockProvider() {
    return configuration.getMockProvider();
  }

}
