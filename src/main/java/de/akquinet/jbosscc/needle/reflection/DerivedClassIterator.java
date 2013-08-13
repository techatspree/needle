package de.akquinet.jbosscc.needle.reflection;


abstract class DerivedClassIterator {
    private final Class<?> clazz;

    DerivedClassIterator(Class<?> clazz) {
        super();
        this.clazz = clazz;
    }

    boolean iterate() {
        Class<?> superClazz = clazz;

        boolean success = false;
        while (superClazz != null) {

            success |= handleClass(superClazz);

            superClazz = superClazz.getSuperclass();
        }
        return success;
    }

    protected abstract boolean handleClass(Class<?> clazz);
}
