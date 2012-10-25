/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.service.common;

import javax.annotation.PostConstruct;

import org.geomajas.plugin.runtimeconfig.RuntimeConfigException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author Oliver May
 *
 */
@Component
public class PostConstructService {

	@Autowired
	private ApplicationStartupService applicationStartupService;
	
	@Autowired
	private DynamicLayerLoadService layerLoadService;
	
	@PostConstruct
	public void onApplicationStart() {
		try {
			layerLoadService.loadDynamicLayers();
		} catch (RuntimeConfigException e) {
			e.printStackTrace();
		}
		applicationStartupService.onStartup();
	}

}
