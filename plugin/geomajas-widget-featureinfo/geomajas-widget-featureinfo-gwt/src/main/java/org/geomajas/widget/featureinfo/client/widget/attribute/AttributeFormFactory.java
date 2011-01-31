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

import com.smartgwt.client.data.DataSourceField;

/**
 * Definition of a factory that is able to create different types of attribute forms. An attribute form is meant to
 * display (and/or edit and/or validate) the attributes of one single feature.
 *
 * @author Jan De Moerloose
 * @author Pieter De Graef
 */
public interface AttributeFormFactory {

	/**
	 * Creates a form using the specified attribute information.
	 *
	 * @param infos
	 *            List of attribute definitions. Normally taken from a {@link VectorLayer}.
	 * @return An attribute form that allows for editing of it's values.
	 */
	EditableAttributeForm createEditableForm(List<AttributeInfo> infos);

	/**
	 * Creates a form using the specified attribute information.
	 *
	 * @param infos
	 *            List of attribute definitions. Normally taken from a {@link VectorLayer}.
	 * @return An attribute form that does not allow editing, but simply display the values.
	 */
	SimpleAttributeForm createSimpleForm(List<AttributeInfo> infos);

	/**
	 * Create a DataSourceField that represents a single attribute. On the DataSourceField, you'll immediately find
	 * default validators.
	 *
	 * @param info
	 *            The type of attribute to create a suitable DataSourceField for.
	 * @return Returns an appropriate DataSourceField for the type of attribute.
	 */
	DataSourceField createDataSourceField(AttributeInfo info);
}
