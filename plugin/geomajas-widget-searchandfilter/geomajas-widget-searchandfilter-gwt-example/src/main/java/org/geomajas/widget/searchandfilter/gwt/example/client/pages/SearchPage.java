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
import org.geomajas.widget.searchandfilter.client.widget.geometricsearch.GeometricSearchPanel;
import org.geomajas.widget.searchandfilter.client.widget.search.AttributeSearchPanel;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchWidgetRegistry;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

/**
 * 
 * @author Kristof Heirwegh
 * @author Bruce Palmkoeck
 */
public class SearchPage extends AbstractTab {

	public SearchPage(String title, final MapWidget map) {
		super(title, map);

		IButton btnAttSearch = new IButton("Open Attribute Search Window");
		btnAttSearch.setAutoFit(true);
		btnAttSearch.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SearchWidgetRegistry.getSearchWidget(AttributeSearchPanel.IDENTIFIER).showForSearch();
			}
		});

		IButton btnGeoSearch = new IButton("Open Geographic Search window");
		btnGeoSearch.setAutoFit(true);
		btnGeoSearch.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SearchWidgetRegistry.getSearchWidget(GeometricSearchPanel.IDENTIFIER).showForSearch();
			}
		});

		mainLayout.setMembersMargin(10);
		mainLayout.setMargin(10);

		mainLayout.addMember(btnAttSearch);
		mainLayout.addMember(btnGeoSearch);
	}

	@Override
	public void initialize() {
	}
}
