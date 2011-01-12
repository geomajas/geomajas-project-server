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

import org.geomajas.configuration.AttributeInfo;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.BlurbItem;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.FormItem;

/**
 * <p>
 * Editable form item implementation that can edit any kind of feature attribute. It starts by using a default
 * <code>TextItem</code> as <code>FormItem</code> representative. Every time the <code>setAttributeInfo</code> method is
 * called, a new internal <code>FormItem</code> will be created and shown in the place of the <code>TextItem</code>. In
 * order to create the correct representation for each kind of attribute, a {@link AttributeFormItemFactory} is used.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class AttributeFormItem extends CanvasItem {

	private AttributeInfo attributeInfo;

	private AttributeFormItemFactory factory;

	private DynamicForm form;

	private FormItem formItem;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Create the form item with the given. An internal form will already be created, and in that form a
	 * <code>TextItem</code> will be shown.
	 * 
	 * @param name
	 */
	public AttributeFormItem(String name) {
		super(name);
		factory = new DefaultAttributeFormItemFactory();

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

	/** Return the current active attribute information. */
	public AttributeInfo getAttributeInfo() {
		return attributeInfo;
	}

	/**
	 * Set a new attribute information object. This will alter the internal form, to display a new <code>FormItem</code>
	 * for the new type of attribute. In order to accomplish this, a {@link AttributeFormItemFactory} is used.
	 * 
	 * @param attributeInfo
	 *            The new attribute definition for whom to display the correct <code>FormItem</code>.
	 */
	public void setAttributeInfo(AttributeInfo attributeInfo) {
		this.attributeInfo = attributeInfo;
		formItem = factory.createEditableFormItem(attributeInfo);
		if (formItem != null) {
			formItem.setDisabled(false);
			formItem.setShowTitle(false);
			form.setFields(formItem);
			form.setDisabled(false);
			form.setCanFocus(true);
		}
	}

	/** Set a new width on this instance. Delegates to the internal form. */
	public void setWidth(int width) {
		form.setWidth(width);
		if (formItem != null) {
			formItem.setWidth(width);
		}
	}

	/** Get the current value form the internal <code>FormItem</code>. */
	public Object getValue() {
		if (formItem != null) {
			return formItem.getValue();
		}
		return null;
	}

	/** Return the form for the inner FormItem. On the returned form, validation will work. */
	public DynamicForm getForm() {
		return form;
	}
}
