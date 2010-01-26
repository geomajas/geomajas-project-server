/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.internal.service;

import java.util.Date;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.attribute.AssociationAttribute;
import org.geomajas.layer.feature.attribute.BooleanAttribute;
import org.geomajas.layer.feature.attribute.CurrencyAttribute;
import org.geomajas.layer.feature.attribute.DateAttribute;
import org.geomajas.layer.feature.attribute.DoubleAttribute;
import org.geomajas.layer.feature.attribute.FloatAttribute;
import org.geomajas.layer.feature.attribute.ImageUrlAttribute;
import org.geomajas.layer.feature.attribute.IntegerAttribute;
import org.geomajas.layer.feature.attribute.LongAttribute;
import org.geomajas.layer.feature.attribute.PrimitiveAttribute;
import org.geomajas.layer.feature.attribute.ShortAttribute;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.layer.feature.attribute.UrlAttribute;
import org.geomajas.service.AttributeConverter;
import org.springframework.stereotype.Component;

/**
 * Converter for attributes between the server-side attribute representation and the DTO version for client-server
 * communication.
 *
 * @author Jan De Moerloose
 */
@Component
public class AttributeConverterImpl implements AttributeConverter {

	private ConvertUtilsBean cub = new ConvertUtilsBean();

	public Object toObject(Attribute attribute) {
		if (attribute instanceof PrimitiveAttribute<?>) {
			return toPrimitiveObject((PrimitiveAttribute<?>) attribute);
		} else if (attribute instanceof AssociationAttribute) {
			return toAssociationObject((AssociationAttribute) attribute);
		} else {
			throw new IllegalArgumentException("AttributeConverter does not support conversion of " + attribute);
		}
	}

	public Attribute toDto(Object object, AttributeInfo info) {
		if (info instanceof PrimitiveAttributeInfo) {
			return toPrimitiveDto(object, (PrimitiveAttributeInfo) info);
		} else if (info instanceof AssociationAttributeInfo) {
			return toAssociationDto(object, (AssociationAttributeInfo) info);
		} else {
			throw new IllegalArgumentException(
					"AttributeConverter does not support conversion of attribute info " + info);
		}
	}

	private Attribute toAssociationDto(Object value, AssociationAttributeInfo associationAttributeInfo) {
		// TODO: implement
		return null;
	}

	private Attribute toPrimitiveDto(Object value, PrimitiveAttributeInfo info) {
		switch (info.getType()) {
			case BOOLEAN:
				return new BooleanAttribute((Boolean) convertToClass(value, Boolean.class));
			case SHORT:
				return new ShortAttribute((Short) convertToClass(value, Short.class));
			case INTEGER:
				return new IntegerAttribute((Integer) convertToClass(value, Integer.class));
			case LONG:
				return new LongAttribute((Long) convertToClass(value, Long.class));
			case FLOAT:
				return new FloatAttribute((Float) convertToClass(value, Float.class));
			case DOUBLE:
				return new DoubleAttribute((Double) convertToClass(value, Double.class));
			case CURRENCY:
				return new CurrencyAttribute((String) convertToClass(value, String.class));
			case STRING:
				return new StringAttribute(value == null ? null : value.toString());
			case DATE:
				return new DateAttribute((Date) value);
			case URL:
				return new UrlAttribute((String) value);
			case IMGURL:
				return new ImageUrlAttribute((String) value);
		}
		throw new IllegalArgumentException("Cannot create primitive attribute of type " + info);
	}

	private Object toAssociationObject(AssociationAttribute primitiveAttribute) {
		// TODO: implement
		return null;
	}

	private Object toPrimitiveObject(PrimitiveAttribute<?> primitive) {
		return primitive.getValue();
	}

	private Object convertToClass(Object object, Class<?> c) {
		if (object == null) {
			return null;
		}
		if (c.isInstance(object)) {
			return object;
		} else {
			return cub.convert(object.toString(), c);
		}
	}

}