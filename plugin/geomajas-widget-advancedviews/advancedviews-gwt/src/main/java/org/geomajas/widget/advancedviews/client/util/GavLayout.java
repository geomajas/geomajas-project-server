/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.widget.advancedviews.client.util;

/**
 * ...
 *
 * @author Joachim Van der Auwera
 */

import org.geomajas.annotation.Api;

/**
 * Class which helps to provide consistent sizes and names for layout purposes.
 * <p/>
 * Implemented as static class to allow overwriting values at application start, thus allowing skinning.
 * 
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public final class GavLayout {

	// CHECKSTYLE VISIBILITY MODIFIER: OFF

	/** Layer label overlay icon URL. */
	public static String layerLabelOverlayUrl = "[ISOMORPHIC]/geomajas/layerIcons/layer_label_overlay.png";

	/** Layer label opacity underlay icon URL. */
	public static String layerOpacityUnderlayUrl = "[ISOMORPHIC]/geomajas/layerIcons/"
			+ "layer_transparency_underlay.png";

	/**
	 * Layer label opacity underlay icon URL.
	 * 
	 * @deprecated use layerOpacityUnderlayUrl.
	 */
	@Deprecated
	public static String layerTransparencyUnderlayUrl = layerOpacityUnderlayUrl;

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

	private GavLayout() {
		// do not allow instantiation.
	}

}
