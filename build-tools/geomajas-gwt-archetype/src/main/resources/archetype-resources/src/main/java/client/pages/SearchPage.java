#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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

package ${package}.client.pages;

import org.geomajas.gwt.client.widget.FeatureListGrid;
import org.geomajas.gwt.client.widget.FeatureSearch;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.event.DefaultSearchHandler;

import com.smartgwt.client.widgets.tab.TabSet;

/**
 * <p>
 * Example tab showing the {@link SearchTable} widget.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class SearchPage extends AbstractTab {

	/**
	 * Create a tab page with a search functionality.
	 * 
	 * @param map
	 *            The map for whom we are searching for features.
	 * @param tabSet
	 *            The set of tabs to whom this tab will be added. Also when a search is done, this tab-set is used to
	 *            make the tab with the feature grid visible.
	 * @param featureGrid
	 *            The actual feature grid instance from the second tab. This feature grid is used to display the
	 *            features that result from a search.
	 */
	public SearchPage(MapWidget map, final TabSet tabSet, FeatureListGrid featureGrid) {
		super("Search", map);

		// Create a SearchWidget, based upon a map's model:
		FeatureSearch searchWidget = new FeatureSearch(map.getMapModel(), true);

		// What to do when the result of a search comes in?
		// The DefaultSearchHandler will add all the features in the result to the given FeatureListGrid.
		searchWidget.addSearchHandler(new DefaultSearchHandler(featureGrid) {

			// After the features have been added to the FeatureListGrid, make sure the tab with the grid is visible:
			public void afterSearch() {
				tabSet.selectTab(1);
			}
		});

		// Limit the maximum number of features that a search may produce:
		searchWidget.setMaximumResultSize(20);
		mainLayout.addMember(searchWidget);
	}

	public void initialize() {
	}
}
