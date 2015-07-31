package com.cache.ws.redis;

import redis.clients.jedis.Jedis;

/**
 * 
 * @author hydm
 *
 */
public class RedisDBOperate {
	/**
	 * 通过key获取DSL值
	 * 
	 * @param key
	 * @return
	 */
	public static String loadDsl(String key) {
		Jedis jedis = RedisConfiguration.getInstance().getClient();
		return jedis.get(key);
	}
	
	public static void main(String[] args) {
	
		
		String s = RedisDBOperate.loadDsl("test_cache");
		
		System.out.println(s);
		
	}
}
