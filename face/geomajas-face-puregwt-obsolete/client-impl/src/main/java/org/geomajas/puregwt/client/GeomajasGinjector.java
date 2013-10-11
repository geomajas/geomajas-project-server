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

package org.geomajas.puregwt.client;

import org.geomajas.annotation.Api;
import org.geomajas.puregwt.client.gfx.GfxUtil;
import org.geomajas.puregwt.client.gfx.HtmlImageFactory;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.service.CommandService;
import org.geomajas.puregwt.client.service.EndPointService;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Ginjector specific for the Geomajas PureGWT client.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@GinModules(GeomajasGinModule.class)
public interface GeomajasGinjector extends Ginjector {

	/**
	 * Create a new empty map. This map still needs to be initialized (it needs to fetch a configuration from the
	 * server, only then is it initialized, and are any layers available).
	 * 
	 * @return An empty map.
	 */
	MapPresenter getMapPresenter();

	/**
	 * Get the {@link GfxUtil} singleton. Utility service that helps out when rendering custom shapes on the map.
	 * 
	 * @return The {@link GfxUtil} singleton.
	 */
	GfxUtil getGfxUtil();

	/**
	 * Get the {@link EndPointService} singleton. Has pointers to the Geomajas services on the back-end, and allows
	 * those end-points to be altered in case your server is somewhere else (for example behind a proxy).
	 * 
	 * @return The {@link EndPointService} singleton.
	 */
	EndPointService getEndPointService();

	/**
	 * Get the {@link CommandService} singleton. This service allows for executing commands on the back-end. It is the
	 * base for all Geomajas client-server communication.
	 * 
	 * @return The {@link EndPointService} singleton.
	 */
	CommandService getCommandService();

	/**
	 * Get the image factory singleton.
	 * 
	 * @return The image factory singleton.
	 */
	HtmlImageFactory getImageFactory();

	/**
	 * Get a general EventBus singleton. This EventBus should should be used outside of the map, to catch application
	 * specific events.
	 * 
	 * @return A general EventBus.
	 */
	EventBus getEventBus();
}