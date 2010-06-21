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
package org.geomajas.plugin.printing.client.action.toolbar;

import org.geomajas.command.CommandResponse;
import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.action.ConfigurableAction;
import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.printing.client.PrintingMessages;
import org.geomajas.plugin.printing.command.dto.PrintGetTemplateRequest;
import org.geomajas.plugin.printing.command.dto.PrintGetTemplateResponse;
import org.geomajas.plugin.printing.command.dto.PrintTemplateInfo;
import org.geomajas.plugin.printing.component.dto.ImageComponentInfo;
import org.geomajas.plugin.printing.component.dto.LabelComponentInfo;
import org.geomajas.plugin.printing.component.dto.LayoutConstraintInfo;
import org.geomajas.plugin.printing.component.dto.LegendComponentInfo;
import org.geomajas.plugin.printing.component.dto.MapComponentInfo;
import org.geomajas.plugin.printing.component.dto.PageComponentInfo;
import org.geomajas.plugin.printing.component.dto.RasterLayerComponentInfo;
import org.geomajas.plugin.printing.component.dto.ScaleBarComponentInfo;
import org.geomajas.plugin.printing.component.dto.VectorLayerComponentInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Action to create a default PDF print of the map.
 * 
 * @author Jan De Moerloose
 * 
 */
public class PrintAction extends ToolbarAction implements ConfigurableAction {

	private PrintingMessages messages = GWT.create(PrintingMessages.class);

	private MapWidget mapWidget;

	public PrintAction(MapWidget mapWidget) {
		super("[ISOMORPHIC]/geomajas/osgeo/print.png", null);
		this.mapWidget = mapWidget;
		setTooltip(messages.printBtnTitle());
	}

	public void onClick(ClickEvent event) {
		PrintGetTemplateRequest request = new PrintGetTemplateRequest();
		request.setDownloadMethod(PrintGetTemplateRequest.DOWNLOAD_METHOD_BROWSER);
		request.setFileName("test.pdf");
		request.setPageSize("A4");
		PrintTemplateInfo template = new PrintTemplateInfo();
		template.setTemplate(false);
		template.setId(1L);
		template.setName("default");
		template.setPage(createDefaultPage());
		request.setTemplate(template);
		GwtCommand command = new GwtCommand("command.print.GetTemplate");
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new CommandCallback() {

			public void execute(CommandResponse r) {
				if (r instanceof PrintGetTemplateResponse) {
					PrintGetTemplateResponse response = (PrintGetTemplateResponse) r;
					GWT.log("" + response.getDocumentId());
					Window.open("printing/geomajas.pdf?documentId=" + response.getDocumentId(), "_blank", null);
				}
			}
		});
	}

	private PageComponentInfo createDefaultPage() {
		PageComponentInfo page = new PageComponentInfo();
		page.setBounds(new Bbox(0, 0, 842, 595));
		page.setTag("page");
		MapComponentInfo map = createMap();
		ImageComponentInfo northarrow = createArrow();
		ScaleBarComponentInfo bar = createBar();
		LabelComponentInfo title = createTitle();
		LegendComponentInfo legend = createLegend();
		map.addChild(bar);
		map.addChild(legend);
		map.addChild(northarrow);
		page.addChild(map);
		page.addChild(title);
		return page;
	}

	private LegendComponentInfo createLegend() {
		LegendComponentInfo legend = new LegendComponentInfo();
		FontStyleInfo style = new FontStyleInfo();
		style.setFamily("Dialog");
		style.setStyle("Italic");
		style.setSize(14);
		legend.setFont(style);
		legend.setMapId(mapWidget.getMapModel().getMapInfo().getId());
		legend.setTag("legend");
		return legend;
	}

	public void configure(String key, String value) {
	}

	private MapComponentInfo createMap() {
		MapComponentInfo map = new MapComponentInfo();
		map.getLayoutConstraint().setMarginX(20);
		map.getLayoutConstraint().setMarginY(20);
		MapView view = mapWidget.getMapModel().getMapView();
		Coordinate origin = view.getBounds().createFittingBox(802, 555).getOrigin();
		map.setLocation(new org.geomajas.geometry.Coordinate(origin.getX(), origin.getY()));
		map.setPpUnit((float) (802f / view.getBounds().createFittingBox(802, 555).getWidth()));
		map.setTag("map");
		map.setMapId(mapWidget.getMapModel().getMapInfo().getId());
		map.setApplicationId(mapWidget.getApplicationId());
		for (Layer layer : mapWidget.getMapModel().getLayers()) {
			if (layer instanceof VectorLayer && layer.isShowing()) {
				VectorLayerComponentInfo info = new VectorLayerComponentInfo();
				VectorLayer vectorLayer = (VectorLayer) layer;
				info.setLayerId(vectorLayer.getServerLayerId());
				ClientVectorLayerInfo layerInfo = vectorLayer.getLayerInfo();
				info.setStyleInfo(layerInfo.getNamedStyleInfo());
				info.setFilter(vectorLayer.getFilter());
				info.setLabelsVisible(vectorLayer.isLabeled());
				info.setSelected(vectorLayer.isSelected());
				info.setSelectedFeatureIds(vectorLayer.getSelectedFeatures().toArray(new String[0]));
				map.addChild(info);
			} else if (layer instanceof RasterLayer && layer.isShowing()) {
				RasterLayerComponentInfo info = new RasterLayerComponentInfo();
				RasterLayer rasterLayer = (RasterLayer) layer;
				info.setLayerId(rasterLayer.getServerLayerId());
				map.addChild(info);
			}
		}
		return map;
	}

	private static ImageComponentInfo createArrow() {
		ImageComponentInfo northarrow = new ImageComponentInfo();
		northarrow.setImagePath("/images/northarrow.gif");
		northarrow.getLayoutConstraint().setAlignmentX(LayoutConstraintInfo.RIGHT);
		northarrow.getLayoutConstraint().setAlignmentY(LayoutConstraintInfo.TOP);
		northarrow.getLayoutConstraint().setMarginX(20);
		northarrow.getLayoutConstraint().setMarginY(20);
		northarrow.getLayoutConstraint().setWidth(10);
		northarrow.setTag("arrow");
		return northarrow;
	}

	private static LabelComponentInfo createTitle() {
		LabelComponentInfo title = new LabelComponentInfo();
		FontStyleInfo style = new FontStyleInfo();
		style.setFamily("Dialog");
		style.setStyle("Italic");
		style.setSize(14);
		title.setFont(style);
		title.setBackgroundColor("0xFFFFFF");
		title.setBorderColor("0x000000");
		title.setFontColor("0x000000");
		title.setText("Map of the world");
		title.getLayoutConstraint().setAlignmentY(LayoutConstraintInfo.TOP);
		title.getLayoutConstraint().setAlignmentX(LayoutConstraintInfo.CENTER);
		title.getLayoutConstraint().setMarginY(40);
		title.setTag("title");
		return title;
	}

	private static ScaleBarComponentInfo createBar() {
		ScaleBarComponentInfo bar = new ScaleBarComponentInfo();
		bar.setTicNumber(3);
		bar.setUnit("m");
		bar.setTag("scalebar");
		return bar;
	}

}
