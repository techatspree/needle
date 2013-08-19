package de.akquinet.jbosscc.needle.mock;

import java.lang.reflect.Field;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.jbosscc.needle.NeedleContext;
import de.akquinet.jbosscc.needle.annotation.Mock;
import de.akquinet.jbosscc.needle.injection.InjectionConfiguration;
import de.akquinet.jbosscc.needle.processor.AbstractNeedleProcessor;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

public class MockAnnotationProcessor extends AbstractNeedleProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(MockAnnotationProcessor.class);

    private final MockProvider mockProvider;

    public MockAnnotationProcessor(final InjectionConfiguration configuration) {
        super(configuration);
        mockProvider = configuration.getMockProvider();
    }


    @Override
    public void process(final NeedleContext context) {
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
