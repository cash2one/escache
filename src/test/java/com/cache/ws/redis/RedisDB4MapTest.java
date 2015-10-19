package com.cache.ws.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class RedisDB4MapTest extends RedisDB4Test {
	//
	public void testMap() {
		Map<String, String> user = new HashMap<String, String>();
		user.put("name", "cd");
		user.put("password", "123456");
		// map存入redis
		jedis.hmset("user", user);
		// mapkey个数
		System.out.println(String.format("len:%d", jedis.hlen("user")));
		// map中的所有键值
		System.out.println(String.format("keys: %s", jedis.hkeys("user")));
		// map中的所有value
		System.out.println(String.format("values: %s", jedis.hvals("user")));
		// 取出map中的name和password字段值
		List<String> rsmap = jedis.hmget("user", "name", "password");
		System.out.println(rsmap);
		// 删除map中的某一个键值 password
		jedis.hdel("user", "password");
		System.out.println(jedis.hmget("user", "name", "password"));
	}
}
