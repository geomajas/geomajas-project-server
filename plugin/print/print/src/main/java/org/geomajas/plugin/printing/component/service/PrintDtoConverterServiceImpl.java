/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
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
import org.geomajas.plugin.printing.PrintingException;
import org.geomajas.plugin.printing.component.PdfContext;
import org.geomajas.plugin.printing.component.PrintComponent;
import org.geomajas.plugin.printing.component.dto.PrintComponentInfo;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Implementation of a print DTO converter service. Prints to pdf.
 * 
 * @author Jan De Moerloose
 */
@Component
public class PrintDtoConverterServiceImpl implements PrintDtoConverterService {

	@Autowired
	private ApplicationContext applicationContext;
	
	private PdfContext context = new PdfContext(null);

	@SuppressWarnings("unchecked")
	public <T extends PrintComponentInfo> PrintComponent<T> toInternal(T info) throws PrintingException {
		// creates a new component, this is a prototype !!!
		Object bean;
		try {
			bean = applicationContext.getBean(info.getPrototypeName(), PrintComponent.class);
		} catch (BeansException be) {
			throw new PrintingException(be, PrintingException.DTO_IMPLEMENTATION_NOT_FOUND,
					info.getClass().getSimpleName(), info.getPrototypeName());
		}
		PrintComponent<T> component = (PrintComponent<T>) bean;
		component.fromDto(info);
		for (PrintComponentInfo child : info.getChildren()) {
			PrintComponent<?> childComponent = toInternal(child);
			component.addComponent(childComponent);
		}
		return component;
	}

	public Color toInternal(String color) {
		return context.getColor(color, 1f);
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
