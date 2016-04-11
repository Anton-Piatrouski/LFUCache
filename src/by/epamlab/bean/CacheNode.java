package by.epamlab.bean;

import by.epamlab.util.LFUCache;

public class CacheNode {

	private final String key;
	private Integer value;
	private int frequency;
	
	public CacheNode(String key, Integer value) {
		this.key = key;
		this.value = value;
		this.frequency = LFUCache.DEFAULT_FREQUENCY;
	}
	
	public void setValue(Integer value) {
		this.value = value;
	}
	
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	public String getKey() {
		return key;
	}
	
	public Integer getValue() {
		return value;
	}
	
	public int getFrequency() {
		return frequency;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CacheNode other = (CacheNode) obj;
		if (key == null) {
			if (other.key != null) {
				return false;
			}
		} else if (!key.equals(other.key)) {
			return false;
		}
		return true;
	}
}
