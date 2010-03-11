/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.gwt.client.widget.attribute;

import java.util.List;

import org.geomajas.configuration.AttributeInfo;

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
}
