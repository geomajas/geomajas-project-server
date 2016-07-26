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
package org.geomajas.plugin.printing.component.impl;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.LabelStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.SymbolInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.plugin.printing.component.LayoutConstraint;
import org.geomajas.plugin.printing.component.PdfContext;
import org.geomajas.plugin.printing.component.PrintComponentVisitor;
import org.geomajas.plugin.printing.component.dto.VectorLayerComponentInfo;
import org.geomajas.plugin.printing.component.service.PrintConfigurationService;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.opengis.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lowagie.text.Rectangle;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateFilter;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Internal implementation of {@link org.geomajas.plugin.printing.component.BaseLayerComponent}.
 * 
 * @author Jan De Moerloose
 */
@Component()
@Scope(value = "prototype")
public class VectorLayerComponentImpl extends BaseLayerComponentImpl<VectorLayerComponentInfo> {

	/** Style for this layer. */
	private NamedStyleInfo styleInfo;

	/** CQL filter */
	private String filter;

	/** Array of selected feature id's for this layer. */
	private String[] selectedFeatureIds = new String[0];

	/** True if labels are visible. */
	private boolean labelsVisible;

	/** The calculated bounds. */
	@XStreamOmitField
	protected Envelope bbox;

	/** List of the features. */
	@XStreamOmitField
	protected List<InternalFeature> features = new ArrayList<InternalFeature>();

	/** A sorted set of selected feature ids. */
	@XStreamOmitField
	protected Set<String> selectedFeatures = new TreeSet<String>();

	@XStreamOmitField
	private final Logger log = LoggerFactory.getLogger(VectorLayerComponentImpl.class);

	// length of the connector line for symbols
	private static final float SYMBOL_CONNECT_LENGTH = 15;

	@Autowired
	@XStreamOmitField
	private GeoService geoService;

	@Autowired
	@XStreamOmitField
	private FilterService filterService;

	@Autowired
	@XStreamOmitField
	private VectorLayerService layerService;

	@Autowired
	@XStreamOmitField
	private PrintConfigurationService configurationService;

	public VectorLayerComponentImpl() {

		// stretch to map
		getConstraint().setAlignmentX(LayoutConstraint.JUSTIFIED);
		getConstraint().setAlignmentY(LayoutConstraint.JUSTIFIED);
	}

	/**
	 * Call back visitor.
	 * 
	 * @param visitor visitor
	 */
	public void accept(PrintComponentVisitor visitor) {
		// nothing to do
	}

	@Override
	public void render(PdfContext context) {
		if (isVisible()) {
			bbox = createBbox();
			Collections.addAll(selectedFeatures, getSelectedFeatureIds());
			// Fetch features
			ClientMapInfo map = configurationService.getMapInfo(getMap().getMapId(), getMap().getApplicationId());
			try {

				// If MapCRS does not equal LayerCRS then instantiate
				// the CrsTransform which will be used for transforming the bbox
				VectorLayerInfo layerInfo = configurationService.getVectorLayerInfo(getLayerId());
				String mapCrs = map.getCrs();
				String layerCrs = layerInfo.getCrs();
				Envelope layerBbox = bbox;
				if (!mapCrs.equals(layerCrs)) {
					layerBbox = geoService.transform(layerBbox, mapCrs, layerCrs);
				}

				String geomName = layerInfo.getFeatureInfo().getGeometryType().getName();

				// If the transform is null then just use the bbox for the filter
				// Else if the transform is not null then transform the bbox for the filter
				Filter filter = filterService.createBboxFilter(layerCrs, layerBbox, geomName);

				if (getFilter() != null) {
					filter = filterService.createAndFilter(filterService.parseFilter(getFilter()), filter);
				}

				features = layerService.getFeatures(getLayerId(), geoService.getCrs2(map.getCrs()), filter, styleInfo,
						VectorLayerService.FEATURE_INCLUDE_ALL);

			} catch (Exception e) { // NOSONAR
				log.error("Error getting features", e);
			}
			
			// order features, selected last
			List<InternalFeature> orderedFeatures = new ArrayList<InternalFeature>();
			for (InternalFeature feature : features) {
				if (selectedFeatures.contains(feature.getId())) {
					orderedFeatures.add(feature);
				} else {
					orderedFeatures.add(0, feature);
				}
			}

			for (InternalFeature f : orderedFeatures) {
				drawFeature(context, map, f);
			}
			if (isLabelsVisible()) {
				for (InternalFeature f : orderedFeatures) {
					drawLabel(context, f);
				}
			}
		}
	}

	private void drawLabel(PdfContext context, InternalFeature f) {
		LabelStyleInfo labelType = styleInfo.getLabelStyle();
		String label = f.getLabel();
		if (label != null) {
			Font font = new Font("Helvetica", Font.ITALIC, 10);
			Color fontColor = Color.black;
			if (labelType.getFontStyle() != null) {
				fontColor = context
						.getColor(labelType.getFontStyle().getColor(), labelType.getFontStyle().getOpacity());
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
				context.drawLine(0.5f * (rect.getLeft() + rect.getRight()), rect.getBottom(),
						0.5f * (rect.getLeft() + rect.getRight()), rect.getBottom() - SYMBOL_CONNECT_LENGTH,
						borderColor, linewidth);
			}
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
		// SPRINT-53 Labels should be rendered in Screen Space
		new MapToUserFilter().filter(labelPosition);

		context.moveRectangleTo(rect, (float) labelPosition.x - rect.getWidth() / 2f,
				(float) labelPosition.y - rect.getHeight() / 2f);
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

	private void drawFeature(PdfContext context, ClientMapInfo map, InternalFeature f) {
		FeatureStyleInfo style = f.getStyleInfo();

		// Color, transparency, dash
		Color fillColor = context.getColor(style.getFillColor(), style.getFillOpacity());
		Color strokeColor = context.getColor(style.getStrokeColor(), style.getStrokeOpacity());
		float[] dashArray = context.getDashArray(style.getDashArray());

		// check if the feature is selected
		if (selectedFeatures.contains(f.getId())) {
			if (f.getGeometry() instanceof MultiPolygon || f.getGeometry() instanceof Polygon) {
				style = mergeStyle(style, map.getPolygonSelectStyle());
				fillColor = context.getColor(style.getFillColor(), style.getFillOpacity());
				strokeColor = context.getColor(style.getStrokeColor(), style.getStrokeOpacity());
			} else if (f.getGeometry() instanceof MultiLineString || f.getGeometry() instanceof LineString) {
				style = mergeStyle(style, map.getLineSelectStyle());
				strokeColor = context.getColor(style.getStrokeColor(), style.getStrokeOpacity());
			} else if (f.getGeometry() instanceof MultiPoint || f.getGeometry() instanceof Point) {
				style = mergeStyle(style, map.getPointSelectStyle());
				strokeColor = context.getColor(style.getStrokeColor(), style.getStrokeOpacity());
			}
		}

		float lineWidth = style.getStrokeWidth();

		SymbolInfo symbol = null;
		if (f.getGeometry() instanceof MultiPoint || f.getGeometry() instanceof Point) {
			symbol = style.getSymbol();
		}
		// clone geometry
		Geometry geometry = (Geometry) f.getGeometry().clone();
		// transform to user space
		geometry.apply(new MapToUserFilter());
		// notify geometry change !!!
		geometry.geometryChanged();
		// now draw
		context.drawGeometry(geometry, symbol, fillColor, strokeColor, lineWidth, dashArray, getSize());
	}

	private FeatureStyleInfo mergeStyle(FeatureStyleInfo base, FeatureStyleInfo extension) {
		FeatureStyleInfo merged = new FeatureStyleInfo();
		merged.setDashArray(extension.getDashArray() != null ? extension.getDashArray() : base.getDashArray());
		merged.setFillColor(extension.getFillColor() != null ? extension.getFillColor() : base.getFillColor());
		merged.setFillOpacity(extension.getFillOpacity() != -1 ? extension.getFillOpacity() : base.getFillOpacity());
		merged.setStrokeColor(extension.getStrokeColor() != null ? extension.getStrokeColor() : base.getStrokeColor());
		merged.setStrokeOpacity(extension.getStrokeOpacity() != -1 ? extension.getStrokeOpacity() : base
				.getStrokeOpacity());
		merged.setSymbol(extension.getSymbol() != null ? extension.getSymbol() : base.getSymbol());
		merged.setStrokeWidth(extension.getStrokeWidth() != -1 ? extension.getStrokeWidth() : base.getStrokeWidth());
		return merged;
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

	/**
	 * Set style for layer.
	 *
	 * @param styleInfo style info
	 */
	public void setStyleInfo(NamedStyleInfo styleInfo) {
		this.styleInfo = styleInfo;
	}

	/**
	 * Get selected feature ids.
	 *
	 * @return selected feature ids.
	 */
	public String[] getSelectedFeatureIds() {
		return selectedFeatureIds;
	}

	/**
	 * Set selected feature ids.
	 *
	 * @param selectedFeatureIds selected feature ids
	 */
	public void setSelectedFeatureIds(String[] selectedFeatureIds) {
		this.selectedFeatureIds = selectedFeatureIds;
	}

	/**
	 * Get layer filter.
	 *
	 * @return layer filter
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * Set the layer filter.
	 *
	 * @param filter layer filter
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * Are labels visible?
	 *
	 * @return true when labels are visible
	 */
	public boolean isLabelsVisible() {
		return labelsVisible;
	}

	/**
	 * Set the label visibility.
	 *
	 * @param labelsVisible labels visible?
	 */
	public void setLabelsVisible(boolean labelsVisible) {
		this.labelsVisible = labelsVisible;
	}

	@Override
	public void fromDto(VectorLayerComponentInfo vectorLayerInfo) {
		super.fromDto(vectorLayerInfo);
		setLabelsVisible(vectorLayerInfo.isLabelsVisible());
		setSelectedFeatureIds(vectorLayerInfo.getSelectedFeatureIds());
		setStyleInfo(vectorLayerInfo.getStyleInfo());
		setFilter(vectorLayerInfo.getFilter());
	}

}