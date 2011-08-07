package de.akquinet.jbosscc.needle;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.configuration.InjectionTypes;
import de.akquinet.jbosscc.needle.configuration.MockInjectionHandler;
import de.akquinet.jbosscc.needle.db.DatabaseRule;
import de.akquinet.jbosscc.needle.injection.EntityManagerProvider;
import de.akquinet.jbosscc.needle.injection.InjectionIntoAnnotationPrcessor;
import de.akquinet.jbosscc.needle.injection.InjectionProvider;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

public class NeedleRule implements MethodRule {
	private static final Logger LOG = LoggerFactory.getLogger(NeedleRule.class);

	private final InjectionIntoAnnotationPrcessor injectionIntoAnnotationPrcessor = new InjectionIntoAnnotationPrcessor();
	private DatabaseRule databaseRule;

	private Map<Object, Object> mockMap = new HashMap<Object, Object>();
	private Map<Class<?>, InjectionProvider<?>> injectionProviderMap = new HashMap<Class<?>, InjectionProvider<?>>();

	public NeedleRule(final InjectionProvider<?>... injectionProvider) {

		super();
		InjectionProvider<?> entityManagerProvider = new EntityManagerProvider(getDatabaseRule());
		injectionProviderMap.put(entityManagerProvider.getType(), entityManagerProvider);

		assignInejctionProvider(injectionProvider);

	}

	public NeedleRule(final DatabaseRule databaseRule, final InjectionProvider<?>... injectionProvider) {

		this(injectionProvider);
		this.databaseRule = databaseRule;

	}

	private void assignInejctionProvider(final InjectionProvider<?>... injectionProvider) {
		for (InjectionProvider<?> provider : injectionProvider) {
			injectionProviderMap.put(provider.getType(), provider);
		}
	}

	private void initTestcase(Object test) {
		LOG.info("assign object under tests");

		List<Field> fields = ReflectionUtil.getAllFieldsWithAnnotation(test, ObjectUnderTest.class);

		LOG.info("found fields {}", fields);

		for (Field field : fields) {
			LOG.info("found field {}", field.getName());
			try {
				Object instance = setInstance(field, test);
				initInstance(instance);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				LOG.error(e.getMessage(), e);
			} catch (IllegalAccessException e) {
				LOG.error(e.getMessage(), e);
			}
		}

		handleMock(test);
		handleInjection(test);

	}

	private void handleInjection(Object test) {
		injectionIntoAnnotationPrcessor.process(test);
	}

	private void handleMock(Object test) {
		// TODO Auto-generated method stub

	}

	private void initInstance(Object instance) {

		final List<Field> fields = ReflectionUtil.getAllFields(instance.getClass());
		for (Field field : fields) {
			Object fieldValue = null;
			Class<?> type = field.getType();
			final InjectionProvider<?> provider = injectionProviderMap.get(type);
			if (provider != null) {
				fieldValue = provider.get();

			} else {
				MockInjectionHandler handler = InjectionTypes.getTypeHandler(type);
				if (handler != null) {
					fieldValue = handleMock(field, handler, null);
				} else {

					Annotation[] annotations = field.getAnnotations();
					for (Annotation annotation : annotations) {
						Class<? extends Annotation> annotationType = annotation.annotationType();
						handler = InjectionTypes.getAnnotationHandler(annotationType);
						if (handler != null) {
							fieldValue = handleMock(field, handler, annotation);
						}
					}

				}

			}

			try {
				ReflectionUtil.setField(field, instance, fieldValue);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private Object handleMock(Field field, MockInjectionHandler handler, Annotation annotation) {
		Class<?> type = field.getType();
		Object mockObject = handler.create(type);
		mockMap.put(handler.getKey(type, annotation), mockObject);
		return mockObject;
	}

	private Object setInstance(Field field, Object test) throws InstantiationException, IllegalAccessException {
		ObjectUnderTest annotation = field.getAnnotation(ObjectUnderTest.class);

		Class<?> implementation = annotation.implementation();
		Object newInstance = null;
		if (implementation != Void.class) {
			newInstance = implementation.newInstance();
		} else {
			newInstance = field.getType().newInstance();

		}

		try {
			ReflectionUtil.setField(field, test, newInstance);
		} catch (Exception e) {

			LOG.error(e.getMessage(), e);
		}

		return newInstance;

	}

	@Override
	public Statement apply(Statement base, FrameworkMethod method, Object target) {
		initTestcase(target);
		return base;
	}

	private boolean hasDatabaseRule() {
		return databaseRule != null;
	}

	public DatabaseRule getDatabaseRule() {
		if (!hasDatabaseRule()) {
			databaseRule = new DatabaseRule();
		}

		return databaseRule;
	}

	public Object getMock(final Object key) {
		return mockMap.get(key);
	}

	@SuppressWarnings("unchecked")
	public <M> M getMock(final Class<M> key) {
		return (M) mockMap.get(key);
	}

}
