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
package org.geomajas.gwt.client.widget.attribute;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.global.FutureApi;
import org.geomajas.gwt.client.map.layer.VectorLayer;
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

import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DSProtocol;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * <p>
 * This class represents a form that is custom tailored for managing the attributes of a feature. The form is
 * initialized by providing a vector layer. After creation, some {@link FeatureFormFactory} should be used to apply the
 * individual items within the form - by making use of the <code>getWidget</code> method, which returns the actual
 * SmartGWT form widget.<br/>
 * This object mainly takes care of getting and setting attribute values from and to the form.
 * </p>
 * <p>
 * This attribute form definition is used internally in the <code>FeatureEditor</code> widget, where by default the
 * {@link DefaultFeatureFormFactory} is used to create the individual items within the form.
 * </p>
 * 
 * @author Pieter De Graef
 */
@FutureApi
public class FeatureForm {

	private VectorLayer layer;

	private Map<String, AttributeInfo> attributeInfoMap = new HashMap<String, AttributeInfo>();

	private DynamicForm formWidget;

	private boolean disabled;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Initialize the attribute form with the given layer. Note that this constructor will NOT define all the form
	 * items. This should still be done by some {@link FeatureFormFactory}. The reason for this is that different
	 * implementations of these factories may want different orders or layouts. They may even want to introduce extra
	 * form items.. who knows.
	 * 
	 * @param layer
	 *            The vector layer that should be presented in this form.
	 */
	public FeatureForm(VectorLayer layer) {
		this.layer = layer;
		for (AttributeInfo info : layer.getLayerInfo().getFeatureInfo().getAttributes()) {
			attributeInfoMap.put(info.getName(), info);
		}

		formWidget = new DynamicForm() {

			public void setDataSource(com.smartgwt.client.data.DataSource dataSource) {
				dataSource.setDataFormat(DSDataFormat.CUSTOM);
				dataSource.setDataProtocol(DSProtocol.CLIENTCUSTOM);
				dataSource.setClientOnly(false);
				super.setDataSource(dataSource);
			};
		};
		formWidget.setStyleName("featureForm");
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/**
	 * Return the actual SmartGWT form widget that is used behind the screens. This method is used in the
	 * {@link FeatureFormFactory}s to retrieve the actual form, and apply the definitive form layout.
	 * 
	 * @return
	 */
	public DynamicForm getWidget() {
		return formWidget;
	}

	/**
	 * Retrieve the vector layer that should be represented in the form.
	 * 
	 * @return The vector layer that should be represented in the form.
	 */
	public VectorLayer getLayer() {
		return layer;
	}

	/**
	 * Enable or disable the form for editing. This means that when the form is disabled, no editing is possible.
	 * 
	 * @param disabled
	 *            Should editing be enabled or disabled?
	 */
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;

		// Don't set disabled on the form, but on the individual items. This way it's easier to overwrite when creating
		// custom form items.
		for (AttributeInfo info : layer.getLayerInfo().getFeatureInfo().getAttributes()) {
			FormItem formItem = formWidget.getItem(info.getName());
			if (formItem != null) {
				if (info.isEditable()) {
					formItem.setDisabled(disabled);
				} else {
					formItem.setDisabled(true);
				}
			}
		}
	}

	/**
	 * See if the form is currently disabled or not?
	 * 
	 * @return Is the form currently disabled or not?
	 */
	public boolean isDisabled() {
		return disabled;
	}

	/**
	 * Validate the contents of the entire attribute form.
	 * 
	 * @return Returns true if all values in the form are validated correctly, false otherwise.
	 */
	public boolean validate() {
		boolean validate = true;
		for (FormItem item : formWidget.getFields()) {
			// workaround for SmartGWT bug, see http://jira.geomajas.org/browse/GWT-226
			if (!(item instanceof SelectItem)) {
				validate = validate & item.validate();
			}
		}
		return validate;
	}

	/**
	 * Attach a handler that reacts to changes in the fields as the user makes them.
	 * 
	 * @param handler
	 * @return
	 */
	public void addItemChangedHandler(final ItemChangedHandler handler) {
		// Due to custom made FormItems, we can't set the handler on the form anymore...
		for (final FormItem formItem : formWidget.getFields()) {
			ChangedHandler h = new ChangedHandler() {

				public void onChanged(ChangedEvent event) {
					handler.onItemChanged(new ItemChangedEvent(formItem.getJsObj()));
				}
			};
			formItem.addChangedHandler(h);
		}
	}

	/**
	 * Apply an <code>attribute</code> value with the given <code>name</code> onto the form.
	 * 
	 * @param name
	 *            The name of the attribute to apply onto the form.
	 * @param attribute
	 *            The attribute value.
	 */
	public void toForm(String name, Attribute<?> attribute) {
		AttributeInfo info = attributeInfoMap.get(name);
		if (info instanceof PrimitiveAttributeInfo) {
			PrimitiveAttribute<?> primitive = (PrimitiveAttribute<?>) attribute;
			if (attribute == null && formWidget.getField(info.getName()) != null) {
				formWidget.getField(info.getName()).setDisabled(true);
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
			}
		} else if (info instanceof AssociationAttributeInfo) {
			AssociationAttribute<?> association = (AssociationAttribute<?>) attribute;
			if (association instanceof ManyToOneAttribute) {
				FormItem item = formWidget.getField(name);
				if (item != null) {
					if (association.getValue() != null) {
						item.setValue(((ManyToOneAttribute) association).getValue().getId().getValue());
					} else {
						item.clearValue();
					}
				}
			} else if (association instanceof OneToManyAttribute) {
				// TODO Implement setter for Associations as well...
			}
		}
		formWidget.fireEvent(new ItemChangedEvent(formWidget.getJsObj()));
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
		AttributeInfo info = attributeInfoMap.get(name);
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
			AssociationAttribute<?> association = (AssociationAttribute<?>) attribute;
			switch (association.getType()) {
				case MANY_TO_ONE:
					// Getting the value through the form doesn't work somehow, so we retrieve the record instead...
					ManyToOneAttribute manyToOne = (ManyToOneAttribute) association;
					if (formWidget.getField(name) != null) {
						ListGridRecord record = formWidget.getField(name).getSelectedRecord();
						if (record != null) {
							Object v = record
									.getAttributeAsObject(AttributeFormFieldRegistry.ASSOCIATION_ITEM_VALUE_ATTRIBUTE);
							if (v != null && v instanceof AssociationValue) {
								manyToOne.setValue((AssociationValue) v);
							}
						}
					}
					break;
				case ONE_TO_MANY:
					// TODO Implement the attribute form getter for the ONE_TO_MANY attribute.
					break;
			}
		}
	}

	/** Clear the entire form. This will remove the entire visual representation. */
	public void clear() {
		formWidget.clear();
	}

	// -------------------------------------------------------------------------
	// Protected methods setting values on the form:
	// -------------------------------------------------------------------------

	/** Apply a boolean attribute value on the form, with the given name. */
	protected void setValue(String name, BooleanAttribute attribute) {
		// formWidget.setValue(name, attribute.getValue());
		FormItem item = formWidget.getField(name);
		if (item != null) {
			item.setValue(attribute.getValue());
		}
	}

	/** Apply a short attribute value on the form, with the given name. */
	protected void setValue(String name, ShortAttribute attribute) {
		formWidget.setValue(name, attribute.getValue());
	}

	/** Apply a integer attribute value on the form, with the given name. */
	protected void setValue(String name, IntegerAttribute attribute) {
		FormItem item = formWidget.getField(name);
		if (item != null) {
			item.setValue(attribute.getValue());
		}
	}

	/** Apply a long attribute value on the form, with the given name. */
	protected void setValue(String name, LongAttribute attribute) {
		FormItem item = formWidget.getField(name);
		if (item != null) {
			item.setValue(attribute.getValue());
		}
	}

	/** Apply a float attribute value on the form, with the given name. */
	protected void setValue(String name, FloatAttribute attribute) {
		FormItem item = formWidget.getField(name);
		if (item != null) {
			item.setValue(attribute.getValue());
		}
	}

	/** Apply a double attribute value on the form, with the given name. */
	protected void setValue(String name, DoubleAttribute attribute) {
		FormItem item = formWidget.getField(name);
		if (item != null) {
			item.setValue(attribute.getValue());
		}
	}

	/** Apply a currency attribute value on the form, with the given name. */
	protected void setValue(String name, CurrencyAttribute attribute) {
		FormItem item = formWidget.getField(name);
		if (item != null) {
			item.setValue(attribute.getValue());
		}
	}

	/** Apply a string attribute value on the form, with the given name. */
	protected void setValue(String name, StringAttribute attribute) {
		FormItem item = formWidget.getField(name);
		if (item != null) {
			item.setValue(attribute.getValue());
		}
	}

	/** Apply an URL attribute value on the form, with the given name. */
	protected void setValue(String name, UrlAttribute attribute) {
		FormItem item = formWidget.getField(name);
		if (item != null) {
			item.setValue(attribute.getValue());
		}
	}

	/** Apply an image attribute value on the form, with the given name. */
	protected void setValue(String name, ImageUrlAttribute attribute) {
		FormItem item = formWidget.getField(name);
		if (item != null) {
			item.setValue(attribute.getValue());
		}
	}

	/** Apply a date attribute value on the form, with the given name. */
	protected void setValue(String name, DateAttribute attribute) {
		FormItem item = formWidget.getField(name);
		if (item != null) {
			item.setValue(attribute.getValue());
		}
	}

	// -------------------------------------------------------------------------
	// Protected methods getting values from the form:
	// -------------------------------------------------------------------------

	/** Get a boolean value from the form, and place it in <code>attribute</code>. */
	protected void getValue(String name, BooleanAttribute attribute) {
		attribute.setValue(toBoolean(formWidget.getValue(name)));
	}

	/** Get a short value from the form, and place it in <code>attribute</code>. */
	protected void getValue(String name, ShortAttribute attribute) {
		attribute.setValue(toShort(formWidget.getValue(name)));
	}

	/** Get a integer value from the form, and place it in <code>attribute</code>. */
	protected void getValue(String name, IntegerAttribute attribute) {
		attribute.setValue(toInteger(formWidget.getValue(name)));
	}

	/** Get a long value from the form, and place it in <code>attribute</code>. */
	protected void getValue(String name, LongAttribute attribute) {
		attribute.setValue(toLong(formWidget.getValue(name)));
	}

	/** Get a float value from the form, and place it in <code>attribute</code>. */
	protected void getValue(String name, FloatAttribute attribute) {
		attribute.setValue(toFloat(formWidget.getValue(name)));
	}

	/** Get a double value from the form, and place it in <code>attribute</code>. */
	protected void getValue(String name, DoubleAttribute attribute) {
		attribute.setValue(toDouble(formWidget.getValue(name)));
	}

	/** Get a currency value from the form, and place it in <code>attribute</code>. */
	protected void getValue(String name, CurrencyAttribute attribute) {
		attribute.setValue((String) formWidget.getValue(name));
	}

	/** Get a string value from the form, and place it in <code>attribute</code>. */
	protected void getValue(String name, StringAttribute attribute) {
		attribute.setValue((String) formWidget.getValue(name));
	}

	/** Get an URL value from the form, and place it in <code>attribute</code>. */
	protected void getValue(String name, UrlAttribute attribute) {
		attribute.setValue((String) formWidget.getItem(name).getValue());
	}

	/** Get an image value from the form, and place it in <code>attribute</code>. */
	protected void getValue(String name, ImageUrlAttribute attribute) {
		attribute.setValue((String) formWidget.getValue(name));
	}

	/** Get a date value from the form, and place it in <code>attribute</code>. */
	protected void getValue(String name, DateAttribute attribute) {
		attribute.setValue((Date) formWidget.getValue(name));
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