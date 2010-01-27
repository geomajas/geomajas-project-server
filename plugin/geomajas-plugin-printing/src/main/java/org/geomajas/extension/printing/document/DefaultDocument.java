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
package org.geomajas.extension.printing.document;

import org.geomajas.configuration.ApplicationInfo;
import org.geomajas.extension.printing.configuration.DefaultConfigurationVisitor;
import org.geomajas.extension.printing.configuration.MapConfigurationVisitor;
import org.geomajas.extension.printing.configuration.PrintTemplate;
import org.geomajas.rendering.painter.PaintFactory;
import org.geomajas.service.ApplicationService;
import org.geomajas.service.BboxService;
import org.geomajas.service.FilterCreator;
import org.geomajas.service.GeoService;

import java.util.Map;

/**
 * ???
 *
 * @author check subversion
 */
public class DefaultDocument extends SinglePageDocument {

	private DefaultConfigurationVisitor defaultVisitor;

	private ApplicationService runtime;

	private GeoService geoService;

	private BboxService bboxService;

	private FilterCreator filterCreator;

	private PaintFactory paintFactory;

	public DefaultDocument(String pageSize, ApplicationInfo application, ApplicationService runtime,
			Map<String, String> filters, DefaultConfigurationVisitor defaultVisitor, GeoService geoService,
			BboxService bboxService, FilterCreator filterCreator, PaintFactory paintFactory) {
		super(PrintTemplate.createDefaultTemplate(pageSize, true).getPage(), application, runtime, filters);
		this.runtime = runtime;
		this.defaultVisitor = defaultVisitor;
		this.geoService = geoService;
		this.bboxService = bboxService;
		this.filterCreator = filterCreator;
		this.paintFactory = paintFactory;
	}

	@Override
	public void render() {
		defaultVisitor.visitTree(getPage());
		MapConfigurationVisitor visitor = new MapConfigurationVisitor(runtime, geoService, bboxService, filterCreator,
				paintFactory);
		visitor.visitTree(getPage());
		super.render();
	}

}
