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
package org.geomajas.gwt.widget.example.client.sample.featureselectbox;

import org.geomajas.gwt.client.GeomajasGinjector;
import org.geomajas.gwt.client.map.MapPresenter;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.widget.MapLayoutPanel;
import org.geomajas.gwt.example.base.client.sample.SamplePanel;
import org.geomajas.gwt.widget.client.featureselectbox.event.FeatureClickedEvent;
import org.geomajas.gwt.widget.client.featureselectbox.event.FeatureClickedHandler;
import org.geomajas.gwt.widget.client.featureselectbox.presenter.FeatureClickedListener;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Class description.
 *
 * @author Dosi Bingov
 */
public class FeatureSelectedExample implements SamplePanel {

	protected DockLayoutPanel rootElement;

	private MapPresenter mapPresenter;

	private static final GeomajasGinjector GEOMAJASINJECTOR = GWT.create(GeomajasGinjector.class);

	@UiField
	protected ResizeLayoutPanel mapPanel;

	@UiField
	protected VerticalPanel layerEventLayout;

	@Override
	public Widget asWidget() {
		// return root layout element
		return rootElement;
	}

	/**
	 * UI binder interface.
	 *
	 * @author Dosi Bingov
	 */
	interface FeatureSelectedExampleUiBinder extends
			UiBinder<DockLayoutPanel, FeatureSelectedExample> {

	}

	private static final FeatureSelectedExampleUiBinder UIBINDER = GWT.create(FeatureSelectedExampleUiBinder.class);

	public FeatureSelectedExample() {
		rootElement = UIBINDER.createAndBindUi(this);

		// Create the MapPresenter
		mapPresenter = GEOMAJASINJECTOR.getMapPresenter();

		// add FeatureClickedHandler where we handle FeatureClickedEvent
		mapPresenter.getEventBus().addHandler(FeatureClickedHandler.TYPE, new MyFeatureClickedHandler());

		// Define the layout:
		ResizeLayoutPanel resizeLayoutPanel = new ResizeLayoutPanel();
		final MapLayoutPanel layout = new MapLayoutPanel();
		resizeLayoutPanel.setWidget(layout);
		resizeLayoutPanel.setSize("100%", "100%");
		layout.setPresenter(mapPresenter);

		mapPanel.add(resizeLayoutPanel);

		// Initialize the map
		mapPresenter.initialize("puregwt-widget-app", "mapGhent");

		// add featured clicked listener.
		mapPresenter.addMapListener(new FeatureClickedListener(7));
	}

	/**
	 * Handler that handles FeatureClickedEvent.
	 */
	private class MyFeatureClickedHandler implements FeatureClickedHandler {

		@Override
		public void onFeatureClicked(FeatureClickedEvent event) {
			Feature feature = event.getFeature();
			layerEventLayout.add(new Label("feature label => " + feature.getLabel()));
			layerEventLayout.add(new Label("layer title => " + feature.getLayer().getTitle()));
		}
	}
}