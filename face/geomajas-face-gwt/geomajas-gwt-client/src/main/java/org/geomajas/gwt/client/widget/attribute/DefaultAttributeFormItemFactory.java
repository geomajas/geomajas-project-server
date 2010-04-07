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

import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;

import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.form.fields.BooleanItem;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.FloatItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.LinkItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

/**
 * <p>
 * Default implementation of the <code>AttributeFormItemFactory</code> interface.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class DefaultAttributeFormItemFactory implements AttributeFormItemFactory {

	/**
	 * Create a specific <code>FormItem</code> for the given type of attribute information. The main focus here is that
	 * the returned <code>FormItem</code> is easy to read. It does not have to be changeable.
	 * 
	 * @param attributeInfo
	 *            The attribute definition for which to create a <code>FormItem</code>.
	 * @return
	 */
	public FormItem createFormItem(AttributeInfo attributeInfo) {
		if (attributeInfo instanceof PrimitiveAttributeInfo) {
			return createFormItem((PrimitiveAttributeInfo) attributeInfo, false);
		} else if (attributeInfo instanceof AssociationAttributeInfo) {
			return createFormItem((AssociationAttributeInfo) attributeInfo);
		}
		return null;
	}

	/**
	 * Create a specific <code>FormItem</code> for the given type of attribute information, where it's vital that the
	 * user can change it's value.
	 */
	public FormItem createEditableFormItem(AttributeInfo attributeInfo) {
		if (attributeInfo instanceof PrimitiveAttributeInfo) {
			return createFormItem((PrimitiveAttributeInfo) attributeInfo, true);
		} else if (attributeInfo instanceof AssociationAttributeInfo) {
			return createFormItem((AssociationAttributeInfo) attributeInfo);
		}
		return null;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private FormItem createFormItem(PrimitiveAttributeInfo info, boolean editable) {
		FormItem item = null;
		switch (info.getType()) {
			case BOOLEAN:
				item = new BooleanItem();
				break;
			case SHORT:
				item = new IntegerItem();
				break;
			case INTEGER:
				item = new IntegerItem();
				break;
			case LONG:
				item = new IntegerItem();
				break;
			case FLOAT:
				item = new FloatItem();
				break;
			case DOUBLE:
				item = new FloatItem();
				break;
			case CURRENCY:
				item = new TextItem();
				break;
			case STRING:
				item = new TextItem();
				break;
			case DATE:
				item = new DateItem();
				break;
			case URL:
				if (editable) {
					item = new TextItem();
				} else {
					item = new LinkItem();
				}
				break;
			case IMGURL:
				if (editable) {
					item = new TextItem();
				} else {
					item = new CanvasItem();
					Img image = new Img();
					image.setMaxHeight(200);
					image.setMaxWidth(300);
					image.setShowDisabled(false);
					((CanvasItem) item).setCanvas(image);
				}
				break;
		}
		item.setName(info.getName());
		item.setTitle(info.getLabel());
		item.setDisabled(true);
		return item;
	}

	private FormItem createFormItem(AssociationAttributeInfo attributeInfo) {
		// TODO implement FormItems for Associations...
		return null;
	}
}
