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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.gwt.client.map.layer.VectorLayer;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.widgets.form.fields.FormItem;

/**
 * Default implementation of the {@link FeatureFormFactory}, which places all form fields underneath each other. It uses
 * the {@link AttributeFormFieldRegistry} to create all the {@link DataSourceField}s and {@link FormItem}s within the
 * form.
 * 
 * @author Pieter De Graef
 */
public class DefaultFeatureFormFactory implements FeatureFormFactory {

	public FeatureForm createFeatureForm(VectorLayer layer) {
		FeatureForm form = new FeatureForm(layer);
		DataSource source = new DataSource();
		List<FormItem> formItems = new ArrayList<FormItem>();
		for (AttributeInfo info : layer.getLayerInfo().getFeatureInfo().getAttributes()) {
			if (info.isIncludedInForm()) {
				DataSourceField field = AttributeFormFieldRegistry.createDataSourceField(layer, info);
				source.addField(field);

				FormItem formItem = AttributeFormFieldRegistry.createFormItem(layer, info);
				formItems.add(formItem);
			}
		}
		form.getWidget().setColWidths(150, "*");
		form.getWidget().setDataSource(source);
		form.getWidget().setFields(formItems.toArray(new FormItem[] {}));
		return form;
	}
}