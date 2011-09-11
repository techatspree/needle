package de.akquinet.jbosscc.needle.injection;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.jbosscc.needle.annotation.InjectInto;
import de.akquinet.jbosscc.needle.annotation.InjectIntoMany;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

public class InjectionAnnotationProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(InjectionAnnotationProcessor.class);

	public void process(final Object testcase, final Map<String, Object> objectUnderTestMap) {
		proccessInjectIntoMany(testcase, objectUnderTestMap);
		proccessInjectInto(testcase, objectUnderTestMap);
	}

	private void proccessInjectIntoMany(final Object testcase, final Map<String, Object> objectUnderTestMap) {
		final List<Field> fieldsWithInjectIntoManyAnnotation = ReflectionUtil.getAllFieldsWithAnnotation(testcase,
		        InjectIntoMany.class);

		for (Field field : fieldsWithInjectIntoManyAnnotation) {
			final Object sourceObject = ReflectionUtil.getFieldValue(testcase, field);

			InjectIntoMany injectIntoManyAnnotation = field.getAnnotation(InjectIntoMany.class);
			InjectInto[] value = injectIntoManyAnnotation.value();

			// inject into all object under test instance
			if (value.length == 0) {
				for (Entry<String, Object> entry : objectUnderTestMap.entrySet()) {
					injectByType(entry.getValue(), sourceObject, field.getType());

				}
			} else {
				for (InjectInto injectInto : value) {
					processInjectInto(objectUnderTestMap, field, sourceObject, injectInto);
				}
			}
		}
	}

	private void proccessInjectInto(Object testcase, Map<String, Object> objectUnderTestMap) {
		final List<Field> fields = ReflectionUtil.getAllFieldsWithAnnotation(testcase, InjectInto.class);

		for (Field field : fields) {
			final Object sourceObject = ReflectionUtil.getFieldValue(testcase, field);
			processInjectInto(objectUnderTestMap, field, sourceObject, field.getAnnotation(InjectInto.class));
		}

	}

	private void processInjectInto(final Map<String, Object> objectUnderTestMap, Field field, Object sourceObject,
	        InjectInto injectInto) {
		final Object object = objectUnderTestMap.get(injectInto.targetComponentId());
		if (object != null) {

			if (injectInto.fieldName().equals("")) {
				injectByType(object, sourceObject, field.getType());
			} else {
				injectByFieldName(object, sourceObject, injectInto.fieldName());
			}

		} else {
			LOG.warn("could not inject component {} -  unknown object under test with id {}", sourceObject,
			        injectInto.targetComponentId());
		}
	}

	private void injectByType(Object objectUnderTest, Object sourceObject, Class<?> type) {
		final List<Field> fields = ReflectionUtil.getAllFieldsAssinableFrom(type, objectUnderTest.getClass());

		for (Field field : fields) {
			try {
				ReflectionUtil.setField(field, objectUnderTest, sourceObject);
			} catch (Exception e) {
				LOG.warn("could not inject into component " + objectUnderTest, e);
			}
		}

	}

	private void injectByFieldName(Object objectUnderTest, Object sourceObject, String fieldName) {
		try {
			ReflectionUtil.setField(fieldName, objectUnderTest, sourceObject);
		} catch (Exception e) {
			LOG.warn("could not inject into component " + objectUnderTest, e);
		}
	}

}
