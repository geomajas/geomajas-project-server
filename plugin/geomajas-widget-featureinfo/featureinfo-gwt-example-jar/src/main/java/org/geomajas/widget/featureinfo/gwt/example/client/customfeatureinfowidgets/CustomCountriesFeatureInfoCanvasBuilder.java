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
package org.geomajas.widget.featureinfo.gwt.example.client.customfeatureinfowidgets;

import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.widget.featureinfo.client.widget.factory.FeatureDetailWidgetBuilder;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;

/**
 * Sample implementation of {@link FeatureDetailWidgetBuilder}.
 *
 * @author Kristof Heirwegh
 */
public class CustomCountriesFeatureInfoCanvasBuilder implements FeatureDetailWidgetBuilder {

	private static final long serialVersionUID = 100L;

	public Window createFeatureDetailWindow(Feature feature, boolean editingAllowed) {
		Window w = new Window();
		w.setAutoSize(true);
		w.setTitle(I18nProvider.getAttribute().getAttributeWindowTitle(feature.getLabel()));
		w.setCanDragReposition(true);
		w.setCanDragResize(true);
		w.addItem(new CustomCountriesFeatureInfoCanvas(feature));
		return w;
	}

	// ----------------------------------------------------------

	/**
	 * The class our builder will return, notice we are using FeatureDetailWidget as parent class.
	 */
	private static class CustomCountriesFeatureInfoCanvas extends Canvas {
		private Label l;

		public CustomCountriesFeatureInfoCanvas(Feature feature) {
			setBackgroundColor("#BBFFBB");
			l = new Label("<b>Custom DetailPanel for feature: </b>" + feature.getId());
			l.setWidth(250);
			l.setPadding(10);
			addChild(l);
		}

	}

	public Canvas createWidget() {
		return null;
	}

	public void configure(String key, String value) {
	}
}
