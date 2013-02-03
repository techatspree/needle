package de.akquinet.jbosscc.needle;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.jbosscc.needle.annotation.InjectInto;
import de.akquinet.jbosscc.needle.annotation.InjectIntoMany;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.common.MapEntry;
import de.akquinet.jbosscc.needle.injection.InjectionAnnotationProcessor;
import de.akquinet.jbosscc.needle.injection.InjectionConfiguration;
import de.akquinet.jbosscc.needle.injection.InjectionProvider;
import de.akquinet.jbosscc.needle.injection.InjectionTargetInformation;
import de.akquinet.jbosscc.needle.injection.TestcaseInjectionProcessor;
import de.akquinet.jbosscc.needle.mock.MockAnnotationProcessor;
import de.akquinet.jbosscc.needle.mock.MockProvider;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

/**
 * Abstract test case to process and initialize all fields annotated with
 * {@link ObjectUnderTest}. After initialization, {@link InjectIntoMany} and
 * {@link InjectInto} annotations are processed for optional additional
 * injections.
 * 
 * <p>
 * Supported injections are:
 * </p>
 * <ol>
 * <li>Constructor injection</li>
 * <li>Field injection</li>
 * <li>Method injection</li>
 * </ol>
 * 
 * @see ObjectUnderTest
 * @see InjectInto
 * @see InjectIntoMany
 * @see InjectionProvider
 * 
 */
public abstract class NeedleTestcase {
    private static final Logger LOG = LoggerFactory.getLogger(NeedleTestcase.class);

    private final InjectionAnnotationProcessor injectionIntoAnnotationProcessor = new InjectionAnnotationProcessor();
    private final TestcaseInjectionProcessor testcaseInjectionProcessor = new TestcaseInjectionProcessor();
    private final MockAnnotationProcessor mockAnnotationProcessor = new MockAnnotationProcessor();

    private final InjectionConfiguration configuration = new InjectionConfiguration();

    private NeedleContext context;

    /**
     * Create an instance of {@link NeedleTestcase} with optional additional
     * injection provider.
     * 
     * @param injectionProvider
     *            optional additional injection provider
     * 
     * @see InjectionProvider
     */
    protected NeedleTestcase(final InjectionProvider<?>... injectionProvider) {
        addInjectionProvider(injectionProvider);
    }

    @Deprecated
    protected final void addInjectionProvider(final InjectionProvider<?>... injectionProvider) {
        configuration.addInjectionProvider(injectionProvider);
    }

    /**
     * Initialize all fields annotated with {@link ObjectUnderTest}. Is an
     * object under test annotated field already initialized, only the injection
     * of dependencies will be executed.
     * 
     * After initialization, {@link InjectIntoMany} and {@link InjectInto}
     * annotations are processed for optional additional injections.
     * 
     * @param test
     *            an instance of the test
     * @throws Exception
     *             thrown if an initialization error occurs.
     */
    protected final void initTestcase(final Object test) throws Exception {
        LOG.info("init testcase {}", test);

        context = new NeedleContext(test);

        final List<Field> fields = context.getAnnotatedTestcaseFields(ObjectUnderTest.class);

        LOG.debug("found fields {}", fields);

        for (final Field field : fields) {
            LOG.debug("found field {}", field.getName());
            try {
                final Object instance = setInstanceIfNotNull(field, test);
                initInstance(instance);
                configuration.getPostConstructProcessor().process(field, instance);
            } catch (final InstantiationException e) {
                LOG.error(e.getMessage(), e);
            } catch (final IllegalAccessException e) {
                LOG.error(e.getMessage(), e);
            }
        }

        mockAnnotationProcessor.process(context, configuration);
        injectionIntoAnnotationProcessor.process(context);
        testcaseInjectionProcessor.process(context, configuration);

    }

    /**
     * Inject dependencies into the given instance. First, all field injections
     * are executed, if there exists an {@link InjectionProvider} for the
     * target. Then the method injection is executed, if the injection
     * annotations are supported.
     * 
     * @param instance
     *            the instance to initialize.
     * @throws ObjectUnderTestInstantiationException
     */
    protected final void initInstance(final Object instance) {
        initFields(instance);
        initMethodInjection(instance);
    }

    private void initMethodInjection(final Object instance) {
        final List<Method> methods = ReflectionUtil.getMethods(instance.getClass());

        for (final Method method : methods) {

            final Annotation[] annotations = method.getDeclaredAnnotations();

            if (!checkAnnotationIsSupported(annotations)) {
                continue;
            }

            final Class<?>[] parameterTypes = method.getParameterTypes();

            final InjectionTargetInformationFactory injectionTargetInformationFactory = new InjectionTargetInformationFactory() {

                @Override
                public InjectionTargetInformation create(final Class<?> parameterType, final int parameterIndex) {

                    return new InjectionTargetInformation(parameterType, method,
                            method.getParameterAnnotations()[parameterIndex]);
                }
            };
            final Object[] arguments = createArguments(parameterTypes, injectionTargetInformationFactory);

            try {
                ReflectionUtil.invokeMethod(method, instance, arguments);
            } catch (final Exception e) {
                LOG.warn("could not invoke method", e);
            }

        }
    }

    private Object[] createArguments(final Class<?>[] parameterTypes,
            final InjectionTargetInformationFactory injectionTargetInformationFactory) {
        final Object[] arguments = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            final InjectionTargetInformation injectionTargetInformation = injectionTargetInformationFactory.create(
                    parameterTypes[i], i);

            Entry<Object, Object> injection = inject(injectionTargetInformation);

            if (injection != null) {
                arguments[i] = injection.getValue();
            }

        }

        return arguments;
    }

    private void initFields(final Object instance) {
        final List<Field> fields = ReflectionUtil.getAllFields(instance.getClass());

        for (final Field field : fields) {
            final InjectionTargetInformation injectionTargetInformation = new InjectionTargetInformation(
                    field.getType(), field);

            Entry<Object, Object> injection = inject(injectionTargetInformation);

            if (injection != null) {
                try {
                    ReflectionUtil.setField(field, instance, injection.getValue());

                } catch (final Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }

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

        context.addObjectUnderTest(id, instance);

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
                public InjectionTargetInformation create(final Class<?> parameterType, final int parameterIndex) {
                    return new InjectionTargetInformation(parameterType, constructor,
                            constructor.getParameterAnnotations()[parameterIndex]);
                }
            };

            final Object[] arguments = createArguments(parameterTypes, injectionTargetInformationFactory);

            try {
                return constructor.newInstance(arguments);
            } catch (final Exception e) {
                throw new ObjectUnderTestInstantiationException(e);
            }

        }

        return null;
    }

    private boolean checkAnnotationIsSupported(final Annotation[] annotations) {

        for (final Annotation annotation : annotations) {
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
        return (X) context.getInjectedObject(key);
    }

    /**
     * Returns an instance of the configured {@link MockProvider}
     * 
     * @return the configured {@link MockProvider}
     */
    @SuppressWarnings("unchecked")
    public <X extends MockProvider> X getMockProvider() {
        return (X) configuration.getMockProvider();
    }

    private interface InjectionTargetInformationFactory {
        InjectionTargetInformation create(Class<?> parameterType, int parameterIndex);
    }

    private Entry<Object, Object> inject(final InjectionTargetInformation injectionTargetInformation) {
        for (final Collection<InjectionProvider<?>> collection : configuration.getInjectionProvider()) {
            final Entry<Object, Object> injection = configuration.handleInjectionProvider(collection,
                    injectionTargetInformation);
            if (injection != null) {

                Object injectionKey = injection.getKey();
                // check if mock object already created
                Object injectionValue = context.getInjectedObject(injectionKey) == null ? injection.getValue()
                        : context.getInjectedObject(injectionKey);

                context.addInjectedObject(injectionKey, injectionValue);
                return new MapEntry<Object, Object>(injectionKey, injectionValue);
            }
        }

        return null;
    }

}
