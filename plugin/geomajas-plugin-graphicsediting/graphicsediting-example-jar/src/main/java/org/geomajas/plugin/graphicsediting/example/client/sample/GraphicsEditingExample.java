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
package org.geomajas.plugin.graphicsediting.example.client.sample;

import org.geomajas.gwt.client.GeomajasGinjector;
import org.geomajas.gwt.client.map.MapConfiguration;
import org.geomajas.gwt.client.map.MapPresenter;
import org.geomajas.gwt.client.widget.MapLayoutPanel;
import org.geomajas.gwt.example.base.client.sample.SamplePanel;
import org.geomajas.plugin.graphicsediting.example.client.annotation.AnnotationToolBar;
import org.geomajas.plugin.graphicsediting.example.client.annotation.SetAnnotationPresenterImpl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Sample panel.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GraphicsEditingExample implements SamplePanel {

	protected DockLayoutPanel rootElement;

	private final MapPresenter mapPresenter;

	private static final GeomajasGinjector INJECTOR = GWT.create(GeomajasGinjector.class);

	@UiField
	protected SimpleLayoutPanel contentPanel;

	@Override
	public Widget asWidget() {
		// return root layout element
		return rootElement;
	}

	/**
	 * UI binder interface.
	 * 
	 * @author Jan De Moerloose
	 */
	interface MyBinder extends UiBinder<DockLayoutPanel, GraphicsEditingExample> {

	}

	private static final MyBinder UIBINDER = GWT.create(MyBinder.class);

	public GraphicsEditingExample() {
		rootElement = UIBINDER.createAndBindUi(this);

		// Initialize the map
		mapPresenter = INJECTOR.getMapPresenter();
		mapPresenter.initialize("appGraphicsEditing", "mapGraphicsEditing");
		mapPresenter.getConfiguration().setMapHintValue(MapConfiguration.ANIMATION_TIME, 300L);
		MapLayoutPanel mapLayout = new MapLayoutPanel();
		mapLayout.setPresenter(mapPresenter);
		AnnotationToolBar toolbar = new AnnotationToolBar();
		new SetAnnotationPresenterImpl(toolbar, INJECTOR.getEventBus(), mapPresenter, INJECTOR.getGfxUtil());
		mapPresenter.getWidgetPane().add(toolbar.asWidget());
		contentPanel.setWidget(mapLayout);
	}

}