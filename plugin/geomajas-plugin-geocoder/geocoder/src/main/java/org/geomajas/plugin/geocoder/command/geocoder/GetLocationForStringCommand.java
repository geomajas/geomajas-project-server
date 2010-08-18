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

package org.geomajas.plugin.geocoder.command.geocoder;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.command.Command;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.global.Api;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.geocoder.api.CombineResultService;
import org.geomajas.plugin.geocoder.api.GeocoderInfo;
import org.geomajas.plugin.geocoder.api.GeocoderService;
import org.geomajas.plugin.geocoder.api.GetLocationResult;
import org.geomajas.plugin.geocoder.api.SplitGeocoderStringService;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringAlternative;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringRequest;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringResponse;
import org.geomajas.plugin.geocoder.service.CombineUnionService;
import org.geomajas.plugin.geocoder.service.GeocoderUtilService;
import org.geomajas.plugin.geocoder.service.SplitCommaReverseService;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Geocoder command which allows getting the location and/or area for a string location description.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api
@Component
public class GetLocationForStringCommand implements Command<GetLocationForStringRequest, GetLocationForStringResponse> {

	@Autowired
	private GeocoderInfo geocoderInfo;

	@Autowired
	private SplitCommaReverseService defaultSplitGeocoderStringService;

	@Autowired
	private CombineUnionService defaultCombineResultService;

	@Autowired
	private DtoConverterService dtoConverterService;

	@Autowired
	private GeocoderUtilService geocoderUtilService;

	@Autowired
	private GeoService geoService;

	public GetLocationForStringResponse getEmptyCommandResponse() {
		return new GetLocationForStringResponse();
	}

	public void execute(GetLocationForStringRequest request, GetLocationForStringResponse response) throws Exception {
		String location = request.getLocation();
		if (null == location) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "location");
		}
		String crsString = request.getCrs();
		if (null == crsString) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "location");
		}

		CoordinateReferenceSystem crs = geoService.getCrs(crsString);

		SplitGeocoderStringService splitGeocoderStringService = geocoderInfo.getSplitGeocoderStringService();
		if (null == splitGeocoderStringService) {
			splitGeocoderStringService = defaultSplitGeocoderStringService;
		}
		CombineResultService combineResultService = geocoderInfo.getCombineResultService();
		if (null == combineResultService) {
			combineResultService = defaultCombineResultService;
		}

		List<String> locationList = splitGeocoderStringService.split(location);

		List<GetLocationResult> results = new ArrayList<GetLocationResult>();
		List<GetLocationResult[]> alternatives = new ArrayList<GetLocationResult[]>();
		for (GeocoderService geocoderService : geocoderInfo.getGeocoderServices()) {
			GetLocationResult[] result = geocoderService.getLocation(locationList);
			if (null != result && result.length > 0) {
				for (GetLocationResult aResult : result) {
					CoordinateReferenceSystem sourceCrs = geocoderService.getCrs();
					Envelope envelope = aResult.getEnvelope();

					// point locations needs to converted to an area based on configuration settings
					if (null == envelope) {
						envelope = geocoderUtilService.extendPoint(aResult.getCoordinate(), sourceCrs,
								geocoderInfo.getPointDisplayWidth(), geocoderInfo.getPointDisplayHeight());
					}

					// result needs to be CRS transformed to request CRS
					aResult.setEnvelope(geocoderUtilService.transform(envelope, sourceCrs, crs));
				}
				if (result.length > 1) {
					alternatives.add(result);
				} else {
					results.add(result[0]);
				}
				if (!geocoderInfo.isLoopAllServices()) {
					break;
				}
			}
		}

		response.setLocationFound(false);
		if (!results.isEmpty()) {
			response.setLocationFound(true);

			// combine match strings, default to search string unless we know we can do better
			String matchedLocation = location;
			if (results.size() == 1) {
				List<String> matchedStrings = results.get(0).getCanonicalStrings();
				if (null != matchedStrings) {
					matchedLocation = splitGeocoderStringService.combine(matchedStrings);
				}
			}
			response.setCanonicalLocation(matchedLocation);

			// combine the user data, only when there is just one result
			if (results.size() == 1) {
				response.setUserData(results.get(0).getUserData());
			}

			// combine location envelopes
			Envelope resultEnvelope = combineResultService.combine(results);
			Bbox bbox = dtoConverterService.toDto(resultEnvelope);
			response.setBbox(bbox);
			response.setCenter(new Coordinate(bbox.getX() + bbox.getWidth() / 2, bbox.getY() + bbox.getHeight() / 2));
		} else {
			List<GetLocationForStringAlternative> altList = new ArrayList<GetLocationForStringAlternative>();
			response.setAlternatives(altList);
			for (GetLocationResult[] altArr : alternatives) {
				for (GetLocationResult alt : altArr) {
					GetLocationForStringAlternative one = new GetLocationForStringAlternative();

					String matchedLocation = location;
					List<String> matchedStrings = alt.getCanonicalStrings();
					if (null != matchedStrings) {
						matchedLocation = splitGeocoderStringService.combine(matchedStrings);
					}
					one.setCanonicalLocation(matchedLocation);

					// set the user data
					one.setUserData(alt.getUserData());

					// combine location envelopes
					Bbox bbox = dtoConverterService.toDto(alt.getEnvelope());
					one.setBbox(bbox);
					one.setCenter(
							new Coordinate(bbox.getX() + bbox.getWidth() / 2, bbox.getY() + bbox.getHeight() / 2));

					altList.add(one);
				}
			}
		}

	}
}
