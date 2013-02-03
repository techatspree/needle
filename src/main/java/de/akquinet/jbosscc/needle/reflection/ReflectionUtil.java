package de.akquinet.jbosscc.needle.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ReflectionUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ReflectionUtil.class);

    private static final Map<Class<?>, Class<?>> PRIMITIVES = new HashMap<Class<?>, Class<?>>();

    static {
        PRIMITIVES.put(int.class, Integer.class);
        PRIMITIVES.put(double.class, Double.class);
        PRIMITIVES.put(boolean.class, Boolean.class);
        PRIMITIVES.put(long.class, Long.class);
        PRIMITIVES.put(float.class, Float.class);
        PRIMITIVES.put(char.class, Character.class);
        PRIMITIVES.put(short.class, Short.class);
        PRIMITIVES.put(byte.class, Byte.class);
    }

    private ReflectionUtil() {
        super();
    }

    public static List<Field> getAllFieldsWithAnnotation(final Class<?> clazz,
            final Class<? extends Annotation> annotation) {
        final List<Field> result = new ArrayList<Field>();

        new DerivedClassIterator(clazz) {

            @Override
            protected void handleClass(final Class<?> clazz) {
                final Field[] fields = clazz.getDeclaredFields();

                for (final Field field : fields) {
                    if (field.getAnnotation(annotation) != null) {
                        result.add(field);
                    }
                }

            }
        }.iterate();

        return result;
    }

    public static Map<Class<? extends Annotation>, List<Field>> getAllAnnotatedFields(final Class<?> clazz) {
        final Map<Class<? extends Annotation>, List<Field>> result = new HashMap<Class<? extends Annotation>, List<Field>>();
        final List<Field> fields = getAllFields(clazz);

        for (final Field field : fields) {
            final Annotation[] annotations = field.getAnnotations();
            for (final Annotation annotation : annotations) {
                final Class<? extends Annotation> annotationType = annotation.annotationType();
                List<Field> list = result.get(annotationType);

                if (list == null) {
                    list = new ArrayList<Field>();
                }

                list.add(field);
                result.put(annotationType, list);

            }

        }
        return result;
    }

    public static List<Field> getAllFieldsAssinableFrom(final Class<?> assinableType, final Class<?> clazz) {
        final List<Field> result = new ArrayList<Field>();

        new DerivedClassIterator(clazz) {

            @Override
            protected void handleClass(final Class<?> clazz) {
                final Field[] fields = clazz.getDeclaredFields();

                for (final Field field : fields) {
                    if (field.getType().isAssignableFrom(assinableType)) {
                        result.add(field);
                    }
                }

            }
        }.iterate();

        return result;

    }

    public static List<Field> getAllFieldsWithAnnotation(final Object instance,
            final Class<? extends Annotation> annotation) {
        return getAllFieldsWithAnnotation(instance.getClass(), annotation);

    }

    public static List<Field> getAllFields(final Class<?> clazz) {

        final List<Field> result = new ArrayList<Field>();

        new DerivedClassIterator(clazz) {

            @Override
            protected void handleClass(final Class<?> clazz) {
                final Field[] fields = clazz.getDeclaredFields();

                Collections.addAll(result, fields);

            }
        }.iterate();

        return result;
    }

    /**
     * 
     * @param class object
     * @return list of method objects 
     * 
     * @see Class#getMethods()
     */
    public static List<Method> getMethods(final Class<?> clazz) {
        return Arrays.asList(clazz.getMethods());
    }

    /**
     * Changing the value of a given field.
     * 
     * @param object
     *            -- target object of injection
     * @param clazz
     *            -- type of argument object
     * @param fieldName
     *            -- name of field whose value is to be set
     * @param value
     *            -- object that is injected
     */
    public static void setFieldValue(final Object object, final Class<?> clazz, final String fieldName,
            final Object value) throws NoSuchFieldException {
        final Field field = clazz.getDeclaredField(fieldName);

        try {
            setField(field, object, value);
        } catch (final Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * Changing the value of a given field.
     * 
     * @param object
     *            -- target object of injection
     * @param fieldName
     *            -- name of field whose value is to be set
     * @param value
     *            -- object that is injected
     */
    public static void setFieldValue(final Object object, final String fieldName, final Object value) {

        new DerivedClassIterator(object.getClass()) {

            @Override
            protected void handleClass(final Class<?> clazz) {
                try {
                    setFieldValue(object, clazz, fieldName, value);
                    return;
                } catch (final NoSuchFieldException e) {
                    LOG.warn("could not set field " + fieldName + " value " + value, e);
                }

            }
        }.iterate();

    }

    /**
     * Get the value of a given field on a given object via reflection.
     * 
     * @param object
     *            -- target object of field access
     * @param clazz
     *            -- type of argument object
     * @param fieldName
     *            -- name of the field
     * @return -- the value of the represented field in object; primitive values
     *         are wrapped in an appropriate object before being returned
     */
    public static Object getFieldValue(final Object object, final Class<?> clazz, final String fieldName) {
        try {
            final Field field = clazz.getDeclaredField(fieldName);
            return getFieldValue(object, field);
        } catch (final Exception e) {
            throw new IllegalArgumentException("Could not get field value: " + fieldName, e);
        }
    }

    /**
     * Get the value of a given field on a given object via reflection.
     * 
     * @param object
     *            -- target object of field access
     * @param field
     *            -- target field
     * 
     * @return -- the value of the represented field in object; primitive values
     *         are wrapped in an appropriate object before being returned
     */
    public static Object getFieldValue(final Object object, final Field field) {
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }

            return field.get(object);
        } catch (final Exception e) {
            throw new IllegalArgumentException("Could not get field value: " + field, e);
        }
    }

    /**
     * Get the value of a given field on a given object via reflection.
     * 
     * @param object
     *            -- target object of field access
     * @param fieldName
     *            -- name of the field
     * @return -- the value of the represented field in object; primitive values
     *         are wrapped in an appropriate object before being returned
     */
    public static Object getFieldValue(final Object object, final String fieldName) {
        return getFieldValue(object, object.getClass(), fieldName);
    }

    /**
     * Invoke a given method with given arguments on a given object via
     * reflection.
     * 
     * @param object
     *            -- target object of invocation
     * @param clazz
     *            -- type of argument object
     * @param methodName
     *            -- name of method to be invoked
     * @param arguments
     *            -- arguments for method invocation
     * @return -- method object to which invocation is actually dispatched
     * @throws Exception
     *             - operation exception
     */
    public static Object invokeMethod(final Object object, final Class<?> clazz, final String methodName,
            final Object... arguments) throws Exception {

        Class<?> superClazz = clazz;

        while (superClazz != null) {
            for (final Method declaredMethod : superClazz.getDeclaredMethods()) {
                if (declaredMethod.getName().equals(methodName)) {
                    final Class<?>[] parameterTypes = declaredMethod.getParameterTypes();

                    if (parameterTypes.length == arguments.length && checkArguments(parameterTypes, arguments)) {
                        return invokeMethod(declaredMethod, object, arguments);
                    }
                }
            }

            superClazz = superClazz.getSuperclass();
        }

        throw new IllegalArgumentException("Method " + methodName + ":" + Arrays.toString(arguments) + " not found");
    }

    public static Object invokeMethod(final Method method, final Object instance, final Object... arguments)
            throws Exception {
        try {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }

            return method.invoke(instance, arguments);
        } catch (final Exception exc) {
            LOG.warn("Error invoking method: " + method.getName(), exc);
            final Throwable cause = exc.getCause();
            if (cause instanceof Exception) {
                throw (Exception) cause;
            }
            throw exc;
        }
    }

    public static Method getMethod(final Class<?> clazz, final String methodName, final Class<?>... parameterTypes)
            throws NoSuchMethodException {

        final List<Method> result = new ArrayList<Method>();

        new DerivedClassIterator(clazz) {

            @Override
            protected void handleClass(final Class<?> clazz) {
                try {
                    result.add(clazz.getDeclaredMethod(methodName, parameterTypes));
                    return;
                } catch (final Exception e) {
                    // do nothing
                }

            }
        }.iterate();

        if (result.isEmpty()) {
            throw new NoSuchMethodException(methodName);
        }
        return result.get(0);

    }

    private static boolean checkArguments(final Class<?>[] parameterTypes, final Object[] arguments) {
        boolean match = true;

        for (int i = 0; i < arguments.length; i++) {
            final Class<?> parameterClass = parameterTypes[i];
            final Class<?> argumentClass = arguments[i].getClass();

            if (!parameterClass.isAssignableFrom(argumentClass)
                    && !checkPrimativeArguments(parameterClass, argumentClass)) {
                match = false;
            }
        }

        return match;
    }

    private static boolean checkPrimativeArguments(final Class<?> parameterClass, final Class<?> argumentClass) {
        boolean result = false;
        for (final Entry<Class<?>, Class<?>> entry : PRIMITIVES.entrySet()) {
            result = result || (parameterClass == entry.getKey()) && (argumentClass == entry.getValue());
        }

        return result;
    }

    /**
     * Invoke a given method with given arguments on a given object via
     * reflection.
     * 
     * @param object
     *            -- target object of invocation
     * @param methodName
     *            -- name of method to be invoked
     * @param arguments
     *            -- arguments for method invocation
     * @return -- method object to which invocation is actually dispatched
     * @throws Exception
     */
    public static Object invokeMethod(final Object object, final String methodName, final Object... arguments)
            throws Exception {
        return invokeMethod(object, object.getClass(), methodName, arguments);
    }

    /**
     * Returns the <code>Class</code> object associated with the class or
     * interface with the given string name.
     * 
     * @param className
     *            the fully qualified name of the desired class.
     * @return <code>Class</code> or null
     */
    public static Class<?> forName(final String className) {
        try {
            return Class.forName(className);
        } catch (final ClassNotFoundException e) {
            return null;
        }
    }

    public static void setField(final Field field, final Object target, final Object value) throws Exception {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }

        field.set(target, value);

    }

    public static void setField(final String fieldName, final Object target, final Object value) throws Exception {

        final Field field = ReflectionUtil.getField(target.getClass(), fieldName);

        if (!field.isAccessible()) {
            field.setAccessible(true);
        }

        field.set(target, value);

    }

    public static Field getField(final Class<?> clazz, final String fieldName) {
        Field field = getFieldByName(clazz, fieldName);

        Class<?> superClazz = clazz.getSuperclass();

        while (superClazz != null && field == null) {
            field = getFieldByName(superClazz, fieldName);
            superClazz = superClazz.getSuperclass();
        }

        return field;

    }

    private static Field getFieldByName(final Class<?> clazz, final String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (final NoSuchFieldException e) {
            LOG.warn(e.getMessage(), e);
            return null;
        }
    }

    public static <T> T createInstance(final Class<T> clazz, final Object... parameter) throws Exception {

        final Class<?>[] parameterTypes = new Class<?>[parameter.length];

        for (int i = 0; i < parameter.length; i++) {
            parameterTypes[i] = parameter[i].getClass();
        }

        final Constructor<T> constructor = clazz.getConstructor(parameterTypes);

        return constructor.newInstance(parameter);
    }

}
