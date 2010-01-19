/*
 * GWT-Ext Widget Library
 * Copyright(c) 2007-2008, GWT-Ext.
 * licensing@gwt-ext.com
 * 
 * http://www.gwt-ext.com/license
 */
package hr.chus.cchat.gwt.image;

public class ImageData {
	
	private String name = null;
	private String url = null;
	private String fileName = null;
	private long size = 0;
	private String searchString = null;
	private String foundLocation = null;
	private String keyword[] = null;
	private String lastModified = null;

	public void clear() {
		name = null;
		url = null;
		fileName = null;
		size = 0;
		searchString = null;
		foundLocation = null;
		keyword = null;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("ImageData[");
		buffer.append("fileName = ").append(fileName);
		buffer.append("; foundLocation = ").append(foundLocation);
		if (keyword == null) {
			buffer.append("; keyword = ").append("null");
		} else {
			for (int i = 0; i < keyword.length; i++) {
				if (i > 0)
					buffer.append(',');
				buffer.append('[');
				buffer.append(i);
				buffer.append("]:");
				buffer.append(keyword[i]);
			}
		}
		buffer.append("; lastModified = ").append(lastModified);
		buffer.append("; name = ").append(name);
		buffer.append("; searchString = ").append(searchString);
		buffer.append("; size = ").append(size);
		buffer.append("; url = ").append(url);
		buffer.append("]");
		return buffer.toString();
	}

	// Getters & setters
	
	public void setName(String name) { this.name = name; }
	public String getName() { return name; }

	public String getUrl() { return url; }
	public void setUrl(String url) { this.url = url; }

	public String getFileName() { return fileName; }
	public void setFileName(String fileName) { this.fileName = fileName; }

	public long getSize() { return size; }
	public void setSize(long size) { this.size = size; }

	public String getSearchString() { return searchString; }
	public void setSearchString(String searchString) { this.searchString = searchString; }

	public String getFoundLocation() { return foundLocation; }
	public void setFoundLocation(String foundLocation) { this.foundLocation = foundLocation; }

	public String[] getKeyword() { return keyword; }
	public void setKeyword(String[] keyword) { this.keyword = keyword; }

	public String getLastModified() { return lastModified; }
	public void setLastModified(String lastModified) { this.lastModified = lastModified; }

}
