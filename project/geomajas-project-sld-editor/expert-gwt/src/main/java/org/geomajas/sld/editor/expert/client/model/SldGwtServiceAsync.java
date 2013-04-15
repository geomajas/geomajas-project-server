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

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Async service.
 * 
 * @author Jan De Moerloose
 */
public interface SldGwtServiceAsync {

	void findTemplates(AsyncCallback<List<SldInfo>> callback);

	void findTemplateByName(String name, AsyncCallback<RawSld> callback);

	void validate(RawSld raw, AsyncCallback<Boolean> callback);

	void convertRawToDescriptor(RawSld raw, AsyncCallback<StyledLayerDescriptorInfo> callback);
	
	void convertDescriptorToRaw(StyledLayerDescriptorInfo sldi, AsyncCallback<RawSld> callback);
	
	// void saveOrUpdate(RawSld sld, AsyncCallback<RawSld> callback);

	//	void remove(String name, AsyncCallback<Boolean> callback);

	//	void create(RawSld sld, AsyncCallback<RawSld> callback);
}
