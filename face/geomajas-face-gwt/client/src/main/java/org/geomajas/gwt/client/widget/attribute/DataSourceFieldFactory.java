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

import com.smartgwt.client.data.DataSourceField;
import org.geomajas.annotation.Api;

/**
 * Definition of a factory capable of creating a certain kind of {@link com.smartgwt.client.data.DataSourceField}. 
 * These factories are stored within {@link AttributeFormFieldRegistry} and used for building
 * {@link org.geomajas.gwt.client.widget.attribute.DefaultFeatureForm}s.
 *
 * @author Pieter De Graef
 * @since 1.10.0
 */
@Api(allMethods = true)
public interface DataSourceFieldFactory {

	/**
	 * Create the data source field.
	 * 
	 * @return data source field
	 */
	DataSourceField create();
}
