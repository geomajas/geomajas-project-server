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

import com.smartgwt.client.widgets.form.fields.FormItem;

/**
 * <p>
 * Factory definition for creation <code>FormItem</code> objects for specific kinds of attributes. These
 * <code>FormItem</code>s, can be used in forms (think of the attribute forms), or in grids (i.e. the FeatureSearch).
 * </p>
 * 
 * @author Pieter De Graef
 */
public interface AttributeFormItemFactory {

	/**
	 * Create a specific <code>FormItem</code> for the given type of attribute information.
	 * 
	 * @param attributeInfo
	 * @return
	 */
	FormItem createFormItem(AttributeInfo attributeInfo);

	/**
	 * Create a specific <code>FormItem</code> for the given type of attribute information where it's necessary that the
	 * user is able to change the value.
	 * 
	 * @param attributeInfo
	 * @return
	 */
	FormItem createEditableFormItem(AttributeInfo attributeInfo);
}
