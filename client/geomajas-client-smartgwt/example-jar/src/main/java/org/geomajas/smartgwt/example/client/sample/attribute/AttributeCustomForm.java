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

package org.geomajas.smartgwt.example.client.sample.attribute;

import org.geomajas.configuration.AbstractReadOnlyAttributeInfo;
import org.geomajas.smartgwt.client.map.layer.VectorLayer;
import org.geomajas.smartgwt.client.widget.attribute.DefaultFeatureForm;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.RowSpacerItem;
import org.geomajas.smartgwt.client.widget.attribute.FormItemList;

/**
 * Custom attribute form.
 * 
 * @author Jan De Moerloose
 */
// @extract-start CustomFeatureForm, CustomFeatureForm
// Creating the custom form:
public class AttributeCustomForm extends DefaultFeatureForm {

	public AttributeCustomForm(VectorLayer vectorLayer) {
		super(vectorLayer);
	}

	@Override
	public FormItem createItem(AbstractReadOnlyAttributeInfo info) {
		FormItem formItem = super.createItem(info);
		formItem.setWidth("*");
		if ("dateAttr".equals(info.getName())) {
			// The data attribute will span all 4 columns:
			formItem.setColSpan(4);
		}
		return formItem;
	}

	@Override
	public void prepareForm(FormItemList formItems, DataSource source) {
		// Quickly insert a row spacer before the 'stringAttr' item (which is the text area).
		formItems.insertBefore("stringAttr", new RowSpacerItem());
		getWidget().setNumCols(4);
		getWidget().setWidth(450);
		getWidget().setColWidths(100, 180, 20, 150);
		getWidget().setGroupTitle("Custom Attribute Form");
		getWidget().setIsGroup(true);
	}
	// @extract-end

}
