package de.akquinet.jbosscc.needle;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.db.DatabaseTestcase;
import de.akquinet.jbosscc.needle.injection.EntityManagerFactoryProvider;
import de.akquinet.jbosscc.needle.injection.EntityManagerProvider;
import de.akquinet.jbosscc.needle.injection.InjectionAnnotationProcessor;
import de.akquinet.jbosscc.needle.injection.InjectionConfiguration;
import de.akquinet.jbosscc.needle.injection.InjectionProvider;
import de.akquinet.jbosscc.needle.injection.InjectionTargetInformation;
import de.akquinet.jbosscc.needle.mock.MockProvider;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

public class NeedleTestcase {
	private static final Logger LOG = LoggerFactory.getLogger(NeedleTestcase.class);

	private static final InjectionAnnotationProcessor INJECTION_INTO_ANNOTATION_PROCESSOR = new InjectionAnnotationProcessor();

	private final List<InjectionProvider<?>> injectionProviderList = new ArrayList<InjectionProvider<?>>();

	private final InjectionConfiguration configuration = new InjectionConfiguration();
	private final Map<String, Object> objectUnderTestMap = new HashMap<String, Object>();
	private final Map<Object, Object> injectedObjectMap = new HashMap<Object, Object>();

	private final List<Collection<InjectionProvider<?>>> injectionProviders;

	@SuppressWarnings("unchecked")
	public NeedleTestcase(final InjectionProvider<?>... injectionProvider) {
		addInjectionProvider(injectionProvider);

		injectionProviders = Arrays.asList(injectionProviderList, configuration.getGlobalCustomInjectionProvider(),
		        configuration.getInjectionProvider());
	}

	public NeedleTestcase(final DatabaseTestcase databaseTestcase, final InjectionProvider<?>... injectionProvider) {
		this(injectionProvider);
		addEntityManagerProvider(databaseTestcase);
	}

	private void addEntityManagerProvider(final DatabaseTestcase databaseTestcase) {
		final InjectionProvider<EntityManager> entityManagerProvider = new EntityManagerProvider(databaseTestcase);
		final InjectionProvider<EntityManagerFactory> entityManagerFactoryProvider = new EntityManagerFactoryProvider(
		        databaseTestcase);

		addInjectionProvider(entityManagerProvider, entityManagerFactoryProvider);
	}

	public final void addInjectionProvider(final InjectionProvider<?>... injectionProvider) {
		for (final InjectionProvider<?> provider : injectionProvider) {
			injectionProviderList.add(0, provider);
		}
	}

	protected final void initTestcase(final Object test) throws Exception {
		LOG.info("init testcase {}", test);

		objectUnderTestMap.clear();
		injectedObjectMap.clear();

		final List<Field> fields = ReflectionUtil.getAllFieldsWithAnnotation(test, ObjectUnderTest.class);

		LOG.debug("found fields {}", fields);

		for (final Field field : fields) {
			LOG.debug("found field {}", field.getName());
			try {
				final Object instance = setInstanceIfNotNull(field, test);
				initInstance(instance);
			} catch (final InstantiationException e) {
				LOG.error(e.getMessage(), e);
			} catch (final IllegalAccessException e) {
				LOG.error(e.getMessage(), e);
			}
		}

		INJECTION_INTO_ANNOTATION_PROCESSOR.process(test, Collections.unmodifiableMap(objectUnderTestMap));

	}

	protected final void initInstance(final Object instance) {
		initFields(instance);
		initMethodInjection(instance);
	}

	private void initMethodInjection(final Object instance) {
		final List<Method> methods = ReflectionUtil.getMethods(instance.getClass());

		for (final Method method : methods) {

			Annotation[] annotations = method.getDeclaredAnnotations();

			if (!checkAnnotationIsSupported(annotations)) {
				continue;
			}

			Class<?>[] parameterTypes = method.getParameterTypes();

			InjectionTargetInformationFactory injectionTargetInformationFactory = new InjectionTargetInformationFactory() {

				@Override
				public InjectionTargetInformation create(Class<?> parameterType, int parameterIndex) {

					return new InjectionTargetInformation(parameterType, method,
					        method.getParameterAnnotations()[parameterIndex]);
				}
			};
			final Object[] arguments = createArguments(parameterTypes, injectionTargetInformationFactory);

			try {
				ReflectionUtil.invokeMethod(method, instance, arguments);
			} catch (Exception e) {
				LOG.warn("could not invoke method", e);
			}

		}
	}

	private Object[] createArguments(Class<?>[] parameterTypes,
	        InjectionTargetInformationFactory injectionTargetInformationFactory) {
		Object[] arguments = new Object[parameterTypes.length];

		parameter: for (int i = 0; i < parameterTypes.length; i++) {
			final InjectionTargetInformation injectionTargetInformation = injectionTargetInformationFactory.create(
			        parameterTypes[i], i);

			for (Collection<InjectionProvider<?>> collection : injectionProviders) {
				Object injection = handleInjectionProvider(collection, injectionTargetInformation);

				if (injection != null) {
					arguments[i] = injection;
					continue parameter;
				}

			}

		}

		return arguments;
	}

	private void initFields(final Object instance) {
		final List<Field> fields = ReflectionUtil.getAllFields(instance.getClass());

		injectionField: for (final Field field : fields) {
			InjectionTargetInformation injectionTargetInformation = new InjectionTargetInformation(field.getType(),
			        field);

			for (Collection<InjectionProvider<?>> collection : injectionProviders) {
				Object injection = handleInjectionProvider(collection, injectionTargetInformation);

				if (injection != null) {
					try {
						ReflectionUtil.setField(field, instance, injection);
					} catch (final Exception e) {
						LOG.error(e.getMessage(), e);
					}
					continue injectionField;
				}

			}

		}

	}

	private Object handleInjectionProvider(final Collection<InjectionProvider<?>> injectionProviders,
	        InjectionTargetInformation injectionTargetInformation) {

		for (final InjectionProvider<?> provider : injectionProviders) {

			if (provider.verify(injectionTargetInformation)) {
				final Object object = provider.getInjectedObject(injectionTargetInformation.getType());

				injectedObjectMap.put(provider.getKey(injectionTargetInformation), object);
				return object;
			}

		}
		return null;
	}

	private Object setInstanceIfNotNull(final Field field, final Object test) throws Exception {
		final ObjectUnderTest objectUnderTestAnnotation = field.getAnnotation(ObjectUnderTest.class);
		final String id = objectUnderTestAnnotation.id().equals("") ? field.getName() : objectUnderTestAnnotation.id();
		Object instance = ReflectionUtil.getFieldValue(test, field);

		if (instance == null) {

			final Class<?> implementation = objectUnderTestAnnotation.implementation() != Void.class ? objectUnderTestAnnotation
			        .implementation() : field.getType();

			if (implementation.isInterface()) {
				throw new ObjectUnderTestInstantiationException("could not create an instance of object under test "
				        + implementation + ", no implementation class configured");
			}

			instance = getInstanceByConstructorInjection(implementation);

			if (instance == null) {
				instance = createInstanceByNoArgConstructor(implementation);
			}

			try {
				ReflectionUtil.setField(field, test, instance);
			} catch (final Exception e) {
				throw new ObjectUnderTestInstantiationException(e);
			}
		}

		objectUnderTestMap.put(id, instance);

		return instance;

	}

	private Object createInstanceByNoArgConstructor(final Class<?> implementation)
	        throws ObjectUnderTestInstantiationException {
		try {
			implementation.getConstructor();

			return implementation.newInstance();

		} catch (final NoSuchMethodException e) {
			throw new ObjectUnderTestInstantiationException("could not create an instance of object under test "
			        + implementation + ",implementation has no public no-arguments constructor", e);
		} catch (final InstantiationException e) {

			throw new ObjectUnderTestInstantiationException(e);
		} catch (final IllegalAccessException e) {
			throw new ObjectUnderTestInstantiationException(e);
		}

	}

	private Object getInstanceByConstructorInjection(final Class<?> implementation)
	        throws ObjectUnderTestInstantiationException {
		final Constructor<?>[] constructors = implementation.getConstructors();

		for (final Constructor<?> constructor : constructors) {

			final Annotation[] annotations = constructor.getAnnotations();

			if (!checkAnnotationIsSupported(annotations)) {
				continue;
			}

			final Class<?>[] parameterTypes = constructor.getParameterTypes();
			final InjectionTargetInformationFactory injectionTargetInformationFactory = new InjectionTargetInformationFactory() {

				@Override
				public InjectionTargetInformation create(Class<?> parameterType, int parameterIndex) {
					return new InjectionTargetInformation(parameterType, constructor,
					        constructor.getParameterAnnotations()[parameterIndex]);
				}
			};

			final Object[] arguments = createArguments(parameterTypes, injectionTargetInformationFactory);

			try {
				return constructor.newInstance(arguments);
			} catch (Exception e) {
				throw new ObjectUnderTestInstantiationException(e);
			}

		}

		return null;
	}

	private boolean checkAnnotationIsSupported(final Annotation[] annotations) {

		for (Annotation annotation : annotations) {
			if (configuration.isAnnotationSupported(annotation.annotationType())) {
				return true;
			}
		}
		return false;
	}

	/**
	 *
	 * Returns the injected object for the given key, or null if no object was
	 * injected with the given key.
	 *
	 * @param key
	 *            the key of the injected object, see
	 *            {@link InjectionProvider#getKey(InjectionTargetInformation)}
	 *
	 * @return the injected object or null
	 */
	@SuppressWarnings("unchecked")
	public <X> X getInjectedObject(final Object key) {
		return (X) injectedObjectMap.get(key);
	}

	/**
	 * Returns an instance of the configured {@link MockProvider}
	 *
	 * @return the configured {@link MockProvider}
	 */
	public <X extends MockProvider> X getMockProvider() {
		return configuration.getMockProvider();
	}

	private interface InjectionTargetInformationFactory {
		InjectionTargetInformation create(Class<?> parameterType, int parameterIndex);
	}
}
