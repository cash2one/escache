package com.cache.ws.redis;

import org.junit.Test;

public class RedisDB4CRUDTest extends RedisDB4Test {
	// 
	public void testGet() {
		print(jedis.get("name"));
	}

	// 
	public void testSet() {
		jedis.set("name", "小猪");
		print(jedis.get("name"));
	}

	// 
	public void testAppend() {
		// 在原有值得基础上添加,如若之前没有该key,则导入该key
		// 之前已经设定了name对应"小猪",此句执行便会使name对应"小猪快跑"
		jedis.append("name", "快跑");
		jedis.append("content", "rabbit");

		print(jedis.get("name"));
		print(jedis.get("content"));
	}

	// 
	public void testMSet() {
		// mset 是设置多个key-value值 参数（key1,value1,key2,value2,...,keyn,valuen）
		// mget 是获取多个key所对应的value值 参数（key1,key2,key3,...,keyn） 返回的是个list
		jedis.mset("name1", "yangw", "name2", "demon", "name3", "elena");
		System.out.println(jedis.mget("name1", "name2", "name3"));
	}

	//
	public void testDelete() {
		jedis.del("name");
		print(jedis.get("name"));
	}
}
