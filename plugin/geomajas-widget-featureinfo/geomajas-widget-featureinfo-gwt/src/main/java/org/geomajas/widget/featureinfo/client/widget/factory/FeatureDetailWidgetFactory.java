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
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.FeatureAttributeWindow;
import org.geomajas.widget.featureinfo.configuration.client.WidgetBuilderInfo;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;

/**
 * With FeatureDetailWidgetFactory you can get FeatureDetail widgets in a
 * general manner. The effective type used depends on configuration.
 * 
 * @author Kristof Heirwegh
 * 
 */
public final class FeatureDetailWidgetFactory {

	public static final String CLIENTWIDGETINFO_KEY = "widgetBuilderName";
	public static final int DEFAULT_WINDOW_HEIGHT = 500;

	private FeatureDetailWidgetFactory() {
		// utility class, hide constructor
	}

	public static FeatureDetailWidget createFeatureDetailCanvas(VectorLayer layer, boolean editingAllowed, 
			int maxHeight) {
		if (layer == null) {
			throw new IllegalArgumentException("Need a feature or layer");
		}
		FeatureDetailWidgetBuilder customBuilder = getCustomBuilder(layer);
		if (customBuilder != null) {
			return customBuilder.createFeatureDetailCanvas(null, editingAllowed, maxHeight);
		} else {
			// TODO do you want it here? eg. should it not be just a list?
			// return new FeatureAttributeCanvas(null, editingAllowed,
			// maxHeight);
			return null;
		}
	}

	public static FeatureDetailWidget createFeatureDetailCanvas(Feature feature, boolean editingAllowed, 
			int maxHeight) {
		if (feature == null) {
			throw new IllegalArgumentException("Need a feature or layer");
		}
		FeatureDetailWidgetBuilder customBuilder = getCustomBuilder(feature.getLayer());
		if (customBuilder != null) {
			return customBuilder.createFeatureDetailCanvas(feature, editingAllowed, maxHeight);
		} else {
			// TODO do you want it here? eg. should it not be just a list?
			// return new FeatureAttributeCanvas(null, editingAllowed,
			// maxHeight);
			return null;
		}
	}

	public static Window createFeatureDetailWindow(Feature feature, boolean editingAllowed) {
		FeatureDetailWidgetBuilder customBuilder = getCustomBuilder(feature.getLayer());
		if (customBuilder != null) {
			FeatureDetailWindow fdw = customBuilder.createFeatureDetailWindow(feature, editingAllowed);
			if (fdw != null) {
				return fdw;
			} else {
				return windowWrap(customBuilder.createFeatureDetailCanvas(feature, editingAllowed,
						DEFAULT_WINDOW_HEIGHT));
			}

		} else {
			return new FeatureAttributeWindow(feature, editingAllowed);
		}
	}

	// ----------------------------------------------------------

	/**
	 * TODO might be better to return FeatureDetailWindow (so feature can be
	 * updated.
	 */
	private static Window windowWrap(Canvas featureDetail) {
		Window w = new Window();
		w.setAutoSize(true);
		w.setTitle(I18nProvider.getAttribute().getAttributeWindowTitle(""));
		w.setCanDragReposition(true);
		w.setCanDragResize(true);
		w.addItem(featureDetail);
		return w;
	}

	private static FeatureDetailWidgetBuilder getCustomBuilder(VectorLayer layer) {
		FeatureDetailWidgetBuilder b = null;
		try {
			if (layer != null) {
				ClientWidgetInfo cwi = layer.getLayerInfo().getWidgetInfo(CLIENTWIDGETINFO_KEY);
				if (cwi != null) {
					if (cwi instanceof WidgetBuilderInfo) {
						WidgetBuilderInfo lcfdii = (WidgetBuilderInfo) cwi;
						WidgetBuilder wb = WidgetFactory.get(lcfdii.getBuilderName());
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
