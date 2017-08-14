package com.inapp.itext.model;

public class TocModel {
	Integer pageNo;
	String  title;
	String parentToc;
	public TocModel() {
		
		// TODO Auto-generated constructor stub
		
	}
	/**
	 * @return the pageNo
	 */
	public Integer getPageNo() {
		return pageNo;
	}
	/**
	 * @param pageNo the pageNo to set
	 */
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the parentToc
	 */
	public String getParentToc() {
		return parentToc;
	}
	/**
	 * @param parentToc the parentToc to set
	 */
	public void setParentToc(String parentToc) {
		this.parentToc = parentToc;
	}

}
