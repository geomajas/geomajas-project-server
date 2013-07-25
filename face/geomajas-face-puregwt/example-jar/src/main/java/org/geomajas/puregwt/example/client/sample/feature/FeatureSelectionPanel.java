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

package org.geomajas.puregwt.example.client.sample.feature;

import org.geomajas.puregwt.client.controller.FeatureSelectionController;
import org.geomajas.puregwt.client.controller.FeatureSelectionController.SelectionMethod;
import org.geomajas.puregwt.client.event.MapInitializationEvent;
import org.geomajas.puregwt.client.event.MapInitializationHandler;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.example.base.client.ExampleBase;
import org.geomajas.puregwt.example.base.client.sample.SamplePanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates a feature selection controller.
 * 
 * @author Pieter De Graef
 */
public class FeatureSelectionPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, FeatureSelectionPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private MapPresenter mapPresenter;

	private FeatureSelectionController featureSelectionController;

	@UiField
	protected RadioButton clickOnly;

	@UiField
	protected RadioButton clickAndDrag;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	public Widget asWidget() {
		Widget layout = UI_BINDER.createAndBindUi(this);

		featureSelectionController = new FeatureSelectionController();

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

		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter = ExampleBase.getInjector().getMapPresenter();
		mapPresenter.setSize(480, 480);
		mapPresenter.getEventBus().addMapInitializationHandler(new MyMapInitializationHandler());

		// Add the map to the GUI, using a decorator for nice borders:
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);

		// Initialize the map, and return the layout:
		mapPresenter.initialize("puregwt-app", "mapCountries");
		return layout;
	}

	/**
	 * Map initialization handler that zooms in.
	 * 
	 * @author Pieter De Graef
	 */
	private class MyMapInitializationHandler implements MapInitializationHandler {

		public void onMapInitialized(MapInitializationEvent event) {
			mapPresenter.getViewPort().applyBounds(ExampleBase.BBOX_AFRICA);
			mapPresenter.setMapController(featureSelectionController);
		}
	}
}