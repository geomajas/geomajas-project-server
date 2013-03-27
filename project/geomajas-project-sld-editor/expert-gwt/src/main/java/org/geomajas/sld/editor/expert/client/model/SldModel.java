package org.geomajas.sld.editor.expert.client.model;

import java.util.List;

import org.geomajas.sld.editor.expert.client.domain.RawSld;
import org.geomajas.sld.editor.expert.client.domain.SldInfo;


/**
 * Basic model for SLD-xml.
 * @author Kristof Heirwegh
 */
public interface SldModel extends SldInfo {

	void clear();
	
	boolean isDirty();

	void setDirty(boolean dirty);

	boolean isValid();

	void setValid(boolean valid);
	
	RawSld getRawSld();
	
	void setRawSld(RawSld rawSld);
	
	void setTemplate(RawSld rawSld);

	/**
	 * Get the currently selected Template.
	 * 
	 * @return the SLD template
	 */
	RawSld getTemplate();
	
	/**
	 * Returns the fetched list of names of all Templates.
	 * 
	 * @return the list of names
	 */
	List<SldInfo> getTemplateNames();

}
