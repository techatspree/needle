package de.akquinet.jbosscc.needle.injection;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.jbosscc.needle.configuration.NeedleConfiguration;
import de.akquinet.jbosscc.needle.mock.MockProvider;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

public class InjectionConfiguration {
	private static final Logger LOG = LoggerFactory.getLogger(InjectionConfiguration.class);

	private static final Class<?> RESOURCE_CLASS = getClass("javax.annotation.Resource");
	private static final Class<?> INJECT_CLASS = getClass("javax.inject.Inject");
	private static final Class<?> EJB_CLASS = getClass("javax.ejb.EJB");
	private static final Class<?> PERSISTENCE_CONTEXT_CLASS = getClass("javax.persistence.PersistenceContext");
	private static final Class<?> PERSISTENCE_UNIT_CLASS = getClass("javax.persistence.PersistenceUnit");

	private final Set<InjectionProvider<?>> injectionProviderSet = new HashSet<InjectionProvider<?>>();
	private final Set<InjectionProvider<?>> globalCustomInjectionProviderSet = new HashSet<InjectionProvider<?>>();
	private final MockProvider mockProvider;

	public InjectionConfiguration() {
		super();

		mockProvider = createMockProvider();

		add(INJECT_CLASS);
		add(EJB_CLASS);
		add(PERSISTENCE_CONTEXT_CLASS);
		add(PERSISTENCE_UNIT_CLASS);
		addResource();

		initGlobalCustomInjectionAnnotation();

	}

	private void addResource() {

		if (RESOURCE_CLASS != null) {
			injectionProviderSet.add(new ResourceMockInjectionProvider(mockProvider));
		}
	}

	private void add(final Class<?> clazz) {

		if (clazz != null) {
			LOG.info("register injection handler for class {}", clazz);
			injectionProviderSet.add(new DefaultMockInjectionProvider(clazz, mockProvider));

		}

	}

	private static Class<?> getClass(final String className) {
		return ReflectionUtil.forName(className);
	}

	public Set<InjectionProvider<?>> getInjectionProvider() {
		return injectionProviderSet;
	}

	public Set<InjectionProvider<?>> getGlobalCustomInjectionProvider() {
		return globalCustomInjectionProviderSet;
	}

	@SuppressWarnings("unchecked")
	public <T extends MockProvider> T getMockProvider() {
		return (T) mockProvider;
	}

	@SuppressWarnings("unchecked")
	private <T extends MockProvider> T createMockProvider() {
		final Class<? extends MockProvider> mockProviderClass = NeedleConfiguration.getMockProviderClass();

		if (mockProviderClass != null) {
			try {
				return (T) mockProviderClass.newInstance();
			} catch (Exception e) {
				throw new RuntimeException("could not create a new instance of mock provider " + mockProviderClass, e);
			}
		}
		throw new RuntimeException("no mock provider configured");
	}


	private void initGlobalCustomInjectionAnnotation(){
		Set<Class<? extends Annotation>> customInjectionAnnotations = NeedleConfiguration.getCustomInjectionAnnotations();

		for (Class<? extends Annotation> annotation : customInjectionAnnotations) {
			globalCustomInjectionProviderSet.add(new DefaultMockInjectionProvider(annotation, getMockProvider()));
        }
	}
}
