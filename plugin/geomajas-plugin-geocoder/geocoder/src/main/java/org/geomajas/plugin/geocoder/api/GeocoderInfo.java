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

package org.geomajas.plugin.geocoder.api;

import org.geomajas.global.Api;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Configuration for the geocoder command.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public class GeocoderInfo {

	private static final long serialVersionUID = 100L;

	private SplitGeocoderStringService splitGeocoderStringService;

	private CombineResultService combineResultService;

	@NotNull
	private List<GeocoderService> geocoderServices;

	private boolean loopAllServices;

	/**
	 * Get the service which is used to split the geocoder request string.
	 *
	 * @return geocoder split service instance
	 */
	public SplitGeocoderStringService getSplitGeocoderStringService() {
		return splitGeocoderStringService;
	}

	/**
	 * Set the service instance which is responsible for splitting the geocoder request string.
	 *
	 * @param splitGeocoderStringService service instance
	 */
	public void setSplitGeocoderStringService(SplitGeocoderStringService splitGeocoderStringService) {
		this.splitGeocoderStringService = splitGeocoderStringService;
	}

	/**
	 * Get the service which is used to combine the results of the geocoder request.
	 *
	 * @returngeocoder combine result service instance
	 */
	public CombineResultService getCombineResultService() {
		return combineResultService;
	}

	/**
	 * Set the service instance which is responsible  for combining the results of the geocoder services.
	 *
	 * @param combineResultService service instance
	 */
	public void setCombineResultService(CombineResultService combineResultService) {
		this.combineResultService = combineResultService;
	}

	/**
	 * Get the geocoder services which should be used to convert the request to a location.
	 *
	 * @return geocoder service instances
	 */
	public List<GeocoderService> getGeocoderServices() {
		return geocoderServices;
	}

	/**
	 * Set the geocoder services which should be used to convert the request to a location.
	 *
	 * @param geocoderServices list of geocoder service instances
	 */
	public void setGeocoderServices(List<GeocoderService> geocoderServices) {
		this.geocoderServices = geocoderServices;
	}

	/**
	 * Should all geocoder {@link GeocoderService}services try to find a location or should the first match be returned?
	 *
	 * @return check all geocoder services or stop after first result
	 */
	public boolean isLoopAllServices() {
		return loopAllServices;
	}

	/**
	 * Set whether all geocoder {@link org.geomajas.plugin.geocoder.api.GeocoderService} services should have a
	 * chance to determine the location and those should be combined, or the result from the first service which
	 * produces a match should be returned.
	 *
	 * @param loopAllServices new status
	 */
	public void setLoopAllServices(boolean loopAllServices) {
		this.loopAllServices = loopAllServices;
	}
}
