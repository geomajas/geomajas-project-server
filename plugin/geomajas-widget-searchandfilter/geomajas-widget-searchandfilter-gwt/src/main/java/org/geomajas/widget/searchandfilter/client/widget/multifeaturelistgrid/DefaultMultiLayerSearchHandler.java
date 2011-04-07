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

import org.geomajas.gwt.client.widget.event.SearchEvent;
import org.geomajas.gwt.client.widget.event.SearchHandler;

/**
 * @see DefaultSearchHandler
 * @author Kristof Heirwegh
 *
 */
public class DefaultMultiLayerSearchHandler implements SearchHandler, MultiLayerSearchHandler {

	private MultiFeatureListGrid target;

	public DefaultMultiLayerSearchHandler(MultiFeatureListGrid target) {
		if (target == null) {
			throw new IllegalArgumentException("Please provide a target");
		}
		this.target = target;
	}

	public void onSearchDone(SearchEvent event) {
		target.addFeatures(event.getLayer(), event.getFeatures());
	}

	public void onSearchDone(MultiLayerSearchEvent event) {
		target.addFeatures(event.getResult());
	}

}
