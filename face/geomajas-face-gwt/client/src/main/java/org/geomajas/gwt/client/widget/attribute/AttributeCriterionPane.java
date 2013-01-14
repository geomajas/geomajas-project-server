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

package org.geomajas.gwt.client.widget.attribute;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.geomajas.configuration.AbstractAttributeInfo;
import org.geomajas.configuration.AbstractReadOnlyAttributeInfo;
import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.AssociationType;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.configuration.SyntheticAttributeInfo;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.layer.feature.SearchCriterion;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.BlurbItem;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

/**
 * <p>
 * A canvas that represents the dynamic form for a single attribute search criterion. Since such a criterion exists of 3
 * items (attribute-name, operator, value), this form will also display 3 <code>FormItem</code>s for the user to fill.
 * The first is an attribute select, where the user select on what attribute to search. The second is an operator select
 * that automatically updates it's list of available operators, in accordance to the selected attribute. The third
 * <code>FormItem</code> will display an {@link AttributeFormItem}. This is a <code>FormItem</code> that is tailored to
 * handle any kind of attribute definition, by displaying the correct delegate <code>FormItem</code>.
 * </p>
 * <p>
 * On top of just displaying a form to create a criterion, it is also possible to retrieve the actual
 * {@link SearchCriterion} object.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class AttributeCriterionPane extends Canvas {

	private static final String CQL_WILDCARD = "%";
	private static final String CQL_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	private static final String ID_SUFFIX = ".@id";

	private DynamicForm form;

	private SelectItem attributeSelect;

	private SelectItem operatorSelect;

	private AttributeFormItem valueItem;

	private VectorLayer layer;

	private AbstractReadOnlyAttributeInfo selectedAttribute;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Create a search criterion pane, for the given vector layer. The layer is required, as it's list of attribute
	 * definitions are a vital part of the search criteria.
	 *
	 * @param layer layer to create criterion for
	 */
	public AttributeCriterionPane(VectorLayer layer) {
		super();
		this.layer = layer;

		buildUI();
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/**
	 * Validate the value that the user filled in. If it is not valid, don't ask for the SearchCriterion.
	 *
	 * @return true when user entered invalid value
	 */
	public boolean hasErrors() {
		return valueItem.getForm().hasErrors();
	}

	/**
	 * Return the actual search criterion object, or null if not all fields have been properly filled.
	 *
	 * @return search criterion
	 */
	public SearchCriterion getSearchCriterion() {
		Object operator = operatorSelect.getValue();
		Object value = valueItem.getValue();

		if (selectedAttribute != null && operator != null) {
			String operatorString = getOperatorCodeFromLabel(operator.toString());
			String valueString = "";
			String nameString = selectedAttribute.getName();
			
			if (value != null) {
				valueString = value.toString();
			}

			// CQL does not recognize "contains", so change to "like":
			if ("contains".equals(operatorString)) {
				operatorString = "LIKE";
				valueString = CQL_WILDCARD + valueString + CQL_WILDCARD;
			}

			// If value was null, and no "contains" operator, return null:
			if (valueString == null || valueString.length() == 0) {
				return null;
			}

			if (selectedAttribute instanceof PrimitiveAttributeInfo) {
				PrimitiveAttributeInfo attr = (PrimitiveAttributeInfo) selectedAttribute;

				if (attr.getType().equals(PrimitiveType.STRING) || attr.getType().equals(PrimitiveType.IMGURL)
						|| attr.getType().equals(PrimitiveType.URL)) {
					// In case of a string, add quotes:
					valueString = "'" + valueString + "'";

				} else if (attr.getType().equals(PrimitiveType.DATE)) {
					if (value instanceof Date) {
						// In case of a date, parse correctly for CQL: 2006-11-30T01:30:00Z
						DateTimeFormat format = DateTimeFormat.getFormat(CQL_TIME_FORMAT);

						if ("=".equals(operatorString)) {
							// Date equals not supported by CQL, so we use the DURING operator instead:
							operatorString = "DURING";
							Date date1 = (Date) value;
							Date date2 = new Date(date1.getTime() + 86400000); // total milliseconds in a day
							valueString = format.format(date1) + "/" + format.format(date2);
						} else {
							// format the date:
							valueString = format.format((Date) value);
						}
					}
				}
			} else if (selectedAttribute instanceof AssociationAttributeInfo) {
				AssociationAttributeInfo assInfo = (AssociationAttributeInfo) selectedAttribute;
				if (AssociationType.MANY_TO_ONE == assInfo.getType()) {
					nameString = nameString + ID_SUFFIX;
				}
			}

			// Now create the criterion:
			SearchCriterion criterion = new SearchCriterion();
			criterion.setAttributeName(nameString);
			criterion.setOperator(operatorString);
			criterion.setValue(valueString);
			return criterion;
		}
		return null;
	}

	/**
	 * Static method that return an array of available operator labels for the given attribute definition. An operator
	 * label is something like "is equal to". Don't confuse this with the operator code (something like "=").
	 * 
	 * @param attributeInfo
	 *            The attribute definition for which to return possible operators.
	 * @return operators
	 */
	public static String[] getOperatorsForAttributeType(AbstractReadOnlyAttributeInfo attributeInfo) {
		if (attributeInfo != null && attributeInfo instanceof PrimitiveAttributeInfo) {
			PrimitiveAttributeInfo primitive = (PrimitiveAttributeInfo) attributeInfo;
			switch (primitive.getType()) {
				case SHORT:
				case INTEGER:
				case LONG:
				case FLOAT:
				case DOUBLE:
				case CURRENCY:
					return new String[] { I18nProvider.getSearch().operatorEquals(),
							I18nProvider.getSearch().operatorNotEquals(), I18nProvider.getSearch().operatorST(),
							I18nProvider.getSearch().operatorSE(), I18nProvider.getSearch().operatorBT(),
							I18nProvider.getSearch().operatorBE() };
				case DATE:
					return new String[] { I18nProvider.getSearch().operatorEquals(),
							I18nProvider.getSearch().operatorBefore(), I18nProvider.getSearch().operatorAfter() };
				case STRING:
				case URL:
				case IMGURL:
					return new String[] { I18nProvider.getSearch().operatorContains(),
							I18nProvider.getSearch().operatorEquals(), I18nProvider.getSearch().operatorNotEquals() };
				case BOOLEAN:
				default:
					return new String[] { I18nProvider.getSearch().operatorEquals(),
							I18nProvider.getSearch().operatorNotEquals() };
			}
		}
		return new String[] { I18nProvider.getSearch().operatorEquals(), I18nProvider.getSearch().operatorNotEquals() };
	}

	/**
	 * Return the operator code from an operator label. The difference is this: an operator label is a string like
	 * "is equal to", while it's code is "=".
	 * 
	 * @param label
	 *            The operator label.
	 * @return operator code
	 */
	public static String getOperatorCodeFromLabel(String label) {
		if (label != null) {
			if (I18nProvider.getSearch().operatorEquals().equals(label)) {
				return "=";
			} else if (I18nProvider.getSearch().operatorNotEquals().equals(label)) {
				return "<>";
			} else if (I18nProvider.getSearch().operatorST().equals(label)) {
				return "<";
			} else if (I18nProvider.getSearch().operatorSE().equals(label)) {
				return "<=";
			} else if (I18nProvider.getSearch().operatorBT().equals(label)) {
				return ">";
			} else if (I18nProvider.getSearch().operatorBE().equals(label)) {
				return ">=";
			} else if (I18nProvider.getSearch().operatorContains().equals(label)) {
				return "contains";
			} else if (I18nProvider.getSearch().operatorBefore().equals(label)) {
				return "BEFORE";
			} else if (I18nProvider.getSearch().operatorAfter().equals(label)) {
				return "AFTER";
			}
		}
		return label;
	}
	
	public static final String[] getSearchableAttributes(VectorLayer layer) {
		List<String> labels = new ArrayList<String>();
		for (AbstractAttributeInfo attribute : layer.getLayerInfo().getFeatureInfo().getAttributes()) {
			if (attribute instanceof AbstractReadOnlyAttributeInfo && !(attribute instanceof SyntheticAttributeInfo)) {
				labels.add(((AbstractReadOnlyAttributeInfo) attribute).getLabel());
			}
		}
		return labels.toArray(new String[labels.size()]);
		
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private void buildUI() {

		// Attribute select:
		attributeSelect = new SelectItem("attributeItem");
		attributeSelect.setWidth(140);
		attributeSelect.setShowTitle(false);
		attributeSelect.setValueMap(getSearchableAttributes(layer));
		attributeSelect.setHint(I18nProvider.getSearch().gridChooseAttribute());
		attributeSelect.setShowHintInField(true);

		attributeSelect.setValidateOnChange(true);
		attributeSelect.setShowErrorStyle(true);
		attributeSelect.setRequired(true);

		// Operator select:
		operatorSelect = new SelectItem("operatorItem");
		operatorSelect.setDisabled(true);
		operatorSelect.setWidth(140);
		operatorSelect.setShowTitle(false);

		operatorSelect.setValidateOnChange(true);
		operatorSelect.setShowErrorStyle(true);
		operatorSelect.setRequired(true);

		// Value form item:
		valueItem = new AttributeFormItem("valueItem");
		valueItem.setShowTitle(false);
		valueItem.setDisabled(true);
		valueItem.setWidth(150);

		// Mechanisms:
		attributeSelect.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				selectedAttribute = getSelectedAttribute();
				if (selectedAttribute != null) {
					// Adjust operator value map and enabled:
					operatorSelect.setDisabled(false);
					String[] operators = getOperatorsForAttributeType(selectedAttribute);
					operatorSelect.setValueMap(operators);
					operatorSelect.setValue(operators[0]);

					// Adjust value form item and enable:
					valueItem.setAttributeInfo(selectedAttribute);
					valueItem.setDisabled(false);
					valueItem.setWidth(form.getWidth() - 290);
				}
			}
		});

		// Finalize:
		form = new DynamicForm();
		form.setNumCols(6);
		form.setHeight(26);
		form.setWidth100();
		form.setFields(attributeSelect, operatorSelect, valueItem);
		addChild(form);
	}

	private AbstractReadOnlyAttributeInfo getSelectedAttribute() {
		Object value = attributeSelect.getValue();
		if (value != null) {
			for (AbstractAttributeInfo attributeInfo : layer.getLayerInfo().getFeatureInfo().getAttributes()) {
				if (attributeInfo instanceof AbstractReadOnlyAttributeInfo && value.equals(
						((AbstractReadOnlyAttributeInfo) attributeInfo).getLabel())) {
					return (AbstractReadOnlyAttributeInfo) attributeInfo;
				}
			}
		}
		return null;
	}

	/**
	 * <p>
	 * Editable form item implementation that can edit any kind of feature attribute. It starts by using a default
	 * <code>TextItem</code> as <code>FormItem</code> representative. Every time the <code>setAttributeInfo</code>
	 * method is called, a new internal <code>FormItem</code> will be created and shown in the place of the
	 * <code>TextItem</code>. In order to create the correct representation for each kind of attribute, a
	 * {@link AttributeFormFieldRegistry} is used.
	 * </p>
	 * 
	 * @author Pieter De Graef
	 */
	private class AttributeFormItem extends CanvasItem {

		private DynamicForm form;

		private FormItem formItem;

		// -------------------------------------------------------------------------
		// Constructors:
		// -------------------------------------------------------------------------

		/**
		 * Create the form item with the given name. An internal form will already be created, and in that form a
		 * <code>TextItem</code> will be shown.
		 *
		 * @param name form item name
		 */
		public AttributeFormItem(String name) {
			super(name);

			form = new DynamicForm();
			form.setHeight(26);
			formItem = new BlurbItem();
			formItem.setShowTitle(false);
			formItem.setValue("...................");
			form.setFields(formItem);
			setCanvas(form);
		}

		// -------------------------------------------------------------------------
		// Public methods:
		// -------------------------------------------------------------------------

		/**
		 * Set a new attribute information object. This will alter the internal form, to display a new
		 * <code>FormItem</code> for the new type of attribute. In order to accomplish this, a
		 * {@link AttributeFormFieldRegistry} is used.
		 * 
		 * @param attributeInfo The new attribute definition for which to display the correct <code>FormItem</code>.
		 */
		public void setAttributeInfo(AbstractReadOnlyAttributeInfo attributeInfo) {
			formItem = AttributeFormFieldRegistry.createFormItem(attributeInfo, layer);
			if (formItem != null) {
				formItem.setDisabled(false);
				formItem.setShowTitle(false);
				form.setFields(formItem);
				form.setDisabled(false);
				form.setCanFocus(true);
			}
		}

		/**
		 * Set a new width on this instance. Delegates to the internal form.
		 *
		 * @param  width width
		 */
		public void setWidth(int width) {
			form.setWidth(width);
			if (formItem != null) {
				formItem.setWidth(width);
			}
		}

		/**
		 * Get the current value form the internal <code>FormItem</code>.
		 *
		 * @return value
		 */
		public Object getValue() {
			if (formItem != null) {
				return formItem.getValue();
			}
			return null;
		}

		/**
		 * Return the form for the inner FormItem. On the returned form, validation will work.
		 *
		 * @return form
		 */
		public DynamicForm getForm() {
			return form;
		}
	}
}