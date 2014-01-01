package cn.itcast.douban.domain;

import java.io.Serializable;

public class TourDetail implements Serializable {
	
	private Long id;
	private TourCommon common;
	private String name;
	
	private Double money;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TourCommon getCommon() {
		return common;
	}

	public void setCommon(TourCommon common) {
		this.common = common;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}
}
