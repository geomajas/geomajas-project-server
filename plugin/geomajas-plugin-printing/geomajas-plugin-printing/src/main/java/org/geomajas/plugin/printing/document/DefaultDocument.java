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

import java.util.Map;

import org.geomajas.layer.VectorLayerService;
import org.geomajas.plugin.printing.configuration.DefaultConfigurationVisitor;
import org.geomajas.plugin.printing.configuration.MapConfigurationVisitor;
import org.geomajas.plugin.printing.configuration.PrintTemplate;
import org.geomajas.service.ConfigurationService;
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

	private ConfigurationService runtime;

	private GeoService geoService;

	private FilterService filterCreator;

	private VectorLayerService layerService;

	public DefaultDocument(String pageSize, ConfigurationService runtime, Map<String, String> filters,
			DefaultConfigurationVisitor defaultVisitor, GeoService geoService, FilterService filterCreator,
			VectorLayerService layerService) {
		super(PrintTemplate.createDefaultTemplate(pageSize, true).getPage(), runtime, filters);
		this.runtime = runtime;
		this.defaultVisitor = defaultVisitor;
		this.geoService = geoService;
		this.filterCreator = filterCreator;
		this.layerService = layerService;
	}

	@Override
	public void render() throws DocumentException {
		defaultVisitor.visitTree(getPage());
		MapConfigurationVisitor visitor = new MapConfigurationVisitor(runtime, geoService, filterCreator, layerService);
		visitor.visitTree(getPage());
		super.render();
	}

}
