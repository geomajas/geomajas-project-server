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

package org.geomajas.gwt.client.widget.control.pan;

import org.geomajas.annotation.Api;

import com.google.gwt.resources.client.CssResource;

/**
 * CSS resource bundle for the {@link PanControl} widget.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface PanControlCssResource extends CssResource {

	/**
	 * The main style of the {@link org.geomajas.gwt.client.widget.control.pan.PanControl}. Provides the background
	 * style.
	 */
	@ClassName("gm-PanControl")
	String panControl();

	/** Top panning button in the {@link org.geomajas.gwt.client.widget.control.pan.PanControl}. */
	@ClassName("gm-PanControl-top")
	String panControlTop();

	/** Right panning button in the {@link org.geomajas.gwt.client.widget.control.pan.PanControl}. */
	@ClassName("gm-PanControl-right")
	String panControlRight();

	/** Bottom panning button in the {@link org.geomajas.gwt.client.widget.control.pan.PanControl}. */
	@ClassName("gm-PanControl-bottom")
	String panControlBottom();

	/** Left panning button in the {@link org.geomajas.gwt.client.widget.control.pan.PanControl}. */
	@ClassName("gm-PanControl-left")
	String panControlLeft();
}