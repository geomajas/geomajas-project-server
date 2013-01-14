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

import java.util.List;

import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.VectorLayer;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event that reports when a search command has been successfully executed. This event will contains the resulting set
 * of features.
 * 
 * @author Pieter De Graef
 */
public class SearchEvent extends GwtEvent<SearchHandler> {

	public static final Type<SearchHandler> TYPE = new Type<SearchHandler>();

	private VectorLayer layer;

	private List<Feature> features;

	public SearchEvent(VectorLayer layer, List<Feature> features) {
		this.layer = layer;
		this.features = features;
	}

	@SuppressWarnings("unchecked")
	public Type getAssociatedType() {
		return TYPE;
	}

	protected void dispatch(SearchHandler searchHandler) {
		searchHandler.onSearchDone(this);
	}

	/**
	 * Return the vector layer in which the search took place.
	 * 
	 * @return
	 */
	public VectorLayer getLayer() {
		return layer;
	}

	/**
	 * Return the list of features that were found during the search.
	 * 
	 * @return
	 */
	public List<Feature> getFeatures() {
		return features;
	}
}
