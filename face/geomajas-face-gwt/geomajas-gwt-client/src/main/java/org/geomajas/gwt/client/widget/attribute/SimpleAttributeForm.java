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
import org.geomajas.layer.feature.attribute.DateAttribute;
import org.geomajas.layer.feature.attribute.ImageUrlAttribute;

import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.FormItem;

/**
 * <p>
 * An attribute form implementation that simply visualizes all the attributes in a form table. There is no support for
 * editing.
 * </p>
 * 
 * @author Jan De Moerloose
 * @author Pieter De Graef
 */
public class SimpleAttributeForm extends AbstractAttributeForm {

	/**
	 * Package visible constructor that immediately set the needed list of attribute definitions. This method is package
	 * visible as it should only be used by an {@link AttributeFormFactory}.
	 * 
	 * @param infoList
	 */
	SimpleAttributeForm(List<AttributeInfo> infoList) {
		super(infoList);
	}

	/**
	 * Override the default way of setting an image attribute, as we need to set an <code>Img</code> source on a
	 * <code>CanvasItem</code>.
	 */
	protected void setValue(String name, ImageUrlAttribute attribute) {
		// form.setValue(name, attribute.getValue());
		CanvasItem formItem = (CanvasItem) form.getItem(name);
		((Img) formItem.getCanvas()).setSrc(attribute.getValue());
	}

	/**
	 * Override the default way of setting a Date value, as a DateItem is used behind the screens.
	 */
	protected void setValue(String name, DateAttribute attribute) {
		// clumsy, but how else ?
		((DateItem) form.getItem(name)).setValue(attribute.getValue());
	}

	// -------------------------------------------------------------------------
	// Private and protected methods:
	// -------------------------------------------------------------------------

	/**
	 * Package visible method for setting the list of <code>FormItem</code>s that will form the actual SmartGWT
	 * <code>DynamicForm</code>. This method is package visible as it should only be used by an
	 * {@link AttributeFormFactory}.
	 */
	void setFields(List<FormItem> fields) {
		form.setFields(fields.toArray(new FormItem[0]));
	}
}