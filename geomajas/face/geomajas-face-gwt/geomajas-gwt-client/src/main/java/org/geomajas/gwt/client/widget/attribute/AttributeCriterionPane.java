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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.layer.feature.SearchCriterion;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
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

	private DynamicForm form;

	private SelectItem attributeSelect;

	private SelectItem operatorSelect;

	private AttributeFormItem valueItem;

	private VectorLayer layer;

	private AttributeInfo selectedAttribute;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Create a search criterion pane, for the given vector layer. The layer is required, as it's list of attribute
	 * definitions are a vital part of the search criteria.
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
	 * Return the actual search criterion object, or null if not all fields have been properly filled.
	 */
	public SearchCriterion getSearchCriterion() {
		Object operator = operatorSelect.getValue();
		Object value = valueItem.getValue();

		if (selectedAttribute != null && operator != null && value != null) {
			SearchCriterion criterion = new SearchCriterion();
			criterion.setAttributeName(selectedAttribute.getName());
			criterion.setOperator(getOperatorCodeFromLabel((String) operator));
			criterion.setValue((String) value);
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
	 */
	public static String[] getOperatorsForAttributeType(AttributeInfo attributeInfo) {
		if (attributeInfo != null && attributeInfo instanceof PrimitiveAttributeInfo) {
			PrimitiveAttributeInfo primitive = (PrimitiveAttributeInfo) attributeInfo;
			switch (primitive.getType()) {
				case BOOLEAN:
					return new String[] { I18nProvider.getSearch().operatorEquals(),
							I18nProvider.getSearch().operatorNotEquals() };
				case SHORT:
					return new String[] { I18nProvider.getSearch().operatorEquals(),
							I18nProvider.getSearch().operatorNotEquals(), I18nProvider.getSearch().operatorST(),
							I18nProvider.getSearch().operatorSE(), I18nProvider.getSearch().operatorBT(),
							I18nProvider.getSearch().operatorBE() };
				case INTEGER:
					return new String[] { I18nProvider.getSearch().operatorEquals(),
							I18nProvider.getSearch().operatorNotEquals(), I18nProvider.getSearch().operatorST(),
							I18nProvider.getSearch().operatorSE(), I18nProvider.getSearch().operatorBT(),
							I18nProvider.getSearch().operatorBE() };
				case LONG:
					return new String[] { I18nProvider.getSearch().operatorEquals(),
							I18nProvider.getSearch().operatorNotEquals(), I18nProvider.getSearch().operatorST(),
							I18nProvider.getSearch().operatorSE(), I18nProvider.getSearch().operatorBT(),
							I18nProvider.getSearch().operatorBE() };
				case FLOAT:
					return new String[] { I18nProvider.getSearch().operatorEquals(),
							I18nProvider.getSearch().operatorNotEquals(), I18nProvider.getSearch().operatorST(),
							I18nProvider.getSearch().operatorSE(), I18nProvider.getSearch().operatorBT(),
							I18nProvider.getSearch().operatorBE() };
				case DOUBLE:
					return new String[] { I18nProvider.getSearch().operatorEquals(),
							I18nProvider.getSearch().operatorNotEquals(), I18nProvider.getSearch().operatorST(),
							I18nProvider.getSearch().operatorSE(), I18nProvider.getSearch().operatorBT(),
							I18nProvider.getSearch().operatorBE() };
				case DATE:
					return new String[] { I18nProvider.getSearch().operatorEquals(),
							I18nProvider.getSearch().operatorNotEquals(), I18nProvider.getSearch().operatorST(),
							I18nProvider.getSearch().operatorSE(), I18nProvider.getSearch().operatorBT(),
							I18nProvider.getSearch().operatorBE() };
				case CURRENCY:
					return new String[] { I18nProvider.getSearch().operatorEquals(),
							I18nProvider.getSearch().operatorNotEquals(), I18nProvider.getSearch().operatorST(),
							I18nProvider.getSearch().operatorSE(), I18nProvider.getSearch().operatorBT(),
							I18nProvider.getSearch().operatorBE() };
				case STRING:
					return new String[] { I18nProvider.getSearch().operatorContains(),
							I18nProvider.getSearch().operatorEquals(), I18nProvider.getSearch().operatorNotEquals() };
				case URL:
					return new String[] { I18nProvider.getSearch().operatorContains(),
							I18nProvider.getSearch().operatorEquals(), I18nProvider.getSearch().operatorNotEquals() };
				case IMGURL:
					return new String[] { I18nProvider.getSearch().operatorContains(),
							I18nProvider.getSearch().operatorEquals(), I18nProvider.getSearch().operatorNotEquals() };
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
			}
		}
		return null;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private void buildUI() {

		// Attribute select:
		attributeSelect = new SelectItem("attributeItem");
		attributeSelect.setWidth(140);
		attributeSelect.setShowTitle(false);
		List<String> labels = new ArrayList<String>();
		for (AttributeInfo attribute : layer.getLayerInfo().getFeatureInfo().getAttributes()) {
			labels.add(attribute.getLabel());
		}
		attributeSelect.setValueMap(labels.toArray(new String[0]));
		attributeSelect.setHint(I18nProvider.getSearch().gridChooseAttribute());
		attributeSelect.setShowHintInField(true);

		// Operator select:
		operatorSelect = new SelectItem("operatorItem");
		operatorSelect.setDisabled(true);
		operatorSelect.setWidth(140);
		operatorSelect.setShowTitle(false);

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

	private AttributeInfo getSelectedAttribute() {
		Object value = attributeSelect.getValue();
		if (value != null) {
			for (AttributeInfo attributeInfo : layer.getLayerInfo().getFeatureInfo().getAttributes()) {
				if (attributeInfo.getLabel().equals((String) value)) {
					return attributeInfo;
				}
			}
		}
		return null;
	}
}
