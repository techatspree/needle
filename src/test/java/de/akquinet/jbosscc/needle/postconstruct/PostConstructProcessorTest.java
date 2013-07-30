package de.akquinet.jbosscc.needle.postconstruct;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import de.akquinet.jbosscc.needle.NeedleContext;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

/**
 * @author Jan Galinski, Holisticon AG (jan.galinski@holisticon.de)
 */
public class PostConstructProcessorTest {

    private Runnable runnableMock = EasyMock.createStrictMock(Runnable.class);
    private Runnable secondRunnableMock = EasyMock.createStrictMock(Runnable.class);

    /**
     * a dummy class without init()
     */
    public class A {}

    /**
     * a dummy class with init()
     */
    public class B extends A {

        @PostConstruct
        protected void init() {
            runnableMock.run();
        }

    }

    /**
     * used to test postconstruct hierarchy
     */
    public class C extends B {

        @PostConstruct
        public void initC() {
            secondRunnableMock.run();
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

    // This Processor test does not use the NeeldeRule!
    @ObjectUnderTest(postConstruct = true)
    private C instanceAndParentClassHavePostconstructMethods = new C();

    @Before
    public void setUp() {
        assertNotNull(postConstructProcessor);
    }

    @Test
    public void testWithoutPostConstructMethod() throws Exception {
        NeedleContext context = new NeedleContext(this);
        ObjectUnderTest objectUnderTestAnnotation = getObjectUnderTestAnnotation("isConfiguredForPostConstructionButDoesNotContainMethod");
        context.addObjectUnderTest(objectUnderTestAnnotation.id(), isConfiguredForPostConstructionButDoesNotContainMethod, objectUnderTestAnnotation);
        
        EasyMock.replay(runnableMock);
        postConstructProcessor.process(context);
        EasyMock.verify(runnableMock);
    }

    @Test
    public void testWithPostConstructMethod() throws Exception {
        runnableMock.run();
        EasyMock.replay(runnableMock);
        
        NeedleContext context = new NeedleContext(this);
        ObjectUnderTest objectUnderTestAnnotation = getObjectUnderTestAnnotation("isConfiguredForPostConstruction");
        context.addObjectUnderTest(objectUnderTestAnnotation.id(), isConfiguredForPostConstruction, objectUnderTestAnnotation);
        
        postConstructProcessor.process(context);
        EasyMock.verify(runnableMock);

    }

    @Test
    public void testWithPostConstructMethod_NotConfigured() throws Exception {
        EasyMock.replay(runnableMock);
        
        NeedleContext context = new NeedleContext(this);
        ObjectUnderTest objectUnderTestAnnotation = getObjectUnderTestAnnotation("isNotConfiguredForPostConstruction");
        context.addObjectUnderTest(objectUnderTestAnnotation.id(), isNotConfiguredForPostConstruction, objectUnderTestAnnotation);
        
        postConstructProcessor.process(context);
        EasyMock.verify(runnableMock);
    }

    @Test
    public void shouldCallPostConstructOnInstanceAndParent() throws Exception {
        runnableMock.run();
        secondRunnableMock.run();
        EasyMock.replay(runnableMock, secondRunnableMock);

        NeedleContext context = new NeedleContext(this);
        ObjectUnderTest objectUnderTestAnnotation = getObjectUnderTestAnnotation("instanceAndParentClassHavePostconstructMethods");
        context.addObjectUnderTest(objectUnderTestAnnotation.id(), instanceAndParentClassHavePostconstructMethods, objectUnderTestAnnotation);
       
        
        postConstructProcessor.process(context);
        EasyMock.verify(runnableMock, secondRunnableMock);
    }

    @Test
    public void shouldFindTwoPostconstructMethodsForC() throws Exception {
        final Set<Method> methods = postConstructProcessor.getPostConstructMethods(C.class);
        assertThat(methods.size(), is(2));
    }

    private ObjectUnderTest getObjectUnderTestAnnotation(final String fieldname) {
        return ReflectionUtil.getField(getClass(), fieldname).getAnnotation(ObjectUnderTest.class);
    }

}
