package de.akquinet.jbosscc.needle;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

	private final InjectionIntoAnnotationProcessor injectionIntoAnnotationPrcessor = new InjectionIntoAnnotationProcessor();

	private final InjectionConfiguration configuration = new InjectionConfiguration();

	private Map<Object, Object> injectedObjectMap = new HashMap<Object, Object>();


	private Set<InjectionProvider> injectionProviderSet = new HashSet<InjectionProvider>();

	public NeedleTestcase(final InjectionProvider... injectionProvider) {
		super();
		assignInejctionProvider(injectionProvider);
	}

	public NeedleTestcase(final DatabaseTestcase databaseTestcase, final InjectionProvider... injectionProvider) {

		this(injectionProvider);
		addEntityManagerProvider(databaseTestcase);

	}

	private void addEntityManagerProvider(final DatabaseTestcase databaseTestcase) {

		final InjectionProvider entityManagerProvider = new EntityManagerProvider(databaseTestcase);
		injectionProviderSet.add(entityManagerProvider);

		final InjectionProvider entityManagerFactoryProvider = new EntityManagerFactoryProvider(databaseTestcase);
		injectionProviderSet.add(entityManagerFactoryProvider);

	}

	private void assignInejctionProvider(final InjectionProvider... injectionProvider) {
		for (InjectionProvider provider : injectionProvider) {
			injectionProviderSet.add(provider);
		}
	}

	protected final void initTestcase(Object test) throws Exception {
		LOG.info("assign object under tests");

		final List<Field> fields = ReflectionUtil.getAllFieldsWithAnnotation(test, ObjectUnderTest.class);

		LOG.debug("found fields {}", fields);

		for (Field field : fields) {
			LOG.info("found field {}", field.getName());
			try {
				Object instance = setInstanceIfNotNull(field, test);
				initInstance(instance);
			} catch (InstantiationException e) {
				LOG.error(e.getMessage(), e);
			} catch (IllegalAccessException e) {
				LOG.error(e.getMessage(), e);
			}
		}

		handleInjectInto(test);

	}

	private void handleInjectInto(Object test) {
		injectionIntoAnnotationPrcessor.process(test);
	}

	private void initInstance(Object instance) {

		final List<Field> fields = ReflectionUtil.getAllFields(instance.getClass());
		fieldIteration: for (Field field : fields) {

			// Custom provider
			for (InjectionProvider provider : injectionProviderSet) {
				if (handleInjection(instance, field, provider)) {
					continue fieldIteration;
				}
			}

			// Default provider from configuration
			for (InjectionProvider provider : configuration.getInjectionProvider()) {
				if (handleInjection(instance, field, provider)) {
					continue fieldIteration;
				}

			}
		}

	}

	private boolean handleInjection(Object instance, Field field, InjectionProvider provider) {
		if (provider.verify(field)) {
			Object object = provider.get(field.getType());
			setFieldValue(instance, field, object);
			injectedObjectMap.put(provider.getKey(field), object);
			return true;
		}

		return false;
	}

	private void setFieldValue(Object instance, Field field, Object fieldValue) {
		try {
			ReflectionUtil.setField(field, instance, fieldValue);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	private Object setInstanceIfNotNull(Field field, Object test) throws Exception {

		Object instance = ReflectionUtil.getFieldValue(test, field);

		if (instance == null) {
			ObjectUnderTest annotation = field.getAnnotation(ObjectUnderTest.class);

			final Class<?> implementation = annotation.implementation() != Void.class ? annotation.implementation()
			        : field.getType();

			if (implementation.isInterface()) {
				throw new ObjectUnderTestInstantiationException("could not create an instance of object under test "
				        + implementation + ", no implementation class configured");
			}

			try {
				implementation.getConstructor();
			} catch (NoSuchMethodException e) {
				throw new ObjectUnderTestInstantiationException("could not create an instance of object under test "
				        + implementation + ",implementation has no public no-arguments constructor");
			}

			instance = implementation.newInstance();

			try {
				ReflectionUtil.setField(field, test, instance);
			} catch (Exception e) {
				throw new ObjectUnderTestInstantiationException(e);
			}
		}

		return instance;

	}

	public Object getInjectedObject(final Object key) {
		return injectedObjectMap.get(key);
	}

	public MockProvider getMockProvider(){
		return configuration.getMockProvider();
	}
}
