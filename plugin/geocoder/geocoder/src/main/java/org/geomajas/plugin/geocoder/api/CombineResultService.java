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

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

import java.util.List;

/**
 * Combine the results for the successful geocoder services.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
@UserImplemented
// @extract-start CombineResultService, Service to combine search results
public interface CombineResultService {

	/**
	 * Combine the envelopes from the match results into the end result.
	 *
	 * @param results results which need to be combined
	 * @return result envelope
	 */
	Envelope combine(List<GetLocationResult> results);
}
// @extract-end
