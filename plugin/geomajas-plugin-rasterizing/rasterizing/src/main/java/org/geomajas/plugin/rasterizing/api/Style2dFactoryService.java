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

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.LabelStyleInfo;
import org.geomajas.global.Api;
import org.geomajas.layer.LayerType;
import org.geotools.renderer.style.Style2D;

/**
 * <p>
 * Factory that creates <code>Style2D</code> objects from a configuration
 * <code>StyleInfo</code> object.
 * </p>
 *
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface Style2dFactoryService {

	/**
	 * Create a Style2D object, given an XML StyleInfo object and the layer
	 * type. Depending on the layer type, a <code>LineStyle2D</code> or
	 * <code>PolygonStyle2D</code> will be returned.
	 *
	 * @param styleInfo The style configuration object.
	 * @param layerType The layer type.
	 * @return Returns a GeoTools style object.
	 */
	Style2D createStyle(FeatureStyleInfo styleInfo, LayerType layerType);

	/**
	 * Create a label style object based on the label style configuration info.
	 * 
	 * @param styleInfo label style info
	 * @return the label style
	 */
	LabelStyle createLabelStyle(LabelStyleInfo styleInfo);

}