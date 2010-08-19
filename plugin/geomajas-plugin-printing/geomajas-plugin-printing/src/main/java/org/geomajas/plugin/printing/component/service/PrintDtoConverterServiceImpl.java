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
import org.geomajas.plugin.printing.PrintingException;
import org.geomajas.plugin.printing.component.PrintComponent;
import org.geomajas.plugin.printing.component.dto.PrintComponentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Implementation of a print DTO converter service. Prints to pdf.
 * 
 * @author Jan De Moerloose
 * 
 */
@Component
public class PrintDtoConverterServiceImpl implements PrintDtoConverterService {

	@Autowired
	private ApplicationContext applicationContext;

	public PrintComponent toInternal(PrintComponentInfo info) throws PrintingException {
		Object bean = null;
		// creates a new component, this is a prototype !!!
		bean = applicationContext.getBean(info.getPrototypeName());
		if (bean == null || !(bean instanceof PrintComponent)) {
			throw new PrintingException(PrintingException.DTO_IMPLEMENTATION_NOT_FOUND,
					info.getClass().getSimpleName(), info.getPrototypeName());
		}
		PrintComponent component = (PrintComponent) bean;
		component.fromDto(info, this);
		for (PrintComponentInfo child : info.getChildren()) {
			PrintComponent childComponent = toInternal(child);
			component.addComponent(childComponent);
		}
		return component;
	}

	public Color toInternal(String color) {
		return Color.decode(color);
	}

	public Font toInternal(FontStyleInfo info) {
		int style = Font.PLAIN;
		if ("bold".equalsIgnoreCase(info.getStyle())) {
			style = Font.BOLD;
		} else if ("italic".equalsIgnoreCase(info.getStyle())) {
			style = Font.ITALIC;
		}
		return new Font(info.getFamily(), style, info.getSize());
	}
}
