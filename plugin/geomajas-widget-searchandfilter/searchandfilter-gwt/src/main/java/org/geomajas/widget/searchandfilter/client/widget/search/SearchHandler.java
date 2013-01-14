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
package org.geomajas.widget.searchandfilter.client.widget.search;

import com.google.gwt.event.shared.EventHandler;

/**
 * Search handler.
 *
 * @see org.geomajas.gwt.client.widget.event.SearchHandler
 * @see SearchWidgetRegistry
 * @author Kristof Heirwegh
 */
public interface SearchHandler extends EventHandler {

	/**
	 * Called before search is started.
	 * TODO might add some cancel possibilities
	 * @param event event
	 */
	void onSearchStart(SearchEvent event);

	/**
	 * Called when a search has been executed and the result was returned.
	 * Not called in case of error.
	 *
	 * @param event
	 *            The search event
	 */
	void onSearchDone(SearchEvent event);

	/**
	 * Called after onSearchDone, or in case of error.
	 * @param event event
	 */
	void onSearchEnd(SearchEvent event);
}
