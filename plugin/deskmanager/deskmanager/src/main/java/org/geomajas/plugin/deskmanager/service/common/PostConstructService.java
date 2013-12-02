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
 * Service that listens for ContextRefreshedEvent so that initialization of the spring context can be done when
 * the application is initialized. This can't be done trough @PostConstruct because it is executed to early in the
 * spring context life cycle.
 * 
 * @author Oliver May
 */
@Component
public class PostConstructService implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private ApplicationStartupService applicationStartupService;
	
	@Autowired
	private DynamicLayerLoadService layerLoadService;
	
	private boolean initialized;
	
	public void onApplicationStart() {
		try {
			layerLoadService.loadDynamicLayers();
		} catch (RuntimeConfigException e) {
			e.printStackTrace();
		}
		applicationStartupService.onStartup();
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (!initialized) {
			initialized = true;
			onApplicationStart();
		}
	}
}
