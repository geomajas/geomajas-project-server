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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.global.FutureApi;
import org.geomajas.gwt.client.map.layer.VectorLayer;
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
import org.geomajas.layer.feature.attribute.ManyToOneAttribute;
import org.geomajas.layer.feature.attribute.OneToManyAttribute;
import org.geomajas.layer.feature.attribute.PrimitiveAttribute;
import org.geomajas.layer.feature.attribute.ShortAttribute;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.layer.feature.attribute.UrlAttribute;

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
 * {@link #createField(AttributeInfo)} and {@link #createItem(AttributeInfo)} methods can be overridden to create custom
 * item and field implementations if necessary. The {@link #prepareForm(List, DataSource)} method can be overridden to
 * perform any additional actions on the form or form item list before the form is created. Attributes can be excluded
 * from the form by overriding the {@link #isIncluded(AttributeInfo)} method.
 * </p>
 * <p>
 * This attribute form definition is used internally in the <code>FeatureAtributeEditor</code> widget. A code example on
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

	private Map<String, AttributeInfo> attributeInfoMap = new HashMap<String, AttributeInfo>();

	private DynamicForm formWidget;

	private boolean disabled;

	private FeatureInfo featureInfo;

	private AttributeProvider attributeProvider;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Initialize the attribute form with the given layer. Note that this constructor will NOT define all the form
	 * items. This should still be done by some {@link FeatureFormFactory}. The reason for this is that different
	 * implementations of these factories may want different orders or layouts. They may even want to introduce extra
	 * form items.. who knows.
	 * 
	 * @param layer The vector layer that should be presented in this form.
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
	 */
	public DefaultFeatureForm(FeatureInfo featureInfo, AttributeProvider attributeProvider) {
		this.featureInfo = featureInfo;
		this.attributeProvider = attributeProvider;
		for (AttributeInfo info : featureInfo.getAttributes()) {
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
		DataSource source = new DataSource();
		FormItemList formItems = new FormItemList();
		for (AttributeInfo info : featureInfo.getAttributes()) {
			if (isIncluded(info)) {
				formItems.add(createItem(info));
				source.addField(createField(info));
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
	protected FormItem createItem(AttributeInfo info) {
		return AttributeFormFieldRegistry.createFormItem(info, attributeProvider.createProvider(info.getName()));
	}

	/**
	 * Create a datasource field for a specific attribute.
	 * 
	 * @param info the attribute information.
	 * @return the datasource field
	 */
	protected DataSourceField createField(AttributeInfo info) {
		return AttributeFormFieldRegistry.createDataSourceField(info);
	}

	/**
	 * Returns whether an attribute should be included in the form.
	 * 
	 * @param info the attribute information
	 * @return true if included, false otherwise
	 */
	protected boolean isIncluded(AttributeInfo info) {
		return info.isIncludedInForm();
	}

	/**
	 * Override this method to make some additional modifications to the list of items and data source before the form
	 * is created. The default implementation does nothing.
	 * 
	 * @param formItems list of items, with special insert operations
	 * @param source datasource
	 */
	protected void prepareForm(FormItemList formItems, DataSource source) {
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.gwt.client.widget.attribute.FeatureForm#getWidget()
	 */
	public DynamicForm getWidget() {
		return formWidget;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.gwt.client.widget.attribute.FeatureForm#setDisabled(boolean)
	 */
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;

		// Don't set disabled on the form, but on the individual items. This way it's easier to overwrite when creating
		// custom form items.
		for (AttributeInfo info : featureInfo.getAttributes()) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.gwt.client.widget.attribute.FeatureForm#isDisabled()
	 */
	public boolean isDisabled() {
		return disabled;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.gwt.client.widget.attribute.FeatureForm#validate()
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.gwt.client.widget.attribute.FeatureForm#addItemChangedHandler(
	 * com.smartgwt.client.widgets.form.events .ItemChangedHandler)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.gwt.client.widget.attribute.FeatureForm#toForm(java.lang.String,
	 * org.geomajas.layer.feature.Attribute)
	 */
	public void toForm(String name, Attribute<?> attribute) {
		AttributeInfo info = attributeInfoMap.get(name);
		if (!isIncluded(info)) {
			return;
		}
		FormItem item = formWidget.getField(info.getName());
		if (info instanceof PrimitiveAttributeInfo) {
			PrimitiveAttribute<?> primitive = (PrimitiveAttribute<?>) attribute;
			if (attribute == null && item != null) {
				item.setDisabled(true);
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
			Object associationItem = item.getAttributeAsObject(AssociationItem.ASSOCIATION_ITEM_ATTRIBUTE_KEY);
			AssociationAttributeInfo associationInfo = (AssociationAttributeInfo) info;
			if (associationItem != null) {
				switch (associationInfo.getType()) {
					case MANY_TO_ONE:
						((ManyToOneItem<?>) associationItem).toItem((ManyToOneAttribute) attribute);
						break;
					case ONE_TO_MANY:
						((OneToManyItem<?>) associationItem).toItem((OneToManyAttribute) attribute);
						break;
				}
			}
		}
		if (item != null) {
			item.fireEvent(new ChangedEvent(item.getJsObj()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.gwt.client.widget.attribute.FeatureForm#fromForm(java.lang.String,
	 * org.geomajas.layer.feature.Attribute)
	 */
	public void fromForm(String name, Attribute<?> attribute) {
		AttributeInfo info = attributeInfoMap.get(name);
		if (attribute == null || !isIncluded(info)) {
			return;
		}
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
			FormItem item = formWidget.getItem(name);
			Object associationItem = item.getAttributeAsObject(AssociationItem.ASSOCIATION_ITEM_ATTRIBUTE_KEY);
			switch (association.getType()) {
				case MANY_TO_ONE:
					((ManyToOneItem<?>) associationItem).fromItem((ManyToOneAttribute) attribute);
					break;
				case ONE_TO_MANY:
					((OneToManyItem<?>) associationItem).fromItem((OneToManyAttribute) attribute);
					break;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.gwt.client.widget.attribute.FeatureForm#clear()
	 */
	public void clear() {
		formWidget.clearValues();
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
	 * 
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
		 * @param name name of the item before which to insert
		 * @param newItem the item to insert
		 */
		public void insertBefore(String name, FormItem newItem) {
			int index = indexOf(name);
			if (index >= 0) {
				add(index, newItem);
			}
		}

		/**
		 * Insert a form item after the item with the specified name.
		 * @param name name of the item after which to insert
		 * @param newItem the item to insert
		 */
		public void insertAfter(String name, FormItem newItem) {
			int index = indexOf(name);
			if (index >= 0) {
				add(index + 1, newItem);
			}
		}
	}
}