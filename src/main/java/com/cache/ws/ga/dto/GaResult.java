package com.cache.ws.ga.dto;

import java.util.List;

import com.cache.ws.constant.GaConstant;

public class GaResult {

	/**区间值*/
	private double intervalValue;

	/** 每行记录 */
	private List<GaResultTrData> gaResultTrData;
	/** 标识 */
	private String code;

	public double getIntervalValue() {
		return intervalValue;
	}

	public void setIntervalValue(double intervalValue) {
		this.intervalValue = intervalValue;
	}

	public GaResult() {
		super();
		this.code = GaConstant.GA_NORMAL_CODE;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<GaResultTrData> getGaResultTrData() {
		return gaResultTrData;
	}

	public void setGaResultTrData(List<GaResultTrData> gaResultTrData) {
		this.gaResultTrData = gaResultTrData;
	}

}
