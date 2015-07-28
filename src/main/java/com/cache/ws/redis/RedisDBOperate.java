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
}
