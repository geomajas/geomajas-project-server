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

package org.geomajas.gwt2.client.widget.control.watermark;

import org.geomajas.annotation.Api;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

/**
 * Client resource bundle for the {@link Watermark} widget.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface WatermarkResource extends ClientBundle {

	/**
	 * Get the default CSS resource.
	 * 
	 * @return The CSS resource.
	 */
	@Source("geomajas-watermark.css")
	WatermarkCssResource css();

	/** Background for the {@link Watermark}. */
	@Source("image/powered_by_geomajas.gif")
	@ImageOptions(repeatStyle = RepeatStyle.None)
	ImageResource poweredByGeomajas();
}