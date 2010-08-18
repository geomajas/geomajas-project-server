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
package org.geomajas.plugin.printing.component.impl;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.LabelStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.SymbolInfo;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.plugin.printing.component.LayoutConstraint;
import org.geomajas.plugin.printing.component.PdfContext;
import org.geomajas.plugin.printing.component.PrintComponentVisitor;
import org.geomajas.plugin.printing.component.VectorLayerComponent;
import org.geomajas.plugin.printing.component.dto.PrintComponentInfo;
import org.geomajas.plugin.printing.component.dto.VectorLayerComponentInfo;
import org.geomajas.plugin.printing.component.service.PrintConfigurationService;
import org.geomajas.plugin.printing.component.service.PrintDtoConverterService;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.geotools.referencing.CRS;
import org.opengis.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lowagie.text.Rectangle;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateFilter;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * ???
 * 
 * @author Pieter De Graef
 */
public class VectorLayerComponentImpl extends BaseLayerComponentImpl implements VectorLayerComponent {

	/** Style for this layer. */
	private NamedStyleInfo styleInfo;

	/** CQL filter */
	private String filter;

	/** Array of selected feature id's for this layer. */
	private String[] selectedFeatureIds = new String[0];

	/** True if labels are visible. */
	private boolean labelsVisible;

	/** The calculated bounds */
	@XStreamOmitField
	protected Envelope bbox;

	/** List of the features */
	@XStreamOmitField
	protected List<InternalFeature> features = new ArrayList<InternalFeature>();

	/** A sorted set of selected feature ids */
	@XStreamOmitField
	protected Set<String> selectedFeatures = new TreeSet<String>();

	@XStreamOmitField
	private final Logger log = LoggerFactory.getLogger(VectorLayerComponentImpl.class);

	// length of the connector line for symbols
	private static final float SYMBOL_CONNECT_LENGTH = 15;

	private GeoService geoService;

	private FilterService filterService;

	private VectorLayerService layerService;

	private PrintConfigurationService configurationService;

	@Autowired(required = false)
	protected Map<String, ClientApplicationInfo> applicationMap = new LinkedHashMap<String, ClientApplicationInfo>();

	public VectorLayerComponentImpl(GeoService geoService, FilterService filterService,
			VectorLayerService layerService, PrintConfigurationService configurationService) {
		this.geoService = geoService;
		this.filterService = filterService;
		this.layerService = layerService;
		this.configurationService = configurationService;

		// stretch to map
		getConstraint().setAlignmentX(LayoutConstraint.JUSTIFIED);
		getConstraint().setAlignmentY(LayoutConstraint.JUSTIFIED);
	}

	/**
	 * Call back visitor.
	 * 
	 * @param visitor
	 */
	public void accept(PrintComponentVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void render(PdfContext context) {
		if (isVisible()) {
			bbox = createBbox();
			Collections.addAll(selectedFeatures, getSelectedFeatureIds());
			// Fetch features
			try {
				ClientMapInfo map = configurationService.getMapInfo(getMap().getMapId(), getMap().getApplicationId());
				String geomName = configurationService.getVectorLayerInfo(getLayerId()).getFeatureInfo()
						.getGeometryType().getName();
				GeometryFactory factory = new GeometryFactory(new PrecisionModel(Math.pow(10, map.getPrecision())),
						geoService.getSridFromCrs(map.getCrs()));

				Filter filter = filterService.createIntersectsFilter(factory.toGeometry(bbox), geomName);
				if (getFilter() != null) {
					filter = filterService.createAndFilter(filterService.parseFilter(getFilter()), filter);
				}

				features = layerService.getFeatures(getLayerId(), CRS.decode(map.getCrs()), filter, styleInfo,
						VectorLayerService.FEATURE_INCLUDE_ALL);
			} catch (Exception e) {
				log.error("Error rendering vectorlayerRenderer", e);
			}

			for (InternalFeature f : features) {
				drawFeature(context, f);
			}
			if (isLabelsVisible()) {
				for (InternalFeature f : features) {
					drawLabel(context, f);
				}
			}
		}
	}

	private void drawLabel(PdfContext context, InternalFeature f) {
		LabelStyleInfo labelType = styleInfo.getLabelStyle();
		String label = f.getLabel();

		Font font = new Font("Helvetica", Font.ITALIC, 10);
		Color fontColor = Color.black;
		if (labelType.getFontStyle() != null) {
			fontColor = context.getColor(labelType.getFontStyle().getColor(), labelType.getFontStyle().getOpacity());
		}
		Rectangle rect = calculateLabelRect(context, f, label, font);
		Color bgColor = Color.white;
		if (labelType.getBackgroundStyle() != null) {
			bgColor = context.getColor(labelType.getBackgroundStyle().getFillColor(), labelType.getBackgroundStyle()
					.getFillOpacity());
		}
		context.fillRoundRectangle(rect, bgColor, 3);
		Color borderColor = Color.black;
		if (labelType.getBackgroundStyle() != null) {
			borderColor = context.getColor(labelType.getBackgroundStyle().getStrokeColor(), labelType
					.getBackgroundStyle().getStrokeOpacity());
		}
		float linewidth = 0.5f;
		if (labelType.getBackgroundStyle() != null) {
			linewidth = labelType.getBackgroundStyle().getStrokeWidth();
		}
		context.strokeRoundRectangle(rect, borderColor, linewidth, 3);
		context.drawText(label, font, rect, fontColor);
		if (f.getGeometry() instanceof Point) {
			context.drawLine(0.5f * (rect.getLeft() + rect.getRight()), rect.getBottom(), 0.5f * (rect.getLeft() + rect
					.getRight()), rect.getBottom() - SYMBOL_CONNECT_LENGTH, borderColor, linewidth);
		}
	}

	private float getSymbolHeight(InternalFeature f) {
		SymbolInfo info = f.getStyleInfo().getSymbol();
		if (info.getCircle() != null) {
			return 2 * info.getCircle().getR();
		} else {
			return info.getRect().getH();
		}
	}

	private Rectangle calculateLabelRect(PdfContext context, InternalFeature f, String label, Font font) {
		Rectangle textSize = context.getTextSize(label, font);
		float margin = 0.25f * font.getSize();
		Rectangle rect = new Rectangle(textSize.getWidth() + 2 * margin, textSize.getHeight() + 2 * margin);
		Coordinate labelPosition = geoService.calcDefaultLabelPosition(f);
		context.moveRectangleTo(rect, (float) labelPosition.x - rect.getWidth() / 2f, (float) labelPosition.y
				- rect.getHeight() / 2f);
		if (f.getGeometry() instanceof Point) {
			float shiftHeight = 0.5f * (rect.getHeight() + getSymbolHeight(f));
			// move up 15 pixels to make the symbol visible
			context.moveRectangleTo(rect, rect.getLeft(), rect.getBottom() + shiftHeight + SYMBOL_CONNECT_LENGTH);
		}
		if (rect.getLeft() < 0) {
			context.moveRectangleTo(rect, 10, rect.getBottom());
		}
		if (rect.getBottom() < 0) {
			context.moveRectangleTo(rect, rect.getLeft(), 10);
		}
		if (rect.getTop() > getBounds().getHeight()) {
			context.moveRectangleTo(rect, rect.getLeft(), getBounds().getHeight() - rect.getHeight() - 10);
		}
		if (rect.getRight() > getBounds().getWidth()) {
			context.moveRectangleTo(rect, getBounds().getWidth() - rect.getWidth() - 10, rect.getBottom());
		}
		return rect;
	}

	private void drawFeature(PdfContext context, InternalFeature f) {
		FeatureStyleInfo style = f.getStyleInfo();

		// Color, transparency, dash
		Color fillColor = context.getColor(style.getFillColor(), style.getFillOpacity());
		Color strokeColor = context.getColor(style.getStrokeColor(), style.getStrokeOpacity());
		float[] dashArray = context.getDashArray(style.getDashArray());

		// check if the feature is selected
		ClientMapInfo map = configurationService.getMapInfo(getMap().getMapId(), getMap().getApplicationId());
		if (selectedFeatures.contains(f.getId())) {
			if (f.getGeometry() instanceof MultiPolygon || f.getGeometry() instanceof Polygon) {
				fillColor = context.getColor(map.getPolygonSelectStyle().getFillColor(), style.getFillOpacity());
			} else if (f.getGeometry() instanceof MultiLineString || f.getGeometry() instanceof LineString) {
				strokeColor = context.getColor(map.getLineSelectStyle().getStrokeColor(), style.getStrokeOpacity());
			} else if (f.getGeometry() instanceof MultiPoint || f.getGeometry() instanceof Point) {
				strokeColor = context.getColor(map.getPointSelectStyle().getStrokeColor(), style.getStrokeOpacity());
			}
		}

		float lineWidth = style.getStrokeWidth();

		SymbolInfo symbol = null;
		if (f.getGeometry() instanceof MultiPoint || f.getGeometry() instanceof Point) {
			symbol = style.getSymbol();
		}
		// transform to user space
		f.getGeometry().apply(new MapToUserFilter());
		// now draw
		context.drawGeometry(f.getGeometry(), symbol, fillColor, strokeColor, lineWidth, dashArray, getSize());
	}

	/**
	 * ???
	 */
	private final class MapToUserFilter implements CoordinateFilter {

		public void filter(Coordinate coordinate) {
			coordinate.x = (coordinate.x - bbox.getMinX()) * getMap().getPpUnit();
			coordinate.y = (coordinate.y - bbox.getMinY()) * getMap().getPpUnit();
		}
	}

	public void setStyleInfo(NamedStyleInfo styleInfo) {
		this.styleInfo = styleInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.printing.component.impl.VectorLayerComponent#getSelectedFeatureIds()
	 */
	public String[] getSelectedFeatureIds() {
		return selectedFeatureIds;
	}

	public void setSelectedFeatureIds(String[] selectedFeatureIds) {
		this.selectedFeatureIds = selectedFeatureIds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.printing.component.impl.VectorLayerComponent#getFilter()
	 */
	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.printing.component.impl.VectorLayerComponent#isLabelsVisible()
	 */
	public boolean isLabelsVisible() {
		return labelsVisible;
	}

	public void setLabelsVisible(boolean labelsVisible) {
		this.labelsVisible = labelsVisible;
	}

	public List<InternalFeature> getFeatures() {
		return features;
	}

	public void fromDto(PrintComponentInfo info, PrintDtoConverterService service) {
		super.fromDto(info, service);
		VectorLayerComponentInfo vectorLayerInfo = (VectorLayerComponentInfo) info;
		setLabelsVisible(vectorLayerInfo.isLabelsVisible());
		setSelectedFeatureIds(vectorLayerInfo.getSelectedFeatureIds());
		setStyleInfo(vectorLayerInfo.getStyleInfo());
		setFilter(vectorLayerInfo.getFilter());
	}

}