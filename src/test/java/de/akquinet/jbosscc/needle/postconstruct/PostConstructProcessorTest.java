package de.akquinet.jbosscc.needle.postconstruct;

import static org.junit.Assert.assertNotNull;

import java.util.HashSet;

import javax.annotation.PostConstruct;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

/**
 * 
 * @author Jan Galinski, Holisticon AG (jan.galinski@holisticon.de)
 * 
 */
public class PostConstructProcessorTest {

    Runnable runnableMock = EasyMock.createStrictMock(Runnable.class);

    /**
     * a dummy class without init()
     */
    public class A {
    }

    /**
     * a dummy class with init()
     */
    public class B extends A {

        @PostConstruct
        public void init() {
            runnableMock.run();
        }

    }

    private static final HashSet<Class<?>> ANNOTATIONS = new HashSet<Class<?>>();

    static {
        ANNOTATIONS.add(PostConstruct.class);
    }

    private final PostConstructProcessor postConstructProcessor = new PostConstructProcessor(ANNOTATIONS);

    // This Processor test does not use the NeeldeRule!
    @ObjectUnderTest(postConstruct = true)
    private A isConfiguredForPostConstructionButDoesNotContainMethod = new A();

    // This Processor test does not use the NeeldeRule!
    @ObjectUnderTest(postConstruct = true)
    private B isConfiguredForPostConstruction = new B();

    // This Processor test does not use the NeeldeRule!
    @ObjectUnderTest
    private B isNotConfiguredForPostConstruction = new B();

    @Before
    public void setUp() {
        assertNotNull(postConstructProcessor);
    }

    @Test
    public void testWithoutPostConstructMethod() throws Exception {
        EasyMock.replay(runnableMock);
        postConstructProcessor.process(
                getObjectUnderTestAnnotation("isConfiguredForPostConstructionButDoesNotContainMethod"),
                isConfiguredForPostConstructionButDoesNotContainMethod);
        EasyMock.verify(runnableMock);
    }

    @Test
    public void testWithPostConstructMethod() throws Exception {
        runnableMock.run();
        EasyMock.replay(runnableMock);
        postConstructProcessor.process(getObjectUnderTestAnnotation("isConfiguredForPostConstruction"),
                isConfiguredForPostConstruction);
        EasyMock.verify(runnableMock);

    }
    
    @Test
    public void testWithPostConstructMethod_NotConfigured() throws Exception {
        EasyMock.replay(runnableMock);
        postConstructProcessor.process(
                getObjectUnderTestAnnotation("isNotConfiguredForPostConstruction"),
                isNotConfiguredForPostConstruction);
        EasyMock.verify(runnableMock);
    }

    private ObjectUnderTest getObjectUnderTestAnnotation(final String fieldname) {
        return ReflectionUtil.getField(getClass(), fieldname).getAnnotation(ObjectUnderTest.class);
    }

}