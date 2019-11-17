package com.programModel.entity;

import java.util.List;

/**
 * 节目播放日程
 * @author Administrator
 *
 */
public class ProgarmPalySchedule {

	private Integer id;
	/**
	 * 播放日期(日期或者 周一、周二) 具体日期
	 */
	private String dateStr;
	/**
	 * 自定义的开始时间
	 */
	private String startDate;
	/**
	 * 自定义的结束时间
	 */
	private String endDate;
	
	/**
	 *  时间段   12:30-13:20  
	 */
	private String times;

	
	public ProgarmPalySchedule() {
		super();
	}
	
	public ProgarmPalySchedule(Integer id,String dateStr) {
		super();
		this.id = id;
		this.dateStr = dateStr;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public String getTimes() {
		return times;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
