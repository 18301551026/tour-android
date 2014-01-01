package cn.itcast.douban.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TourCommon implements Serializable {
	
	private Long id;
	private Integer totalPersonNum;// 接待人次
	private Double totalIncome; // 总收入
	private Date reportDate;// 申报时间
	private Integer status;// 申报状态
	private Integer reportMonth;// 申报月份
	private Integer reportYear; // 申报年份
	private Long time;
	private String desc; // 描述
	private User user;
	private String type;
	private Integer quarter;//季度

	private List<TourDetail> details = new ArrayList<TourDetail>();// 详情

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getTotalPersonNum() {
		return totalPersonNum;
	}

	public void setTotalPersonNum(Integer totalPersonNum) {
		this.totalPersonNum = totalPersonNum;
	}

	public Double getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(Double totalIncome) {
		this.totalIncome = totalIncome;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getReportMonth() {
		return reportMonth;
	}

	public void setReportMonth(Integer reportMonth) {
		this.reportMonth = reportMonth;
	}

	public Integer getReportYear() {
		return reportYear;
	}

	public void setReportYear(Integer reportYear) {
		this.reportYear = reportYear;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getQuarter() {
		return quarter;
	}

	public void setQuarter(Integer quarter) {
		this.quarter = quarter;
	}

	public List<TourDetail> getDetails() {
		return details;
	}

	public void setDetails(List<TourDetail> details) {
		this.details = details;
	}

}
