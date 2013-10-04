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

package org.geomajas.gwt2.example.client.sample.general;

import org.geomajas.gwt2.client.event.MapInitializationEvent;
import org.geomajas.gwt2.client.event.MapInitializationHandler;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.example.client.ExampleJar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates some options regarding map navigation.
 * 
 * @author Pieter De Graef
 */
public class NavigationOptionPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, NavigationOptionPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private final long defaultMillis = 400;

	private MapPresenter mapPresenter;

	@UiField
	protected CheckBox enableAnimation;

	@UiField
	protected TextBox millisBox;

	@UiField
	protected VerticalPanel layerPanel;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	public Widget asWidget() {
		Widget layout = UI_BINDER.createAndBindUi(this);

		// Add a change handler to the animation toggle checkbox:
		enableAnimation.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			public void onValueChange(ValueChangeEvent<Boolean> event) {
				mapPresenter.getConfiguration().setMapHintValue(MapConfiguration.ANIMATION_ENABLED, event.getValue());
			}
		});

		// Initialize the map, and return the layout:
		mapPresenter = ExampleJar.getInjector().getMapPresenter();
		mapPresenter.setSize(480, 480);
		mapPresenter.getEventBus().addMapInitializationHandler(new MyMapInitializationHandler());
		mapPresenter.initialize("gwt-app", "mapCountries");
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);

		// Make sure the text box also reacts to the "Enter" key:
		millisBox.addKeyUpHandler(new KeyUpHandler() {

			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					changeAnimationMillis();
				}
			}
		});

		return layout;
	}

	@UiHandler("millisBtn")
	protected void onMillisButtonClicked(ClickEvent event) {
		changeAnimationMillis();
	}

	private void changeAnimationMillis() {
		String txt = millisBox.getValue();
		long time = defaultMillis;
		try {
			time = Integer.parseInt(txt);
		} catch (Exception e) { // NOSONAR
			Window.alert("Could not parse milliseconds... Default value of " + defaultMillis + " is used");
			mapPresenter.getConfiguration().setMapHintValue(MapConfiguration.ANIMATION_TIME, defaultMillis);
			millisBox.setValue(defaultMillis + "");
		}
		mapPresenter.getConfiguration().setMapHintValue(MapConfiguration.ANIMATION_TIME, time);
	}

	/**
	 * Map initialization handler that zooms in.
	 * 
	 * @author Pieter De Graef
	 */
	private class MyMapInitializationHandler implements MapInitializationHandler {

		public void onMapInitialized(MapInitializationEvent event) {
			// Add layer specific animation CheckBoxes:
			for (int i = 0; i < mapPresenter.getLayersModel().getLayerCount(); i++) {
				final Layer layer = mapPresenter.getLayersModel().getLayer(i);
				CheckBox cb = new CheckBox("Animate: " + layer.getTitle());
				cb.setValue(true);
				layerPanel.add(cb);
				cb.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					public void onValueChange(ValueChangeEvent<Boolean> event) {
						mapPresenter.getConfiguration().setAnimated(layer, event.getValue());
					}
				});
			}

			// Zoom in (scale times 4), to get a better view:
			double scale = mapPresenter.getViewPort().getScale() * 4;
			mapPresenter.getViewPort().applyScale(scale);
		}
	}
}