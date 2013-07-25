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

package org.geomajas.puregwt.client.widget.control.zoom;

import org.geomajas.annotation.Api;

import com.google.gwt.resources.client.CssResource;

/**
 * CSS resource bundle for the {@link ZoomControl} widget.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface ZoomControlCssResource extends CssResource {

	/** The top style of the {@link org.geomajas.puregwt.client.widget.control.zoom.ZoomControl}. */
	@ClassName("gm-ZoomControl")
	String zoomControl();

	/** Zoom in button in the {@link org.geomajas.puregwt.client.widget.control.zoom.ZoomControl}. */
	@ClassName("gm-ZoomControl-zoomIn")
	String zoomControlZoomIn();

	/** Zoom in button in the {@link org.geomajas.puregwt.client.widget.control.zoom.ZoomControl} when being touched. */
	@ClassName("gm-ZoomControl-zoomIn-touch")
	String zoomControlZoomInTouch();

	/** Zoom out button in the {@link org.geomajas.puregwt.client.widget.control.zoom.ZoomControl}. */
	@ClassName("gm-ZoomControl-zoomOut")
	String zoomControlZoomOut();

	/**
	 * Zoom out button in the {@link org.geomajas.puregwt.client.widget.control.zoom.ZoomControl} when being touched.
	 */
	@ClassName("gm-ZoomControl-zoomOut-touch")
	String zoomControlZoomOutTouch();
}