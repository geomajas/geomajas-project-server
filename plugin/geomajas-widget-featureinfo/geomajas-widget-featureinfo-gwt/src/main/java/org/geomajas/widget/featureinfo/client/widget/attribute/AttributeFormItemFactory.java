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
