/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.example.gwt.client.samples.attribute;

import org.geomajas.example.gwt.client.samples.base.SamplePanel;
import org.geomajas.example.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.example.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.widget.FeatureListGrid;
import org.geomajas.gwt.client.widget.FeatureSearch;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.event.DefaultSearchHandler;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows a FeatureSearch widget that allows the user to search for features.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class SearchSample extends SamplePanel {

	public static final String TITLE = "FeatureSearchSample";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new SearchSample();
		}
	};

	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		// switch off lazy loading, we want to get everything
		GwtCommandDispatcher.getInstance().setUseLazyLoading(false);

		// Create a map with the African countries and make it invisible:
		final MapWidget map = new MapWidget("africanCountriesMap", "gwt-samples");
		map.setVisible(false);
		layout.addMember(map);
		map.init();

		// Create a layout with a FeatureListGrid in it:
		final FeatureListGrid grid = new FeatureListGrid(map.getMapModel());
		grid.setShowEdges(true);

		// Create a search widget that displays it's results in the FeatureListGrid:
		final FeatureSearch search = new FeatureSearch(map.getMapModel(), true);
		search.addSearchHandler(new DefaultSearchHandler(grid) {

			public void afterSearch() {
			}
		});
		VLayout searchLayout = new VLayout();
		searchLayout.setHeight("30%");
		searchLayout.setShowEdges(true);
		searchLayout.addMember(search);
		searchLayout.setShowResizeBar(true);

		// Add the search and the grid to the layout:
		layout.addMember(searchLayout);
		layout.addMember(grid);

		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().searchDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/example/gwt/client/samples/attribute/SearchSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/example/gwt/clientcfg/attribute/mapFeatureListGrid.xml",
				"classpath:org/geomajas/example/gwt/servercfg/vector/layerCountries.xml",
				"classpath:org/geomajas/example/gwt/servercfg/raster/layerWmsBluemarble.xml", };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
