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

import java.util.List;

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.gwt.client.i18n.I18nProvider;

import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.client.widgets.form.fields.FormItem;

/**
 * <p>
 * An attribute form implementation that allows for editing of the attributes. To this end it supports validation rules
 * on the different types of attributes, and a validate method.
 * </p>
 * 
 * @author Jan De Moerloose
 * @author Pieter De Graef
 */
public class EditableAttributeForm extends AbstractAttributeForm {

	/**
	 * Package visible constructor that immediately set the needed list of attribute definitions. This method is package
	 * visible as it should only be used by an {@link AttributeFormFactory}.
	 * 
	 * @param infoList
	 */
	EditableAttributeForm(List<AttributeInfo> infoList) {
		super(infoList);
	}

	/**
	 * Validate the contents of the entire attribute form.
	 * 
	 * @return Returns true if all values in the form are validated correctly, false otherwise.
	 */
	public boolean validate() {
		return form.validate();
	}

	/**
	 * Attach a handler that reacts to changes in the fields as the user makes them.
	 * 
	 * @param handler
	 * @return
	 */
	public HandlerRegistration addItemChangedHandler(ItemChangedHandler handler) {
		return form.addItemChangedHandler(handler);
	}

	// -------------------------------------------------------------------------
	// Private and protected methods:
	// -------------------------------------------------------------------------

	/**
	 * Package visible method for setting the <code>DataSource</code> that will form the actual SmartGWT
	 * <code>DynamicForm</code>. This method is package visible as it should only be used by an
	 * {@link AttributeFormFactory}.
	 */
	void setDataSource(DataSource dataSource) {
		form.setDataSource(dataSource);
		for (AttributeInfo info : infos.values()) {
			FormItem formItem = form.getItem(info.getName());
			formItem.setWidth(250);
			String tooltipParam = info.getValidator().getToolTip();
			if (tooltipParam != null && tooltipParam.length() > 0) {
				formItem.setTooltip(I18nProvider.lookupParameter(tooltipParam));
			}
		}
	}
}