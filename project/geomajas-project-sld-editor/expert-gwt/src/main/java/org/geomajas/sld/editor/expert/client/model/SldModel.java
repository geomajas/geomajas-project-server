package org.geomajas.sld.editor.expert.client.model;

import org.geomajas.sld.editor.expert.client.domain.RawSld;
import org.geomajas.sld.editor.expert.client.domain.SldInfo;


/**
 * Basic model for SLD-xml.
 * @author Kristof Heirwegh
 */
public interface SldModel extends SldInfo {

	boolean isDirty();

	void setDirty(boolean dirty);

	boolean isValid();

	void setValid(boolean valid);
	
	RawSld getRawSld();
	
	void setRawSld(RawSld rawSld);

}
