/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.client;

import java.util.List;

import org.geomajas.sld.StyledLayerDescriptorInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Async service.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface SldGwtServiceAsync {

	void findAll(AsyncCallback<List<String>> callback);

	void findByName(String name, AsyncCallback<StyledLayerDescriptorInfo> callback);

	void remove(String name, AsyncCallback<Boolean> callback);

	void saveOrUpdate(StyledLayerDescriptorInfo sld, AsyncCallback<StyledLayerDescriptorInfo> callback);

	void create(StyledLayerDescriptorInfo sld, AsyncCallback<StyledLayerDescriptorInfo> callback);
}
