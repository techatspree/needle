package de.akquinet.jbosscc.needle.injection;

import javax.inject.Inject;
import javax.inject.Provider;

import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;

public class InjectionInstanceProvider {

	public static class A {

		@Inject
		private Provider<B> b;

	}

	public static interface B {

	}

	@ObjectUnderTest
	private A a;

	@Test
	public void test() {
	}

}
