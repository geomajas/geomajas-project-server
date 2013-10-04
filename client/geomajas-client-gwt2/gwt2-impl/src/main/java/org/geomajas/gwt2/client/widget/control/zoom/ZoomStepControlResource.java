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
package org.geomajas.gwt2.client.widget.control.zoom;

import org.geomajas.annotation.Api;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

/**
 * Client resource bundle for the {@link ZoomStepControl} widget.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface ZoomStepControlResource extends ClientBundle {

	/**
	 * Get the default CSS resource.
	 * 
	 * @return The CSS resource.
	 */
	@Source("geomajas-zoomstep-control.css")
	ZoomStepControlCssResource css();

	// ------------------------------------------------------------------------
	// Images:
	// ------------------------------------------------------------------------

	/** Zoom step background for the {@link org.geomajas.gwt.client.widget.control.ZoomStepWidget}. */
	@Source("image/map-zoom-step.png")
	@ImageOptions(repeatStyle = RepeatStyle.None)
	ImageResource zoomStepControlStep();

	/** Zoom step handle for the {@link org.geomajas.gwt.client.widget.control.ZoomStepWidget}. */
	@Source("image/map-zoom-step-handle.png")
	@ImageOptions(repeatStyle = RepeatStyle.None)
	ImageResource zoomStepControlHandle();

	/** Zoom step handle for the {@link org.geomajas.gwt.client.widget.control.ZoomStepWidget} when highlighted. */
	@Source("image/map-zoom-step-handle-hover.png")
	@ImageOptions(repeatStyle = RepeatStyle.None)
	ImageResource zoomStepControlHandleHover();
}