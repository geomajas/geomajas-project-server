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
package org.geomajas.gwt.client.util;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.configuration.AbstractAttributeInfo;
import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.attribute.AssociationAttribute;
import org.geomajas.layer.feature.attribute.AssociationValue;
import org.geomajas.layer.feature.attribute.BooleanAttribute;
import org.geomajas.layer.feature.attribute.CurrencyAttribute;
import org.geomajas.layer.feature.attribute.DateAttribute;
import org.geomajas.layer.feature.attribute.DoubleAttribute;
import org.geomajas.layer.feature.attribute.FloatAttribute;
import org.geomajas.layer.feature.attribute.ImageUrlAttribute;
import org.geomajas.layer.feature.attribute.IntegerAttribute;
import org.geomajas.layer.feature.attribute.LongAttribute;
import org.geomajas.layer.feature.attribute.ManyToOneAttribute;
import org.geomajas.layer.feature.attribute.OneToManyAttribute;
import org.geomajas.layer.feature.attribute.PrimitiveAttribute;
import org.geomajas.layer.feature.attribute.ShortAttribute;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.layer.feature.attribute.UrlAttribute;

/**
 * Utility for attribute manipulations.
 * 
 * @author Jan De Moerloose
 * @since 1.9.0
 */
public final class AttributeUtil {

	private AttributeUtil() {
	}

	/**
	 * Creates an empty association value. An empty association value is a value for which the attributes and identifier
	 * are empty.
	 * 
	 * @param attributeInfo attribute info
	 * @return an empty value
	 */
	public static AssociationValue createEmptyAssociationValue(AssociationAttributeInfo attributeInfo) {
		AssociationValue value = new AssociationValue();
		Map<String, Attribute<?>> attributes = new HashMap<String, Attribute<?>>();
		for (AbstractAttributeInfo attrInfo : attributeInfo.getFeature().getAttributes()) {
			attributes.put(attrInfo.getName(), createEmptyAttribute(attrInfo));
		}
		value.setAllAttributes(attributes);
		value.setId(createEmptyPrimitiveAttribute(attributeInfo.getFeature().getIdentifier()));
		return value;
	}

	/**
	 * Creates an empty attribute. An empty attribute is an attribute which has null as its value.
	 * 
	 * @param attrInfo attribute info
	 * @return an empty attribute.
	 */
	public static Attribute<?> createEmptyAttribute(AbstractAttributeInfo attrInfo) {
		if (attrInfo instanceof PrimitiveAttributeInfo) {
			return createEmptyPrimitiveAttribute((PrimitiveAttributeInfo) attrInfo);
		} else if (attrInfo instanceof AssociationAttributeInfo) {
			return createEmptyAssociationAttribute((AssociationAttributeInfo) attrInfo);
		}
		return null;
	}

	/**
	 * Creates an empty primitive attribute. An empty attribute is an attribute which has null as its value.
	 * 
	 * @param info attribute info
	 * @return an empty primitive attribute of the right type.
	 */
	public static PrimitiveAttribute<?> createEmptyPrimitiveAttribute(PrimitiveAttributeInfo info) {
		PrimitiveAttribute<?> attribute;
		switch (info.getType()) {
			case BOOLEAN:
				attribute = new BooleanAttribute();
				break;
			case SHORT:
				attribute = new ShortAttribute();
				break;
			case INTEGER:
				attribute = new IntegerAttribute();
				break;
			case LONG:
				attribute = new LongAttribute();
				break;
			case FLOAT:
				attribute = new FloatAttribute();
				break;
			case DOUBLE:
				attribute = new DoubleAttribute();
				break;
			case CURRENCY:
				attribute = new CurrencyAttribute();
				break;
			case STRING:
				attribute = new StringAttribute();
				break;
			case DATE:
				attribute = new DateAttribute();
				break;
			case URL:
				attribute = new UrlAttribute();
				break;
			case IMGURL:
				attribute = new ImageUrlAttribute();
				break;
			default:
				String msg = "Trying to build an unknown primitive attribute type " + info.getType();
				Log.logError(msg);
				throw new IllegalStateException(msg);
		}
		attribute.setEditable(info.isEditable());
		return attribute;
	}

	/**
	 * Creates an empty association attribute. An empty attribute is an attribute which has null as its value.
	 * 
	 * @param info attribute info
	 * @return an empty association attribute of the right type.
	 */
	public static AssociationAttribute<?> createEmptyAssociationAttribute(AssociationAttributeInfo info) {
		AssociationAttribute<?> association;
		switch (info.getType()) {
			case MANY_TO_ONE:
				association = new ManyToOneAttribute();
				break;
			case ONE_TO_MANY:
				association = new OneToManyAttribute();
				break;
			default:
				String msg = "Trying to build an unknown association attribute type " + info.getType();
				Log.logError(msg);
				throw new IllegalStateException(msg);
		}
		association.setEditable(info.isEditable());
		return association;
	}
}
