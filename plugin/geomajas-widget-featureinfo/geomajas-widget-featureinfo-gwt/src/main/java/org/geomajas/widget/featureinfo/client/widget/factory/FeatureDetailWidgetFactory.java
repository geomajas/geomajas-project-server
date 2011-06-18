/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.featureinfo.client.widget.factory;

import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.FeatureAttributeWindow;
import org.geomajas.widget.featureinfo.configuration.client.WidgetBuilderInfo;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Window;

/**
 * With FeatureDetailWidgetFactory you can get FeatureDetail widgets in a
 * general manner. The effective type used depends on configuration.
 * 
 * @author Kristof Heirwegh
 * @author Oliver May
 * 
 */
public final class FeatureDetailWidgetFactory {

	public static final int DEFAULT_WINDOW_HEIGHT = 500;
	public static final int DEFAULT_WINDOW_WIDTH = 500;

	private FeatureDetailWidgetFactory() {
		// utility class, hide constructor
	}

	public static Window createFeatureDetailWindow(Feature feature, Layer layer, boolean editingAllowed) {
		FeatureDetailWidgetBuilder customBuilder = getCustomBuilder(layer);
		if (customBuilder != null) {
			return customBuilder.createFeatureDetailWindow(feature, editingAllowed);
		} else if (layer instanceof VectorLayer ) {
			return new FeatureAttributeWindow(feature, editingAllowed);
		} else {
			return new RasterLayerAttributeWindow(feature, layer);
		}
	}
	
	// ----------------------------------------------------------

	private static FeatureDetailWidgetBuilder getCustomBuilder(Layer layer) {
		FeatureDetailWidgetBuilder b = null;
		try {
			if (layer != null) {
				ClientWidgetInfo cwi = layer.getLayerInfo().getWidgetInfo(WidgetBuilderInfo.IDENTIFIER);
				if (cwi != null) {
					if (cwi instanceof WidgetBuilderInfo) {
						WidgetBuilderInfo lcfdii = (WidgetBuilderInfo) cwi;
						WidgetBuilder wb = WidgetFactory.get(lcfdii);
						if (wb instanceof FeatureDetailWidgetBuilder) {
							return (FeatureDetailWidgetBuilder) wb;
						} else {
							GWT.log("Builder is not of type FeatureDetailWidgetBuilder: " + lcfdii.getBuilderName());
						}
					} else {
						GWT.log("ClientWidgetInfo is not of classtype LayerCustomFeatureDetailInfoInfo!! (layertype: "
								+ layer.getServerLayerId() + ")");
					}
				}
			}
		} catch (Exception e) {
			GWT.log("Error getting custom detailwidget: " + e.getMessage());
		}
		return b;
	}
}
