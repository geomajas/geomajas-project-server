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

package org.geomajas.plugin.caching.service;

import com.vividsolutions.jts.geom.Geometry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Dummy spatial index for testing. This is actually not spatial at all.
 *
 * @author Joachim Van der Auwera
 */
public class DummyCacheIndexService implements CacheIndexService {

	Set<String> set = new HashSet<String>();

	public void put(String key, Geometry geometry) {
		set.add(key);
	}

	public void remove(String key) {
		set.remove(key);
	}

	public void drop() {
		set.clear();
	}

	public List<String> getOverlappingKeys(Geometry geometry) {
		List<String> res = new ArrayList<String>();
		res.addAll(set);
		return res;
	}
}
