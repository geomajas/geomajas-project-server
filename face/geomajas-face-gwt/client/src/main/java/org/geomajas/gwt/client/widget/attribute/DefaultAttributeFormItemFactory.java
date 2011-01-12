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
				item.setValue(false); // avoid the null value
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
		item.setValidateOnChange(true);
		return item;
	}

	private FormItem createFormItem(AssociationAttributeInfo attributeInfo) {
		// TODO implement FormItems for Associations...
		return null;
	}
}
