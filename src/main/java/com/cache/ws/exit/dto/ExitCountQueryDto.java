package com.cache.ws.exit.dto;

import java.util.List;

public class ExitCountQueryDto {

	private List<String> tables;

	private String isNew;

	private String se;

	private String rf_type;

	private String type;

	public List<String> getTables() {
		return tables;
	}

	public void setTables(List<String> tables) {
		this.tables = tables;
	}

	public String getIsNew() {
		return isNew;
	}

	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}

	public String getSe() {
		return se;
	}

	public void setSe(String se) {
		this.se = se;
	}

	public String getRf_type() {
		return rf_type;
	}

	public void setRf_type(String rf_type) {
		this.rf_type = rf_type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
