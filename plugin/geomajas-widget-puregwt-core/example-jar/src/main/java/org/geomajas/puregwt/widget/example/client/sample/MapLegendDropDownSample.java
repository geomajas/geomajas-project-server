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

package org.geomajas.puregwt.widget.example.client.sample;

import org.geomajas.gwt.client.GeomajasGinjector;
import org.geomajas.gwt.client.map.MapPresenter;
import org.geomajas.gwt.example.base.client.sample.SamplePanel;
import org.geomajas.puregwt.widget.client.map.MapLegendDropDown;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates changing layer order.
 * 
 * @author Jan De Moerloose
 */
public class MapLegendDropDownSample implements SamplePanel {

	/**
	 * UI binder for this widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, MapLegendDropDownSample> {
	}

	private static final GeomajasGinjector GEOMAJASINJECTOR = GWT.create(GeomajasGinjector.class);

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private MapPresenter mapPresenter;

	@UiField
	protected VerticalPanel legendPanel;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	public Widget asWidget() {
		// Define the left layout:
		Widget layout = UI_BINDER.createAndBindUi(this);

		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter = GEOMAJASINJECTOR.getMapPresenter();
		mapPresenter.setSize(480, 480);

		// Add a MapLegendDropDown to the panel on the left:
		legendPanel.add(new MapLegendDropDown(mapPresenter));

		// Add a MapLegendDropDown to the MapPresenter:
		MapLegendDropDown mapDropDown = new MapLegendDropDown(mapPresenter);
		mapPresenter.getWidgetPane().add(mapDropDown, 0, 5);

		// Align the MapLegendDropDown on the map to the top-right:
		mapDropDown.getElement().getStyle().clearLeft();
		mapDropDown.getElement().getStyle().setRight(5, Unit.PX);

		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);

		// Initialize the map, and return the layout:
		mapPresenter.initialize("gwt-app", "mapLegend");
		return layout;
	}
}