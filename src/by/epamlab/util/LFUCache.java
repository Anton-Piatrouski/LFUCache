package by.epamlab.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import by.epamlab.bean.CacheNode;

public class LFUCache {

	public static final int DEFAULT_FREQUENCY = 0;
	
	private final Map<String, CacheNode> cache;
	private final Set<CacheNode>[] frequencyList;
	private int lowestFrequency;
	private int highestFrequency;
	private final int maxCacheSize;
	private final float evictionFactor;
	
	@SuppressWarnings("unchecked")
	public LFUCache(int maxCacheSize, float evictionFactor) {
		cache = new HashMap<String, CacheNode>(maxCacheSize);
		frequencyList = new Set[maxCacheSize + 1];
		lowestFrequency = DEFAULT_FREQUENCY;
		highestFrequency = maxCacheSize;
		this.maxCacheSize = maxCacheSize;
		this.evictionFactor = evictionFactor;
		initFrequencyList();
	}
	
	/**
	 * The new entry is added to hash table and to the first frequency’s linked set.
	 * If the cache reaches its maximum size then we need to evict few entries based on eviction factor.
	 * Internally, a cache entry is created for each key/value pair to hold the key, value and the frequency.
	 * Since the cache entry is new to cache, its frequency will be 0.
	 * @param key	key with which the specified value is to be associated
	 * @param value	value to be associated with the specified key
	 */
	public void put(String key, Integer value) {
		CacheNode node = cache.get(key);
		if (node == null) {
			if (cache.size() == maxCacheSize) {
				evict();
			}
			Set<CacheNode> nodes = frequencyList[DEFAULT_FREQUENCY];
			node = new CacheNode(key, value);
			nodes.add(node);
			cache.put(key, node);
		} else {
			node.setValue(value);
		}
	}
	
	/**
	 * Returns the value to which the specified key is mapped, or null if cache contains no mapping for the key.
	 * Accessing the key, moves it to the next frequency list.
	 * @param	key the key whose associated value is to be returned
	 * @return	the value to which the specified key is mapped, or null if cache contains no mapping for the key
	 */
	public Integer get(String key) {
		Integer value = null;
		
		CacheNode node = cache.get(key);
		if (node != null) {
			moveToNextFrequency(node);
			
			value = node.getValue();
		}
		return value;
	}
	
	/**
	 * Evicts few entries based on eviction factor.
	 * The number of entries deleted would  be = max cache size * eviction factor.
	 * The entries would be removed from cache in order of the frequency that they appear, from the lowest frequency and to the highest frequency.
	 */
	private void evict() {
		int counter = 0;
		int target = maxCacheSize;
		target *= evictionFactor;
		
		for (int i = lowestFrequency; i <= highestFrequency; i++) {
			if (counter == target) {
				return;
			}
			Set<CacheNode> nodes = frequencyList[i];
			
			if (!nodes.isEmpty()) {
				Iterator<CacheNode> itr = nodes.iterator();
				
				while (itr.hasNext() && (counter < target)) {
					CacheNode node = itr.next();
					itr.remove();
					cache.remove(node.getKey());
					counter++;
				}
			}
		}
	}
	
	private void initFrequencyList() {
		for (int i = 0; i <= highestFrequency; i++) {
			frequencyList[i] = new LinkedHashSet<CacheNode>();
		}
	}
	
	/**
	 * Removes the cache entry from current frequency list and adds it to the next one.
	 * Since the cache entry is already there in the highest frequency list, it will simply be removed from the list and added again,
	 * so most recently accessed elements will be ahead of less recently ones.
	 * @param node cache entry is to be moved to the next frequency list
	 */
	private void moveToNextFrequency(CacheNode node) {
		int currentFrequency = node.getFrequency();
		Set<CacheNode> currentNodes = frequencyList[currentFrequency];
		currentNodes.remove(node);
		
		if (currentFrequency < highestFrequency) {
			int nextFrequency = currentFrequency + 1;
			Set<CacheNode> nextNodes = frequencyList[nextFrequency];
			nextNodes.add(node);
			node.setFrequency(nextFrequency);
		} else {
			currentNodes.add(node);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i <= highestFrequency; i++) {
			Set<CacheNode> nodes = frequencyList[i];
			
			sb.append(i).append('\t');
			for (CacheNode node : nodes) {
				sb.append(node.getKey()).append('\t');
			}
			sb.append('\n');
		}
		return sb.toString();
	}
}
