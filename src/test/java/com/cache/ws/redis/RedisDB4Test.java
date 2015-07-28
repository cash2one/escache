package com.cache.ws.redis;

import org.junit.After;
import org.junit.Before;

import redis.clients.jedis.Jedis;

public class RedisDB4Test {
	protected Jedis jedis = null;

	@Before
	public void init() {
		// 连接redis服务
		jedis = new Jedis("182.92.227.23", 6379);
		// 密码验证，如果你没有设置redis密码可不验证。即可不使用相关命令
		jedis.auth("3edcvfr4");
	}

	@After
	public void destory() {
		if (jedis != null) {
			jedis.close();
		}
		System.gc();
	}

	public void print(Object o) {
		System.out.println(o);
	}

}
