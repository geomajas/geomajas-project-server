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
package org.geomajas.plugin.deskmanager.client.gwt.common;

import java.util.List;

import org.geomajas.annotation.Api;


/**
 * Configuration of a UserApplication. Values returned by this interface should be the same for all instances of one
 * user application class.
 * 
 * @author Oliver May
 * @since 1.0.0
 */
@Api
public interface UserApplicationConfiguration {

	/**
	 * Get the key by wich this user application type is referred to.
	 * 
	 * @return the key
	 */
	String getClientApplicationKey();
	
	/**
	 * Get the name of this user application type.
	 * @return
	 */
	String getClientApplicationName();
	
	/**
	 * Get a list of widgetinfo's that this user application supports.
	 * 
	 * @return the list.
	 */
	List<String> getSupportedApplicationWidgetKeys();
	
	/**
	 * Get a list of widgetinfo's that the main map of this user application supports.
	 * 
	 * @return the list.
	 */
	List<String> getSupportedMainMapWidgetKeys();
	
	/**
	 * Get a list of widgetinfo's that the overview map of this user application supports.
	 * 
	 * @return the list.
	 */
	List<String> getSupportedOverviewMapWidgetKeys();
	
	/**
	 * Get a list of widgetinfo's that a layer of this user application supports.
	 * 
	 * @return the list.
	 */
	List<String> getSupportedLayerWidgetKeys();

}
