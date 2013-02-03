package de.akquinet.jbosscc.needle.injection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import javax.inject.Inject;

import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.db.User;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

public class InjectionQualifierTest {

    private final User currentUser = new User();

    private final InjectionProvider<User> currentUserprovider = new InjectionProvider<User>() {
        @Override
        public boolean verify(final InjectionTargetInformation information) {
            return information.getAnnotation(CurrentUser.class) != null;
        }

        @Override
        public Object getKey(final InjectionTargetInformation information) {
            return CurrentUser.class;
        }

        @Override
        public User getInjectedObject(final Class<?> type) {
            return currentUser;
        }
    };

    @Inject
    @CurrentUser
    private User currentUserToInject;

    @Inject
    private User user;

    @Rule
    public NeedleRule needleRule = new NeedleRule(currentUserprovider);

    @ObjectUnderTest
    private UserDao userDao;

    @Test
    public void testInject() throws Exception {
        assertNotNull(userDao);
        assertEquals(currentUser, userDao.getCurrentUser());
        assertNotNull(userDao.getUser());
        assertNotSame(currentUser, userDao.getUser());

        assertEquals(currentUser, needleRule.getInjectedObject(CurrentUser.class));
    }

    @Test
    public void testTestInjection() throws Exception {
        assertNotNull(user);
        assertNotNull(currentUserToInject);
    }

}
