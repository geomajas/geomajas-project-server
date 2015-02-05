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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Spatial index implementation which does not really index. It never says that something needs to be invalidated.
 * <p/>
 * This is used for the rebuild cache as this does not contain the actual data, only how to get it.
 *
 * @author Joachim Van der Auwera
 */
public class NoInvalidateIndexService implements CacheIndexService {

	private static final List<String> EMPTY_LIST = Collections.unmodifiableList(new ArrayList<String>());

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
		return EMPTY_LIST;
	}
}
