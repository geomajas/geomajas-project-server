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

import org.geomajas.global.FutureApi;
import org.geomajas.global.UserImplemented;
import org.geomajas.gwt.client.map.layer.VectorLayer;

/**
 * <p>
 * Definition of a factory that is able to create different types of attribute forms. An attribute form is meant to
 * display (and/or edit and/or validate) the attributes of one single feature. There is a default implementation which
 * places all attribute form fields underneath each other, but feel free to write your own implementations to change the
 * default look and feel of an {@link FeatureForm}.
 * </p>
 * <p>
 * When creating your own implementations, it is recommended to set both a <code>DataSourceField</code> and
 * <code>FormItem</code> for every attribute you want to appear in the form. In order to acquire useful instances of
 * these, take a look at the {@link AttributeFormFieldRegistry}<br/>
 * That said, let's provide some example code that shows you how such a factory could be implemented:
 * </p>
 * <code>
 * <pre>
 * 	public FeatureForm createFeatureForm(VectorLayer layer) {
 * 
 * 		// Only make specific FeatureForms for a specific layer:
 * 		if (layer.getId().equals("my-specific-layer") {
 * 
 *          // Prepare the data source and form items:
 * 			DataSource source = new DataSource();
 * 			List<FormItem> formItems = new ArrayList<FormItem>();
 * 
 * 			for (AttributeInfo info : layer.getLayerInfo().getFeatureInfo().getAttributes()) {
 * 
 * 				// Only add attributes that want to. The AttributeFormFieldRegistry provides the rest:
 * 				if (info.isIncludedInForm()) {
 * 					DataSourceField field = AttributeFormFieldRegistry.createDataSourceField(layer, info);
 * 					source.addField(field);
 * 
 * 					FormItem formItem = AttributeFormFieldRegistry.createFormItem(layer, info);
 * 					formItems.add(formItem);
 * 				}
 * 			}
 * 
 *          // Create the form, with some specifics:
 * 			FeatureForm form = new FeatureForm(layer);
 * 			form.getWidget().setGroupTitle("My Custom Attribute Form");
 * 			form.getWidget().setIsGroup(true);
 * 			form.getWidget().setNumCols(4);
 * 			form.getWidget().setWidth(450);
 * 			form.getWidget().setColWidths(100, 180, 20, 150);
 * 			form.getWidget().setDataSource(source);
 * 			form.getWidget().setFields(formItems.toArray(new FormItem[] {}));
 * 			return form;
 * 		}
 * 
 * 		// For all other layers, use the default factory:
 * 		DefaultFeatureFormFactory defaultFactory = new DefaultFeatureFormFactory();
 * 		return factory.createFeatureForm(layer);
 * 	}
 * 	</pre>
 * </code>
 * 
 * @author Pieter De Graef
 */
@FutureApi(allMethods = true)
@UserImplemented
public interface FeatureFormFactory {

	/**
	 * Creates a form using the specified attribute information.
	 * 
	 * @param infos
	 *            List of attribute definitions. Normally taken from a {@link VectorLayer}.
	 * @return An attribute form that allows for editing of it's values.
	 */
	FeatureForm createFeatureForm(VectorLayer layer);
}