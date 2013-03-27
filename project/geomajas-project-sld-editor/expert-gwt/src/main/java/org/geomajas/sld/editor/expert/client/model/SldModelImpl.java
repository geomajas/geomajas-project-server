package org.geomajas.sld.editor.expert.client.model;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.sld.editor.expert.client.domain.RawSld;
import org.geomajas.sld.editor.expert.client.domain.SldInfo;

/**
 * Basic implementation of SldModel.
 * 
 * @author Kristof Heirwegh
 */
public class SldModelImpl implements SldModel {

	private boolean dirty;
	private boolean valid;
	private RawSld rawSld = new RawSld();
	private RawSld template;
	private List<SldInfo> templateNames = new ArrayList<SldInfo>();

	public String getName() {
		return rawSld.getName();
	}

	/**
	 * @return true if there is a rawSld and dirty bit is set.
	 */
	public boolean isDirty() {
		return rawSld != null && dirty;
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
	}

	public String getTitle() {
		return rawSld.getTitle();
	}
	
	public RawSld getTemplate() {
		return template;
	}

	public void setTemplate(RawSld template) {
		this.template = template;
	}

	public List<SldInfo> getTemplateNames() {
		return templateNames;
	}
	
	/**
	 * Does not clear templateNames.
	 */
	public void clear() {
		rawSld = new RawSld();
		dirty = false;
		valid = false;
		template = null;
	}
}
