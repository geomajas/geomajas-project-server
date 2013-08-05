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

package org.geomajas.smartgwt.example.client.sample.attribute;

import com.google.gwt.core.client.GWT;
import org.geomajas.smartgwt.example.base.SamplePanel;
import org.geomajas.smartgwt.example.base.SamplePanelFactory;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.smartgwt.client.widget.FeatureListGrid;
import org.geomajas.smartgwt.client.widget.FeatureSearch;
import org.geomajas.smartgwt.client.widget.MapWidget;
import org.geomajas.smartgwt.client.widget.event.DefaultSearchHandler;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.smartgwt.example.client.sample.i18n.SampleMessages;

/**
 * <p>
 * Sample that shows a FeatureSearch widget that allows the user to search for features.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class SearchSample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

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

		// Switch off lazy loading, we want to get everything at once
		GwtCommandDispatcher.getInstance().setUseLazyLoading(false);

		// Create a map with the African countries and make it invisible:
		final MapWidget map = new MapWidget("mapFeatureListGrid", "gwtExample");
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

		// Set the maximum number of features to retrieve.
		search.setMaximumResultSize(50);

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
		return MESSAGES.searchDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/smartgwt/example/context/mapFeatureListGrid.xml",
				"classpath:org/geomajas/smartgwt/example/base/layerCountries110m.xml",
				"classpath:org/geomajas/smartgwt/example/base/layerWmsBluemarble.xml", };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
