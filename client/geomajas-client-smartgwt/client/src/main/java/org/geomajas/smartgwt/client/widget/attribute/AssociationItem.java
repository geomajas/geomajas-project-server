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

import org.geomajas.annotation.Api;

import com.smartgwt.client.widgets.form.fields.FormItem;

/**
 * <p>
 * An item representing an association attribute. The interface is generically defined using the actual {@link FormItem}
 * class as a parameter. This allows to accommodate different {@link FormItem} types (SelectItem, CanvasItem, etc...)
 * without having to resort to casting. The form item is accessible through the getItem() method.
 * </p>
 * 
 * @param <F> the form item class
 * 
 * @author Jan De Moerloose
 * @since 1.11.1
 */
@Api(allMethods = true)
public interface AssociationItem<F extends FormItem> {

	/**
	 * The key to store the item as an attribute in the actual form item. This can be used to retrieve the
	 * {@link AssociationItem} from the actual form item in cases where the form item is available but the
	 * {@link AssociationItem} not.
	 */
	String ASSOCIATION_ITEM_ATTRIBUTE_KEY = "_AssociationItemAttribute";

	/**
	 * Return the actual form item.
	 * 
	 * @return the actual form item
	 */
	F getItem();
}
