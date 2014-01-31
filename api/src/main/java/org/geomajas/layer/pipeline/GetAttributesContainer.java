/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.pipeline;

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.layer.feature.Attribute;

/**
 * Container for result of getAttributes in {@link org.geomajas.layer.VectorLayerService}.
 *
 * @author Joachim Van der Auwera
 * @since 1.8.0
 */
@Api(allMethods = true)
public class GetAttributesContainer {

	private List<Attribute<?>> attributes;

	/**
	 * Get the attributes or null when not yet determined.
	 *
	 * @return list of attributes
	 */
	public List<Attribute<?>> getAttributes() {
		return attributes;
	}

	/**
	 * Seth the attributes.
	 *
	 * @param attributes attributes
	 */
	public void setAttributes(List<Attribute<?>> attributes) {
		this.attributes = attributes;
	}
}
