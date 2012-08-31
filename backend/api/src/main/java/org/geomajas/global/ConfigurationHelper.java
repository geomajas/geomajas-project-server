/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.global;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.layer.LayerException;

/**
 * Helper class that can be used by {@link ConfigurationDtoPostProcess} instances to perform some common configuration
 * post-processing tasks.
 * 
 * @author Jan De Moerloose
 * @since 1.11.1
 * 
 */
@Api(allMethods = true)
public interface ConfigurationHelper {

	/**
	 * Complete the scale information using the given map unit in pixels.
	 * Convert the scale in pixels per unit or relative values, which ever is missing.
	 *
	 * @param scaleInfo scaleInfo object which needs to be completed
	 * @param mapUnitInPixels map unit in pixels
	 */
	void completeScale(ScaleInfo scaleInfo, double mapUnitInPixels);

	/**
	 * Complete the client layer info using the given map CRS and unit in pixels.
	 * @param layer client layer info
	 * @param mapCrs map CRS
	 * @param mapUnitInPixels map unit in pixels
	 * @throws LayerException thrown when the server layer is not found
	 */
	void postProcess(ClientLayerInfo layer, String mapCrs, double mapUnitInPixels) throws LayerException;
}
