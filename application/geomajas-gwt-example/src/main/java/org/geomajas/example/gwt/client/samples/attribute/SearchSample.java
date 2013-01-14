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

		// Switch off lazy loading, we want to get everything at once
		GwtCommandDispatcher.getInstance().setUseLazyLoading(false);

		// Create a map with the African countries and make it invisible:
		final MapWidget map = new MapWidget("mapFeatureListGrid", "gwt-samples");
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
		return I18nProvider.getSampleMessages().searchDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/example/gwt/client/samples/attribute/SearchSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "WEB-INF/mapFeatureListGrid.xml", "WEB-INF/layerCountries110m.xml",
				"WEB-INF/layerWmsBluemarble.xml", };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
