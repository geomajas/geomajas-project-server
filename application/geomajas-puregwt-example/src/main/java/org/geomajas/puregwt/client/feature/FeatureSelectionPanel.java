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

package org.geomajas.puregwt.client.feature;

import org.geomajas.puregwt.client.ContentPanel;
import org.geomajas.puregwt.client.controller.FeatureSelectionController;
import org.geomajas.puregwt.client.controller.FeatureSelectionController.SelectionMethod;
import org.geomajas.puregwt.client.event.MapInitializationEvent;
import org.geomajas.puregwt.client.event.MapInitializationHandler;
import org.geomajas.puregwt.client.map.MapPresenter;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates a feature selection controller.
 * 
 * @author Pieter De Graef
 */
public class FeatureSelectionPanel extends ContentPanel {

	private MapPresenter mapPresenter;

	private FeatureSelectionController featureSelectionController;

	public FeatureSelectionPanel(MapPresenter mapPresenter) {
		super(mapPresenter);
	}

	public String getTitle() {
		return "Feature Selection";
	}

	public String getDescription() {
		return "This example demonstrates the use of the FeatureSelectionController. This controller allows the user "
				+ "to select features on the map.";
	}

	public Widget getContentWidget() {
		featureSelectionController = new FeatureSelectionController();

		// Create a vertical option panel:
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setWidth("200px");
		verticalPanel.setSpacing(5);

		// Use a radio button to determine the selection method:
		Label selectionType = new Label("Choose a selection method for the map controller:");
		RadioButton clickOnly = new RadioButton("selection_type", "Click only");
		clickOnly.setValue(true);
		RadioButton clickAndDrag = new RadioButton("selection_type", "Click and Drag");

		clickOnly.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					featureSelectionController.setSelectionMethod(SelectionMethod.CLICK_ONLY);
				}
			}
		});
		clickAndDrag.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					featureSelectionController.setSelectionMethod(SelectionMethod.CLICK_AND_DRAG);
				}
			}
		});
		verticalPanel.add(selectionType);
		verticalPanel.add(clickOnly);
		verticalPanel.add(clickAndDrag);

		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter.setSize(480, 480);
		mapPresenter.getEventBus().addMapInitializationHandler(new MyMapInitializationHandler());

		// Define the whole layout:
		HorizontalPanel layout = new HorizontalPanel();
		layout.add(verticalPanel);
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		layout.add(mapDecorator);

		// Initialize the map, and return the layout:
		mapPresenter.initialize("pure-gwt-app", "mapCountries");
		return layout;
	}

	/**
	 * Map initialization handler that zooms in.
	 * 
	 * @author Pieter De Graef
	 */
	private class MyMapInitializationHandler implements MapInitializationHandler {

		public void onMapInitialized(MapInitializationEvent event) {
			double scale = mapPresenter.getViewPort().getScale() * 4;
			mapPresenter.getViewPort().applyScale(scale);

			mapPresenter.getLayersModel().getLayer(1).setMarkedAsVisible(true);
			mapPresenter.setMapController(featureSelectionController);
		}
	}
}