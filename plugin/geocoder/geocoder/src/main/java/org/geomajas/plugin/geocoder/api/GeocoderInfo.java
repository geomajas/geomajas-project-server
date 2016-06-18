/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.geocoder.api;

import org.geomajas.annotation.Api;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Configuration for the geocoder command.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public class GeocoderInfo implements Serializable {

	private static final long serialVersionUID = 100L;

	private SplitGeocoderStringService splitGeocoderStringService;

	private CombineResultService combineResultService;

	@NotNull
	private List<GeocoderService> geocoderServices;

	private boolean loopAllServices;

	private double pointDisplayWidth = 2000.0;

	private double pointDisplayHeight = 1000.0;

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
	 * @return geocoder combine result service instance
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

	/**
	 * Get width in meters of area which needs to be displayed around a point.
	 *
	 * @return area width
	 */
	public double getPointDisplayWidth() {
		return pointDisplayWidth;
	}

	/**
	 * Set width in meters of area which needs to be displayed around a point.
	 *
	 * @param pointDisplayWidth area width
	 */
	public void setPointDisplayWidth(double pointDisplayWidth) {
		this.pointDisplayWidth = pointDisplayWidth;
	}

	/**
	 * Get height in meters of area which needs to be displayed around a point.
	 *
	 * @return area height
	 */
	public double getPointDisplayHeight() {
		return pointDisplayHeight;
	}

	/**
	 * Set width in meters of area which needs to be displayed around a point.
	 *
	 * @param pointDisplayHeight area height
	 */
	public void setPointDisplayHeight(double pointDisplayHeight) {
		this.pointDisplayHeight = pointDisplayHeight;
	}
}
