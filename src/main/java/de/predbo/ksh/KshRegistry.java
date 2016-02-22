package de.predbo.ksh;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

public class KshRegistry {

	private static final AtomicInteger ENTRY_ID = new AtomicInteger(0);
	private final Map<Integer, KshEntry> _registry = new HashMap<Integer, KshEntry>();
	
	public void registerEntry(KshEntry kshEntry) {
		System.out.println("put into registry: " + kshEntry.toString());
		_registry.put(ENTRY_ID.incrementAndGet(), kshEntry);
	}
	
	public Map<Integer, KshEntry> getEntries() {
		System.out.println("called getEntries:");
		for (Entry<Integer, KshEntry> entry : _registry.entrySet()) {
			System.out.println(entry.getValue().toString());
		}
		return _registry;
	}
	
}
