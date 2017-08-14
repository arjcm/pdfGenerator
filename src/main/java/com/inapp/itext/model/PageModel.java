/**
 * 
 */
package com.inapp.itext.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ArjunM
 *
 */
public class PageModel {

	/**
	 * 
	 */
	@JsonProperty
	public String Header;
	@JsonProperty
	public String footer;
	@JsonProperty
	public String orientation;
	@JsonProperty
	public String pageContent;
	@JsonProperty
	public String file;
	@JsonProperty
	public String type;
	
	public PageModel() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the header
	 */
	public String getHeader() {
		return Header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(String header) {
		Header = header;
	}

	/**
	 * @return the footer
	 */
	public String getFooter() {
		return footer;
	}

	/**
	 * @param footer the footer to set
	 */
	public void setFooter(String footer) {
		this.footer = footer;
	}

	/**
	 * @return the orientation
	 */
	public String getOrientation() {
		return orientation;
	}

	/**
	 * @param orientation the orientation to set
	 */
	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	/**
	 * @return the pageContent
	 */
	public String getPageContent() {
		return pageContent;
	}

	/**
	 * @param pageContent the pageContent to set
	 */
	public void setPageContent(String pageContent) {
		this.pageContent = pageContent;
	}

	/**
	 * @return the file
	 */
	public String getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(String file) {
		this.file = file;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	
}
