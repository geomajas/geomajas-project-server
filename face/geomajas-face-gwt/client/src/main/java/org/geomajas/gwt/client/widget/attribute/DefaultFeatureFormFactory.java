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

package org.geomajas.gwt.client.widget.attribute;

import org.geomajas.gwt.client.map.layer.VectorLayer;

import com.smartgwt.client.widgets.form.DynamicForm;

/**
 * Default implementation of the {@link FeatureFormFactory}, which places all form fields underneath each other. It uses
 * the {@link AttributeFormFieldRegistry} to create all the fields and items within the form.
 * 
 * @author Pieter De Graef
 */
public class DefaultFeatureFormFactory implements FeatureFormFactory<DynamicForm> {

	/**
	 * Creates a form using the specified attribute information.
	 * 
	 * @param vectorLayer vector layer for which the form needs to be built
	 * @return An attribute form that allows for editing of it's values.
	 */
	public FeatureForm<DynamicForm> createFeatureForm(VectorLayer vectorLayer) {
		return new DefaultFeatureForm(vectorLayer);
	}

}