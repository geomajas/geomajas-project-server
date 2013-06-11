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
package org.geomajas.puregwt.client.resource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;

/**
 * Client resource bundle interface for pure GWT widgets.
 * 
 * @author Dosi Bingov
 */
public interface GeomajasWidgetResource extends ClientBundle {

	GeomajasWidgetResource INSTANCE = GWT.create(GeomajasWidgetResource.class);

	@Source("geomajas-widgets.css")
	GeomajasWidgetCssResource css();
}