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

package org.geomajas.plugin.rasterizing.step;

import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.plugin.caching.step.CacheContainer;

/**
 * Container for the objects which need to be stored in the rebuild cache.
 *
 * @author Joachim Van der Auwera
 */
public class RebuildCacheContainer extends CacheContainer {

	private static final long serialVersionUID = 100L;

	private TileMetadata metadata;

	public TileMetadata getMetadata() {
		return metadata;
	}

	public void setMetadata(TileMetadata metadata) {
		this.metadata = metadata;
	}
}
