package de.akquinet.jbosscc.needle.db;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.akquinet.jbosscc.needle.db.transaction.TransactionHelper;
import de.akquinet.jbosscc.needle.injection.InjectionTargetInformation;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

public class TransactionHelperProviderTest {
	private TransactionHelperProvider provider = new TransactionHelperProvider(
	        new DatabaseTestcase());

	@SuppressWarnings("unused")
    private TransactionHelper helper;

	@Test
	public void testVerify() throws Exception {
		InjectionTargetInformation injectionTargetInformation = new InjectionTargetInformation(
		        TransactionHelper.class, ReflectionUtil.getField(this.getClass(), "helper"));
		assertTrue(provider.verify(injectionTargetInformation));
		assertNotNull(provider.getInjectedObject(TransactionHelper.class));
	}

}
