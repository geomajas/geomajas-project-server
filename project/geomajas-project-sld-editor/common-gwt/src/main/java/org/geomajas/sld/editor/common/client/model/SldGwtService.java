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
package org.geomajas.sld.editor.common.client.model;

import java.util.List;

import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geomajas.sld.service.SldException;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * GWT-RPC service for SLD retrieval/persistence.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface SldGwtService extends RemoteService {

	List<String> findAll() throws SldException;

	StyledLayerDescriptorInfo findByName(String name) throws SldException;

	StyledLayerDescriptorInfo saveOrUpdate(StyledLayerDescriptorInfo sld) throws SldException;

	StyledLayerDescriptorInfo create(StyledLayerDescriptorInfo sld) throws SldException;

	boolean remove(String name) throws SldException;

}
