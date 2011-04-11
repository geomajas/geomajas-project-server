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

package org.geomajas.widget.searchandfilter.gwt.example.client.pages;

import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.searchandfilter.client.widget.geometricsearch.FreeDrawingSearch;
import org.geomajas.widget.searchandfilter.client.widget.geometricsearch.GeometricSearchWidget;
import org.geomajas.widget.searchandfilter.client.widget.geometricsearch.SelectionSearch;
import org.geomajas.widget.searchandfilter.client.widget.multifeaturelistgrid.MultiFeatureListGrid;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

/**
 * 
 * @author Kristof Heirwegh
 * @author Bruce Palmkoeck
 */
public class GeometricSearchPage extends AbstractTab {

	public GeometricSearchPage(String title, final MapWidget map, final MultiFeatureListGrid target) {
		super(title, map);

		IButton btnSearch = new IButton("Open Search window");
		btnSearch.setAutoFit(true);
		btnSearch.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window window = new Window();
				GeometricSearchWidget geometricSearchWidget = new GeometricSearchWidget(map, target);
				geometricSearchWidget.addSearchMethod(new SelectionSearch());
				geometricSearchWidget.addSearchMethod(new FreeDrawingSearch());
				window.addItem(geometricSearchWidget);
				window.setTitle(geometricSearchWidget.getTitle());
				window.setAutoSize(true);
				window.setAutoCenter(true);
				window.setCanDragResize(true);
				window.show();
			}
		});
		mainLayout.addMember(btnSearch);
	}

	@Override
	public void initialize() {
	}
}
