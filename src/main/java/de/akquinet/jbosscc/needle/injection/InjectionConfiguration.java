package de.akquinet.jbosscc.needle.injection;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.jbosscc.needle.common.MapEntry;
import de.akquinet.jbosscc.needle.configuration.NeedleConfiguration;
import de.akquinet.jbosscc.needle.mock.MockProvider;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

public final class InjectionConfiguration {
	private static final Logger LOG = LoggerFactory.getLogger(InjectionConfiguration.class);

	private static final Class<?> RESOURCE_CLASS = getClass("javax.annotation.Resource");
	private static final Class<?> INJECT_CLASS = getClass("javax.inject.Inject");
	private static final Class<?> EJB_CLASS = getClass("javax.ejb.EJB");
	private static final Class<?> PERSISTENCE_CONTEXT_CLASS = getClass("javax.persistence.PersistenceContext");
	private static final Class<?> PERSISTENCE_UNIT_CLASS = getClass("javax.persistence.PersistenceUnit");

	private static final Class<? extends MockProvider> MOCK_PROVIDER_CLASS = lookupMockProviderClass(NeedleConfiguration
	        .getMockProviderClassName());

	// Default InjectionProvider for annotations
	private final List<InjectionProvider<?>> injectionProviderList = new ArrayList<InjectionProvider<?>>();

	// Global InjectionProvider for custom implementation
	private final List<InjectionProvider<?>> globalInjectionProviderList = new ArrayList<InjectionProvider<?>>();

	// Test-specific custom injection provider
	private final List<InjectionProvider<?>> testInjectionProvider = new ArrayList<InjectionProvider<?>>();

	// all with priority order
	private final List<List<InjectionProvider<?>>> allInjectionProvider;

	private final Set<Class<? extends Annotation>> injectionAnnotationClasses = new HashSet<Class<? extends Annotation>>();

	private final MockProvider mockProvider;

	@SuppressWarnings("unchecked")
	public InjectionConfiguration() {
		super();
		mockProvider = createMockProvider();

		add(INJECT_CLASS);
		add(EJB_CLASS);
		add(PERSISTENCE_CONTEXT_CLASS);
		add(PERSISTENCE_UNIT_CLASS);
		addResource();

		initGlobalInjectionAnnotation();
		initGlobalInjectionProvider();


		injectionProviderList.add(0, new MockProviderInjectionProvider(mockProvider));

		allInjectionProvider = Arrays.asList(testInjectionProvider, globalInjectionProviderList, injectionProviderList);

	}

	private void addResource() {

		if (RESOURCE_CLASS != null) {
			addInjectionAnnotation(RESOURCE_CLASS);
			injectionProviderList.add(new ResourceMockInjectionProvider(mockProvider));
		}
	}

	private void add(final Class<?> clazz) {

		if (clazz != null) {
			LOG.debug("register injection handler for class {}", clazz);
			injectionProviderList.add(new DefaultMockInjectionProvider(clazz, mockProvider));
			addInjectionAnnotation(clazz);
		}

	}

	private static Class<?> getClass(final String className) {
		return ReflectionUtil.forName(className);
	}

	@SuppressWarnings("unchecked")
	public <T extends MockProvider> T getMockProvider() {
		return (T) mockProvider;
	}

	public final void addInjectionProvider(final InjectionProvider<?>... injectionProvider) {
		for (final InjectionProvider<?> provider : injectionProvider) {
			testInjectionProvider.add(0, provider);
		}
	}

	public List<List<InjectionProvider<?>>> getInjectionProvider() {
		return allInjectionProvider;
	}

	private void initGlobalInjectionAnnotation() {
		Set<Class<Annotation>> customInjectionAnnotations = NeedleConfiguration.getCustomInjectionAnnotations();

		for (Class<? extends Annotation> annotation : customInjectionAnnotations) {
			addInjectionAnnotation(annotation);
			globalInjectionProviderList.add(0, new DefaultMockInjectionProvider(annotation, getMockProvider()));
		}
	}

	private void initGlobalInjectionProvider() {
		Set<Class<InjectionProvider<?>>> customInjectionProviders = NeedleConfiguration
		        .getCustomInjectionProviderClasses();

		for (Class<InjectionProvider<?>> injectionProviderClass : customInjectionProviders) {
			try {
				InjectionProvider<?> injection = ReflectionUtil.createInstance(injectionProviderClass);
				globalInjectionProviderList.add(0, injection);
			} catch (Exception e) {
				LOG.warn("could not create an instance of injection provider " + injectionProviderClass, e);
			}
		}

	}

	@SuppressWarnings("unchecked")
	private void addInjectionAnnotation(Class<?> clazz) {
		if (clazz.isAnnotation()) {
			injectionAnnotationClasses.add((Class<? extends Annotation>) clazz);
		}
	}

	public boolean isAnnotationSupported(final Class<? extends Annotation> annotation) {
		return injectionAnnotationClasses.contains(annotation);
	}

	Set<Class<? extends Annotation>> getSupportedAnnotations() {
		return Collections.unmodifiableSet(injectionAnnotationClasses);
	}

	public Entry<Object, Object> handleInjectionProvider(final Collection<InjectionProvider<?>> injectionProviders,
	        final InjectionTargetInformation injectionTargetInformation) {

		for (final InjectionProvider<?> provider : injectionProviders) {

			if (provider.verify(injectionTargetInformation)) {
				final Object object = provider.getInjectedObject(injectionTargetInformation.getType());
				final Object key = provider.getKey(injectionTargetInformation);

				return new MapEntry<Object, Object>(key, object);

			}

		}
		return null;
	}

	@SuppressWarnings("unchecked")
	<T extends MockProvider> T createMockProvider() {
		try {
			return (T) MOCK_PROVIDER_CLASS.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("could not create a new instance of mock provider " + MOCK_PROVIDER_CLASS, e);
		}
	}

	@SuppressWarnings("unchecked")
	static Class<? extends MockProvider> lookupMockProviderClass(final String mockProviderClassName) {

		try {
			if (mockProviderClassName != null) {
				return (Class<? extends MockProvider>) Class.forName(mockProviderClassName);
			}
		} catch (Exception e) {
			LOG.warn("could not load mock provider class " + mockProviderClassName, e);
		}

		throw new RuntimeException("no mock provider configured");
	}
}
