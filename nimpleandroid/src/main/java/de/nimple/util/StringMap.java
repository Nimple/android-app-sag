package de.nimple.util;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

public class StringMap {
	private Map<String, String> map = new HashMap<String, String>();

	public String put(String key, String value) {
		return map.put(key, value.trim());
	}

	public String get(Object key) {
		if (map.get(key) == null) {
			return "";
		}
		return map.get(key);
	}

	public boolean contains(String key) {
		return (map.get(key) != null && !map.get(key).trim().equals(""));
	}

	public int size() {
		return map.size();
	}
}