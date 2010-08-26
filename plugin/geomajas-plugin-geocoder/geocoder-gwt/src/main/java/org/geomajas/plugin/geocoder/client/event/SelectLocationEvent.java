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

package org.geomajas.plugin.geocoder.client.event;

import com.google.gwt.event.shared.GwtEvent;
import org.geomajas.configuration.client.ClientUserDataInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.global.Api;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringAlternative;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringResponse;

/**
 * Event which is used when a location was selected, This can occur either because the geocoder returned a match or
 * because one of the alternatives was selected by the user.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api
public class SelectLocationEvent extends GwtEvent<SelectLocationHandler> {

	private MapWidget mapWidget;
	private String canonicalLocation;
	private Coordinate center;
	private Bbox bbox;
	private String geocoderName;
	private ClientUserDataInfo userData;

	@SuppressWarnings("unchecked")
	public Type getAssociatedType() {
		return SelectLocationHandler.TYPE;
	}

	protected void dispatch(SelectLocationHandler handler) {
		handler.onSelectLocation(this);
	}

	public SelectLocationEvent(MapWidget mapWidget, GetLocationForStringAlternative alternative) {
		this.mapWidget = mapWidget;
		canonicalLocation = alternative.getCanonicalLocation();
		center = alternative.getCenter();
		bbox = alternative.getBbox();
		geocoderName = alternative.getGeocoderName();
		userData = alternative.getUserData();
	}

	public SelectLocationEvent(MapWidget mapWidget, GetLocationForStringResponse alternative) {
		this.mapWidget = mapWidget;
		canonicalLocation = alternative.getCanonicalLocation();
		center = alternative.getCenter();
		bbox = alternative.getBbox();
		geocoderName = alternative.getGeocoderName();
		userData = alternative.getUserData();
	}

	/**
	 * Get map widget the geocoder applies to.
	 *
	 * @return map widget
	 */
	@Api
	public MapWidget getMapWidget() {
		return mapWidget;
	}

	/**
	 * Get string preferred description of matched location.
	 *
	 * @return preferred string for searching this location
	 */
	@Api
	public String getCanonicalLocation() {
		return canonicalLocation;
	}

	/**
	 * Get center of the located area.
	 *
	 * @return center of area
	 */
	@Api
	public Coordinate getCenter() {
		return center;
	}

	/**
	 * Get bounding box for the located area.
	 *
	 * @return located area
	 */
	@Api
	public Bbox getBbox() {
		return bbox;
	}

	/**
	 * Name of the geocoder service which produced this result.
	 * <p/>
	 * This is filled in automatically by the command.
	 *
	 * @return name of the geocoder service which produced this result
	 */
	@Api
	public String getGeocoderName() {
		return geocoderName;
	}

	/**
	 * Get extra user data for the geocoder search result. Only set when there was only one results from the list of
	 * geocoder services.
	 *
	 * @return user data
	 */
	@Api
	public ClientUserDataInfo getUserData() {
		return userData;
	}
}
