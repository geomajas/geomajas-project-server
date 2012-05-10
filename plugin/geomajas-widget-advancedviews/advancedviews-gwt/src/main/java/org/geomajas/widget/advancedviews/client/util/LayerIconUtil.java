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

import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.widget.advancedviews.configuration.client.ExtraLayerInfo;

import com.smartgwt.client.widgets.Img;

/**
 * Helper class for working with layerIcons.
 *
 * @author Kristof Heirwegh
 *
 */
public final class LayerIconUtil {

	private static final String LAYER_LABEL_OVERLAY_URL = "[ISOMORPHIC]/geomajas/layerIcons/layer_label_overlay.png";
	private static final String LAYER_TRANSPARENCY_UNDERLAY_URL = "[ISOMORPHIC]/geomajas/layerIcons/layer_transparenc" +
			"y_underlay.png";
	private static final String LAYER_RASTER_ICON_LARGE_URL = "[ISOMORPHIC]/geomajas/layerIcons/raster_icon_large.png";
	private static final String LAYER_VECTOR_POINT_ICON_LARGE_URL = "[ISOMORPHIC]/geomajas/layerIcons/vector_point_ic" +
			"on_large.png";
	private static final String LAYER_VECTOR_LINE_ICON_LARGE_URL = "[ISOMORPHIC]/geomajas/layerIcons/vector_line_icon" +
			"_large.png";
	private static final String LAYER_VECTOR_POLYGON_ICON_LARGE_URL = "[ISOMORPHIC]/geomajas/layerIcons/vector_polygo" +
			"n_icon_large.png";

	private LayerIconUtil() {
	}

	public static Img getSmallLayerIcon(Layer<?> layer) {
		String url = getSmallLayerIconUrl(layer);
		if (url != null) {
			return new Img(url);
		} else {
			return null;
		}
	}

	public static String getSmallLayerIconUrl(Layer<?> layer) {
		ExtraLayerInfo eli = WidgetInfoUtil.getClientWidgetInfo(ExtraLayerInfo.IDENTIFIER, ExtraLayerInfo.class,
				layer);
		if (eli != null && eli.getSmallLayerIconUrl() != null && !"".equals(eli.getSmallLayerIconUrl())) {
			return eli.getSmallLayerIconUrl();
		} else {
			return null;
		}
	}

	public static Img getLargeLayerIcon(Layer<?> layer) {
		ExtraLayerInfo eli = WidgetInfoUtil.getClientWidgetInfo(ExtraLayerInfo.IDENTIFIER, ExtraLayerInfo.class,
				layer);
		if (eli != null && eli.getLargeLayerIconUrl() != null && !"".equals(eli.getLargeLayerIconUrl())) {
			return new Img(eli.getLargeLayerIconUrl());
		} else {
			if (layer instanceof RasterLayer) {
				return new Img(LAYER_RASTER_ICON_LARGE_URL);
			} else if (layer instanceof VectorLayer) { // handle unchecked cast below NOSONAR
				switch (((VectorLayer) layer).getLayerInfo().getLayerType()) {
				case POINT:
				case MULTIPOINT:
					return new Img(LAYER_VECTOR_POINT_ICON_LARGE_URL);

				case LINESTRING:
				case MULTILINESTRING:
					return new Img(LAYER_VECTOR_LINE_ICON_LARGE_URL);

				case POLYGON:
				case MULTIPOLYGON:
					return new Img(LAYER_VECTOR_POLYGON_ICON_LARGE_URL);

				default:
					return new Img(""); // should not happen
				}
			}
		}
		return new Img(""); // in case eli=null && layer != raster or vector
	}

	public static Img getLegendImage(Layer<?> layer) {
		ExtraLayerInfo eli = WidgetInfoUtil.getClientWidgetInfo(ExtraLayerInfo.IDENTIFIER, ExtraLayerInfo.class,
				layer);
		if (eli != null && eli.getLegendImageUrl() != null && !"".equals(eli.getLegendImageUrl())) {
			return new Img(eli.getLegendImageUrl());
		} else {
			return null;
		}
	}

	public static String getLegendUrl(Layer<?> layer) {
		ExtraLayerInfo eli = WidgetInfoUtil.getClientWidgetInfo(ExtraLayerInfo.IDENTIFIER, ExtraLayerInfo.class,
				layer);
		if (eli != null && eli.getLegendImageUrl() != null && !"".equals(eli.getLegendImageUrl())) {
			return "images/" + eli.getLegendImageUrl();
		} else {
			return null;
		}
	}

	public static Img getLabelOverlayImg() {
		return new Img(LAYER_LABEL_OVERLAY_URL);
	}

	public static Img getTransparencyUnderlayImg() {
		return new Img(LAYER_TRANSPARENCY_UNDERLAY_URL);
	}
}
