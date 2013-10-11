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

package org.geomajas.gwt.client.widget.event;

import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.widget.FeatureListGrid;

/**
 * <p>
 * Default handler for the {@link SearchEvent}. It requires a {@link FeatureListGrid}, which it fills up with the
 * features from the event. After the {@link FeatureListGrid} has been filled, the <code>afterSearch</code> method is
 * called. This is an abstract method that you still need to implement.
 * </p>
 * <p>
 * The reason why the <code>afterSearch</code> is abstract, is to have an easy way doing something after the search has
 * completed. For example, the {@link FeatureListGrid} may have been filled with features, but it may not be currently
 * visible. In that case, one could implement the <code>afterSearch</code> method so that is get the
 * {@link FeatureListGrid} visible.
 * </p>
 * 
 * @author Pieter De Graef
 */
public abstract class DefaultSearchHandler implements SearchHandler {

	private FeatureListGrid featureListTable;

	// Constructors:

	/**
	 * Requires a {@link FeatureListGrid} at construction time. Every time a search is executed, this widget fill be
	 * filled with the resulting set of features.
	 */
	public DefaultSearchHandler(FeatureListGrid featureListTable) {
		this.featureListTable = featureListTable;
	}

	// Public methods:

	/**
	 * Executed when a search command returns. Fills the {@link FeatureListGrid}, and then calls the
	 * <code>afterSearch</code> method.
	 */
	public void onSearchDone(SearchEvent event) {
		if (featureListTable != null) {
			featureListTable.setLayer(event.getLayer());
			for (Feature feature : event.getFeatures()) {
				featureListTable.addFeature(feature);
			}
		}
		afterSearch();
	}

	/**
	 * Needs to be implemented! Is called after a search.
	 */
	public abstract void afterSearch();

	// Getters and setters:

	public FeatureListGrid getFeatureListTable() {
		return featureListTable;
	}

	public void setFeatureListTable(FeatureListGrid featureListTable) {
		this.featureListTable = featureListTable;
	}
}
