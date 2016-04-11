import by.epamlab.util.LFUCache;

public class Runner {

	public static void main(String[] args) {

		LFUCache lfu = new LFUCache(4, 0.8f);
		lfu.put("a", 1);
		lfu.put("b", 2);
		lfu.put("c", 3);
		lfu.put("d", 4);
		
		lfu.get("c");
		lfu.get("d");
		lfu.get("c");
		
		lfu.get("c");
		lfu.get("c");
		lfu.get("d");
		lfu.get("d");
		lfu.get("b");
		lfu.get("b");
		lfu.get("a");
		lfu.get("a");
		
		lfu.get("d");
		lfu.get("c");
		
		System.out.println(lfu.toString());
		
		lfu.put("e", 5);
		
		System.out.println(lfu.toString());
	}

}
