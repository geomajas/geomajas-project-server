/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.printing.client.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.plugin.printing.client.util.PrintingLayout;
import org.geomajas.plugin.printing.command.dto.PrintTemplateInfo;
import org.geomajas.plugin.printing.component.dto.ImageComponentInfo;
import org.geomajas.plugin.printing.component.dto.LabelComponentInfo;
import org.geomajas.plugin.printing.component.dto.LayoutConstraintInfo;
import org.geomajas.plugin.printing.component.dto.LegendComponentInfo;
import org.geomajas.plugin.printing.component.dto.LegendIconComponentInfo;
import org.geomajas.plugin.printing.component.dto.LegendItemComponentInfo;
import org.geomajas.plugin.printing.component.dto.MapComponentInfo;
import org.geomajas.plugin.printing.component.dto.PageComponentInfo;
import org.geomajas.plugin.printing.component.dto.PrintComponentInfo;
import org.geomajas.plugin.printing.component.dto.RasterLayerComponentInfo;
import org.geomajas.plugin.printing.component.dto.ScaleBarComponentInfo;
import org.geomajas.plugin.printing.component.dto.VectorLayerComponentInfo;

/**
 * Default print template builder, parameters include title, size, raster DPI, orientation, etc...
 * 
 * @author Jan De Moerloose
 */
public class DefaultTemplateBuilder extends AbstractTemplateBuilder {

	private double pageWidth;

	private double pageHeight;

	private int marginX;

	private int marginY;

	private String titleText;

	private int rasterDpi;

	private boolean withScaleBar;

	private boolean withArrow;

	private MapModel mapModel;

	private String applicationId;

	@Override
	public PrintTemplateInfo buildTemplate() {
		PrintTemplateInfo template = super.buildTemplate();
		template.setId(1L);
		template.setName("default");
		return template;
	}

	@Override
	protected PageComponentInfo buildPage() {
		PageComponentInfo page = super.buildPage();
		page.getLayoutConstraint().setWidth((float) pageWidth);
		page.getLayoutConstraint().setHeight((float) pageHeight);
		return page;
	}

	@Override
	protected MapComponentInfo buildMap() {
		MapComponentInfo map = super.buildMap();
		map.getLayoutConstraint().setMarginX(marginX);
		map.getLayoutConstraint().setMarginY(marginY);
		MapView view = mapModel.getMapView();
		double mapWidth = getPageWidth() - 2 * marginX;
		double mapHeight = getPageHeight() - 2 * marginY;
		Coordinate origin = view.getBounds().createFittingBox(mapWidth, mapHeight).getOrigin();
		map.setLocation(new org.geomajas.geometry.Coordinate(origin.getX(), origin.getY()));
		map.setPpUnit((float) (mapWidth / view.getBounds().createFittingBox(mapWidth, mapHeight).getWidth()));
		map.setTag("map");
		map.setMapId(mapModel.getMapInfo().getId());
		map.setApplicationId(applicationId);
		map.setRasterResolution(rasterDpi);
		List<PrintComponentInfo> layerChildren = new ArrayList<PrintComponentInfo>();
		for (Layer layer : mapModel.getLayers()) {
			if (layer instanceof VectorLayer && layer.isShowing()) {
				VectorLayerComponentInfo info = new VectorLayerComponentInfo();
				VectorLayer vectorLayer = (VectorLayer) layer;
				info.setLayerId(vectorLayer.getServerLayerId());
				ClientVectorLayerInfo layerInfo = vectorLayer.getLayerInfo();
				info.setStyleInfo(layerInfo.getNamedStyleInfo());
				info.setFilter(vectorLayer.getFilter());
				info.setLabelsVisible(vectorLayer.isLabelsShowing());
				info.setSelected(vectorLayer.isSelected());
				Set<String> features = vectorLayer.getSelectedFeatures();
				info.setSelectedFeatureIds(features.toArray(new String[features.size()]));
				layerChildren.add(info);
			} else if (layer instanceof RasterLayer && layer.isShowing()) {
				RasterLayerComponentInfo info = new RasterLayerComponentInfo();
				RasterLayer rasterLayer = (RasterLayer) layer;
				info.setLayerId(rasterLayer.getServerLayerId());
				info.setStyle(rasterLayer.getLayerInfo().getStyle());
				layerChildren.add(info);
			}
		}
		map.getChildren().addAll(0, layerChildren);
		return map;
	}

	@Override
	protected ImageComponentInfo buildArrow() {
		if (isWithArrow()) {
			ImageComponentInfo northarrow = super.buildArrow();
			northarrow.setImagePath("/images/northarrow.gif");
			northarrow.getLayoutConstraint().setAlignmentX(LayoutConstraintInfo.RIGHT);
			northarrow.getLayoutConstraint().setAlignmentY(LayoutConstraintInfo.TOP);
			northarrow.getLayoutConstraint().setMarginX((float) PrintingLayout.templateMarginX);
			northarrow.getLayoutConstraint().setMarginY((float) PrintingLayout.templateMarginY);
			northarrow.getLayoutConstraint().setWidth((float) PrintingLayout.templateNorthArrowWidth);
			northarrow.setTag("arrow");
			return northarrow;
		} else {
			return null;
		}
	}

	@Override
	protected LegendComponentInfo buildLegend() {
		LegendComponentInfo legend = super.buildLegend();
		FontStyleInfo style = new FontStyleInfo();
		style.setFamily(PrintingLayout.templateDefaultFontFamily);
		style.setStyle(PrintingLayout.templateDefaultFontStyle);
		style.setSize((int) PrintingLayout.templateDefaultFontSize);
		legend.setFont(style);
		legend.setMapId(mapModel.getMapInfo().getId());
		legend.setTag("legend");
		for (Layer layer : mapModel.getLayers()) {
			if (layer instanceof VectorLayer && layer.isShowing()) {
				VectorLayer vectorLayer = (VectorLayer) layer;
				ClientVectorLayerInfo layerInfo = vectorLayer.getLayerInfo();
				String label = layerInfo.getLabel();
				List<FeatureStyleInfo> defs = layerInfo.getNamedStyleInfo().getFeatureStyles();
				for (FeatureStyleInfo styleDefinition : defs) {
					String text;
					if (defs.size() > 1) {
						text = label + "(" + styleDefinition.getName() + ")";
					} else {
						text = label;
					}
					LegendItemComponentInfo item = new LegendItemComponentInfo();
					LegendIconComponentInfo icon = new LegendIconComponentInfo();
					icon.setLabel(text);
					icon.setStyleInfo(styleDefinition);
					icon.setLayerType(layerInfo.getLayerType());
					item.addChild(icon);
					item.addChild(getLegendLabel(legend, text));
					legend.addChild(item);
				}
			} else if (layer instanceof RasterLayer && layer.isShowing()) {
				RasterLayer rasterLayer = (RasterLayer) layer;
				ClientRasterLayerInfo layerInfo = rasterLayer.getLayerInfo();
				LegendItemComponentInfo item = new LegendItemComponentInfo();
				LegendIconComponentInfo icon = new LegendIconComponentInfo();
				icon.setLabel(layerInfo.getLabel());
				icon.setLayerType(layerInfo.getLayerType());
				item.addChild(icon);
				item.addChild(getLegendLabel(legend, layerInfo.getLabel()));
				legend.addChild(item);
			}
		}
		return legend;
	}

	private LabelComponentInfo getLegendLabel(LegendComponentInfo legend, String text) {
		LabelComponentInfo legendLabel = new LabelComponentInfo();
		legendLabel.setBackgroundColor(PrintingLayout.templateDefaultBackgroundColor);
		legendLabel.setBorderColor(PrintingLayout.templateDefaultBorderColor);
		legendLabel.setFontColor(PrintingLayout.templateDefaultColor);
		legendLabel.setFont(legend.getFont());
		legendLabel.setText(text);
		legendLabel.setTextOnly(true);
		return legendLabel;
	}

	@Override
	protected ScaleBarComponentInfo buildScaleBar() {
		if (isWithScaleBar()) {
			ScaleBarComponentInfo bar = super.buildScaleBar();
			bar.setTicNumber(3);
			bar.setTag("scalebar");
			return bar;
		} else {
			return null;
		}
	}

	@Override
	protected LabelComponentInfo buildTitle() {
		if (titleText != null) {
			LabelComponentInfo title = super.buildTitle();
			title.setText(titleText);
			title.getLayoutConstraint().setMarginY(2 * marginY);
			return title;
		} else {
			return null;
		}
	}

	public double getPageWidth() {
		return pageWidth;
	}

	public void setPageWidth(double pageWidth) {
		this.pageWidth = pageWidth;
	}

	public double getPageHeight() {
		return pageHeight;
	}

	public void setPageHeight(double pageHeight) {
		this.pageHeight = pageHeight;
	}

	public String getTitleText() {
		return titleText;
	}

	public void setTitleText(String titleText) {
		this.titleText = titleText;
	}

	public int getRasterDpi() {
		return rasterDpi;
	}

	public void setRasterDpi(int rasterDpi) {
		this.rasterDpi = rasterDpi;
	}

	public boolean isWithScaleBar() {
		return withScaleBar;
	}

	public void setWithScaleBar(boolean withScaleBar) {
		this.withScaleBar = withScaleBar;
	}

	public boolean isWithArrow() {
		return withArrow;
	}

	public void setWithArrow(boolean withArrow) {
		this.withArrow = withArrow;
	}

	public int getMarginX() {
		return marginX;
	}

	public void setMarginX(int marginX) {
		this.marginX = marginX;
	}

	public int getMarginY() {
		return marginY;
	}

	public void setMarginY(int marginY) {
		this.marginY = marginY;
	}

	public MapModel getMapModel() {
		return mapModel;
	}

	public void setMapModel(MapModel mapModel) {
		this.mapModel = mapModel;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

}
