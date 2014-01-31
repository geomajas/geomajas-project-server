/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.printing.component.service;

import java.awt.Color;
import java.awt.Font;

import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.annotation.Api;
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
