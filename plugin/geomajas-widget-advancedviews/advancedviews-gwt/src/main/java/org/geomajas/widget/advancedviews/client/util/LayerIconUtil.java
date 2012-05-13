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
import org.geomajas.layer.LayerType;
import org.geomajas.widget.advancedviews.configuration.client.ExtraLayerInfo;

import com.smartgwt.client.widgets.Img;

/**
 * Helper class for working with layerIcons.
 *
 * @author Kristof Heirwegh
 */
public final class LayerIconUtil {

	private LayerIconUtil() {
		// utility class, hide constructor
	}

	/**
	 * Get small layer icon as image.
	 *
	 * @param layer layer
	 * @return image
	 */
	public static Img getSmallLayerIcon(Layer<?> layer) {
		String url = getSmallLayerIconUrl(layer);
		if (url != null) {
			return new Img(url);
		} else {
			return null;
		}
	}

	/**
	 * Get small layer image URL.
	 *
	 * @param layer layer
	 * @return image URL
	 */
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
				return new Img(GavLayout.layerRasterIconLargeUrl);
			} else if (layer instanceof VectorLayer) {
				LayerType layerType = ((VectorLayer) layer).getLayerInfo().getLayerType();
				switch (layerType) {
					case POINT:
					case MULTIPOINT:
						return new Img(GavLayout.layerVectorPointIconLargeUrl);

					case LINESTRING:
					case MULTILINESTRING:
						return new Img(GavLayout.layerVectorLineIconLargeUrl);

					case POLYGON:
					case MULTIPOLYGON:
					case GEOMETRY:
						return new Img(GavLayout.layerVectorPolygonIconLargeUrl);

					default:
						throw new IllegalStateException("Unknown vector layer type " + layerType);
				}
			} else {
				throw new IllegalStateException("Unknown layer type");
			}
		}
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
		return new Img(GavLayout.layerLabelOverlayUrl);
	}

	public static Img getTransparencyUnderlayImg() {
		return new Img(GavLayout.layerTransparencyUnderlayUrl);
	}
}
