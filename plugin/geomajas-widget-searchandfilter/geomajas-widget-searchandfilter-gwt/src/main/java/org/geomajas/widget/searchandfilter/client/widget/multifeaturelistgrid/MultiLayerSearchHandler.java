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
package org.geomajas.widget.searchandfilter.client.widget.multifeaturelistgrid;

import com.google.gwt.event.shared.EventHandler;

/**
 * @see SearchHandler.
 * @author Kristof Heirwegh
 *
 */
public interface MultiLayerSearchHandler extends EventHandler {

	/**
	 * Called when a search over multiple layers has been executed.
	 *
	 * @param event
	 *            The search event
	 */
	void onSearchDone(MultiLayerSearchEvent event);
}
