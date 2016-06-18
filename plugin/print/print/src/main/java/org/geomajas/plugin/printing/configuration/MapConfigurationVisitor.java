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
package org.geomajas.plugin.printing.configuration;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.plugin.printing.PrintingException;
import org.geomajas.plugin.printing.component.BaseLayerComponent;
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
		List<BaseLayerComponent> layers = new ArrayList<BaseLayerComponent>();
		for (ClientLayerInfo info : map.getLayers()) {
			if (info instanceof ClientVectorLayerInfo) {
				VectorLayerComponentInfo vectorInfo = new VectorLayerComponentInfo();
				vectorInfo.setLabelsVisible(false);
				vectorInfo.setLayerId(info.getServerLayerId());
				vectorInfo.setStyleInfo(((ClientVectorLayerInfo) info).getNamedStyleInfo());
				vectorInfo.setVisible(true);
				try {
					VectorLayerComponentImpl comp = (VectorLayerComponentImpl) printDtoConverterService
							.toInternal(vectorInfo);
					layers.add(comp);
				} catch (PrintingException e) {
					// should never fail
					log.error("unexpected exception while adding layers to map" , e);
				}
			} else if (info instanceof ClientRasterLayerInfo) {
				RasterLayerComponentInfo rasterInfo = new RasterLayerComponentInfo();
				rasterInfo.setLayerId(info.getServerLayerId());
				rasterInfo.setVisible(true);
				rasterInfo.setStyle(((ClientRasterLayerInfo) info).getStyle());
				try {
					RasterLayerComponentImpl comp = (RasterLayerComponentImpl) printDtoConverterService
							.toInternal(rasterInfo);
					comp.setLayerId(info.getServerLayerId());
					comp.setVisible(true);
					layers.add(comp);
				} catch (PrintingException e) {
					// should never fail
					log.error("unexpected exception while adding layers to map" , e);
				}
			}
		}
		mapComponent.addComponents(0, layers);
	}

}
