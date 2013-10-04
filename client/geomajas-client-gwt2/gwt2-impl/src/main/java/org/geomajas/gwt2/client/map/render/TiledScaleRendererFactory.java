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

package org.geomajas.gwt2.client.map.render;

import org.geomajas.gwt2.client.gfx.HtmlContainer;
import org.geomajas.gwt2.client.map.layer.RasterServerLayer;
import org.geomajas.gwt2.client.map.layer.VectorServerLayer;

import com.google.inject.name.Named;

/**
 * Gin factory for {@link LayerScaleRenderer}.
 * 
 * @author Jan De Moerloose
 */
public interface TiledScaleRendererFactory {

	String RASTER_NAME = "raster";

	String VECTOR_NAME = "vector";

	@Named(RASTER_NAME)
	LayerScaleRenderer create(Object eventSource, String crs, RasterServerLayer layer, HtmlContainer container,
			double scale);

	@Named(VECTOR_NAME)
	LayerScaleRenderer create(Object eventSource, String crs, VectorServerLayer layer, HtmlContainer container,
			double scale);
}
