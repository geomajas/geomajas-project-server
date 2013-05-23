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

package org.geomajas.internal.layer.feature;

import org.geomajas.configuration.SyntheticAttributeInfo;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.feature.SyntheticAttributeBuilder;
import org.geomajas.layer.feature.attribute.StringAttribute;

/**
 * Sample SyntheticAttributeBuilder for testing.
 *
 * @author Joachim Van der Auwera
 */
public class NameDateAttributeBuilder implements SyntheticAttributeBuilder<String> {

	@Override
	public Attribute<String> getAttribute(SyntheticAttributeInfo info, InternalFeature feature) {
		StringAttribute nameAttribute = ((StringAttribute) feature.getAttributes().get("name"));
		return new StringAttribute(nameAttribute.getValue() + " 1984-12-25");
	}
}
