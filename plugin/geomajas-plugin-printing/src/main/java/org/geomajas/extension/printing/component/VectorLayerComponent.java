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
package org.geomajas.extension.printing.component;

import com.lowagie.text.Rectangle;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateFilter;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.geomajas.configuration.LabelAttributeInfo;
import org.geomajas.configuration.MapInfo;
import org.geomajas.configuration.StyleInfo;
import org.geomajas.configuration.SymbolInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.extension.printing.PdfContext;
import org.geomajas.geometry.Bbox;
import org.geomajas.layer.Layer;
import org.geomajas.layer.feature.RenderedFeature;
import org.geomajas.service.BboxService;
import org.geomajas.service.FilterCreator;
import org.geomajas.service.GeoService;
import org.geomajas.service.VectorLayerService;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.referencing.CRS;
import org.opengis.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * ???
 *
 * @author Pieter De Graef
 */
@XmlRootElement
public class VectorLayerComponent extends BaseLayerComponent {

	/** Array of StyleDefinition for this layer. */
	private List<StyleInfo> styleDefinitions;

	/** CQL filter */
	private String filter;

	/** Array of selected feature id's for this layer. */
	private String[] selectedFeatureIds = new String[0];

	/** True if labels are visible. */
	private boolean labelsVisible;

	/** The calculated bounds */
	@XmlTransient
	protected Bbox bbox;

	/** List of the features */
	@XmlTransient
	protected List<RenderedFeature> features = new ArrayList<RenderedFeature>();

	/** A sorted set of selected feature ids */
	@XmlTransient
	protected Set<String> selectedFeatures = new TreeSet<String>();

	private final Logger log = LoggerFactory.getLogger(VectorLayerComponent.class);

	// length of the connector line for symbols
	private static final float SYMBOL_CONNECT_LENGTH = 15;

	private GeoService geoService;

	private BboxService bboxService;

	private FilterCreator filterCreator;

	private VectorLayerService layerService;

	public VectorLayerComponent() {
		// todo needed for JAXB but looses the services, causing NPE later on
	}

	public VectorLayerComponent(GeoService geoService, BboxService bboxService, FilterCreator filterCreator,
			VectorLayerService layerService) {
		this.geoService = geoService;
		this.bboxService = bboxService;
		this.filterCreator = filterCreator;
		this.layerService = layerService;

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
				MapInfo map = context.getMap(getMap().getMapId());
				String geomName = ((VectorLayerInfo) context.getLayer(getLayerId()).getLayerInfo()).getFeatureInfo()
						.getGeometryType().getName();
				GeometryFactory factory = new GeometryFactory(new PrecisionModel(Math.pow(10, map.getPrecision())),
						geoService.getSridFromCrs(map.getCrs()));

				Filter filter = filterCreator
						.createIntersectsFilter(factory.toGeometry(bboxService.toEnvelope(bbox)), geomName);
				if (getFilter() != null) {
					filter = filterCreator.createLogicFilter(CQL.toFilter(getFilter()), "and", filter);
				}

				features = layerService.getFeatures(getLayerId(), CRS.decode(map.getCrs()), filter,
						styleDefinitions, VectorLayerService.FEATURE_INCLUDE_ALL);
			} catch (Exception e) {
				log.error("Error rendering vectorlayerRenderer", e);
			}

			for (RenderedFeature f : features) {
				drawFeature(context, f);
			}
			if (isLabelsVisible()) {
				for (RenderedFeature f : features) {
					drawLabel(context, f);
				}
			}
		}
	}

	private void drawLabel(PdfContext context, RenderedFeature f) {
		Layer layer = context.getLayer(getLayerId());
		LabelAttributeInfo labelType = ((VectorLayerInfo) layer.getLayerInfo()).getLabelAttribute();
		String label = f.getLabel();

		Font font = new Font("Helvetica", Font.ITALIC, 10);
		Color fontColor = Color.black;
		if (labelType.getFontStyle() != null) {
			fontColor = context.getColor(labelType.getFontStyle().getFillColor(), labelType.getFontStyle()
					.getFillOpacity());
		}
		Rectangle rect = calculateLabelRect(context, f, label, font);
		Color bgColor = Color.white;
		if (labelType.getBackgroundStyle() != null) {
			bgColor = context.getColor(labelType.getBackgroundStyle().getFillColor(), labelType
					.getBackgroundStyle().getFillOpacity());
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

	private float getSymbolHeight(RenderedFeature f) {
		SymbolInfo info = f.getStyleInfo().getSymbol();
		if (info.getCircle() != null) {
			return 2 * info.getCircle().getR();
		} else {
			return info.getRect().getH();
		}
	}

	private Rectangle calculateLabelRect(PdfContext context, RenderedFeature f, String label, Font font) {
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

	private void drawFeature(PdfContext context, RenderedFeature f) {
		StyleInfo style = f.getStyleInfo();

		// Color, transparency, dash
		Color fillColor = context.getColor(style.getFillColor(), style.getFillOpacity());
		Color strokeColor = context.getColor(style.getStrokeColor(), style.getStrokeOpacity());
		float[] dashArray = context.getDashArray(style.getDashArray());

		// check if the feature is selected
		MapInfo map = context.getMap(getMap().getMapId());
		if (selectedFeatures.contains(f.getLocalId())) {
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
			coordinate.x = (coordinate.x - bbox.getX()) * getMap().getPpUnit();
			coordinate.y = (coordinate.y - bbox.getY()) * getMap().getPpUnit();
		}
	}

	/**
	 * Resets cyclic references like child -> parent relationship.
	 *
	 * @param u
	 * @param parent
	 */
	public void afterUnmarshal(Unmarshaller u, Object parent) {
		setParent((PrintComponent) parent);
	}

	public void setStyleDefinitions(StyleInfo... styleDefs) {
		styleDefinitions = new ArrayList<StyleInfo>();
		Collections.addAll(styleDefinitions, styleDefs);
	}

	public String[] getSelectedFeatureIds() {
		return selectedFeatureIds;
	}

	public void setSelectedFeatureIds(String[] selectedFeatureIds) {
		this.selectedFeatureIds = selectedFeatureIds;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public boolean isLabelsVisible() {
		return labelsVisible;
	}

	public void setLabelsVisible(boolean labelsVisible) {
		this.labelsVisible = labelsVisible;
	}

	public List<RenderedFeature> getFeatures() {
		return features;
	}
}