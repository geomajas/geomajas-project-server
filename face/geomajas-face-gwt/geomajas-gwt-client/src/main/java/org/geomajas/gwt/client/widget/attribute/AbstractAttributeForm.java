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
package org.geomajas.gwt.client.widget.attribute;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.layer.feature.Attribute;
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

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.fields.TextItem;

/**
 * Abstract definition for a "form" that display the attributes of a feature. Different implementations may or may not
 * support editing of these attributes.
 * 
 * @author Jan De Moerloose
 * @author Pieter De Graef
 */
public class AbstractAttributeForm {

	protected DynamicForm form;

	protected Map<String, AttributeInfo> infos = new HashMap<String, AttributeInfo>();

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Package visible constructor. Implementations are to use this one.
	 * 
	 * @param infoList
	 *            The list of attribute definitions in correct order that this form must be able to display.
	 */
	AbstractAttributeForm(List<AttributeInfo> infoList) {
		for (AttributeInfo info : infoList) {
			infos.put(info.getName(), info);
		}
		form = new DynamicForm();
		form.setWidth100();
		form.setHeight100();
		form.setStyleName("attributeForm");
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/**
	 * Apply an <code>attribute</code> value with the given <code>name</code> onto the form.
	 * 
	 * @param name
	 *            The name of the attribute to apply onto the form.
	 * @param attribute
	 *            The attribute value.
	 */
	public void toForm(String name, Attribute<?> attribute) {
		AttributeInfo info = infos.get(name);
		if (info instanceof PrimitiveAttributeInfo) {
			PrimitiveAttribute<?> primitive = (PrimitiveAttribute<?>) attribute;
			if (attribute == null) {
				form.getField(info.getName()).setDisabled(true);
			} else {
				if (!primitive.isEmpty()) {
					switch (primitive.getType()) {
						case BOOLEAN:
							setValue(info.getName(), (BooleanAttribute) primitive);
							break;
						case SHORT:
							setValue(info.getName(), (ShortAttribute) primitive);
							break;
						case INTEGER:
							setValue(info.getName(), (IntegerAttribute) primitive);
							break;
						case LONG:
							setValue(info.getName(), (LongAttribute) primitive);
							break;
						case FLOAT:
							setValue(info.getName(), (FloatAttribute) primitive);
							break;
						case DOUBLE:
							setValue(info.getName(), (DoubleAttribute) primitive);
							break;
						case CURRENCY:
							setValue(info.getName(), (CurrencyAttribute) primitive);
							break;
						case STRING:
							setValue(info.getName(), (StringAttribute) primitive);
							break;
						case URL:
							setValue(info.getName(), (UrlAttribute) primitive);
							break;
						case IMGURL:
							setValue(info.getName(), (ImageUrlAttribute) primitive);
							break;
						case DATE:
							setValue(info.getName(), (DateAttribute) primitive);
							break;
					}
				}
				form.getField(info.getName()).setDisabled(!attribute.isEditable());
			}
		} else {
			// TODO
		}
		form.fireEvent(new ItemChangedEvent(form.getJsObj()));
	}

	/**
	 * Get the value of the attribute associated with the given <code>name</code>, and place it in the
	 * <code>attribute</code>.
	 * 
	 * @param name
	 *            The name of the attribute to get the value from. This value must be fetched from the form.
	 * @param attribute
	 *            The actual attribute to place the value in.
	 */
	public void fromForm(String name, Attribute<?> attribute) {
		if (attribute == null) {
			return;
		}
		AttributeInfo info = infos.get(name);
		if (info instanceof PrimitiveAttributeInfo) {
			PrimitiveAttribute<?> primitive = (PrimitiveAttribute<?>) attribute;
			switch (primitive.getType()) {
				case BOOLEAN:
					getValue(name, (BooleanAttribute) primitive);
					break;
				case SHORT:
					getValue(name, (ShortAttribute) primitive);
					break;
				case INTEGER:
					getValue(name, (IntegerAttribute) primitive);
					break;
				case LONG:
					getValue(name, (LongAttribute) primitive);
					break;
				case FLOAT:
					getValue(name, (FloatAttribute) primitive);
					break;
				case DOUBLE:
					getValue(name, (DoubleAttribute) primitive);
					break;
				case CURRENCY:
					getValue(name, (CurrencyAttribute) primitive);
					break;
				case STRING:
					getValue(name, (StringAttribute) primitive);
					break;
				case URL:
					getValue(name, (UrlAttribute) primitive);
					break;
				case IMGURL:
					getValue(name, (ImageUrlAttribute) primitive);
					break;
				case DATE:
					getValue(name, (DateAttribute) primitive);
					break;
			}
		} else {
			// TODO Implement Associations...
		}
	}

	/**
	 * Clear the entire form. This will remove the entire visual representation.
	 */
	public void clear() {
		form.clear();
	}

	/**
	 * Return the actual SmartGWT form widget that is used behind the screens.
	 * 
	 * @return
	 */
	public Canvas getWidget() {
		return form;
	}

	// -------------------------------------------------------------------------
	// Protected methods setting values on the form:
	// -------------------------------------------------------------------------

	/** Apply a boolean attribute value on the form, with the given name. */
	protected void setValue(String name, BooleanAttribute attribute) {
		form.setValue(name, attribute.getValue());
	}

	/** Apply a short attribute value on the form, with the given name. */
	protected void setValue(String name, ShortAttribute attribute) {
		form.setValue(name, attribute.getValue());
	}

	/** Apply a integer attribute value on the form, with the given name. */
	protected void setValue(String name, IntegerAttribute attribute) {
		form.setValue(name, attribute.getValue());
	}

	/** Apply a long attribute value on the form, with the given name. */
	protected void setValue(String name, LongAttribute attribute) {
		form.setValue(name, attribute.getValue());
	}

	/** Apply a float attribute value on the form, with the given name. */
	protected void setValue(String name, FloatAttribute attribute) {
		form.setValue(name, attribute.getValue());
	}

	/** Apply a double attribute value on the form, with the given name. */
	protected void setValue(String name, DoubleAttribute attribute) {
		form.setValue(name, attribute.getValue());
	}

	/** Apply a currency attribute value on the form, with the given name. */
	protected void setValue(String name, CurrencyAttribute attribute) {
		form.setValue(name, attribute.getValue());
	}

	/** Apply a string attribute value on the form, with the given name. */
	protected void setValue(String name, StringAttribute attribute) {
		form.setValue(name, attribute.getValue());
	}

	/** Apply an URL attribute value on the form, with the given name. */
	protected void setValue(String name, UrlAttribute attribute) {
		form.setValue(name, attribute.getValue());
	}

	/** Apply an image attribute value on the form, with the given name. */
	protected void setValue(String name, ImageUrlAttribute attribute) {
		form.setValue(name, attribute.getValue());
	}

	/** Apply a date attribute value on the form, with the given name. */
	protected void setValue(String name, DateAttribute attribute) {
		// clumsy, but how else ?
		((TextItem) form.getItem(name)).setValue(attribute.getValue());
	}

	// -------------------------------------------------------------------------
	// Protected methods getting values from the form:
	// -------------------------------------------------------------------------

	/** Get a boolean value from the form, and place it in <code>attribute</code>. */
	protected void getValue(String name, BooleanAttribute attribute) {
		attribute.setValue(toBoolean(form.getValue(name)));
	}

	/** Get a short value from the form, and place it in <code>attribute</code>. */
	protected void getValue(String name, ShortAttribute attribute) {
		attribute.setValue(toShort(form.getValue(name)));
	}

	/** Get a integer value from the form, and place it in <code>attribute</code>. */
	protected void getValue(String name, IntegerAttribute attribute) {
		attribute.setValue(toInteger(form.getValue(name)));
	}

	/** Get a long value from the form, and place it in <code>attribute</code>. */
	protected void getValue(String name, LongAttribute attribute) {
		attribute.setValue(toLong(form.getValue(name)));
	}

	/** Get a float value from the form, and place it in <code>attribute</code>. */
	protected void getValue(String name, FloatAttribute attribute) {
		attribute.setValue(toFloat(form.getValue(name)));
	}

	/** Get a double value from the form, and place it in <code>attribute</code>. */
	protected void getValue(String name, DoubleAttribute attribute) {
		attribute.setValue(toDouble(form.getValue(name)));
	}

	/** Get a currency value from the form, and place it in <code>attribute</code>. */
	protected void getValue(String name, CurrencyAttribute attribute) {
		attribute.setValue((String) form.getValue(name));
	}

	/** Get a string value from the form, and place it in <code>attribute</code>. */
	protected void getValue(String name, StringAttribute attribute) {
		attribute.setValue((String) form.getValue(name));
	}

	/** Get an URL value from the form, and place it in <code>attribute</code>. */
	protected void getValue(String name, UrlAttribute attribute) {
		attribute.setValue((String) form.getValue(name));
	}

	/** Get an image value from the form, and place it in <code>attribute</code>. */
	protected void getValue(String name, ImageUrlAttribute attribute) {
		attribute.setValue((String) form.getValue(name));
	}

	/** Get a date value from the form, and place it in <code>attribute</code>. */
	protected void getValue(String name, DateAttribute attribute) {
		attribute.setValue((Date) form.getValue(name));
	}

	// -------------------------------------------------------------------------
	// Private methods for type casting:
	// -------------------------------------------------------------------------

	private Boolean toBoolean(Object value) {
		if (value == null) {
			return null;
		} else if (value instanceof Boolean) {
			return (Boolean) value;
		} else if (value instanceof String) {
			return Boolean.parseBoolean((String) value);
		} else if (value instanceof Short) {
			return (Short) value != 0;
		} else if (value instanceof Integer) {
			return (Integer) value != 0;
		} else if (value instanceof Long) {
			return (Long) value != 0;
		} else if (value instanceof Float) {
			return (Float) value != 0;
		} else if (value instanceof Double) {
			return (Double) value != 0;
		}

		return null;
	}

	private Float toFloat(Object value) {
		if (value == null) {
			return null;
		} else if (value instanceof Float) {
			return (Float) value;
		} else if (value instanceof String) {
			return Float.parseFloat((String) value);
		} else if (value instanceof Integer) {
			return ((Integer) value).floatValue();
		} else if (value instanceof Short) {
			return ((Short) value).floatValue();
		} else if (value instanceof Long) {
			return ((Long) value).floatValue();
		} else if (value instanceof Double) {
			return ((Double) value).floatValue();
		}

		return null;
	}

	private Double toDouble(Object value) {
		if (value == null) {
			return null;
		} else if (value instanceof Double) {
			return (Double) value;
		} else if (value instanceof String) {
			return Double.parseDouble((String) value);
		} else if (value instanceof Integer) {
			return ((Integer) value).doubleValue();
		} else if (value instanceof Short) {
			return ((Short) value).doubleValue();
		} else if (value instanceof Long) {
			return ((Long) value).doubleValue();
		} else if (value instanceof Float) {
			return ((Float) value).doubleValue();
		}

		return null;
	}

	private Short toShort(Object value) {
		if (value == null) {
			return null;
		} else if (value instanceof Short) {
			return (Short) value;
		} else if (value instanceof String) {
			return Short.parseShort((String) value);
		} else if (value instanceof Integer) {
			return ((Integer) value).shortValue();
		} else if (value instanceof Long) {
			return ((Long) value).shortValue();
		} else if (value instanceof Float) {
			return ((Float) value).shortValue();
		} else if (value instanceof Double) {
			return ((Double) value).shortValue();
		}

		return null;
	}

	private Integer toInteger(Object value) {
		if (value == null) {
			return null;
		} else if (value instanceof Integer) {
			return (Integer) value;
		} else if (value instanceof String) {
			return Integer.parseInt((String) value);
		} else if (value instanceof Short) {
			return ((Short) value).intValue();
		} else if (value instanceof Long) {
			return ((Long) value).intValue();
		} else if (value instanceof Float) {
			return ((Float) value).intValue();
		} else if (value instanceof Double) {
			return ((Double) value).intValue();
		}

		return null;
	}

	private Long toLong(Object value) {
		if (value == null) {
			return null;
		} else if (value instanceof Long) {
			return (Long) value;
		} else if (value instanceof String) {
			return Long.parseLong((String) value);
		} else if (value instanceof Short) {
			return ((Short) value).longValue();
		} else if (value instanceof Integer) {
			return ((Integer) value).longValue();
		} else if (value instanceof Float) {
			return ((Float) value).longValue();
		} else if (value instanceof Double) {
			return ((Double) value).longValue();
		}

		return null;
	}
}