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
package org.geomajas.sld.editor.expert.server.service;

import java.util.List;

import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geomajas.sld.editor.expert.client.domain.RawSld;
import org.geomajas.sld.editor.expert.client.domain.SldInfo;
import org.geomajas.sld.service.SldException;

/**
 * Service to lookup and persist SLD (Styled Layer Descriptor) styles. Implementations can be backed by e.g. the file
 * system or a database.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface SldService {

	List<SldInfo> findTemplates() throws SldException;

	RawSld findTemplateByName(String name) throws SldException;

	boolean validate(RawSld sld) throws SldException;

	RawSld toXml(StyledLayerDescriptorInfo sldi) throws SldException;
	
	StyledLayerDescriptorInfo toSldI(RawSld sld) throws SldException;
	
}
