/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
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
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.UserStyleInfo;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;

/**
 * Service that converts SLD styles to {@link NamedStyleInfo} styles.
 * 
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public interface StyleConverterService {

	/**
	 * Converts SLD (user) style info to named style info used by vector rendering.
	 * 
	 * @param userStyleInfo SLD user style
	 * @param featureInfo feature information for the layer
	 * @return named style
	 * @throws LayerException oops
	 */
	NamedStyleInfo convert(UserStyleInfo userStyleInfo, FeatureInfo featureInfo) throws LayerException;

	/**
	 * Converts SLD (user) style info to GT style used by legend/raster rendering.
	 * 
	 * @param userStyleInfo the user style
	 * @return the GT style
	 * @throws LayerException oops
	 */
	Style convert(UserStyleInfo userStyleInfo) throws LayerException;

	/**
	 * Converts SLD rule info to GT rule used by legend/raster rendering.
	 * 
	 * @param ruleInfo the rule info
	 * @return the GT rule
	 * @throws LayerException oops
	 */
	Rule convert(RuleInfo ruleInfo) throws LayerException;

	/**
	 * Converts named style info to GT style used by legend/raster rendering.
	 * 
	 * @param namedStyleInfo the named style
	 * @param geometryName the name of the default geometry attribute
	 * @return the GT style
	 * @throws LayerException oops
	 */
	UserStyleInfo convert(NamedStyleInfo namedStyleInfo, String geometryName) throws LayerException;
}
