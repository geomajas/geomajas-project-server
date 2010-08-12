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
import org.geomajas.layer.RasterLayerService;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.plugin.printing.component.PrintComponent;
import org.geomajas.plugin.printing.component.dto.ImageComponentInfo;
import org.geomajas.plugin.printing.component.dto.LabelComponentInfo;
import org.geomajas.plugin.printing.component.dto.LegendComponentInfo;
import org.geomajas.plugin.printing.component.dto.LegendIconComponentInfo;
import org.geomajas.plugin.printing.component.dto.LegendItemComponentInfo;
import org.geomajas.plugin.printing.component.dto.MapComponentInfo;
import org.geomajas.plugin.printing.component.dto.PageComponentInfo;
import org.geomajas.plugin.printing.component.dto.PrintComponentInfo;
import org.geomajas.plugin.printing.component.dto.RasterLayerComponentInfo;
import org.geomajas.plugin.printing.component.dto.ScaleBarComponentInfo;
import org.geomajas.plugin.printing.component.dto.VectorLayerComponentInfo;
import org.geomajas.plugin.printing.component.dto.ViewPortComponentInfo;
import org.geomajas.plugin.printing.component.impl.ImageComponentImpl;
import org.geomajas.plugin.printing.component.impl.LabelComponentImpl;
import org.geomajas.plugin.printing.component.impl.LegendComponentImpl;
import org.geomajas.plugin.printing.component.impl.LegendIconComponentImpl;
import org.geomajas.plugin.printing.component.impl.LegendItemComponentImpl;
import org.geomajas.plugin.printing.component.impl.MapComponentImpl;
import org.geomajas.plugin.printing.component.impl.PageComponentImpl;
import org.geomajas.plugin.printing.component.impl.RasterLayerComponentImpl;
import org.geomajas.plugin.printing.component.impl.ScaleBarComponentImpl;
import org.geomajas.plugin.printing.component.impl.VectorLayerComponentImpl;
import org.geomajas.plugin.printing.component.impl.ViewPortComponentImpl;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.springframework.beans.factory.annotation.Autowired;
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
	private GeoService geoService;

	@Autowired
	private FilterService filterService;

	@Autowired
	private VectorLayerService vectorLayerService;

	@Autowired
	private RasterLayerService rasterLayerService;

	@Autowired
	private PrintConfigurationService configurationService;

	public PrintComponent toInternal(PrintComponentInfo info) {
		PrintComponent component = null;
		if (info instanceof ImageComponentInfo) {
			component = new ImageComponentImpl();
		} else if (info instanceof LabelComponentInfo) {
			component = new LabelComponentImpl();
		} else if (info instanceof LegendComponentInfo) {
			component = new LegendComponentImpl();
		} else if (info instanceof LegendItemComponentInfo) {
			component = new LegendItemComponentImpl();
		} else if (info instanceof LegendIconComponentInfo) {
			component = new LegendIconComponentImpl();
		} else if (info instanceof MapComponentInfo) {
			component = new MapComponentImpl();
		} else if (info instanceof PageComponentInfo) {
			component = new PageComponentImpl();
		} else if (info instanceof RasterLayerComponentInfo) {
			component = new RasterLayerComponentImpl(rasterLayerService, configurationService);
		} else if (info instanceof ScaleBarComponentInfo) {
			component = new ScaleBarComponentImpl(configurationService);
		} else if (info instanceof VectorLayerComponentInfo) {
			component = new VectorLayerComponentImpl(geoService, filterService, vectorLayerService,
					configurationService);
		} else if (info instanceof ViewPortComponentInfo) {
			component = new ViewPortComponentImpl();
		}
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
