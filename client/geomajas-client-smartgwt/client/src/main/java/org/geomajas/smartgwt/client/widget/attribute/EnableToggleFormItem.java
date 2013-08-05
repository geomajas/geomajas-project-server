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
package org.geomajas.smartgwt.client.widget.attribute;

import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Base class for the default URL and IMGURL type form items. It allows to switch between 2 types of form items when
 * enabled/disabled.
 * 
 * @author Pieter De Graef
 */
public class EnableToggleFormItem extends CanvasItem {

	private static final String FORM_FIELD_NAME = "theOnlyOne";

	private VLayout layout;

	private DynamicForm enabledForm;

	private DynamicForm disabledForm;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	public EnableToggleFormItem() {
	}

	public EnableToggleFormItem(FormItem enabledItem, FormItem disabledItem) {
		initialize(enabledItem, disabledItem);
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	@Override
	public void clearValue() {
		super.clearValue();
		enabledForm.clearValues();
		disabledForm.clearValues();
	}

	public void initialize(FormItem enabledItem, FormItem disabledItem) {
		setWidth("*");
		setHeight(1);
		layout = new VLayout();

		enabledForm = new DynamicForm();
		enabledForm.setWidth("100%");
		enabledItem.setName(FORM_FIELD_NAME);
		enabledItem.setShowTitle(false);
		enabledItem.setWidth("*");
		enabledItem.setColSpan(3);
		enabledForm.setFields(enabledItem);
		enabledForm.setVisible(false);

		disabledForm = new DynamicForm();
		disabledForm.setWidth("100%");
		disabledItem.setName(FORM_FIELD_NAME);
		disabledItem.setShowTitle(false);
		disabledItem.setWidth("*");
		disabledForm.setFields(disabledItem);
		disabledForm.setVisible(false);

		layout.addMember(enabledForm);
		layout.addMember(disabledForm);
		setCanvas(layout);

		enabledForm.addItemChangedHandler(new ItemChangedHandler() {

			public void onItemChanged(ItemChangedEvent event) {
				String disabledValue = (event.getNewValue() == null ? null : event.getNewValue().toString());
				disabledForm.getItem(FORM_FIELD_NAME).setValue(disabledValue);
			}
		});
	}

	// ------------------------------------------------------------------------
	// Method overriding:
	// ------------------------------------------------------------------------

	public HandlerRegistration addChangedHandler(final ChangedHandler handler) {
		return enabledForm.addItemChangedHandler(new ItemChangedHandler() {

			public void onItemChanged(ItemChangedEvent event) {
				handler.onChanged(new ChangedEvent(EnableToggleFormItem.this.getJsObj()));
			}
		});

	}

	public Object getValue() {
		return enabledForm.getValue(FORM_FIELD_NAME);
	}

	public void setValue(String value) {
		enabledForm.getItem(FORM_FIELD_NAME).setValue(value);
		disabledForm.getItem(FORM_FIELD_NAME).setValue(value);
		super.setValue(value);
	}

	public void setDisabled(Boolean disabled) {
		boolean realValue = disabled.booleanValue();
		enabledForm.setVisible(!realValue);
		disabledForm.setVisible(realValue);
	}

	@Override
	public Boolean validate() {
		return enabledForm.validate();
	}


}