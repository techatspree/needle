package de.akquinet.jbosscc.needle.injection;

import java.util.HashMap;
import java.util.Map;

public class CustomMapInjectionProvider implements InjectionProvider<Map> {

	public static final Map MAP = new HashMap();

	@Override
    public Map getInjectedObject(Class<?> injectionPointType) {
	    return MAP;
    }

	@Override
    public boolean verify(InjectionTargetInformation injectionTargetInformation) {
	    if(injectionTargetInformation.getType() == Map.class){
	    	return true;
	    }
	    return false;
    }

	@Override
    public Object getKey(InjectionTargetInformation injectionTargetInformation) {
	    return injectionTargetInformation.getType();
    }

}
