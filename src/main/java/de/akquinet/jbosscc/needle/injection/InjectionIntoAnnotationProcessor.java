package de.akquinet.jbosscc.needle.injection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.jbosscc.needle.annotation.AnnotationProcessor;
import de.akquinet.jbosscc.needle.annotation.InjectInto;
import de.akquinet.jbosscc.needle.annotation.InjectManyInto;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

public class InjectionIntoAnnotationProcessor implements AnnotationProcessor<InjectInto> {

  private static final Logger LOG = LoggerFactory.getLogger(InjectionIntoAnnotationProcessor.class);

  @Override
  public void process(final Object instance) {
    try {
      final Class<? extends Object> clazz = instance.getClass();
      final List<Field> fields = getAnnotatedFields(clazz);

      for (final Field field : fields) {
        final List<InjectInto> injectIntos = getInjectIntos(field);

        for (final InjectInto annotation : injectIntos) {
          final Object fieldValue = ReflectionUtil.getFieldValue(instance, field);

          final String value = annotation.targetComponent();
          final String fieldName = annotation.fieldName();

          final Field objectUnderTestField = ReflectionUtil.getField(clazz, value);

          final Object targetObject = ReflectionUtil.getFieldValue(instance, objectUnderTestField);

          ReflectionUtil.setField(fieldName, targetObject, fieldValue);
        }
      }
    } catch (final Exception e) {
      LOG.error(e.getMessage(), e);
    }
  }

  private List<InjectInto> getInjectIntos(final Field field) {
    final InjectInto annotation = field.getAnnotation(InjectInto.class);

    if (annotation == null) {
      final InjectManyInto injectManyInto = field.getAnnotation(InjectManyInto.class);
      return Arrays.asList(injectManyInto.value());
    } else {
      return Arrays.asList(annotation);
    }
  }

  private List<Field> getAnnotatedFields(final Class<? extends Object> clazz) {
    final List<Field> result = new ArrayList<Field>();

    final List<Field> injectIntoFields = ReflectionUtil.getAllFieldsWithAnnotation(clazz, InjectInto.class);
    final List<Field> injectManyIntoFields = ReflectionUtil.getAllFieldsWithAnnotation(clazz, InjectManyInto.class);

    result.addAll(injectIntoFields);
    result.addAll(injectManyIntoFields);

    return result;
  }
}
