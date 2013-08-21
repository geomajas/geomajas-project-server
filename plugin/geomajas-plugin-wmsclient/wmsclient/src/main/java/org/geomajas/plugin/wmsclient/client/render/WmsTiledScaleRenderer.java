/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.wmsclient.client.render;

import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.gwt.client.map.render.LayerScaleRenderer;
import org.geomajas.layer.tile.RasterTile;

/**
 * Renderer for a fixed scale of a WMS layer.
 * 
 * @author Pieter De Graef
 */
public interface WmsTiledScaleRenderer extends LayerScaleRenderer {

	List<RasterTile> getTiles(Bbox worldBounds);
}