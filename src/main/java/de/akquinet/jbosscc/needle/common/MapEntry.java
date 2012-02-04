package de.akquinet.jbosscc.needle.common;

public class MapEntry<K, V> implements java.util.Map.Entry<K, V> {

	private K key;
	private V value;

	public MapEntry(K key, V value) {
	    super();
	    this.key = key;
	    this.value = value;
    }

	@Override
	public K getKey() {
		return key;
	}

	@Override
	public V getValue() {
		return value;
	}

	@Override
	public V setValue(Object value) {
		throw new UnsupportedOperationException();
	}

}
