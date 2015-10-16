package com.cache.ws.ga.dto;

import java.util.List;

import com.cache.ws.constant.GaConstant;

public class GaResult {

	/** 最大参数 */
	private double max;

	/** 每行记录 */
	private List<GaResultTrData> gaResultTrData;
	/** 标识 */
	private String code;

	public double getMax() {
		return max;
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

	public void setMax(double max) {
		this.max = max;
	}

	public List<GaResultTrData> getGaResultTrData() {
		return gaResultTrData;
	}

	public void setGaResultTrData(List<GaResultTrData> gaResultTrData) {
		this.gaResultTrData = gaResultTrData;
	}

}
