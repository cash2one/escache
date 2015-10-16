package com.cache.ws.redis;

import org.junit.Test;

public class RedisDB4ListTest extends RedisDB4Test {
	//
	public void test() {
		jedis.del("listDemo");
		jedis.lpush("listDemo", "A");
		jedis.lpush("listDemo", "B");
		jedis.lpush("listDemo", "C");
		jedis.lpush("listDemo", "D");
		System.out.println(jedis.lrange("listDemo", 0, -1));
		System.out.println(jedis.lrange("listDemo", 0, 1));
		System.out.println(jedis.lrange("listDemo", 1, 1));
	}
}
