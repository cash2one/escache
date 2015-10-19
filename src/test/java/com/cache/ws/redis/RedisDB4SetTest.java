package com.cache.ws.redis;

import org.junit.Test;

public class RedisDB4SetTest extends RedisDB4Test {
	//
	public void testSet() {
		// set
		jedis.sadd("sname", "wobby");
		jedis.sadd("sname", "kings");
		jedis.sadd("sname", "demon");
		System.out.println(String.format("set num: %d", jedis.scard("sname")));
		System.out.println(String.format("all members: %s",
				jedis.smembers("sname")));
		System.out.println(String.format("is member: %B",
				jedis.sismember("sname", "wobby")));
		System.out.println(String.format("rand member: %s",
				jedis.srandmember("sname")));
		// 删除一个对象
		jedis.srem("sname", "demon");
		System.out.println(String.format("all members: %s",
				jedis.smembers("sname")));
	}
}
