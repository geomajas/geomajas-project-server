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
package org.geomajas.plugin.rasterizing.tms;

import java.io.Writer;

import org.geomajas.global.GeomajasException;

/**
 * The TMS service is responsible for publishing the TMS metadata (vector layers only!). See
 * {@link org.geomajas.plugin.rasterizing.mvc.TmsController} for the actual rendering of the tiles.
 * 
 * @author Jan De Moerloose
 *
 */
public interface TmsService {

	/**
	 * Write the root service with all layers.
	 * 
	 * @param writer writer to which xml will be written
	 * @throws GeomajasException
	 */
	void writeService(Writer writer) throws GeomajasException;

	/**
	 * Write all levels of a layer. The local profile is based on the CRS and maximum extent of the layer.
	 * 
	 * @param layerId the layer id
	 * @param styleRef the style id
	 * @param profile the profile type
	 * @param writer writer to which xml will be written
	 * @throws GeomajasException
	 */
	void writeTileMap(String layerId, String styleRef, ProfileType profile, Writer writer) throws GeomajasException;

}
