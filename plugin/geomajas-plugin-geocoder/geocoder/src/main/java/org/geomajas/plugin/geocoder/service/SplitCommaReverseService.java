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

package org.geomajas.plugin.geocoder.service;

import org.geomajas.global.Api;
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
		for (int pos = remaining.indexOf(","); pos >= 0; pos = remaining.indexOf(",")) {
			res.add(0, remaining.substring(0, pos).trim());
			remaining = remaining.substring(pos + 1);
		}
		res.add(0, remaining.trim());
		return res;
	}

	public String combine(List<String> matchedStrings) {
		StringBuilder sb = new StringBuilder();
		for (int i = matchedStrings.size() - 1; i >= 0; i--) {
			sb.append(matchedStrings.get(i));
			sb.append(", ");
		}
		return sb.substring(0, sb.length() - 2);
	}
}
