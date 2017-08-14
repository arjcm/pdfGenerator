package com.inapp.itext.model;

import java.util.List;

public class PdfFileDetails {
	String path ="";
    List<String> toc;
    int fileIndexNo;
	public PdfFileDetails() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * @return the toc
	 */
	public List<String> getFileToc() {
		return toc;
	}
	/**
	 * @param tocTitle the toc to set
	 */
	public void setFileToc(List<String> tocTitle) {
		this.toc = tocTitle;
	}
	/**
	 * @return the pageNo
	 */
	public int getFileIndexNo() {
		return fileIndexNo;
	}
	/**
	 * @param pageNo the pageNo to set
	 */
	public void setFileIndexNo(int pageNo) {
		this.fileIndexNo = pageNo;
	}
	
	

}
