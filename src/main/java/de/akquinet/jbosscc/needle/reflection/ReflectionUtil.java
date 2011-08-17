package de.akquinet.jbosscc.needle.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ReflectionUtil {

	private static final Logger LOG = LoggerFactory.getLogger(ReflectionUtil.class);

	private ReflectionUtil() {
		super();
	}

	public static List<Field> getAllFieldsWithAnnotation(final Class<?> clazz,
	        final Class<? extends Annotation> annotation) {
		Field[] fields = clazz.getDeclaredFields();

		List<Field> result = new ArrayList<Field>();
		for (Field field : fields) {
			if (field.getAnnotation(annotation) != null) {
				result.add(field);
			}
		}

		Class<?> superClazz = clazz.getSuperclass();

		while (superClazz != null) {
			Field[] extraFields = superClazz.getDeclaredFields();

			for (Field extra : extraFields) {
				if (Modifier.isPrivate(extra.getModifiers()) && extra.getAnnotation(annotation) != null) {
					result.add(extra);
				}
			}

			superClazz = superClazz.getSuperclass();
		}

		return result;
	}

	public static List<Field> getAllFieldsWithAnnotation(final Object instance,
	        final Class<? extends Annotation> annotation) {
		return getAllFieldsWithAnnotation(instance.getClass(), annotation);

	}

	public static List<Field> getAllFields(final Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();

		List<Field> result = new ArrayList<Field>();

		Collections.addAll(result, fields);

		Class<?> superClazz = clazz.getSuperclass();

		while (superClazz != null) {
			Field[] extraFields = superClazz.getDeclaredFields();

			Collections.addAll(result, extraFields);

			superClazz = superClazz.getSuperclass();
		}

		return result;
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
		} catch (Exception e) {
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
		Class<?> clazz = object.getClass();

		while (clazz != null) {
			try {
				setFieldValue(object, clazz, fieldName, value);
				return;
			} catch (NoSuchFieldException e) {
			}

			clazz = clazz.getSuperclass();
		}
	}

	/**
	 * Get the value of a given fields on a given object via reflection.
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
			Field field = clazz.getDeclaredField(fieldName);
			return getFieldValue(object, field);
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not get field value: " + fieldName, e);
		}
	}

	/**
	 * Get the value of a given fields on a given object via reflection.
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
	 * Get the value of a given fields on a given object via reflection.
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

		for (final Method declaredMethod : clazz.getDeclaredMethods()) {
			if (declaredMethod.getName().equals(methodName)) {
				final Class<?>[] parameterTypes = declaredMethod.getParameterTypes();

				if (parameterTypes.length == arguments.length) {
					final boolean match = checkArguments(parameterTypes, arguments);

					if (match) {
						return invokeMethod(declaredMethod, object, arguments);
					}
				}
			}
		}

		throw new IllegalArgumentException("Method " + methodName + ":" + Arrays.toString(arguments) + " not found");
	}

	public static Object invokeMethod(final Method method, final Object instance, Object... arguments) throws Exception {
		try {
			if (!method.isAccessible()) {
				method.setAccessible(true);
			}

			return method.invoke(instance, arguments);
		} catch (final Exception exc) {
			LOG.warn("Error invoking method: " + method.getName(), exc);
			Throwable cause = exc.getCause();
			if (cause instanceof Exception) {
				throw (Exception) cause;
			}
			throw exc;
		}
	}

	public static Method getMethod(final Class<?> clazz, final String methodName, Class<?>... parameterTypes)
	        throws NoSuchMethodException {

		Class<?> superClazz = null;
		try {
			return clazz.getDeclaredMethod(methodName, parameterTypes);

		} catch (Exception e) {
			superClazz = clazz.getSuperclass();
		}

		while (superClazz != null) {
			try {
				return superClazz.getDeclaredMethod(methodName, parameterTypes);
			} catch (Exception e) {
				superClazz = superClazz.getSuperclass();
			}
		}

		throw new NoSuchMethodException(methodName);

	}

	private static boolean checkArguments(final Class<?>[] parameterTypes, final Object[] arguments) {
		boolean match = true;

		for (int i = 0; i < arguments.length; i++) {
			final Class<?> parameterClass = parameterTypes[i];
			final Class<?> argumentClass = arguments[i].getClass();

			if (!parameterClass.isAssignableFrom(argumentClass)) {
				boolean isInt = (parameterClass == int.class) && (argumentClass == Integer.class);
				boolean isDouble = (parameterClass == double.class) && (argumentClass == Double.class);

				if (!isInt && !isDouble) {
					match = false;
				}
			}
		}

		return match;
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
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	public static void setField(Field field, Object target, Object value) throws Exception {
		if (!field.isAccessible()) {
			field.setAccessible(true);
		}

		field.set(target, value);

	}

	public static void setField(String fieldName, Object target, Object value) throws Exception {

		final Field field = ReflectionUtil.getField(target.getClass(), fieldName);

		if (!field.isAccessible()) {
			field.setAccessible(true);
		}

		field.set(target, value);

	}

	public static Field getField(final Class<?> clazz, final String fieldName) {
		Field field = null;
		field = getFieldByName(clazz, fieldName);

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
		} catch (NoSuchFieldException e) {
			return null;
		}
	}

}
