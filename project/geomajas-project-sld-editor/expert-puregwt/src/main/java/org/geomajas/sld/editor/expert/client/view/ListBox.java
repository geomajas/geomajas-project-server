/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.sld.editor.expert.client.view;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;

/**
 * This singleselection listbox allows you to use a map of String,String values & returns the selected value instead of
 * index.
 * <p>
 * This listbox has an empty first row (default).
 * 
 * @author Kristof Heirwegh
 */
public class ListBox extends com.google.gwt.user.client.ui.ListBox implements HasValue<String> {

	private boolean valueChangeHandlerInitialized;
	private boolean throwExceptionIfNotFound; // == false
	private boolean hasEmptyItem;
	private String emptyItem = "";
	private String emptyValue = "";

	public ListBox() {
		this(true);
	}

	/**
	 * @param hasEmptyItem if true the list will always contain an empty item (eg. this allows you to select
	 *        'none-of-the-above').
	 */
	public ListBox(boolean hasEmptyItem) {
		super(false);
		this.hasEmptyItem = hasEmptyItem;
		if (hasEmptyItem) {
			addItem(emptyItem, emptyValue);
		}
	}

	@Override
	public void clear() {
		super.clear();
		if (hasEmptyItem) {
			addItem(emptyItem, emptyValue);
		}
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		// Initialization code
		if (!valueChangeHandlerInitialized) {
			valueChangeHandlerInitialized = true;
			addChangeHandler(new ChangeHandler() {

				public void onChange(ChangeEvent event) {
					ValueChangeEvent.fire(ListBox.this, getValue());
				}
			});
		}
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public String getValue() {
		if (getSelectedIndex() < 1) { // 0 == empty value
			return null;
		} else {
			return getValue(getSelectedIndex());
		}
	}

	/**
	 * This selects the row with given value, or nothing (0) if value is not found.
	 * 
	 * @throws IllegalArgumentException if value is not found in list.
	 */
	@Override
	public void setValue(String value) {
		setValue(value, false);
	}

	/**
	 * This selects the row with given value, or nothing (0) if value is not found.
	 * 
	 * @throws IllegalArgumentException if value is not found in list (and value != null).
	 */
	@Override
	public void setValue(String value, boolean fireEvents) {
		if (value == null || "".equals(value)) {
			setSelectedIndex(0);
		} else {
			int newIdx = -1;
			for (int i = 1; i < getItemCount(); i++) {
				if (value.equals(getValue(i))) {
					newIdx = i;
					break;
				}
			}
			if (newIdx > 0) {
				setSelectedIndex(newIdx);
			} else {
				if (throwExceptionIfNotFound) {
					throw new IllegalArgumentException("Ongeldige waarde: " + value);
				} else {
					setSelectedIndex(0);
				}
			}
		}
		if (fireEvents) {
			ValueChangeEvent.fire(ListBox.this, getValue());
		}
	}

	public void setItems(LinkedHashMap<String, String> data) {
		clear();
		if (data != null) {
			for (Entry<String, String> entry : data.entrySet()) {
				addItem(entry.getValue(), entry.getKey());
			}
		}
	}

	public boolean isThrowExceptionIfNotFound() {
		return throwExceptionIfNotFound;
	}

	public void setThrowExceptionIfNotFound(boolean throwExceptionIfNotFound) {
		this.throwExceptionIfNotFound = throwExceptionIfNotFound;
	}

	public void setEmptyItemValue(String emptyItem, String emptyValue) {
		this.emptyItem = emptyItem;
		this.emptyValue = emptyValue;
	}

}
