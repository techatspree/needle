package de.akquinet.jbosscc.needle.mock;

import java.lang.reflect.Field;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.jbosscc.needle.NeedleContext;
import de.akquinet.jbosscc.needle.annotation.Mock;
import de.akquinet.jbosscc.needle.injection.InjectionConfiguration;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

public class MockAnnotationProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(MockAnnotationProcessor.class);

	public void process(final NeedleContext context, final InjectionConfiguration configuration) {
	    final MockProvider mockProvider = configuration.getMockProvider();
	    final List<Field> fields = context.getAnnotatedTestcaseFields(Mock.class);

	    for (Field field : fields) {
	        Object mock = mockProvider.createMockComponent(field.getType());

	        try {
				ReflectionUtil.setField(field, context.getTest(), mock);
			} catch (Exception e) {
				LOG.warn("could not assign mock obejct " + mock, e);
			}
        }




    }



}
