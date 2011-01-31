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
			// NPE: int width = formItem.getWidth();
			formItem.setWidth("*");
			String tooltipParam = info.getValidator().getToolTip();
			if (tooltipParam != null && tooltipParam.length() > 0) {
				formItem.setTooltip(I18nProvider.lookupParameter(tooltipParam));
			}
		}
	}
}