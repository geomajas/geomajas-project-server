/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.geocoder.service;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.geocoder.api.SplitGeocoderStringService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Separate meaningful pieces using comma as separator and assume biggest area is last, this will split "Gaston
 * Crommenlaan, Gent, Be" into ["Be", "Gent", "Gaston Crommenlaan"].
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api
@Component
public class SplitCommaReverseService implements SplitGeocoderStringService {

	public List<String> split(String location) {
		List<String> res = new ArrayList<String>();
		String remaining = location;
		for (int pos = remaining.indexOf(','); pos >= 0; pos = remaining.indexOf(',')) {
			String part = remaining.substring(0, pos).trim();
			if (part.length() > 0) {
				res.add(0, part);
			}
			remaining = remaining.substring(pos + 1);
		}
		remaining = remaining.trim();
		if (remaining.length() > 0) {
			res.add(0, remaining.trim());
		}
		return res;
	}

	public String combine(List<String> matchedStrings) {
		StringBuilder sb = new StringBuilder();
		for (int i = matchedStrings.size() - 1; i >= 0; i--) {
			sb.append(matchedStrings.get(i));
			sb.append(", ");
		}
		if (sb.length() == 0) {
			return "";
		} else {
			return sb.substring(0, sb.length() - 2); // remove trailing ", "
		}
	}
}
