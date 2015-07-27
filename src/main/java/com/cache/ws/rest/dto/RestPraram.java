package com.cache.ws.rest.dto;

import java.util.Arrays;

import com.cache.ws.util.FastJsonUtils;

public class RestPraram {

	/** 索引集合 */
	private String[] indexes;
	/** 类型集合 */
	private String[] types;
	/** 开始 */
	private String startTime;
	/** 结束 */
	private String endTime;
	/** 缓存语句 */
	private String dsl;

	public String getDsl() {
		return dsl;
	}

	public void setDsl(String dsl) {
		this.dsl = dsl;
	}

	public String[] getIndexes() {
		return indexes;
	}

	public void setIndexes(String[] indexes) {
		this.indexes = indexes;
	}

	public String[] getTypes() {
		return types;
	}

	public void setTypes(String[] types) {
		this.types = types;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "RestPraram [indexes=" + Arrays.toString(indexes) + ", types="
				+ Arrays.toString(types) + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", dsl=" + dsl + "]";
	}

	public static void main(String[] args) {
		RestPraram rp = new RestPraram();

		rp.setDsl("period_hour.ftl");
		rp.setEndTime("1437235200000");
		rp.setStartTime("1437235200000");
		rp.setTypes(new String[] { "1" });
		rp.setIndexes(new String[] { "access-2015-07-19" });

		String json = FastJsonUtils.obj2json(rp);

		System.out.println(json);

	}

}
