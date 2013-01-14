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
package org.geomajas.layer.entity;

import java.util.Map;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.Attribute;

/**
 * Service that merges a map of attributes with a feature or association attribute object. The
 * {@link #setAttributes(Object, FeatureInfo, EntityMapper, Map)} operation makes use of a custom {@link EntityMapper}
 * that has be implemented by each layer that wants to make use of this service. The service also provides a
 * {@link #getAttribute(Object, FeatureInfo, EntityMapper, String)} method to recursively extract attributes.
 * 
 * @author Jan De Moerloose
 * @since 1.9.0
 * 
 */
@Api(allMethods = true)
public interface EntityAttributeService {

	/**
	 * Merges the specified attribute map with this layer object. This means that the complete attribute graph is merged
	 * into the object's entity graph, overriding existing attributes where applicable and creating new ones as well.
	 * The resulting objects should be ready to pass to the layer for persistence.
	 * 
	 * @param object the layer-specific object
	 * @param mapper the entity mapper
	 * @param attributes attribute map
	 * @param featureInfo the feature information of the object
	 * @throws LayerException oops
	 */
	void setAttributes(Object object, FeatureInfo featureInfo, EntityMapper mapper, Map<String, 
			Attribute<?>> attributes) throws LayerException;

	/**
	 * Recursively gets the attribute from the specified feature.
	 * <p/>
	 * Only works on real attributes, not on synthetic attributes!
	 * 
	 * @param feature the internal feature object
	 * @param featureInfo the feature information object
	 * @param mapper the entity mapper
	 * @param name the name (may be recursive using . or /)
	 * @return the attribute
	 * @throws LayerException oops
	 */
	Attribute<?> getAttribute(Object feature, FeatureInfo featureInfo, EntityMapper mapper, String name)
			throws LayerException;
	
}
