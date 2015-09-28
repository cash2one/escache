package com.cache.ws.ga.dto;

import java.util.List;

public class GaResultTrData {

	private String code;

	private String userNumber;

	private String title;

	private String data;

	private List<GaResultTdData> GaResultTdDatas;



	public List<GaResultTdData> getGaResultTdDatas() {
		return GaResultTdDatas;
	}

	public void setGaResultTdDatas(List<GaResultTdData> gaResultTdDatas) {
		GaResultTdDatas = gaResultTdDatas;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}

}
