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
package org.geomajas.plugin.printing.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.layer.RasterLayerService;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.plugin.printing.component.LegendComponent;
import org.geomajas.plugin.printing.component.MapComponent;
import org.geomajas.plugin.printing.component.TopDownVisitor;
import org.geomajas.plugin.printing.component.impl.RasterLayerComponentImpl;
import org.geomajas.plugin.printing.component.impl.VectorLayerComponentImpl;
import org.geomajas.plugin.printing.component.service.PrintConfigurationService;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;

/**
 * Visitor that configures the default layers of all the maps and legends in the print component. Maps and legends
 * should have valid map id's.
 * 
 * @author Jan De Moerloose
 */
public class MapConfigurationVisitor extends TopDownVisitor {

	private PrintConfigurationService configurationService;

	private GeoService geoService;

	private FilterService filterCreator;

	private VectorLayerService vectorLayerService;

	private RasterLayerService rasterLayerService;

	public MapConfigurationVisitor(PrintConfigurationService configurationService, GeoService geoService,
			FilterService filterCreator, VectorLayerService vectorLayerService, RasterLayerService rasterLayerService) {
		this.configurationService = configurationService;
		this.geoService = geoService;
		this.filterCreator = filterCreator;
		this.vectorLayerService = vectorLayerService;
		this.rasterLayerService = rasterLayerService;
	}

	@Override
	public void visit(LegendComponent legend) {
		legend.clearItems();
		ClientMapInfo map = configurationService.getMapInfo(legend.getMapId(), legend.getApplicationId());
		for (ClientLayerInfo info : map.getLayers()) {
			if (info instanceof ClientVectorLayerInfo) {
				legend.addVectorLayer((ClientVectorLayerInfo) info);
			} else if (info instanceof ClientRasterLayerInfo) {
				legend.addRasterLayer((ClientRasterLayerInfo) info);
			}
		}
	}

	@Override
	public void visit(MapComponent mapComponent) {
		mapComponent.clearLayers();
		ClientMapInfo map = configurationService.getMapInfo(mapComponent.getMapId(), mapComponent.getApplicationId());
		List<ClientLayerInfo> layers = new ArrayList<ClientLayerInfo>(map.getLayers());
		Collections.reverse(layers);
		for (ClientLayerInfo info : layers) {
			if (info instanceof ClientVectorLayerInfo) {
				VectorLayerComponentImpl comp = new VectorLayerComponentImpl(geoService, filterCreator,
						vectorLayerService, configurationService);
				comp.setLabelsVisible(false);
				comp.setLayerId(info.getServerLayerId());
				comp.setStyleInfo(((ClientVectorLayerInfo) info).getNamedStyleInfo());
				comp.setVisible(true);
				mapComponent.addComponent(0, comp);
			} else if (info instanceof ClientRasterLayerInfo) {
				RasterLayerComponentImpl comp = new RasterLayerComponentImpl(rasterLayerService, configurationService);
				comp.setLayerId(info.getServerLayerId());
				comp.setVisible(true);
				mapComponent.addComponent(0, comp);
			}
		}
	}

}
