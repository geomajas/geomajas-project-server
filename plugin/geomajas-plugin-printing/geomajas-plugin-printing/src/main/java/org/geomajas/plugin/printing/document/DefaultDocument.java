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
package org.geomajas.plugin.printing.document;

import java.io.OutputStream;
import java.util.Map;

import org.geomajas.layer.RasterLayerService;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.plugin.printing.component.service.PrintConfigurationService;
import org.geomajas.plugin.printing.configuration.DefaultConfigurationVisitor;
import org.geomajas.plugin.printing.configuration.MapConfigurationVisitor;
import org.geomajas.plugin.printing.configuration.PrintTemplate;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;

import com.lowagie.text.DocumentException;

/**
 * Default document for printing.
 * 
 * @author Jan De Moerloose
 */
public class DefaultDocument extends SinglePageDocument {

	private DefaultConfigurationVisitor defaultVisitor;

	private PrintConfigurationService configurationService;

	private GeoService geoService;

	private FilterService filterCreator;

	private VectorLayerService vectorLayerService;

	private RasterLayerService rasterLayerService;

	public DefaultDocument(String pageSize, PrintConfigurationService configurationService,
			Map<String, String> filters, DefaultConfigurationVisitor defaultVisitor, GeoService geoService,
			FilterService filterCreator, VectorLayerService vectorLayerService, RasterLayerService rasterLayerService) {
		super(PrintTemplate.createDefaultTemplate(pageSize, true, configurationService).getPage(), filters);
		this.configurationService = configurationService;
		this.defaultVisitor = defaultVisitor;
		this.geoService = geoService;
		this.filterCreator = filterCreator;
		this.vectorLayerService = vectorLayerService;
		this.rasterLayerService = rasterLayerService;
	}

	@Override
	public void render(OutputStream outputStream) throws DocumentException {
		defaultVisitor.visitTree(getPage());
		MapConfigurationVisitor visitor = new MapConfigurationVisitor(configurationService, geoService, filterCreator,
				vectorLayerService, rasterLayerService);
		visitor.visitTree(getPage());
		super.render(outputStream);
	}

}
