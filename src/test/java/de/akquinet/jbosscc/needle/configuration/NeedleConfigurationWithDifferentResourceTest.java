package de.akquinet.jbosscc.needle.configuration;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.ClassRule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.db.operation.hsql.HSQLDeleteOperation;

public class NeedleConfigurationWithDifferentResourceTest {

    // load mockito resource and switch back to default after().
    @ClassRule
    public static NeedleConfigurationResetRule configurationReset = new NeedleConfigurationResetRule("needle-mockito");

    private final NeedleConfiguration needleConfiguration = NeedleConfiguration.get();

    @Test
    public void shouldReTestWithDifferentPropertyFile() throws Exception {

        // repeat other tests on needle-mockito file
        assertThat(needleConfiguration.getMockProviderClassName(), is("de.akquinet.jbosscc.needle.mock.MockitoProvider"));
        assertEquals(HSQLDeleteOperation.class.getName(), needleConfiguration.getDBOperationClassName());
        assertEquals(HSQLDeleteOperation.class.getName(), needleConfiguration.getDBOperationClassName());
        assertEquals(HSQLDeleteOperation.class.getName(), needleConfiguration.getDBOperationClassName());

    }

}
