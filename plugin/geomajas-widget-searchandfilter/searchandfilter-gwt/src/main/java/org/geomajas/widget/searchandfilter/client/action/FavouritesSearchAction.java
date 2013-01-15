/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.searchandfilter.client.action;

import org.geomajas.gwt.client.action.ConfigurableAction;
import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.client.util.GsfLayout;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchWidgetRegistry;
import org.geomajas.widget.searchandfilter.client.widget.searchfavourites.SearchFavouritesListCreator;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * @author Oliver May
 */
public class FavouritesSearchAction extends ToolbarAction implements ConfigurableAction {

	private static final SearchAndFilterMessages MESSAGES = GWT.create(SearchAndFilterMessages.class);
	
	public static final String IDENTIFIER = SearchFavouritesListCreator.IDENTIFIER;
	

	public FavouritesSearchAction() {
		super(GsfLayout.iconSearchFavourites, MESSAGES.searchFavoriteTitle(), MESSAGES.searchFavoriteTooltip());
	}

	public void configure(String key, String value) {
	}

	public void onClick(ClickEvent event) {
		SearchWidgetRegistry.getSearchWidgetInstance(SearchFavouritesListCreator.IDENTIFIER).showForSearch();
	}

}
