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
package org.geomajas.puregwt.client.widget.control.zoomtorect;

import org.geomajas.annotation.Api;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

/**
 * Client resource bundle interface for pure GWT widgets.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface ZoomToRectangleControlResource extends ClientBundle {

	/**
	 * Get the default CSS resource.
	 * 
	 * @return The CSS resource.
	 */
	@Source("geomajas-zoomtorect-control.css")
	ZoomToRectangleCssResource css();

	// ------------------------------------------------------------------------
	// ZoomToRectangleWidget images:
	// ------------------------------------------------------------------------

	/** Icon for the {@link org.geomajas.puregwt.client.widget.control.ZoomToRectangleWidget}. */
	@Source("image/map-zoom-to-rect.png")
	@ImageOptions(repeatStyle = RepeatStyle.None)
	ImageResource zoomToRectangle();

	/** Icon for the {@link org.geomajas.puregwt.client.widget.control.ZoomToRectangleWidget} when highlighted. */
	@Source("image/map-zoom-to-rect-hover.png")
	@ImageOptions(repeatStyle = RepeatStyle.None)
	ImageResource zoomToRectangleHover();
}