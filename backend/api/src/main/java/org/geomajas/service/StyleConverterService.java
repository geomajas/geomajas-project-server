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

package org.geomajas.service;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.layer.LayerException;
import org.geomajas.sld.StyledLayerDescriptorInfo;

/**
 * Service that converts SLD styles to {@link NamedStyleInfo} styles.
 * 
 * @author Jan De Moerloose
 * @since 1.10.0
 * 
 */
@Api(allMethods = true)
public interface StyleConverterService {

	/**
	 * Converts SLD (user) style info to named style info used by vector rendering.
	 * 
	 * @param styledLayerDescriptorInfo styled layer descriptor style
	 * @param featureInfo feature information for the layer
	 * @param layerName (optional) NamedLayer name to pick from SLD
	 * @param styleName (optional) UserStyle name to pick from SLD
	 * @return named style
	 * @throws LayerException oops
	 */
	NamedStyleInfo convert(StyledLayerDescriptorInfo styledLayerDescriptorInfo, FeatureInfo featureInfo,
			String layerName, String styleName) throws LayerException;
}
