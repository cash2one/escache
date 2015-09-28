package com.cache.ws.redis;

import java.util.ArrayList;
import java.util.List;

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
	
		Jedis jedis = RedisConfiguration.getInstance().getClient();
		   System.out.println("清空库中所有数据："+jedis.flushDB());
		
		   
		   String[] test = new String[]{"123"};
		  
		   
	        System.out.println("sets1中添加元素element001："+jedis.sadd("sets1",test)); 
	        System.out.println("sets1中添加元素element002："+jedis.sadd("sets1", "element002")); 
	        System.out.println("sets1中添加元素element003："+jedis.sadd("sets1", "element003")); 
	        System.out.println("sets2中添加元素element002："+jedis.sadd("sets2", "element002")); 
	        System.out.println("sets2中添加元素element003："+jedis.sadd("sets2", "element003")); 
	        System.out.println("sets2中添加元素element004："+jedis.sadd("sets2", "element004"));
	        
	        
	        System.out.println("查看sets1集合中的所有元素:"+jedis.smembers("sets1"));
	        System.out.println("查看sets2集合中的所有元素:"+jedis.smembers("sets2"));
	        System.out.println("sets1和sets2交集："+jedis.sinter("sets1", "sets2"));
	        System.out.println("sets1和sets2并集："+jedis.sunion("sets1", "sets2"));
	        System.out.println("sets1和sets2差集："+jedis.sdiff("sets1", "sets2"));//差集：set1中有，set2中没有的元素
		
	       
		
	}
}
