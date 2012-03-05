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

package org.geomajas.puregwt.client.general;

import org.geomajas.puregwt.client.ContentPanel;
import org.geomajas.puregwt.client.event.MapInitializationEvent;
import org.geomajas.puregwt.client.event.MapInitializationHandler;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.render.MapRenderer;
import org.geomajas.puregwt.client.map.render.MapRendererImpl;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
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

	private MapPresenter mapPresenter;

	public String getTitle() {
		return "Navigation options";
	}

	public String getDescription() {
		return "This example demonstrates some options for navigating the map. How to turn the animated navigation "
				+ "on or off, etc.";
	}

	public Widget getContentWidget() {
		final int defaultMillis = 400;

		final TextBox millisTxt = new TextBox();
		millisTxt.setText(defaultMillis + "");
		millisTxt.setWidth("200px");
		final TextBox nrLayerTxt = new TextBox();
		nrLayerTxt.setText("1");
		nrLayerTxt.setWidth("200px");

		// Define the left layout:
		VerticalPanel leftLayout = new VerticalPanel();
		leftLayout.setSize("220px", "100%");

		leftLayout.add(new HTML("<h3>Navigation options:</h3>"));

		Button animationOffBtn = new Button("Turn off animation");
		animationOffBtn.setWidth("200");
		animationOffBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent arg0) {
				MapRenderer r = mapPresenter.getMapRenderer();
				r.setAnimationMillis(0);
			}
		});
		leftLayout.add(animationOffBtn);

		Button animationOnBtn = new Button("Turn on animation");
		animationOnBtn.setWidth("200");
		animationOnBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent arg0) {
				String txt = millisTxt.getValue();
				int time = defaultMillis;
				try {
					time = Integer.parseInt(txt);
				} catch (Exception e) { // NOSONAR
					Window.alert("Could not parse milliseconds... Default value of " + defaultMillis + " is taken");
				}

				MapRendererImpl r = (MapRendererImpl) mapPresenter.getMapRenderer();
				r.setAnimationMillis(time);
			}
		});
		leftLayout.add(animationOnBtn);

		leftLayout.add(new HTML("<br>Animation time in milliseconds:"));
		leftLayout.add(millisTxt);
		millisTxt.addChangeHandler(new ChangeHandler() {

			public void onChange(ChangeEvent event) {
				String txt = millisTxt.getValue();
				int time = defaultMillis;
				try {
					time = Integer.parseInt(txt);
				} catch (Exception e) { // NOSONAR
					Window.alert("Could not parse milliseconds... Default value of " + defaultMillis + " is taken");
				}

				MapRendererImpl r = (MapRendererImpl) mapPresenter.getMapRenderer();
				r.setAnimationMillis(time);
			}
		});

		leftLayout.add(new HTML("<br>Number of animated layers:"));
		leftLayout.add(nrLayerTxt);
		nrLayerTxt.addChangeHandler(new ChangeHandler() {

			public void onChange(ChangeEvent event) {
				String txt = nrLayerTxt.getValue();
				int count = 1;
				try {
					count = Integer.parseInt(txt);
				} catch (Exception e) { // NOSONAR
					Window.alert("Could not parse number of layers... Default value of 1 is taken");
				}

				MapRendererImpl r = (MapRendererImpl) mapPresenter.getMapRenderer();
				r.setNrAnimatedLayers(count);
			}
		});

		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter = getInjector().getMapPresenter();
		mapPresenter.setSize(640, 480);
		mapPresenter.getEventBus().addHandler(MapInitializationEvent.TYPE, new MyMapInitializationHandler());

		// Define the whole layout:
		HorizontalPanel layout = new HorizontalPanel();
		layout.add(leftLayout);
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
		}
	}
}