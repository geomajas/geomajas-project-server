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

package org.geomajas.widget.featureinfo.client.widget;

import java.util.List;
import java.util.Map;

import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.featureinfo.client.FeatureInfoMessages;
import org.geomajas.widget.featureinfo.client.widget.factory.FeatureDetailWidgetFactory;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Window;

/**
 * <p>
 * The <code>MultilayerFeatureInfoWindow</code> is a floating window that shows a list of features, grouped by layers.
 * </p>
 * 
 * @author Oliver May
 * @author Wout Swartenbroekx
 */
public class MultiLayerFeatureInfoWindow extends DockableWindow {

	private FeatureInfoMessages featureInfoMessages = GWT.create(FeatureInfoMessages.class);

	private MultiLayerFeaturesList featuresList;

	private MapWidget mapWidget;

	/**
	 * Construct a MultiLayerFeatureInfoWindow, allowing feature info of multiple features on one location.
	 * 
	 * @param mapWidget the map widget
	 * @param featureMap a Map (Layer, List(Feature)) that contains all the features on this position
	 * @param showDetailWindowInline should the detailwindow be displayed inline or in a popup 
	 */
	public MultiLayerFeatureInfoWindow(MapWidget mapWidget,
			Map<String, List<org.geomajas.layer.feature.Feature>> featureMap) {
		super();
		this.mapWidget = mapWidget;
		buildWidget();
		setFeatureMap(featureMap);
	}
	
	/**
	 * Construct a MultiLayerFeatureInfoWindow with specified featuresListLabels.
	 * Useful when using labels composed of multiple attributes
	 * 
	 * @param mapWidget the map widget
	 * @param featureMap a Map (Layer, List(Feature)) that contains all the features on this position
	 * @param showDetailWindowInline should the detailwindow be displayed inline or in a popup
	 * @param featuresListLabels contains for each layer specified in SLD attributeName 
	 * to be used as shown list entry value
	 */
	public MultiLayerFeatureInfoWindow(MapWidget mapWidget,	Map<String, 
			List<org.geomajas.layer.feature.Feature>> featureMap, Map<String, String> featuresListLabels) {
		super();
		this.mapWidget = mapWidget;
		buildWidget();
		setFeaturesListLabels(featuresListLabels);
		setFeatureMap(featureMap);
	}

	private void setFeaturesListLabels(Map<String, String> featuresListLabels) {
		featuresList.setFeaturesListLabels(featuresListLabels);
	}
	private void setFeatureMap(Map<String, List<org.geomajas.layer.feature.Feature>> featureMap) {
		featuresList.setFeatures(featureMap);
	}

	private void buildWidget() {
		setAutoSize(true);
		setTitle(featureInfoMessages.nearbyFeaturesWindowTitle());
		setCanDragReposition(true);
		setCanDragResize(true);
		setWidth("250px");
		setMinWidth(250);
		setKeepInParentRect(true);

		featuresList = new MultiLayerFeaturesList(mapWidget, new FeatureClickHandler() {

			public void onClick(Feature feature, Layer layer) {
				Window window = FeatureDetailWidgetFactory.createFeatureDetailWindow(feature, layer, false);
				window.setPageTop(mapWidget.getAbsoluteTop() + 25);
				window.setPageLeft(mapWidget.getAbsoluteLeft() + 25);
				window.setKeepInParentRect(true);
				window.draw();
			}

		});
		addItem(featuresList);
	}
}
