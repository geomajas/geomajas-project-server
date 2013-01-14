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

package org.geomajas.plugin.geocoder.service;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.annotation.Api;
import org.geomajas.plugin.geocoder.api.CombineResultService;
import org.geomajas.plugin.geocoder.api.GetLocationResult;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Combine locations by taking the union of the areas.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api
@Component
public class CombineUnionService implements CombineResultService {
	public Envelope combine(List<GetLocationResult> results) {
		Envelope result = null;
		for (GetLocationResult add : results) {
			Envelope envelope = add.getEnvelope();
			if (null == result) {
				result = envelope;
			} else {
				result.expandToInclude(envelope);
			}
		}
		return result;
	}
}
