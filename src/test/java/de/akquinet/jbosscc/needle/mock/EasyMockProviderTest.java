package de.akquinet.jbosscc.needle.mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.easymock.EasyMock;
import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.MyComponent;
import de.akquinet.jbosscc.needle.MyComponentBean;
import de.akquinet.jbosscc.needle.MyEjbComponent;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.db.User;
import de.akquinet.jbosscc.needle.injection.constuctor.UserDao;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

public class EasyMockProviderTest {

    @Rule
    public final NeedleRule needleRule = new NeedleRule();

    @ObjectUnderTest(implementation = MyComponentBean.class)
    private MyComponent component;

    private EasyMockProvider mockProvider = (EasyMockProvider) needleRule.getMockProvider();

    @Test
    public void testNiceMock() throws Exception {
        String testMock = component.testMock();
        assertNull(testMock);
    }

    @Test
    public void testStricMock() throws Exception {
        MyEjbComponent myEjbComponentMock = (MyEjbComponent) needleRule.getInjectedObject(MyEjbComponent.class);
        assertNotNull(myEjbComponentMock);

        EasyMock.resetToStrict(myEjbComponentMock);

        EasyMock.expect(myEjbComponentMock.doSomething()).andReturn("Hello World");

        mockProvider.replayAll();
        String testMock = component.testMock();

        assertEquals("Hello World", testMock);

        mockProvider.verifyAll();
    }

    @Test(expected = IllegalStateException.class)
    public void testResetToStric_Mocks() throws Exception {

        User userMock = mockProvider.createMock(User.class);
        UserDao userDaoMock = mockProvider.createMock(UserDao.class);
        mockProvider.resetToStrict(userMock, userDaoMock);
        userMock.getId();

        userDaoMock.getUser();

        mockProvider.replayAll();

        mockProvider.verifyAll();
    }

    @Test(expected = IllegalStateException.class)
    public void testResetToStric_Mock() throws Exception {

        User userMock = mockProvider.resetToStrict(mockProvider.createMock(User.class));

        userMock.getId();

        mockProvider.replayAll();

        mockProvider.verifyAll();
    }
}
