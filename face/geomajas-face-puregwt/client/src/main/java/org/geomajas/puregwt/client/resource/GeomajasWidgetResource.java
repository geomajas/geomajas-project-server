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

import org.geomajas.annotation.Api;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

/**
 * Client resource bundle interface for pure GWT widgets.
 * 
 * @author Dosi Bingov
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface GeomajasWidgetResource extends ClientBundle {

	GeomajasWidgetResource INSTANCE = GWT.create(GeomajasWidgetResource.class);

	@Source("geomajas-widgets.css")
	GeomajasWidgetCssResource css();

	// ------------------------------------------------------------------------
	// ExceptionDialog images:
	// ------------------------------------------------------------------------

	@Source("images/close.png")
	@ImageOptions(repeatStyle = RepeatStyle.None)
	ImageResource exceptionDialogCloseIcon();

	@Source("images/close_hover.png")
	@ImageOptions(repeatStyle = RepeatStyle.None)
	ImageResource exceptionDialogCloseIconHover();
	
	
}