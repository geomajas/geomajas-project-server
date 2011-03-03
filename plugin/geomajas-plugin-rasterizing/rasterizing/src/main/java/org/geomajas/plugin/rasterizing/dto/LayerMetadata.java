/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing.dto;

/**
 * Base interface for rasterizing metadata classes. All renderable objects are considered to be layers: vector layers,
 * raster layers as well as random geometries or map addons (scalebar, watermark, copyright).
 * 
 * @author Jan De Moerloose
 * 
 */
public interface LayerMetadata {

	/**
	 * Returns the layer id.
	 * 
	 * @return
	 */
	String getLayerId();
}
