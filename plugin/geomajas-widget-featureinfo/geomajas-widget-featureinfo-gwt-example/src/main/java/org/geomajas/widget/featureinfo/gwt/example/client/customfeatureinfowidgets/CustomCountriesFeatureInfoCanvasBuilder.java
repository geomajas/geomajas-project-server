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
package org.geomajas.widget.featureinfo.gwt.example.client.customfeatureinfowidgets;

import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.widget.featureinfo.client.widget.factory.FeatureDetailWidget;
import org.geomajas.widget.featureinfo.client.widget.factory.FeatureDetailWidgetBuilder;
import org.geomajas.widget.featureinfo.client.widget.factory.FeatureDetailWindow;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;

/**
 * Sample implementation of a FeatureDetailWidgetBuilder.
 * @author Kristof Heirwegh
 *
 */
public class CustomCountriesFeatureInfoCanvasBuilder implements FeatureDetailWidgetBuilder {

	private static final long serialVersionUID = 1L;

	public FeatureDetailWidget createFeatureDetailCanvas(Feature feature, boolean editingAllowed, int maxHeight) {
		return new CustomCountriesFeatureInfoCanvas(feature);
	}

	public FeatureDetailWindow createFeatureDetailWindow(Feature feature, boolean editingAllowed) {
		return null; // optional
	}

	public Canvas createWidget() {
		// TODO not used, maybe better remove from interface altogether
		return null;
	}

	// ----------------------------------------------------------

	/**
	 * The class our builder will return, notice we are using FeatureDetailWidget as parent class.
	 */
	private static class CustomCountriesFeatureInfoCanvas extends FeatureDetailWidget {
		private Label l;

		public CustomCountriesFeatureInfoCanvas(Feature feature) {
			setBackgroundColor("#BBFFBB");
			l = new Label("<b>Custom DetailPanel for feature: </b>" + feature.getId());
			l.setWidth(250);
			l.setPadding(10);
			addChild(l);
		}

		@Override
		public void setFeature(Feature feature, int maxHeight) {
			l.setTitle("<b>Custom DetailPanel for feature: </b>" + feature.getId());
		}
	}
}
