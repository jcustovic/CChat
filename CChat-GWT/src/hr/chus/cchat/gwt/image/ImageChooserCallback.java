/*
 * GWT-Ext Widget Library
 * Copyright(c) 2007-2008, GWT-Ext.
 * licensing@gwt-ext.com
 * 
 * http://www.gwt-ext.com/license
 */
package hr.chus.cchat.gwt.image;

/**
 * Interface that provides the callback name so that it can be called once the
 * image is selected.
 * 
 * @author mlim
 */
public interface ImageChooserCallback {
	/**
	 * This method will be called by the dialog upon an image been selected
	 * 
	 * @param data
	 *            is the data information of the selected image
	 */
	public void onImageSelection(ImageData data);
}
