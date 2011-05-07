/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.layers;

import org.geomajas.puregwt.client.ContentPanel;
import org.geomajas.puregwt.client.GeomajasGinjector;
import org.geomajas.puregwt.client.map.MapPresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public class DefaultContentPanel extends ContentPanel {

	private final GeomajasGinjector geomajasInjector = GWT.create(GeomajasGinjector.class);

	public String getTitle() {
		return "Default sample";
	}

	public String getDescription() {
		return "We're testing something.... what else???";
	}

	public Widget getContentWidget() {
		final MapPresenter mapPresenter = geomajasInjector.getMapPresenter();
		mapPresenter.setSize(640, 480);

		VerticalPanel layout = new VerticalPanel();
		Button initButton = new Button("Initialize");
		initButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				mapPresenter.initialize("pure-gwt-app", "mapOsm");
			}
		});
		layout.add(initButton);
		layout.add(mapPresenter.asWidget());

		return layout;
	}
}