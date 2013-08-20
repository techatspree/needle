package de.akquinet.jbosscc.needle.injection.cdi.instance;

import static de.akquinet.jbosscc.needle.junit.NeedleBuilders.needleRule;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.Mock;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

@Ignore
public class InjectMockForInstanceTest {

    @Rule
    public final NeedleRule needle = needleRule().with("needle-mockito").build();

    @Inject
    private Instance<InstanceTestBean> testBeanInstance;

    @Mock
    private InstanceTestBean testBean;

    @Test
    public void shouldInjectMockInstance() throws Exception {
        when(testBean.toString()).thenReturn("foo");

        assertThat(testBeanInstance, not(nullValue()));
        assertThat(testBeanInstance.get(), not(nullValue()));

        assertThat(testBeanInstance.toString(), is("foo"));

    }

}
