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

package org.geomajas.internal.layer.vector.lazy;

import java.util.Date;

import org.geomajas.configuration.PrimitiveType;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lazy variant of a {@link PrimitiveAttribute}. The value is only converted at first use, not at instantiation.
 *
 * @param <VALUE_TYPE> type of primitive value
 *
 * @author Joachim Van der Auwera
 */
public class LazyPrimitiveAttribute<VALUE_TYPE> extends PrimitiveAttribute<VALUE_TYPE>
		implements LazyAttribute<VALUE_TYPE> {

	private static final long serialVersionUID = 190L;

	private final FeatureModel featureModel;
	private final Object pojo;
	private final String name;
	private boolean gotValue;

	public LazyPrimitiveAttribute(PrimitiveType type, FeatureModel featureModel, Object pojo, String attribute) {
		super(type);
		this.featureModel = featureModel;
		this.pojo = pojo;
		this.name = attribute;
	}

	@SuppressWarnings("unchecked")
	public Attribute<VALUE_TYPE> instantiate() {
		PrimitiveAttribute result;
		Object value = getValue();
		switch (getType()) {
			case BOOLEAN:
				result = new BooleanAttribute((Boolean) value);
				break;
			case SHORT:
				result = new ShortAttribute((Short) value);
				break;
			case INTEGER:
				result = new IntegerAttribute((Integer) value);
				break;
			case LONG:
				result = new LongAttribute((Long) value);
				break;
			case FLOAT:
				result = new FloatAttribute((Float) value);
				break;
			case DOUBLE:
				result = new DoubleAttribute((Double) value);
				break;
			case CURRENCY:
				result = new CurrencyAttribute((String) value);
				break;
			case STRING:
				result = new StringAttribute(value == null ? null : value.toString());
				break;
			case DATE:
				result = new DateAttribute((Date) value);
				break;
			case URL:
				result = new UrlAttribute((String) value);
				break;
			case IMGURL:
				result = new ImageUrlAttribute((String) value);
				break;
			default:
				throw new IllegalArgumentException("Cannot create primitive attribute " + name + " of type " +
						getType());
		}
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public VALUE_TYPE getValue() {
		if (!gotValue) {
			try {
				Attribute<VALUE_TYPE> attribute = featureModel.getAttribute(pojo, name);
				super.setValue(attribute.getValue());
			} catch (LayerException le) {
				Logger log = LoggerFactory.getLogger(LazyPrimitiveAttribute.class);
				log.error("Could not lazily get attribute " + name, le);
			}
		}
		return super.getValue();
	}

	@Override
	public void setValue(VALUE_TYPE value) {
		gotValue = true;
		super.setValue(value);
	}

	@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "CN_IDIOM_NO_SUPER_CALL",
			justification = "needed for GWT")
	@Override
	public LazyPrimitiveAttribute<VALUE_TYPE> clone() { // NOSONAR super.clone() not supported by GWT
		return new LazyPrimitiveAttribute<VALUE_TYPE>(getType(), featureModel, pojo, name);
	}
}
