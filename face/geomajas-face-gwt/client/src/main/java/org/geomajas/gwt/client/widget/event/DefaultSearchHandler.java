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
