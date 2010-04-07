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

	/**
	 * Return the current active attribute information.
	 */
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

	/**
	 * Set a new width on this instance. Delegates to the internal form.
	 */
	public void setWidth(int width) {
		form.setWidth(width);
		if (formItem != null) {
			formItem.setWidth(width);
		}
	}

	/**
	 * Get the current value form the internal <code>FormItem</code>.
	 */
	public Object getValue() {
		if (formItem != null) {
			return formItem.getValue();
		}
		return null;
	}
}
