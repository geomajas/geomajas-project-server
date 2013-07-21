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
package org.geomajas.puregwt.widget.client.map;

import org.geomajas.annotation.Api;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;

/**
 * Client resource bundle interface for pure GWT widgets related to the map.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface WidgetMapResources extends ClientBundle {

	/** Instance for use outside UIBinder. */
	WidgetMapResources INSTANCE = GWT.create(WidgetMapResources.class);

	/**
	 * Get the css resource.
	 * 
	 * @return the css resource
	 */
	@Source("geomajas-widget-map.css")
	WidgetMapCssResource css();
}