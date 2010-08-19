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
import org.geomajas.plugin.printing.PrintingException;
import org.geomajas.plugin.printing.component.LegendComponent;
import org.geomajas.plugin.printing.component.MapComponent;
import org.geomajas.plugin.printing.component.TopDownVisitor;
import org.geomajas.plugin.printing.component.dto.RasterLayerComponentInfo;
import org.geomajas.plugin.printing.component.dto.VectorLayerComponentInfo;
import org.geomajas.plugin.printing.component.impl.RasterLayerComponentImpl;
import org.geomajas.plugin.printing.component.impl.VectorLayerComponentImpl;
import org.geomajas.plugin.printing.component.service.PrintConfigurationService;
import org.geomajas.plugin.printing.component.service.PrintDtoConverterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Visitor that configures the default layers of all the maps and legends in the print component. Maps and legends
 * should have valid map id's.
 * 
 * @author Jan De Moerloose
 */
public class MapConfigurationVisitor extends TopDownVisitor {

	private PrintConfigurationService configurationService;

	private PrintDtoConverterService printDtoConverterService;

	private final Logger log = LoggerFactory.getLogger(MapConfigurationVisitor.class);

	public MapConfigurationVisitor(PrintConfigurationService configurationService,
			PrintDtoConverterService printDtoConverterService) {
		this.configurationService = configurationService;
		this.printDtoConverterService = printDtoConverterService;
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
				VectorLayerComponentImpl comp;
				try {
					comp = (VectorLayerComponentImpl) printDtoConverterService
							.toInternal(new VectorLayerComponentInfo());
					comp.setLabelsVisible(false);
					comp.setLayerId(info.getServerLayerId());
					comp.setStyleInfo(((ClientVectorLayerInfo) info).getNamedStyleInfo());
					comp.setVisible(true);
					mapComponent.addComponent(0, comp);
				} catch (PrintingException e) {
					// should never fail
					log.error("unexpected exception while adding layers to map" , e);
				}
			} else if (info instanceof ClientRasterLayerInfo) {
				RasterLayerComponentImpl comp;
				try {
					comp = (RasterLayerComponentImpl) printDtoConverterService
							.toInternal(new RasterLayerComponentInfo());
					comp.setLayerId(info.getServerLayerId());
					comp.setVisible(true);
					mapComponent.addComponent(0, comp);
				} catch (PrintingException e) {
					// should never fail
					log.error("unexpected exception while adding layers to map" , e);
				}
			}
		}
	}

}
