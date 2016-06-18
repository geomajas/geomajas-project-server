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

package org.geomajas.plugin.printing.mvc;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.plugin.printing.command.dto.PrintTemplateInfo;
import org.geomajas.plugin.printing.component.dto.ImageComponentInfo;
import org.geomajas.plugin.printing.component.dto.LabelComponentInfo;
import org.geomajas.plugin.printing.component.dto.LayoutConstraintInfo;
import org.geomajas.plugin.printing.component.dto.LegendComponentInfo;
import org.geomajas.plugin.printing.component.dto.MapComponentInfo;
import org.geomajas.plugin.printing.component.dto.PageComponentInfo;
import org.geomajas.plugin.printing.component.dto.PrintComponentInfo;
import org.geomajas.plugin.printing.component.dto.RasterizedLayersComponentInfo;
import org.geomajas.plugin.printing.component.dto.ScaleBarComponentInfo;
import org.geomajas.plugin.rasterizing.command.dto.LegendRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.MapRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.RasterLayerRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.VectorLayerRasterizingInfo;

/**
 * Default print template builder, parameters include title, size, raster DPI, orientation, etc...
 * 
 * @author Jan De Moerloose (smartGWT version)
 * @author An Buyle (GWT2 version)
 */
public class TestTemplateBuilder {

	protected double pageWidth;

	protected double pageHeight;

	protected int marginX;

	protected int marginY;

	protected String titleText;

	protected int rasterDpi;

	protected boolean withScaleBar;

	protected boolean withArrow;

	protected String applicationId;

	protected ClientMapInfo mapInfo;

	protected Bbox bounds;

	public PrintTemplateInfo buildTemplate() {
		PrintTemplateInfo template = new PrintTemplateInfo();
		template.setPage(buildPage());
		template.setId(1L);
		template.setName("default");
		return template;
	}

	protected PageComponentInfo buildPage() {
		PageComponentInfo page = new PageComponentInfo();
		page.setLocale("EN");
		page.addChild(buildMap());
		page.addChild(buildTitle());
		page.setTag("page");
		page.getLayoutConstraint().setWidth((float) pageWidth);
		page.getLayoutConstraint().setHeight((float) pageHeight);
		return page;
	}

	protected MapComponentInfo buildMap() {
		double printWidth = getPageWidth() - 2 * marginX;
		double printHeight = getPageHeight() - 2 * marginY;

		Bbox fittingBox = createFittingBox(bounds, printWidth / printHeight);

		MapComponentInfo map = new MapComponentInfo();
		map.addChild(buildScaleBar());
		LegendComponentInfo legend = new LegendComponentInfo();
		map.addChild(legend);
		map.addChild(buildArrow());
		map.getLayoutConstraint().setMarginX(marginX);
		map.getLayoutConstraint().setMarginY(marginY);

		map.setLocation(new org.geomajas.geometry.Coordinate(fittingBox.getX(), fittingBox.getY()));
		map.setPpUnit((float) (printWidth / fittingBox.getWidth()));

		map.setTag("map");
		map.setMapId(mapInfo.getId());

		map.setApplicationId(applicationId);
		map.setRasterResolution(rasterDpi);

		// use rasterized layers for pure GWT
		double rasterScale = map.getPpUnit() * map.getRasterResolution() / 72.0;
		// map.getPpUnit() = number of pixels per map unit at 72 dpi

		List<PrintComponentInfo> layers = new ArrayList<PrintComponentInfo>();
		RasterizedLayersComponentInfo rasterizedLayersComponentInfo = new RasterizedLayersComponentInfo();
		prepareMap();
		rasterizedLayersComponentInfo.setMapInfo(mapInfo);
		layers.add(rasterizedLayersComponentInfo);
		map.getChildren().addAll(0, layers);
		return map;
	}

	private void prepareMap() {
		MapRasterizingInfo mapRasterizingInfo = new MapRasterizingInfo();
		mapInfo.getWidgetInfo().put(MapRasterizingInfo.WIDGET_KEY, mapRasterizingInfo);
		mapRasterizingInfo.setTransparent(true);
		mapRasterizingInfo.setDpi(96);
		for (ClientLayerInfo clientLayerInfo : mapInfo.getLayers()) {
			if (clientLayerInfo instanceof ClientVectorLayerInfo) {
				ClientVectorLayerInfo vectorLayerInfo = (ClientVectorLayerInfo) clientLayerInfo;
				VectorLayerRasterizingInfo vectorLayerRasterizingInfo = new VectorLayerRasterizingInfo();
				vectorLayerRasterizingInfo.setPaintGeometries(true);
				vectorLayerRasterizingInfo.setStyle(vectorLayerInfo.getNamedStyleInfo());
				vectorLayerRasterizingInfo.setShowing(true);
				vectorLayerInfo.getWidgetInfo().put(VectorLayerRasterizingInfo.WIDGET_KEY, vectorLayerRasterizingInfo);
			} else if (clientLayerInfo instanceof ClientRasterLayerInfo) {
				ClientRasterLayerInfo rasterLayerInfo = (ClientRasterLayerInfo) clientLayerInfo;
				RasterLayerRasterizingInfo rasterLayerRasterizingInfo = new RasterLayerRasterizingInfo();
				rasterLayerRasterizingInfo.setCssStyle(rasterLayerInfo.getStyle());
				rasterLayerRasterizingInfo.setShowing(true);
				rasterLayerInfo.getWidgetInfo().put(RasterLayerRasterizingInfo.WIDGET_KEY, rasterLayerRasterizingInfo);
			}
		}
		LegendRasterizingInfo legend = new LegendRasterizingInfo();
		FontStyleInfo fontStyleInfo = new FontStyleInfo();
		fontStyleInfo.applyDefaults();
		legend.setFont(fontStyleInfo);
		legend.setTitle("test");
		legend.setWidth(100);
		legend.setHeight(500);
		mapRasterizingInfo.setLegendRasterizingInfo(legend);
	}

	protected ImageComponentInfo buildArrow() {
		if (isWithArrow()) {
			ImageComponentInfo northarrow = new ImageComponentInfo();
			northarrow.setImagePath("/images/northarrow.gif");
			northarrow.getLayoutConstraint().setAlignmentX(LayoutConstraintInfo.RIGHT);
			northarrow.getLayoutConstraint().setAlignmentY(LayoutConstraintInfo.TOP);
			northarrow.getLayoutConstraint().setMarginX(10);
			northarrow.getLayoutConstraint().setMarginY(10);
			northarrow.getLayoutConstraint().setWidth(10);
			northarrow.setTag("arrow");
			return northarrow;
		} else {
			return null;
		}
	}

	protected LegendComponentInfo buildLegend() {
		LegendComponentInfo legend = new LegendComponentInfo();
		legend.setMapId(mapInfo.getId());
		legend.setTag("legend");
		return legend;
	}

	protected ScaleBarComponentInfo buildScaleBar() {
		if (isWithScaleBar()) {
			ScaleBarComponentInfo bar = new ScaleBarComponentInfo();
			bar.setTicNumber(3);
			bar.setTag("scalebar");
			return bar;
		} else {
			return null;
		}
	}

	protected LabelComponentInfo buildTitle() {
		if (titleText != null) {
			LabelComponentInfo title = new LabelComponentInfo();
			FontStyleInfo style = new FontStyleInfo();
			style.setFamily("Arial");
			style.setStyle("Italic");
			style.setSize(14);
			title.setFont(style);
			title.setBackgroundColor("#FFFFFF");
			title.setBorderColor("#000000");
			title.setFontColor("#000000");
			title.getLayoutConstraint().setAlignmentY(LayoutConstraintInfo.TOP);
			title.getLayoutConstraint().setAlignmentX(LayoutConstraintInfo.CENTER);
			title.setTag("title");
			title.setText(titleText);
			title.getLayoutConstraint().setMarginY(2 * marginY);
			return title;
		} else {
			return null;
		}
	}

	public ClientMapInfo getMapInfo() {
		return mapInfo;
	}

	public void setMapInfo(ClientMapInfo mapInfo) {
		this.mapInfo = mapInfo;
	}

	public Bbox getBounds() {
		return bounds;
	}

	public void setBounds(Bbox bounds) {
		this.bounds = bounds;
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

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * Creates the largest possible bounding box that fits around the specified bounding box but has a different
	 * width/height ratio. and the same centerpoint.
	 * 
	 * @param bbox
	 * @param newRatio width/height ratio
	 * @return bbox
	 */
	public Bbox createFittingBox(Bbox bbox, double newRatio) {
		double oldRatio = bbox.getWidth() / bbox.getHeight();
		double newWidth = bbox.getWidth();
		double newHeight = bbox.getHeight();
		if (newRatio < oldRatio) {
			// Keep width of bbox , decrease height to fullfill newRatio
			newHeight = newWidth / newRatio;
		} else {
			// Keep height of bbox , decrease width to fullfill newRatio
			newWidth = newHeight * newRatio;
		}
		Bbox result = new Bbox(0, 0, newWidth, newHeight);
		result.setX(bbox.getX() + (bbox.getWidth() - newWidth) / 2.0);
		result.setY(bbox.getY() + (bbox.getHeight() - newHeight) / 2.0);
		return result;
	}

}
