/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.panels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

/**
 * @author Kristof Heirwegh
 */
public class KeyValueForm extends DynamicForm {

	public static final String ITEMTYPE_TEXT = "TextItem";

	public static final String ITEMTYPE_PASSWORD = "PasswordItem";

	private static final String DEFAULT_WIDTH = "*";

	private List<FormItem> fields = new ArrayList<FormItem>();

	private Map<String, String> values;

	public KeyValueForm() {
		setWidth100();
		setColWidths("125", "*");
	}

	public void updateFields(List<FormElement> items) {
		if (items == null || items.size() == 0) {
			return;
		}
		fields.clear();

		for (FormElement el : items) {
			FormItem fi = null;
			if (el.getItemType() == null || ITEMTYPE_TEXT.equals(el.getItemType())) {
				fi = new TextItem();
			} else if (ITEMTYPE_PASSWORD.equals(el.getItemType())) {
				fi = new PasswordItem();
			} else {
				SC.logWarn("Unsupported item type: " + el.getItemType());
				fi = new PasswordItem();
			}
			fi.setName(el.getName());
			fi.setTitle(el.getTitle());
			fi.setTooltip(el.getTooltip());
			if (el.getWidth() > 0) {
				fi.setWidth(el.getWidth());
			} else {
				fi.setWidth(DEFAULT_WIDTH);
			}
			fi.setRequired(el.isRequired());
			if (el.getDefaultValue() != null) {
				fi.setDefaultValue(el.getDefaultValue());
			}

			fields.add(fi);
		}

		setFields(fields.toArray(new FormItem[fields.size()]));
		markForRedraw();
	}

	public void setData(Map<String, String> values) {
		this.values = values;
		for (FormItem fi : fields) {
			fi.setValue(values.get(fi.getName()));
		}
	}

	public Map<String, String> getData(boolean excludeEmptyValues) {
		for (FormItem fi : fields) {
			String value = (fi.getValue() == null ? null : fi.getValue().toString());
			if (excludeEmptyValues && (value == null || "".equals(value))) {
				values.remove(fi.getName());
			} else {
				values.put(fi.getName(), value);
			}
		}
		return values;
	}

	public Map<String, String> getData() {
		return getData(false);
	}
}
