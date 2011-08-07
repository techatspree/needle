package de.akquinet.jbosscc.needle.injection;

public interface InjectionProvider<T> {

	T get();

	Class<T> getType();

}
