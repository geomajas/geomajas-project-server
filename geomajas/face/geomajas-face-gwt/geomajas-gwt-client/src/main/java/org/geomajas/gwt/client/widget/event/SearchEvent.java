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
