/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.service.common;

import org.geomajas.plugin.runtimeconfig.RuntimeConfigException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


/**
 * @author Oliver May
 *
 */
@Component
public class PostConstructService implements ApplicationListener<ContextRefreshedEvent>{

	@Autowired
	private ApplicationStartupService applicationStartupService;
	
	@Autowired
	private DynamicLayerLoadService layerLoadService;
	
	private boolean loaded = false;
	
	public void onApplicationStart() {
		try {
			layerLoadService.loadDynamicLayers();
		} catch (RuntimeConfigException e) {
			e.printStackTrace();
		}
		applicationStartupService.onStartup();
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (!loaded) {
			loaded = true;
			onApplicationStart();
		}
	}
	
}
