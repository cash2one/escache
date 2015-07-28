package com.cache.ws.redis;

import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;

import redis.clients.jedis.Jedis;

/**
 * 
 * @author hydm
 *
 */
public class RedisConfiguration {
	private static RedisConfiguration singleton = new RedisConfiguration();
	private Settings settings;

	private RedisConfiguration() {
		settings = ImmutableSettings.settingsBuilder()
				.loadFromClasspath("redis.yml").build();
	}

	public static RedisConfiguration getInstance() {
		return singleton;
	}

	public Jedis getClient() {
		Jedis jedis = new Jedis(settings.get("IP"), Integer.parseInt(settings
				.get("PORT")));
		// 密码验证，如果你没有设置redis密码可不验证。即可不使用相关命令
		jedis.auth(settings.get("AUTH"));
		return jedis;
	}

}
