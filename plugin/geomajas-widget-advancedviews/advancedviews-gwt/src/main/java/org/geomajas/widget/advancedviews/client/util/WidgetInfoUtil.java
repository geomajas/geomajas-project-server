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

import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.MapWidget;

import com.google.gwt.core.client.GWT;

/**
 * Easily get a ClientInfo object (if configured).
 * @todo: replace use by {@link org.geomajas.gwt.client.service.service.ConfigurationService}.
 *
 * @author Kristof Heirwegh
 */
public final class WidgetInfoUtil {

	private WidgetInfoUtil() {
	}

	@SuppressWarnings("unchecked")
	public static <T extends ClientWidgetInfo> T getClientWidgetInfo(String id, Class<T> clazz, Layer<?> layer) {
		if (layer != null) {
			ClientWidgetInfo cli = null;
			if (layer instanceof VectorLayer) {
				cli = ((VectorLayer) layer).getLayerInfo().getWidgetInfo(id);
			} else if (layer instanceof RasterLayer) { // handle unchecked cast below
				cli = ((RasterLayer) layer).getLayerInfo().getWidgetInfo(id);
			}

			if (cli != null) {
				try {
					// cannot check because of erasure
					return (T) cli;
				} catch (Exception e) { // NOSONAR
					GWT.log("ClientWidgetInfo is not of expected type: " + id + " (layer: " + layer.getId());
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T extends ClientWidgetInfo> T getClientWidgetInfo(String id, MapWidget mapWidget) {
		if (mapWidget != null) {
			if (mapWidget.getMapModel().getMapInfo() == null) {
				GWT.log("MapInfo not found, has mapWidget been initialized?");
			}
			ClientWidgetInfo cli = mapWidget.getMapModel().getMapInfo().getWidgetInfo(id);
			if (cli != null) {
				try {
					// cannot check because of type erasure
					return (T) cli;
				} catch (Exception e) { // NOSONAR
					GWT.log("ClientWidgetInfo is not of expected type: " + id + " (map: " + mapWidget.getMapModel().
							getId());
				}
			}
		}
		return null;
	}
}
