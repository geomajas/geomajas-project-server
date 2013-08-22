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

package org.geomajas.plugin.geocoder.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ClientUserDataInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.MapPresenter;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringAlternative;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringResponse;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event which is used when a location was selected, This can occur either because the geocoder returned a match or
 * because one of the alternatives was selected by the user.
 *
 * @author Joachim Van der Auwera
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api
public class SelectLocationEvent extends GwtEvent<SelectLocationHandler> {

	private MapPresenter mapPresenter;
	private String canonicalLocation;
	private Coordinate center;
	private Bbox bbox;
	private String geocoderName;
	private ClientUserDataInfo userData;

	@Override
	public Type<SelectLocationHandler> getAssociatedType() {
		return SelectLocationHandler.TYPE;
	}

	@Override
	protected void dispatch(SelectLocationHandler handler) {
		handler.onSelectLocation(this);
	}

	public SelectLocationEvent(MapPresenter mapPresenter, GetLocationForStringAlternative alternative) {
		super();
		this.mapPresenter = mapPresenter;
		canonicalLocation = alternative.getCanonicalLocation();
		center = alternative.getCenter();
		bbox = alternative.getBbox();
		geocoderName = alternative.getGeocoderName();
		userData = alternative.getUserData();
	}

	public SelectLocationEvent(MapPresenter mapPresenter, GetLocationForStringResponse alternative) {
		super();
		this.mapPresenter = mapPresenter;
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
	public MapPresenter getMapWidget() {
		return mapPresenter;
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