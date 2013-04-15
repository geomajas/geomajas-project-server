/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.editor.expert.client.model;

import java.util.List;

import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geomajas.sld.editor.expert.client.domain.RawSld;
import org.geomajas.sld.editor.expert.client.domain.SldInfo;


/**
 * Basic model for SLD-xml.
 * 
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
	
	/**
	 * Will only be correct/filled in if sld has been validated/saved.
	 */
	StyledLayerDescriptorInfo getSldDescriptor();

	void setSldDescriptor(StyledLayerDescriptorInfo sldDescriptor);

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
