package de.akquinet.jbosscc.needle.injection;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

public class InjectionPriorityTest {

	private Map map = new HashMap();

	private InjectionProvider<Map> injectionProvider = new CustomMapInjectionProvider(){
		public Map getInjectedObject(java.lang.Class<?> injectionPointType) {
			return map;
		};
	};

	@Rule
	public NeedleRule needleRule = new NeedleRule(injectionProvider);


	@ObjectUnderTest
	private CustomeInjectionTestComponent component;

	@Test
	public void testInjectionProviderPriority() throws Exception {
		Assert.assertSame(map, component.getMap());
    }

}
