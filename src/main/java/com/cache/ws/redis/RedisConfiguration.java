package com.cache.ws.redis;

import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * @author hydm
 *
 */
public class RedisConfiguration {
	private static RedisConfiguration singleton = new RedisConfiguration();
	private static JedisPool jedisPool;
	private Settings settings;

	private RedisConfiguration() {
		settings = ImmutableSettings.settingsBuilder()
				.loadFromClasspath("redis.yml").build();
	}

	public static RedisConfiguration getInstance() {
		return singleton;
	}

	public Jedis getClient() {

		if (jedisPool == null) {
			JedisPoolConfig jedisPoolConfig = initPoolConfig();
			String host = settings.get("IP");
			int port = Integer.valueOf(settings.get("Port"));
			int timeout = Integer.valueOf(settings.get("TimeOut"));
			String password = settings.get("Auth");
			// 构造连接池
			jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout,
					password);
		}

		return jedisPool.getResource();
	}

	private JedisPoolConfig initPoolConfig() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(Integer.valueOf(settings.get("MaxIdle")));
		jedisPoolConfig.setMaxWaitMillis(Integer.valueOf(settings
				.get("MaxWaitMillis")));
		jedisPoolConfig.setTestOnBorrow(true);
		jedisPoolConfig.setTestOnReturn(true);
		return jedisPoolConfig;
	}
}
