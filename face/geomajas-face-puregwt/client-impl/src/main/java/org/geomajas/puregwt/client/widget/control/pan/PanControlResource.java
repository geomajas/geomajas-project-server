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
package org.geomajas.puregwt.client.widget.control.pan;

import org.geomajas.annotation.Api;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

/**
 * Client resource bundle for the {@link PanControl} widget.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface PanControlResource extends ClientBundle {

	/**
	 * Get the default CSS resource.
	 * 
	 * @return The CSS resource.
	 */
	@Source("geomajas-pan-control.css")
	PanControlCssResource css();

	// ------------------------------------------------------------------------
	// Images:
	// ------------------------------------------------------------------------

	/** Background for the {@link org.geomajas.puregwt.client.widget.control.pan.PanControl}. */
	@Source("image/map-pan-background.png")
	@ImageOptions(repeatStyle = RepeatStyle.None)
	ImageResource panControlBackground();

	/** Top pan button for the {@link org.geomajas.puregwt.client.widget.control.pan.PanControl}. */
	@Source("image/map-pan-top.png")
	@ImageOptions(repeatStyle = RepeatStyle.None)
	ImageResource panControlTop();

	/** Top pan button for the {@link org.geomajas.puregwt.client.widget.control.pan.PanControl} when highlighted. */
	@Source("image/map-pan-top-hover.png")
	@ImageOptions(repeatStyle = RepeatStyle.None)
	ImageResource panControlTopHover();

	/** Right pan button for the {@link org.geomajas.puregwt.client.widget.control.pan.PanControl}. */
	@Source("image/map-pan-right.png")
	@ImageOptions(repeatStyle = RepeatStyle.None)
	ImageResource panControlRight();

	/** Right pan button for the {@link org.geomajas.puregwt.client.widget.control.pan.PanControl} when highlighted. */
	@Source("image/map-pan-right-hover.png")
	@ImageOptions(repeatStyle = RepeatStyle.None)
	ImageResource panControlRightHover();

	/** Bottom pan button for the {@link org.geomajas.puregwt.client.widget.control.pan.PanControl}. */
	@Source("image/map-pan-bottom.png")
	@ImageOptions(repeatStyle = RepeatStyle.None)
	ImageResource panControlBottom();

	/** Bottom pan button for the {@link org.geomajas.puregwt.client.widget.control.pan.PanControl} when highlighted. */
	@Source("image/map-pan-bottom-hover.png")
	@ImageOptions(repeatStyle = RepeatStyle.None)
	ImageResource panningBottomHover();

	/** Left pan button for the {@link org.geomajas.puregwt.client.widget.control.pan.PanControl}. */
	@Source("image/map-pan-left.png")
	@ImageOptions(repeatStyle = RepeatStyle.None)
	ImageResource panControlLeft();

	/** Left pan button for the {@link org.geomajas.puregwt.client.widget.control.pan.PanControl} when highlighted. */
	@Source("image/map-pan-left-hover.png")
	@ImageOptions(repeatStyle = RepeatStyle.None)
	ImageResource panControlLeftHover();
}