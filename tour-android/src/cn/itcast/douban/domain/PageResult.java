package cn.itcast.douban.domain;

import java.io.Serializable;
import java.util.List;

public class PageResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<TourCommon> result;
	
	private Integer rowCount;

	public List<TourCommon> getResult() {
		return result;
	}

	public void setResult(List<TourCommon> result) {
		this.result = result;
	}

	public Integer getRowCount() {
		return rowCount;
	}

	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}

}
