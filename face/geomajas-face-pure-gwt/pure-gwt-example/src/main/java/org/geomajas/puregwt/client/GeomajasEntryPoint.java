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

package org.geomajas.puregwt.client;

import org.geomajas.puregwt.client.widget.MapWidgetImpl;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point and main class for pure GWT example application.
 * 
 * @author Pieter De Graef
 */
public class GeomajasEntryPoint implements EntryPoint {

	private final PureGwtExampleGinjector injector = GWT.create(PureGwtExampleGinjector.class);

	public void onModuleLoad() {
		MapWidgetImpl map = injector.getMap();
		map.setSize("800px", "600px");

		Button b2 = new Button("Init", new ClickHandler() {

			public void onClick(ClickEvent event) {
				//map.init();
			}
		});

		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.add(b2);

		RootPanel.get().add(hPanel);
		
		DecoratorPanel decPanel = new DecoratorPanel();
		decPanel.setWidget(map);
		RootPanel.get().add(decPanel);
	}
}