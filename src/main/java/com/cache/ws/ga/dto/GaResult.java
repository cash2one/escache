package com.cache.ws.ga.dto;

import java.util.List;

public class GaResult {

	/**最大参数*/
	private double max;

	/**每行记录*/
	private List<GaResultTrData> gaResultTrData;

	public double getMax() {
		return max;
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
