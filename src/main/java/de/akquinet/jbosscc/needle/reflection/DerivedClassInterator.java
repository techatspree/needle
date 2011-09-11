package de.akquinet.jbosscc.needle.reflection;

abstract class DerivedClassInterator {
	private final Class<?> clazz;

	DerivedClassInterator(Class<?> clazz) {
		super();
		this.clazz = clazz;
	}

	void iterate() {
		Class<?> superClazz = clazz;

		while (superClazz != null) {

			handleClass(superClazz);

			superClazz = superClazz.getSuperclass();
		}
	}

	protected abstract void handleClass(Class<?> clazz);
}
