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

package org.geomajas.plugin.caching.service;

import com.vividsolutions.jts.geom.Envelope;

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

	public void put(String key, Envelope envelope) {
		set.add(key);
	}

	public void remove(String key) {
		set.remove(key);
	}

	public void clear() {
		set.clear();
	}

	public void drop() {
		clear();
	}

	public List<String> getOverlappingKeys(Envelope envelope) {
		List<String> res = new ArrayList<String>();
		res.addAll(set);
		return res;
	}
}
