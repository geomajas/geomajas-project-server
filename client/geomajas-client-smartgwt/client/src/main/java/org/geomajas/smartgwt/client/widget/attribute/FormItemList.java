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

import com.smartgwt.client.widgets.form.fields.FormItem;
import org.geomajas.annotation.Api;

import java.util.Arrays;

import java.util.ArrayList;

/**
 * A list wrapper that allows easy insertion of form items by name.
 *
 * @author Jan De Moerloose
 * @since 1.11.1
 */
@Api(allMethods = true)
public final class FormItemList extends ArrayList<FormItem> {

	@Override
	public FormItem[] toArray() {
		return super.toArray(new FormItem[size()]);
	}

	/**
	 * Get the index of the field with given name.
	 *
	 * @param name field name
	 * @return index
	 */
	public int indexOf(String name) {
		int i = 0;
		for (FormItem formItem : this) {
			if (name.equals(formItem.getName())) {
				return i;
			}
			i++;
		}
		return -1;
	}

	/**
	 * Insert a form item before the item with the specified name.
	 *
	 * @param name name of the item before which to insert
	 * @param newItem the item to insert
	 */
	public void insertBefore(String name, FormItem... newItem) {
		int index = indexOf(name);
		if (index >= 0) {
			addAll(index, Arrays.asList(newItem));
		}
	}
}
