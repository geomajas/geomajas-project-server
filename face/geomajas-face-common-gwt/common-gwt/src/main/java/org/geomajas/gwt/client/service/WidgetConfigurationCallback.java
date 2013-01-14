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

package org.geomajas.gwt.client.service;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;
import org.geomajas.configuration.client.ClientWidgetInfo;

/**
 * Call-back interface for widget configuration retrieval.
 * 
 * @param <WIDGET_INFO> Type of widget configuration object.
 *
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api
@UserImplemented
public interface WidgetConfigurationCallback<WIDGET_INFO extends ClientWidgetInfo> {

	/**
	 * This method is executed upon retrieval of the application configuration.
	 * 
	 * @param widgetInfo
	 *            The widget configuration object that was requested.
	 */
	void execute(WIDGET_INFO widgetInfo);

}