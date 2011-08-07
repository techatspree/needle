package de.akquinet.jbosscc.needle.injection;

import java.lang.reflect.Field;
import java.util.List;

import de.akquinet.jbosscc.needle.annotation.AnnotationProcessor;
import de.akquinet.jbosscc.needle.annotation.InjectInto;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

public class InjectionIntoAnnotationPrcessor implements AnnotationProcessor<InjectInto> {

	@Override
	public void process(Object instance) {
		Class<? extends Object> clazz = instance.getClass();
		List<Field> fields = ReflectionUtil.getAllFieldsWithAnnotation(clazz, InjectInto.class);

		for (Field field : fields) {
			InjectInto annotation = field.getAnnotation(InjectInto.class);
			try {

				Object fieldValue = ReflectionUtil.getFieldValue(instance, field);

				String value = annotation.targetComponent();
				String fieldName = annotation.fieldName();

				Field objectUndterTestField =  ReflectionUtil.getField(clazz, value);
				if (objectUndterTestField == null) {
					throw new NoSuchFieldException("***" + value);
				}

				Object targetObject = ReflectionUtil.getFieldValue(instance, objectUndterTestField);

				Class<? extends Object> targetClass = targetObject.getClass();

				Field targetField = ReflectionUtil.getField(targetClass, fieldName);
				ReflectionUtil.setField(targetField, targetObject, fieldValue);

			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
