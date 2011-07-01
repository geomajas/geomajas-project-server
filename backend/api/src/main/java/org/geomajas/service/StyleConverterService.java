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

import java.util.List;

import org.geomajas.annotations.Api;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.global.GeomajasException;
import org.geomajas.sld.StyledLayerDescriptorInfo;

/**
 * 
 * @author Jan De Moerloose
 * @since 1.10.0
 * 
 */
@Api(allMethods = true)
public interface StyleConverterService {

	/**
	 * Converts SLD style info to named style info used by vector rendering.
	 * 
	 * @param styledLayerDescriptorInfo styled layer descriptor style
	 * @param layerName optional layer name to pick from SLD
	 * @return list of named styles
	 * @throws GeomajasException oops
	 */
	List<NamedStyleInfo> extractLayerStyle(StyledLayerDescriptorInfo styledLayerDescriptorInfo, String layerName)
			throws GeomajasException;
}
