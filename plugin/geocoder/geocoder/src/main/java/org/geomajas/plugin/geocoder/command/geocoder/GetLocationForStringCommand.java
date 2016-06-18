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

package org.geomajas.plugin.geocoder.command.geocoder;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.annotation.Api;
import org.geomajas.command.CommandHasRequest;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Crs;
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
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Geocoder command which allows getting the location and/or area for a string location description.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api
@Component
public class GetLocationForStringCommand
		implements CommandHasRequest<GetLocationForStringRequest, GetLocationForStringResponse> {

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

	@Override
	public GetLocationForStringRequest getEmptyCommandRequest() {
		return new GetLocationForStringRequest();
	}

	@Override
	public GetLocationForStringResponse getEmptyCommandResponse() {
		return new GetLocationForStringResponse();
	}

	@Override
	public void execute(GetLocationForStringRequest request, GetLocationForStringResponse response) throws Exception {
		String location = request.getLocation();
		if (null == location) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "location");
		}
		String crsString = request.getCrs();
		if (null == crsString) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "crs");
		}
		Locale locale = null;
		if (null != request.getLocale()) {
			locale = new Locale(request.getLocale());
		}
		int maxAlternatives = request.getMaxAlternatives();

		Crs crs = geoService.getCrs2(crsString);

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
		Pattern namePattern = getShouldUsePattern(request.getServicePattern());
		for (GeocoderService geocoderService : geocoderInfo.getGeocoderServices()) {
			if (shouldUse(namePattern, geocoderService.getName())) {
				GetLocationResult[] result = geocoderService.getLocation(locationList, maxAlternatives, locale);
				if (null != result && result.length > 0) {
					for (GetLocationResult aResult : result) {
						aResult.setGeocoderName(geocoderService.getName());

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
		}

		response.setLocationFound(false);
		if (!results.isEmpty()) {
			response.setLocationFound(true);

			// combine match strings, default to search string unless we know we can do better
			String matchedLocation = location;
			String geocoderName = null;
			if (results.size() == 1) {
				List<String> matchedStrings = results.get(0).getCanonicalStrings();
				if (null != matchedStrings) {
					matchedLocation = splitGeocoderStringService.combine(matchedStrings);
				}
				geocoderName = results.get(0).getGeocoderName();
			}
			response.setCanonicalLocation(matchedLocation);
			response.setGeocoderName(geocoderName);

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
					if (maxAlternatives > 0 && maxAlternatives <= altList.size()) {
						break;
					}
					GetLocationForStringAlternative one = new GetLocationForStringAlternative();

					String matchedLocation = location;
					List<String> matchedStrings = alt.getCanonicalStrings();
					if (null != matchedStrings) {
						matchedLocation = splitGeocoderStringService.combine(matchedStrings);
					}
					one.setCanonicalLocation(matchedLocation);

					// set additional info data
					one.setGeocoderName(alt.getGeocoderName());
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

	private Pattern getShouldUsePattern(String namePattern) {
		if (null == namePattern) {
			return null;
		}
		if (!namePattern.startsWith("^")) {
			namePattern = "^" + namePattern;
		}
		if (!namePattern.endsWith("$")) {
			namePattern = namePattern + "$";
		}
		if ("^.*$".equals(namePattern)) {
			return null;
		}
		return Pattern.compile(namePattern, Pattern.CASE_INSENSITIVE);
	}

	private boolean shouldUse(Pattern namePattern, String serviceName) {
		return null == namePattern || namePattern.matcher(serviceName).matches();
	}
}