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
package org.geomajas.widget.layer.client.util;

import org.geomajas.annotation.Api;


/**
 * @author Oliver May
 *
 * @since 1.0.0
 */
@Api
public final class GltLayout {

	private GltLayout() {  }
	
	// CHECKSTYLE VISIBILITY MODIFIER: OFF

	/** Layer label overlay icon URL. */
	public static String layerLabelOverlayUrl = "[ISOMORPHIC]/geomajas/layerIcons/layer_label_overlay.png";

	/** Layer label opacity underlay icon URL. */
	public static String layerOpacityUnderlayUrl = "[ISOMORPHIC]/geomajas/layerIcons/"
			+ "layer_transparency_underlay.png";

	/** Raster layer large icon URL. */
	public static String layerRasterIconLargeUrl = "[ISOMORPHIC]/geomajas/layerIcons/raster_icon_large.png";

	/** Point layer large icon- URL. */
	public static String layerVectorPointIconLargeUrl = "[ISOMORPHIC]/geomajas/layerIcons/"
			+ "vector_point_icon_large.png";

	/** Line layer large icon- URL. */
	public static String layerVectorLineIconLargeUrl = "[ISOMORPHIC]/geomajas/layerIcons/"
			+ "vector_line_icon_large.png";

	/** Polygon layer large icon- URL. */
	public static String layerVectorPolygonIconLargeUrl = "[ISOMORPHIC]/geomajas/layerIcons/"
			+ "vector_polygon_icon_large.png";

	// CHECKSTYLE VISIBILITY MODIFIER: ON
	
}
