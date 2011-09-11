package de.akquinet.jbosscc.needle.injection;

public interface InjectionProvider<T> {

	T getInjectedObject(final Class<?> injectionPointType);

	boolean verify(InjectionTargetInformation injectionTargetInformation);

	Object getKey(InjectionTargetInformation injectionTargetInformation);
}
