package de.akquinet.jbosscc.needle.injection;

import java.lang.reflect.Field;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.jbosscc.needle.annotation.AnnotationProcessor;
import de.akquinet.jbosscc.needle.annotation.InjectInto;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

public class InjectionIntoAnnotationProcessor implements AnnotationProcessor<InjectInto> {

	private static final Logger LOG = LoggerFactory.getLogger(InjectionIntoAnnotationProcessor.class);

	@Override
	public void process(final Object instance) {
		final Class<? extends Object> clazz = instance.getClass();
		final List<Field> fields = ReflectionUtil.getAllFieldsWithAnnotation(clazz, InjectInto.class);

		for (Field field : fields) {
			final InjectInto annotation = field.getAnnotation(InjectInto.class);
			try {

				final Object fieldValue = ReflectionUtil.getFieldValue(instance, field);

				final String value = annotation.targetComponent();
				final String fieldName = annotation.fieldName();

				final Field objectUnderTestField = ReflectionUtil.getField(clazz, value);

				final Object targetObject = ReflectionUtil.getFieldValue(instance, objectUnderTestField);

				ReflectionUtil.setField(fieldName, targetObject, fieldValue);

			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}

		}

	}

}
