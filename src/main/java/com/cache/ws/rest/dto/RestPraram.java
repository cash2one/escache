package com.cache.ws.rest.dto;


import java.util.Arrays;

import com.cache.ws.util.FastJsonUtils;

public class RestPraram {

	/** 索引集合 */
	private String[] indexes;
	/** 类型集合 */
	private String[] types;
	
	
	/**key*/
	private String redisKey;

	public String getRedisKey() {
		return redisKey;
	}

	public void setRedisKey(String redisKey) {
		this.redisKey = redisKey;
	}

	public String[] getIndexes() {
		return indexes;
	}

	public void setIndexes(String[] indexes) {
		this.indexes = indexes;
	}
	
	public void setIndexes(String indexes) {
		this.indexes = indexes.split(",");
	}

	public String[] getTypes() {
		return types;
	}

	public void setTypes(String[] types) {
		this.types = types;
	}


	public void setTypes(String types) {
		
		this.types = types.split(",");
	}
	

	@Override
	public String toString() {
		return "RestPraram [indexes=" + Arrays.toString(indexes) + ", types="
				+ Arrays.toString(types) + ", redisKey=" + redisKey + "]";
	}

	public static void main(String[] args) {
		RestPraram rp = new RestPraram();

		rp.setTypes(new String[] {"1"});
		rp.setIndexes(new String[] { "access-2015-07-19" });
		rp.setRedisKey("test_cache");
		
		String json = FastJsonUtils.obj2json(rp);

		System.out.println(json);

	}

}
