/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.caching.index;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.plugin.caching.service.CacheIndexService;

import java.util.List;

/**
 * Spatial index implementation which does not really index. When something needs to be invalidated, everything
 * is invalidated.
 *
 * @author Joachim Van der Auwera
 */
public class NoCacheIndexService implements CacheIndexService {

	@Override
	public void put(String key, Envelope envelope) {
		// nothing to do
	}

	@Override
	public void remove(String key) {
		// nothing to do
	}

	@Override
	public void clear() {
		// nothing to do
	}

	@Override
	public void drop() {
		// nothing to do
	}

	@Override
	public List<String> getOverlappingKeys(Envelope envelope) {
		return ALL_KEYS;
	}
}
