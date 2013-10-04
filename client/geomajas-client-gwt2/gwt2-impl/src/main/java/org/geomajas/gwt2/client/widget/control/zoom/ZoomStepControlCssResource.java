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

import com.google.gwt.resources.client.CssResource;

/**
 * CSS resource bundle for the {@link ZoomStepControl} widget.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface ZoomStepControlCssResource extends CssResource {

	/** The top style of the {@link org.geomajas.gwt2.client.widget.control.zoom.ZoomStepControl}. */
	@ClassName("gm-ZoomStepControl")
	String zoomStepControl();

	/** Zoom in button in the {@link org.geomajas.gwt2.client.widget.control.zoom.ZoomStepControl}. */
	@ClassName("gm-ZoomStepControl-zoomIn")
	String zoomStepControlZoomIn();

	/** Zoom out button in the {@link org.geomajas.gwt2.client.widget.control.zoom.ZoomStepControl}. */
	@ClassName("gm-ZoomStepControl-zoomOut")
	String zoomStepControlZoomOut();

	/** Zoom steps parent DIV in the {@link org.geomajas.gwt2.client.widget.control.zoom.ZoomStepControl}. */
	@ClassName("gm-ZoomStepControl-steps")
	String zoomStepControlSteps();

	/** Zoom step style in the {@link org.geomajas.gwt2.client.widget.control.zoom.ZoomStepControl}. */
	@ClassName("gm-ZoomStepControl-step")
	String zoomStepControlStep();

	/** Zoom step handle style in the {@link org.geomajas.gwt2.client.widget.control.zoom.ZoomStepControl}. */
	@ClassName("gm-ZoomStepControl-handle")
	String zoomStepControlStepHandle();
}