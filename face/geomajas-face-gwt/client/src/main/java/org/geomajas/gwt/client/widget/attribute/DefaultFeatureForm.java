/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.gwt.client.widget.attribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geomajas.annotation.FutureApi;
import org.geomajas.configuration.AbstractAttributeInfo;
import org.geomajas.configuration.AbstractReadOnlyAttributeInfo;
import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.EditableAttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
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

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DSProtocol;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

/**
 * <p>
 * Default implementation of a {@link FeatureForm} based on a {@link DynamicForm}. The form is initialized by providing
 * a vector layer or the feature information of an association attribute. The implementation uses the
 * {@link AttributeFormFieldRegistry} to create the individual form items and fields. The
 * {@link #createField(AbstractReadOnlyAttributeInfo)} and {@link #createItem(AbstractReadOnlyAttributeInfo)} methods
 * can be overridden to create custom item and field implementations if necessary. The {@link #prepareForm(FormItemList,
 * DataSource)} method can be overridden to perform any additional actions on the form or form item list before the 
 * form is created. Attributes can be excluded from the form by overriding the {@link #isIncluded
 * (AbstractReadOnlyAttributeInfo)} method.
 * </p>
 * <p>
 * This attribute form definition is used internally in the
 * {@link org.geomajas.gwt.client.widget.FeatureAttributeEditor} widget. A code example on
 * how to override the {@link #prepareForm(FormItemList, DataSource)} method is shown below:
 * </p>
 * <code>
 * <pre>
 * 	protected void prepareForm(List<FormItem> formItems, DataSource source) {
 * 
 * 			getWidget().setGroupTitle("My Custom Attribute Form");
 * 			getWidget().setIsGroup(true);
 * 			getWidget().setNumCols(4);
 * 			getWidget().setWidth(450);
 * 			getWidget().setColWidths(100, 180, 20, 150);
 * 
 * 	}
 * 	</pre>
 * </code>
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
@FutureApi
public class DefaultFeatureForm implements FeatureForm<DynamicForm> {

	public static final String STYLE_FEATURE_FORM = "featureForm";

	private Map<String, AbstractReadOnlyAttributeInfo> attributeInfoMap =
			new HashMap<String, AbstractReadOnlyAttributeInfo>();

	private DynamicForm formWidget;

	private boolean disabled;

	private FeatureInfo featureInfo;

	private AttributeProvider attributeProvider;

	private HandlerManager manager = new HandlerManager(this);
	
	private Set<String> editableItemNames = new HashSet<String>(); 
	
	private boolean useEditableFromInfo;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Initialize the attribute form with the given layer. Note that this constructor will NOT define all the form
	 * items. This should still be done by some {@link FeatureFormFactory}. The reason for this is that different
	 * implementations of these factories may want different orders or layouts. They may even want to introduce extra
	 * form items.. who knows.
	 * 
	 * @param vectorLayer The vector layer that should be presented in this form.
	 */
	public DefaultFeatureForm(VectorLayer vectorLayer) {
		this(vectorLayer.getLayerInfo().getFeatureInfo(), new DefaultAttributeProvider(vectorLayer.getLayerInfo()
				.getServerLayerId()));
	}

	/**
	 * Initialize the attribute form with the given feature info. Note that this constructor will NOT define all the
	 * form items. This should still be done by some {@link FeatureFormFactory}. The reason for this is that different
	 * implementations of these factories may want different orders or layouts. They may even want to introduce extra
	 * form items.. who knows.
	 * 
	 * @param featureInfo The feature information that should be presented in this form.
	 * @param attributeProvider attribute provider
	 */
	public DefaultFeatureForm(FeatureInfo featureInfo, AttributeProvider attributeProvider) {
		this.featureInfo = featureInfo;
		this.attributeProvider = attributeProvider;
		for (AbstractAttributeInfo info : featureInfo.getAttributes()) {
			if (info instanceof AbstractReadOnlyAttributeInfo) {
				AbstractReadOnlyAttributeInfo roInfo = (AbstractReadOnlyAttributeInfo) info;
				attributeInfoMap.put(roInfo.getName(), roInfo);
			}
		}
		formWidget = new DynamicForm() {

			public void setDataSource(com.smartgwt.client.data.DataSource dataSource) {
				dataSource.setDataFormat(DSDataFormat.CUSTOM);
				dataSource.setDataProtocol(DSProtocol.CLIENTCUSTOM);
				dataSource.setClientOnly(false);
				super.setDataSource(dataSource);
			}
		};
		formWidget.setStyleName(STYLE_FEATURE_FORM);
		DataSource source = new DataSource();
		FormItemList formItems = new FormItemList();
		for (AbstractAttributeInfo info : featureInfo.getAttributes()) {
			if (info instanceof AbstractReadOnlyAttributeInfo) {
				AbstractReadOnlyAttributeInfo roInfo = (AbstractReadOnlyAttributeInfo) info;
				if (isIncluded(roInfo)) {
					formItems.add(createItem(roInfo));
					source.addField(createField(roInfo));
				}
			}
		}
		prepareForm(formItems, source);
		getWidget().setDataSource(source);
		getWidget().setFields(formItems.toArray());
	}

	/**
	 * Creates a form item for a specific attribute.
	 * 
	 * @param info the attribute information.
	 * @return the form item
	 */
	protected FormItem createItem(AbstractReadOnlyAttributeInfo info) {
		return AttributeFormFieldRegistry.createFormItem(info, attributeProvider.createProvider(info.getName()));
	}

	/**
	 * Create a data source field for a specific attribute.
	 * 
	 * @param info the attribute information.
	 * @return the data source field
	 */
	protected DataSourceField createField(AbstractReadOnlyAttributeInfo info) {
		return AttributeFormFieldRegistry.createDataSourceField(info);
	}

	/**
	 * Returns whether an attribute should be included in the form.
	 * 
	 * @param info the attribute information
	 * @return true if included, false otherwise
	 */
	protected boolean isIncluded(AbstractReadOnlyAttributeInfo info) {
		return !info.isHidden();
	}

	/**
	 * Override this method to make some additional modifications to the list of items and data source before the form
	 * is created. The default implementation does nothing.
	 * 
	 * @param formItems list of items, with special insert operations
	 * @param source data source
	 */
	protected void prepareForm(FormItemList formItems, DataSource source) {
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/** {@inheritDoc} */
	public DynamicForm getWidget() {
		return formWidget;
	}

	/** {@inheritDoc} */
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;

		// Don't set disabled on the form, but on the individual items. This way it's easier to overwrite when creating
		// custom form items.
		for (AbstractAttributeInfo info : featureInfo.getAttributes()) {
			FormItem formItem = formWidget.getItem(info.getName());
			if (formItem != null) {
				if (editableItemNames.contains(info.getName())) {
					formItem.setDisabled(disabled);
				} else {
					formItem.setDisabled(true);
				}
			}
		}
	}

	/** {@inheritDoc} */
	public boolean isDisabled() {
		return disabled;
	}

	/** {@inheritDoc} */
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
	 * If this flag is set to true, the editability of an attribute is determined by the attribute info only. If set
	 * to false, the editability is determined by the actual attribute value.
	 * 
	 * @see Attribute#isEditable()
	 * @see AbstractReadOnlyAttributeInfo#isEditable()
	 * @param useEditableFromInfo
	 */
	public void setUseEditableFromInfo(boolean useEditableFromInfo) {
		this.useEditableFromInfo = useEditableFromInfo;
	}

	public boolean silentValidate() {
		return formWidget.valuesAreValid(false);
	}

	public void fireEvent(GwtEvent<?> event) {
		manager.fireEvent(event);
	}

	/** {@inheritDoc} */
	public HandlerRegistration addItemChangedHandler(ItemChangedHandler handler) {
		MultiHandlerRegistration registration = new MultiHandlerRegistration();
		// Due to custom made FormItems, we can't set the handler on the form anymore...
		final ItemChangedHandler itemChangedHandler = handler;
		registration.addRegistration(manager.addHandler(ItemChangedEvent.getType(), handler));
		for (final FormItem formItem : formWidget.getFields()) {
			ChangedHandler h = new ChangedHandler() {

				public void onChanged(ChangedEvent event) {
					itemChangedHandler.onItemChanged(new ItemChangedEvent(formItem.getJsObj()));
				}
			};
			registration.addRegistration(formItem.addChangedHandler(h));
		}
		return registration;
	}

	public void toForm(AssociationValue value) {
		for (Map.Entry<String, Attribute<?>> entry : value.getAllAttributes().entrySet()) {
			toForm(entry.getKey(), entry.getValue());
		}
	}

	public void fromForm(AssociationValue value) {
		for (Map.Entry<String, Attribute<?>> entry : value.getAllAttributes().entrySet()) {
			fromForm(entry.getKey(), entry.getValue());
		}
	}

	/** {@inheritDoc} */
	public void toForm(String name, Attribute<?> attribute) {
		AbstractReadOnlyAttributeInfo info = attributeInfoMap.get(name);
		if (info == null || !isIncluded(info)) {
			return;
		}
		FormItem item = formWidget.getField(info.getName());
		boolean editable = (attribute != null && attribute.isEditable()) || (useEditableFromInfo && info.isEditable());
		if (editable) {
			editableItemNames.add(info.getName());
		} else {
			editableItemNames.remove(info.getName());
		}
		item.setDisabled(isDisabled() || !editable);
		if (attribute != null) {
			if (attribute instanceof StringAttribute) {
				setValue(info.getName(), (StringAttribute) attribute);
			} else if (attribute instanceof ShortAttribute) {
				setValue(info.getName(), (ShortAttribute) attribute);
			} else if (attribute instanceof IntegerAttribute) {
				setValue(info.getName(), (IntegerAttribute) attribute);
			} else if (attribute instanceof LongAttribute) {
				setValue(info.getName(), (LongAttribute) attribute);
			} else if (attribute instanceof FloatAttribute) {
				setValue(info.getName(), (FloatAttribute) attribute);
			} else if (attribute instanceof DoubleAttribute) {
				setValue(info.getName(), (DoubleAttribute) attribute);
			} else if (attribute instanceof CurrencyAttribute) {
				setValue(info.getName(), (CurrencyAttribute) attribute);
			} else if (attribute instanceof BooleanAttribute) {
				setValue(info.getName(), (BooleanAttribute) attribute);
			} else if (attribute instanceof UrlAttribute) {
				setValue(info.getName(), (UrlAttribute) attribute);
			} else if (attribute instanceof ImageUrlAttribute) {
				setValue(info.getName(), (ImageUrlAttribute) attribute);
			} else if (attribute instanceof DateAttribute) {
				setValue(info.getName(), (DateAttribute) attribute);

			} else if (attribute instanceof ManyToOneAttribute) {
				Object associationItem = item.getAttributeAsObject(AssociationItem.ASSOCIATION_ITEM_ATTRIBUTE_KEY);
				((ManyToOneItem<?>) associationItem).toItem((ManyToOneAttribute) attribute);
			} else if (attribute instanceof OneToManyAttribute) {
				Object associationItem = item.getAttributeAsObject(AssociationItem.ASSOCIATION_ITEM_ATTRIBUTE_KEY);
				((OneToManyItem<?>) associationItem).toItem((OneToManyAttribute) attribute);

			} else {
				throw new IllegalStateException("Unhandled attribute for " + name +
						" with value " + attribute);
			}
			item.fireEvent(new ChangedEvent(item.getJsObj()));
		}
	}

	/** {@inheritDoc} */
	public void fromForm(String name, Attribute<?> attribute) {
		AbstractReadOnlyAttributeInfo info = attributeInfoMap.get(name);
		if (null == attribute || null == info || !isIncluded(info) || !(info instanceof EditableAttributeInfo)) {
			return;
		}
		if (info instanceof PrimitiveAttributeInfo) {
			PrimitiveAttribute<?> primitive = (PrimitiveAttribute<?>) attribute;
			switch (primitive.getType()) {
				case BOOLEAN:
					getValue(name, (BooleanAttribute) primitive); // NOSONAR valid cast
					break;
				case SHORT:
					getValue(name, (ShortAttribute) primitive); // NOSONAR valid cast
					break;
				case INTEGER:
					getValue(name, (IntegerAttribute) primitive); // NOSONAR valid cast
					break;
				case LONG:
					getValue(name, (LongAttribute) primitive); // NOSONAR valid cast
					break;
				case FLOAT:
					getValue(name, (FloatAttribute) primitive); // NOSONAR valid cast
					break;
				case DOUBLE:
					getValue(name, (DoubleAttribute) primitive); // NOSONAR valid cast
					break;
				case CURRENCY:
					getValue(name, (CurrencyAttribute) primitive); // NOSONAR valid cast
					break;
				case STRING:
					getValue(name, (StringAttribute) primitive); // NOSONAR valid cast
					break;
				case URL:
					getValue(name, (UrlAttribute) primitive); // NOSONAR valid cast
					break;
				case IMGURL:
					getValue(name, (ImageUrlAttribute) primitive); // NOSONAR valid cast
					break;
				case DATE:
					getValue(name, (DateAttribute) primitive); // NOSONAR valid cast
					break;
				default:
					throw new IllegalStateException("Unhandled primitive attribute type " +
							primitive.getType());
			}
		} else {
			AssociationAttribute<?> association = (AssociationAttribute<?>) attribute;
			FormItem item = formWidget.getItem(name);
			Object associationItem = item.getAttributeAsObject(AssociationItem.ASSOCIATION_ITEM_ATTRIBUTE_KEY);
			switch (association.getType()) {
				case MANY_TO_ONE:
					((ManyToOneItem<?>) associationItem).fromItem((ManyToOneAttribute) attribute); // NOSONAR valid cast
					break;
				case ONE_TO_MANY:
					((OneToManyItem<?>) associationItem).fromItem((OneToManyAttribute) attribute); // NOSONAR valid cast
					break;
				default:
					throw new IllegalStateException("Unhandled association attribute type " +
							association.getType());
			}
		}
	}

	/** {@inheritDoc} */
	public void clear() {
		formWidget.clearValues();
		// the above does not call clearValue() on every item ?!! so do it explicitly
		for (AbstractAttributeInfo info : featureInfo.getAttributes()) {
			FormItem formItem = formWidget.getItem(info.getName());
			if (formItem != null) {
				if (info instanceof AssociationAttributeInfo) {
					Object associationItem = formItem
							.getAttributeAsObject(AssociationItem.ASSOCIATION_ITEM_ATTRIBUTE_KEY);
					AssociationAttributeInfo associationInfo = (AssociationAttributeInfo) info;
					if (associationItem != null) {
						switch (associationInfo.getType()) {
							case MANY_TO_ONE:
								((ManyToOneItem<?>) associationItem).clearValue(); // NOSONAR valid cast
								break;
							case ONE_TO_MANY:
								((OneToManyItem<?>) associationItem).clearValue(); // NOSONAR valid cast
								break;
							default:
								throw new IllegalStateException("Unhandled association attribute type " +
										associationInfo.getType());
						}
					}
				} else {
					formItem.clearValue();

				}
			}
		}
	}

	// -------------------------------------------------------------------------
	// Protected methods setting values on the form:
	// -------------------------------------------------------------------------

	/**
	 * Apply a boolean attribute value on the form, with the given name.
	 *
	 * @param name attribute name
	 * @param attribute attribute value
	 */
	protected void setValue(String name, BooleanAttribute attribute) {
		FormItem item = formWidget.getField(name);
		if (item != null) {
			item.setValue(attribute.getValue());
		}
	}

	/**
	 * Apply a short attribute value on the form, with the given name.
	 *
	 * @param name attribute name
	 * @param attribute attribute value
	 */
	protected void setValue(String name, ShortAttribute attribute) {
		FormItem item = formWidget.getField(name);
		if (item != null) {
			item.setValue(attribute.getValue());
		}
	}

	/**
	 * Apply a integer attribute value on the form, with the given name.
	 *
	 * @param name attribute name
	 * @param attribute attribute value
	 */
	protected void setValue(String name, IntegerAttribute attribute) {
		FormItem item = formWidget.getField(name);
		if (item != null) {
			item.setValue(attribute.getValue());
		}
	}

	/**
	 * Apply a long attribute value on the form, with the given name.
	 *
	 * @param name attribute name
	 * @param attribute attribute value
	 */
	protected void setValue(String name, LongAttribute attribute) {
		FormItem item = formWidget.getField(name);
		if (item != null) {
			item.setValue(attribute.getValue());
		}
	}

	/**
	 * Apply a float attribute value on the form, with the given name.
	 *
	 * @param name attribute name
	 * @param attribute attribute value
	 */
	protected void setValue(String name, FloatAttribute attribute) {
		FormItem item = formWidget.getField(name);
		if (item != null) {
			item.setValue(attribute.getValue());
		}
	}

	/**
	 * Apply a double attribute value on the form, with the given name.
	 *
	 * @param name attribute name
	 * @param attribute attribute value
	 */
	protected void setValue(String name, DoubleAttribute attribute) {
		FormItem item = formWidget.getField(name);
		if (item != null) {
			item.setValue(attribute.getValue());
		}
	}

	/**
	 * Apply a currency attribute value on the form, with the given name.
	 *
	 * @param name attribute name
	 * @param attribute attribute value
	 */
	protected void setValue(String name, CurrencyAttribute attribute) {
		FormItem item = formWidget.getField(name);
		if (item != null) {
			item.setValue(attribute.getValue());
		}
	}

	/**
	 * Apply a string attribute value on the form, with the given name.
	 *
	 * @param name attribute name
	 * @param attribute attribute value
	 */
	protected void setValue(String name, StringAttribute attribute) {
		FormItem item = formWidget.getField(name);
		if (item != null) {
			item.setValue(attribute.getValue());
		}
	}

	/**
	 * Apply an URL attribute value on the form, with the given name.
	 *
	 * @param name attribute name
	 * @param attribute attribute value
	 */
	protected void setValue(String name, UrlAttribute attribute) {
		FormItem item = formWidget.getField(name);
		if (item != null) {
			item.setValue(attribute.getValue());
		}
	}

	/**
	 * Apply an image attribute value on the form, with the given name.
	 *
	 * @param name attribute name
	 * @param attribute attribute value
	 */
	protected void setValue(String name, ImageUrlAttribute attribute) {
		FormItem item = formWidget.getField(name);
		if (item != null) {
			item.setValue(attribute.getValue());
		}
	}

	/**
	 * Apply a date attribute value on the form, with the given name.
	 *
	 * @param name attribute name
	 * @param attribute attribute value
	 */
	protected void setValue(String name, DateAttribute attribute) {
		FormItem item = formWidget.getField(name);
		if (item != null) {
			item.setValue(attribute.getValue());
		}
	}

	// -------------------------------------------------------------------------
	// Protected methods getting values from the form:
	// -------------------------------------------------------------------------

	/**
	 * Get a boolean value from the form, and place it in <code>attribute</code>.
	 *
	 * @param name attribute name
	 * @param attribute attribute to put value
	 */
	protected void getValue(String name, BooleanAttribute attribute) {
		attribute.setValue(toBoolean(formWidget.getValue(name)));
	}

	/**
	 * Get a short value from the form, and place it in <code>attribute</code>.
	 *
	 * @param name attribute name
	 * @param attribute attribute to put value
	 */
	protected void getValue(String name, ShortAttribute attribute) {
		attribute.setValue(toShort(formWidget.getValue(name)));
	}

	/**
	 * Get a integer value from the form, and place it in <code>attribute</code>.
	 *
	 * @param name attribute name
	 * @param attribute attribute to put value
	 */
	protected void getValue(String name, IntegerAttribute attribute) {
		attribute.setValue(toInteger(formWidget.getValue(name)));
	}

	/**
	 * Get a long value from the form, and place it in <code>attribute</code>.
	 *
	 * @param name attribute name
	 * @param attribute attribute to put value
	 */
	protected void getValue(String name, LongAttribute attribute) {
		attribute.setValue(toLong(formWidget.getValue(name)));
	}

	/** Get a float value from the form, and place it in <code>attribute</code>.
	 *
	 * @param name attribute name
	 * @param attribute attribute to put value
	 */
	protected void getValue(String name, FloatAttribute attribute) {
		attribute.setValue(toFloat(formWidget.getValue(name)));
	}

	/** Get a double value from the form, and place it in <code>attribute</code>.
	 *
	 * @param name attribute name
	 * @param attribute attribute to put value
	 */
	protected void getValue(String name, DoubleAttribute attribute) {
		attribute.setValue(toDouble(formWidget.getValue(name)));
	}

	/** Get a currency value from the form, and place it in <code>attribute</code>.
	 *
	 * @param name attribute name
	 * @param attribute attribute to put value
	 */
	protected void getValue(String name, CurrencyAttribute attribute) {
		attribute.setValue((String) formWidget.getValue(name));
	}

	/**
	 * Get a string value from the form, and place it in <code>attribute</code>.
	 *
	 * @param name attribute name
	 * @param attribute attribute to put value
	 */
	protected void getValue(String name, StringAttribute attribute) {
		attribute.setValue((String) formWidget.getValue(name));
	}

	/**
	 * Get an URL value from the form, and place it in <code>attribute</code>.
	 *
	 * @param name attribute name
	 * @param attribute attribute to put value
	 */
	protected void getValue(String name, UrlAttribute attribute) {
		attribute.setValue((String) formWidget.getItem(name).getValue());
	}

	/**
	 * Get an image value from the form, and place it in <code>attribute</code>.
	 *
	 * @param name attribute name
	 * @param attribute attribute to put value
	 */
	protected void getValue(String name, ImageUrlAttribute attribute) {
		attribute.setValue((String) formWidget.getValue(name));
	}

	/**
	 * Get a date value from the form, and place it in <code>attribute</code>.
	 *
	 * @param name attribute name
	 * @param attribute attribute to put value
	 */
	protected void getValue(String name, DateAttribute attribute) {
		attribute.setValue((Date) formWidget.getValue(name));
	}

	protected FeatureInfo getFeatureInfo() {
		return featureInfo;
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

	public Canvas getCanvas() {
		return getWidget();
	}

	/**
	 * A list wrapper that allows easy insertion of form items by name.
	 * 
	 * @author Jan De Moerloose
	 */
	public class FormItemList implements Iterable<FormItem> {

		private List<FormItem> list = new ArrayList<FormItem>();

		public int size() {
			return list.size();
		}

		public FormItem[] toArray() {
			return list.toArray(new FormItem[size()]);
		}

		public Iterator<FormItem> iterator() {
			return list.iterator();
		}

		public boolean add(FormItem e) {
			return list.add(e);
		}

		public boolean addAll(Collection<? extends FormItem> c) {
			return list.addAll(c);
		}

		public boolean addAll(int index, Collection<? extends FormItem> c) {
			return list.addAll(index, c);
		}

		public boolean remove(Object o) {
			return list.remove(o);
		}

		public FormItem set(int index, FormItem element) {
			return list.set(index, element);
		}

		public void add(int index, FormItem element) {
			list.add(index, element);
		}

		public FormItem remove(int index) {
			return list.remove(index);
		}

		public int indexOf(String name) {
			Iterator<FormItem> it = list.iterator();
			int i = 0;
			while (it.hasNext()) {
				if (it.next().getName().equals(name)) {
					return i;
				}
				i++;
			}
			return -1;
		}

		/**
		 * Insert a form item before the item with the specified name.
		 * 
		 * @param name name of the item before which to insert
		 * @param newItem the item to insert
		 */
		public void insertBefore(String name, FormItem... newItem) {
			int index = indexOf(name);
			if (index >= 0) {
				addAll(index, Arrays.asList(newItem));
			}
		}

		/**
		 * Insert a form item after the item with the specified name.
		 * 
		 * @param name name of the item after which to insert
		 * @param newItem the item to insert
		 */
		public void insertAfter(String name, FormItem... newItem) {
			int index = indexOf(name);
			if (index >= 0) {
				addAll(index + 1, Arrays.asList(newItem));
			}
		}
	}

	/**
	 * Class that represents multiple registrations as one.
	 * 
	 * @author Jan De Moerloose
	 */
	class MultiHandlerRegistration implements HandlerRegistration {

		private List<HandlerRegistration> registrations = new ArrayList<HandlerRegistration>();

		public void addRegistration(HandlerRegistration registration) {
			registrations.add(registration);
		}

		public void removeHandler() {
			for (HandlerRegistration registration : registrations) {
				registration.removeHandler();
			}
		}

	}

}