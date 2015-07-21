package com.cache.ws.rest.dto;

import java.util.Arrays;

public class RestPraram {

	
	/**索引集合*/
	private String[] indexes;
	/**类型集合*/
	private String[] types;
	/**查询指标*/
	private String[] indics;
	/**开始*/
	private Long startTime;
	/**结束*/
	private Long endTime;
	/**格式判断*/
	private Long formartInterval;
	
	@Override
	public String toString() {
		return "RestPraram [indexes=" + Arrays.toString(indexes) + ", types="
				+ Arrays.toString(types) + ", indics="
				+ Arrays.toString(indics) + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", formartInterval="
				+ formartInterval + "]";
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

	public String[] getIndics() {
		return indics;
	}

	public void setIndics(String[] indics) {
		this.indics = indics;
	}

	public Long getFormartInterval() {
		return formartInterval;
	}

	public void setFormartInterval(Long formartInterval) {
		this.formartInterval = formartInterval;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	
	

	

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}




	
}
