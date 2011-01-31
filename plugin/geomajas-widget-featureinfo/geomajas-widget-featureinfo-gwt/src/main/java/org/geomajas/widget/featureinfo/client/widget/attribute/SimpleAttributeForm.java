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
package org.geomajas.widget.featureinfo.client.widget.attribute;

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