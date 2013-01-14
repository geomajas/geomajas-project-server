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

package org.geomajas.plugin.jsapi.example.client;

import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.LayerTree;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.Toolbar;
import org.geomajas.plugin.jsapi.gwt.client.exporter.GeomajasServiceImpl;
import org.geomajas.plugin.jsapi.gwt.client.exporter.map.MapImpl;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Entry point and main class for GWT application. This class defines the layout and functionality of this application.
 * 
 * @author Pieter De Graef
 */
public class ComplexApp implements EntryPoint {

	public void onModuleLoad() {
		HLayout layout = new HLayout(5);
		layout.setMargin(5);
		layout.setSize("100%", "100%");

		// ---------------------------------------------------------------------
		// Create the left-side:
		// ---------------------------------------------------------------------
		final MapWidget mapWidget = new MapWidget("mapMain", "app");
		GeomajasServiceImpl.getInstance().registerMap("app", "mapMain", new MapImpl(mapWidget));

		final Toolbar toolbar = new Toolbar(mapWidget);
		toolbar.setButtonSize(WidgetLayout.toolbarLargeButtonSize);

		VLayout leftLayout = new VLayout();
		leftLayout.setShowEdges(true);
		leftLayout.addMember(toolbar);
		leftLayout.addMember(mapWidget);
		leftLayout.setHeight("100%");

		layout.addMember(leftLayout);

		// ---------------------------------------------------------------------
		// Create the right-side:
		// ---------------------------------------------------------------------

		VLayout rightLayout = new VLayout();
		rightLayout.setShowEdges(true);
		rightLayout.setWidth(200);
		LayerTree layerTree = new LayerTree(mapWidget);
		rightLayout.addMember(layerTree);
		layout.addMember(rightLayout);

		// ---------------------------------------------------------------------
		// Finally add to html and draw everything:
		// ---------------------------------------------------------------------

		Element element = DOM.getElementById("gwt-app-element");
		if (element != null) {
			layout.setHtmlElement(DOM.getElementById("gwt-app-element"));
			layout.setWidth(DOM.getElementById("gwt-app-element").getStyle().getWidth());
			layout.setHeight(DOM.getElementById("gwt-app-element").getStyle().getHeight());
			layout.draw();
		}
	}
}