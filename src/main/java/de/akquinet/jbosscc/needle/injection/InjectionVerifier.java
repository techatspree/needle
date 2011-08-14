package de.akquinet.jbosscc.needle.injection;

import java.lang.reflect.Field;

public interface InjectionVerifier {

	boolean verify(Field field);

}
