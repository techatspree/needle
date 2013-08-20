package de.akquinet.jbosscc.needle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ejb.SessionContext;
import javax.persistence.EntityManagerFactory;

import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.InjectIntoMany;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.junit.DatabaseRule;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

public class NeedleTest {

    @Rule
    public DatabaseRule databaseRule = new DatabaseRule();

    @Rule
    public NeedleRule needle = new NeedleRule(databaseRule);

    @ObjectUnderTest
    private MyComponentBean componentBean;

    @InjectIntoMany
    @ObjectUnderTest(implementation = MyEjbComponentBean.class)
    private MyEjbComponent ejbComponent;

    private MyComponentBean componentBean1 = new MyComponentBean();

    @ObjectUnderTest
    private MyComponentBean componentBean2 = componentBean1;

    @Test
    public void testBasicInjection() throws Exception {
        assertNotNull(componentBean);
        assertNotNull(componentBean.getEntityManager());
        assertNotNull(componentBean.getMyEjbComponent());

        MyEjbComponent mock = (MyEjbComponent) needle.getInjectedObject(MyEjbComponent.class);

        assertNotNull(mock);
    }

    @Test
    public void testResourceMock() throws Exception {
        SessionContext sessionContextMock = (SessionContext) needle.getInjectedObject(SessionContext.class);
        assertNotNull(sessionContextMock);

        assertNotNull(needle.getInjectedObject("queue1"));
        assertNotNull(needle.getInjectedObject("queue2"));
    }

    @Test
    public void testInjectInto() throws Exception {
        assertNotNull(ejbComponent);
        assertEquals(ejbComponent, componentBean.getMyEjbComponent());
    }

    @Test
    public void testInitInstance() throws Exception {
        assertEquals(componentBean1, componentBean2);
    }

    @Test
    public void testEntityManagerFactoryInjection() throws Exception {
        EntityManagerFactory entityManagerFactory = componentBean2.getEntityManagerFactory();
        assertNotNull(entityManagerFactory);

        assertNotNull(needle.getInjectedObject(EntityManagerFactory.class));

    }

}
