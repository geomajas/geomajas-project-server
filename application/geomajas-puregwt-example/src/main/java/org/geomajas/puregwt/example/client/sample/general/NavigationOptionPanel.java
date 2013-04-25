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

package org.geomajas.puregwt.example.client.sample.general;

import org.geomajas.puregwt.client.event.MapInitializationEvent;
import org.geomajas.puregwt.client.event.MapInitializationHandler;
import org.geomajas.puregwt.client.map.MapConfiguration;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.example.client.ContentPanel;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates some options regarding map navigation.
 * 
 * @author Pieter De Graef
 */
public class NavigationOptionPanel extends ContentPanel {

	public NavigationOptionPanel(MapPresenter mapPresenter) {
		super(mapPresenter);
	}

	public String getTitle() {
		return "Navigation options";
	}

	public String getDescription() {
		return "This example demonstrates some options for navigating the map. How to turn the animated navigation "
				+ "on or off, etc.";
	}

	public Widget getContentWidget() {
		final long defaultMillis = 400;

		final TextBox millisTxt = new TextBox();
		millisTxt.setText(defaultMillis + "");
		millisTxt.setWidth("200px");

		// Define the left layout:
		VerticalPanel leftLayout = new VerticalPanel();
		leftLayout.setSize("220px", "100%");

		leftLayout.add(new HTML("<h3>Navigation options:</h3>"));

		Button animationOffBtn = new Button("Turn off animation");
		animationOffBtn.setWidth("200");
		animationOffBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent arg0) {
				mapPresenter.getConfiguration().setMapHintValue(MapConfiguration.ANIMATION_ENABLED, false);
			}
		});
		leftLayout.add(animationOffBtn);

		Button animationOnBtn = new Button("Turn on animation");
		animationOnBtn.setWidth("200");
		animationOnBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent arg0) {
				mapPresenter.getConfiguration().setMapHintValue(MapConfiguration.ANIMATION_ENABLED, true);
			}
		});
		leftLayout.add(animationOnBtn);

		leftLayout.add(new HTML("<br>Animation time in milliseconds:"));
		leftLayout.add(millisTxt);
		millisTxt.addChangeHandler(new ChangeHandler() {

			public void onChange(ChangeEvent event) {
				String txt = millisTxt.getValue();
				long time = defaultMillis;
				try {
					time = Integer.parseInt(txt);
				} catch (Exception e) { // NOSONAR
					Window.alert("Could not parse milliseconds... Default value of " + defaultMillis + " is taken");
				}

				mapPresenter.getConfiguration().setMapHintValue(MapConfiguration.ANIMATION_TIME, time);
			}
		});

		leftLayout.add(new HTML("<h3>Toggle layer animation:</h3>Works only when animation is enabled."));

		final CheckBox toggleOsm = new CheckBox("Open Street Map");
		toggleOsm.setValue(true);
		toggleOsm.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			public void onValueChange(ValueChangeEvent<Boolean> event) {
				mapPresenter.getConfiguration().setAnimated(mapPresenter.getLayersModel().getLayer(0),
						toggleOsm.getValue());
			}
		});
		leftLayout.add(toggleOsm);

		final CheckBox toggleCountries = new CheckBox("Countries");
		toggleCountries.setValue(true);
		toggleCountries.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			public void onValueChange(ValueChangeEvent<Boolean> event) {
				mapPresenter.getConfiguration().setAnimated(mapPresenter.getLayersModel().getLayer(1),
						toggleCountries.getValue());
			}
		});
		leftLayout.add(toggleCountries);

		// Create the mapPresenter and add an InitializationHandler:
		mapPresenter.setSize(640, 480);
		mapPresenter.getEventBus().addMapInitializationHandler(new MyMapInitializationHandler());

		// Define the whole layout:
		HorizontalPanel layout = new HorizontalPanel();
		layout.add(leftLayout);
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		layout.add(mapDecorator);

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
			double scale = mapPresenter.getViewPort().getScale() * 4;
			mapPresenter.getViewPort().applyScale(scale);
		}
	}
}