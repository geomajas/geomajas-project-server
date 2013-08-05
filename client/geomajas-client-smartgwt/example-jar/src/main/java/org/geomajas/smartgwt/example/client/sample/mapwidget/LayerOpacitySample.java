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

package org.geomajas.smartgwt.example.client.sample.mapwidget;

import com.google.gwt.core.client.GWT;
import org.geomajas.smartgwt.example.base.SamplePanel;
import org.geomajas.smartgwt.example.base.SamplePanelFactory;
import org.geomajas.smartgwt.client.controller.PanController;
import org.geomajas.smartgwt.client.map.layer.RasterLayer;
import org.geomajas.smartgwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Slider;
import com.smartgwt.client.widgets.events.ValueChangedEvent;
import com.smartgwt.client.widgets.events.ValueChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.smartgwt.example.client.sample.i18n.SampleMessages;

/**
 * Sample that shows the difference in rendering in screen space versus world space.
 * 
 * @author Pieter De Graef
 */
public class LayerOpacitySample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String TITLE = "LayerOpacity";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new LayerOpacitySample();
		}
	};

	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(10);

		// Create map with OSM layer, and add a PanController to it:
		VLayout mapLayout = new VLayout();
		mapLayout.setShowEdges(true);
		mapLayout.setHeight("60%");

		final MapWidget map = new MapWidget("mapOsm", "gwtExample");
		map.setController(new PanController(map));
		mapLayout.addMember(map);

		VLayout opacityLayout = new VLayout();
		Slider slider = new Slider("Opacity");
		slider.setWidth(300);
		slider.setVertical(false);
		slider.setMinValue(0);
		slider.setMaxValue(100);
		slider.setNumValues(101);
		slider.setValue(100);
		slider.addValueChangedHandler(new ValueChangedHandler() {

			public void onValueChanged(ValueChangedEvent event) {
				int intValue = event.getValue();
				double value = 0;
				if (intValue != 0) {
					value = (double) intValue / (double) 100;
				}

				RasterLayer layer = (RasterLayer) map.getMapModel().getLayer("clientLayerOsm");
				if (layer != null) {
					layer.setOpacity(value);
				}
			}
		});
		opacityLayout.addMember(slider);

		// Place both in the layout:
		layout.addMember(mapLayout);
		layout.addMember(opacityLayout);

		return layout;
	}

	public String getDescription() {
		return MESSAGES.layerOpacityDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] {
				"classpath:org/geomajas/smartgwt/example/context/mapOsm.xml",
				"classpath:org/geomajas/smartgwt/example/base/layerOsm.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
