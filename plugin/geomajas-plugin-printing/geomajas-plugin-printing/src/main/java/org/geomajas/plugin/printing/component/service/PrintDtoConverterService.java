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
package org.geomajas.plugin.printing.component.service;

import java.awt.Color;
import java.awt.Font;

import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.global.Api;
import org.geomajas.plugin.printing.PrintingException;
import org.geomajas.plugin.printing.component.PrintComponent;
import org.geomajas.plugin.printing.component.dto.PrintComponentInfo;

/**
 * Service to convert print DTO components to internal representation and back.
 * 
 * @author Jan De Moerloose
 * @since 2.0.0
 * 
 */
@Api(allMethods = true)
public interface PrintDtoConverterService {

	/**
	 * Converts a print DTO component to its internal representative.
	 * 
	 * @param info
	 *            the DTO component
	 * @return the internal representative
	 * @throws PrintingException
	 *             the internal bean definition is missing
	 */
	<T extends PrintComponentInfo> PrintComponent<T> toInternal(T info) throws PrintingException;

	/**
	 * Converts a hexadecimal color string to an AWT color.
	 * 
	 * @param color
	 *            the color string
	 * @return the AWT color
	 */
	Color toInternal(String color);

	/**
	 * Converts a font style DTO to an AWT font.
	 * 
	 * @param info
	 *            the font style DTO
	 * @return the AWT font
	 */
	Font toInternal(FontStyleInfo info);
}
