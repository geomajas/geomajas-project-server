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
package org.geomajas.sld.server;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geomajas.sld.client.model.SldGwtService;
import org.geomajas.sld.service.SldException;
import org.geomajas.sld.service.SldService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation of {@link SldGwtService} that exposes some methods of the {@link SldService}.
 * 
 * @author Jan De Moerloose
 * @author An Buyle
 * 
 */
public class SldGwtServiceImpl implements SldGwtService {

	@Autowired
	private SldService sldService;

	public List<String> findAll() throws SldException {
		List<StyledLayerDescriptorInfo> slds = sldService.findAll();
		List<String> result = new ArrayList<String>();
		for (StyledLayerDescriptorInfo sld : slds) {
			result.add(sld.getName());
		}
		return result;
	}

	public StyledLayerDescriptorInfo findByName(String name) throws SldException {
		return sldService.findByName(name);
	}

	public StyledLayerDescriptorInfo saveOrUpdate(StyledLayerDescriptorInfo sld) throws SldException {
		return sldService.saveOrUpdate(sld);
	}

	public StyledLayerDescriptorInfo create(StyledLayerDescriptorInfo sld) throws SldException {
		return sldService.create(sld);
	}

	public boolean remove(String name) throws SldException {
		return sldService.remove(name);
	}

}
