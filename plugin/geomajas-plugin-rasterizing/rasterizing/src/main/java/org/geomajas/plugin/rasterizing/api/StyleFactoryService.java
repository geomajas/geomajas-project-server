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
package org.geomajas.plugin.rasterizing.api;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerType;
import org.geomajas.layer.VectorLayer;
import org.geomajas.plugin.rasterizing.command.dto.VectorLayerRasterizingInfo;
import org.geotools.styling.Style;

/**
 * Factory for creating Geotools styles based on Geomajas metadata.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 * 
 */
@Api(allMethods = true)
public interface StyleFactoryService {

	/**
	 * Creates a style for the specified vector layer.
	 * 
	 * @param layer
	 *            the layer
	 * @param vectorLayerRasterizingInfo
	 *            the layer metadata
	 * @return a suitable Geotools style
	 * @throws GeomajasException
	 */
	Style createStyle(VectorLayer layer, VectorLayerRasterizingInfo vectorLayerRasterizingInfo)
			throws GeomajasException;

	/**
	 * Creates a style for the specified feature style.
	 * 
	 * @param type
	 *            the type of the layer/feature/geometry
	 * @param featureStyleInfo
	 *            the style dto
	 * @return a suitable Geotools style
	 * @throws GeomajasException
	 */
	Style createStyle(LayerType type, FeatureStyleInfo featureStyleInfo) throws GeomajasException;

}
