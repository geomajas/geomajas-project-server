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
package org.geomajas.plugin.rasterizing.tms;

import org.geomajas.plugin.rasterizing.layer.tile.TmsTileMetadata;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

/**
 * A TMS profile.
 * 
 * @author Jan De Moerloose
 *
 */
public interface TmsProfile {

	ProfileType getProfile();

	void prepareMetadata(TmsTileMetadata metadata);

	Envelope getBounds();

	Coordinate getOrigin();

	int getTileWidth();

	int getTileHeight();

	double[] getResolutions();
}
