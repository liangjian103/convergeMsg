package com.ctfo.parameter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {

	/** 终端缓存 */
	static Map<String, Integer> mapTerminalCache = new ConcurrentHashMap<String, Integer>();
}
