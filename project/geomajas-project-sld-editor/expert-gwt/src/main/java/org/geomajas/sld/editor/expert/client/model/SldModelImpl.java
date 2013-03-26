package org.geomajas.sld.editor.expert.client.model;

import org.geomajas.sld.editor.expert.client.domain.RawSld;

/**
 * Basic implementation of SldModel.
 * 
 * @author Kristof Heirwegh
 */
public class SldModelImpl implements SldModel {

	private boolean dirty;
	private boolean valid;
	private RawSld rawSld;

	public String getName() {
		return rawSld.getName();
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	/**
	 * Valid only applies to raw data, not name or layername.
	 * <p>Only valid if not dirty & valid is set.
	 */
	public boolean isValid() {
		return valid && !dirty;
	}
	
	/**
	 * Setting valid will not setDirty.
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public RawSld getRawSld() {
		return rawSld;
	}

	public void setRawSld(RawSld rawSld) {
		this.rawSld = rawSld;
		this.dirty = true;
	}

	public String getTitle() {
		return rawSld.getTitle();
	}
}
