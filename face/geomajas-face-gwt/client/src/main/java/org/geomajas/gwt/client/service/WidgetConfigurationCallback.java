/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt.client.service;

import org.geomajas.annotation.FutureApi;
import org.geomajas.annotation.UserImplemented;
import org.geomajas.configuration.client.ClientWidgetInfo;

/**
 * Call-back interface for widget configuration retrieval.
 * 
 * @param <WIDGETINFO> Type of widget configuration object.
 * @author Pieter De Graef
 * @since 1.10.0
 */
@FutureApi
@UserImplemented
public interface WidgetConfigurationCallback<WIDGETINFO extends ClientWidgetInfo> {

	/**
	 * This method is executed upon retrieval of the application configuration.
	 * 
	 * @param widgetInfo
	 *            The widget configuration object that was requested.
	 */
	void execute(WIDGETINFO widgetInfo);
};