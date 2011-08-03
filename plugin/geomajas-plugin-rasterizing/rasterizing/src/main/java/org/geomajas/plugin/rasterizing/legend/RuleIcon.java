/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing.legend;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.renderer.lite.MetaBufferEstimator;
import org.geotools.renderer.lite.StyledShapePainter;
import org.geotools.renderer.style.SLDStyleFactory;
import org.geotools.renderer.style.Style2D;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.visitor.RescaleStyleVisitor;
import org.geotools.util.Range;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Fixed size Swing component that graphically represents an SLD rule.
 * 
 * @author Jan De Moerloose
 * 
 */
public class RuleIcon extends JComponent {

	private int width;

	private int height;

	private Rule rule;

	private SLDStyleFactory styleFactory = new SLDStyleFactory();

	private GeometryFactory geometryFactory = new GeometryFactory();

	private static final StyledShapePainter STYLED_SHAPE_PAINTER = new StyledShapePainter();

	private LiteShape2 line;

	private LiteShape2 polygon;

	private LiteShape2 point;

	private SimpleFeatureType schema;

	public RuleIcon(SimpleFeatureType schema, Rule rule, int width, int height) {
		this.rule = rule;
		this.width = width;
		this.height = height;
		this.schema = schema;
		setSize(width, height);
	}

	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		for (Symbolizer symbolizer : rule.getSymbolizers()) {
			if (symbolizer instanceof RasterSymbolizer) {
				throw new IllegalStateException("It is not legal to have a RasterSymbolizer here");
			} else {
				Range<Double> scaleRange = new Range<Double>(Double.class, 0.0, Double.MAX_VALUE);
				Object sample = createSampleFeature();
				double w = width - 1;
				double h = height - 1;
				// must cram symbol in icon space, so rescale if too big !
				// metabuffer is a good way to judge the size...
				MetaBufferEstimator estimator = new MetaBufferEstimator();
				estimator.visit(symbolizer);
				double estimatedSize = estimator.getBuffer();
				Symbolizer rescaled = symbolizer;
				if (estimatedSize > w || estimatedSize > h) {
					// rescale necessary
					double scale = Math.min(w, h) / estimatedSize;
					RescaleStyleVisitor rescaler = new RescaleStyleVisitor(scale);
					rescaler.visit(symbolizer);
					rescaled = (Symbolizer) rescaler.getCopy();
				}
				Style2D style2d = styleFactory.createStyle(sample, rescaled, scaleRange);
				LiteShape2 shape = createShape(symbolizer, w, h);
				if (style2d != null) {
					STYLED_SHAPE_PAINTER.paint(g2d, shape, style2d, 1.0);
				}
			}
		}
	}

	private Object createSampleFeature() {
		Object sample = null;
		if (schema != null) {
			sample = SimpleFeatureBuilder.template(schema, "drawMe");
		}
		return sample;
	}

	protected LiteShape2 createShape(Symbolizer symbolizer, double w, double h) {
		try {
			if (symbolizer instanceof LineSymbolizer) {
				if (line == null) {
					List<Coordinate> coords = new ArrayList<Coordinate>();
					coords.add(new Coordinate(0, 0));
					coords.add(new Coordinate(0.75 * w, 0.25 * h));
					coords.add(new Coordinate(0.25 * w, 0.75 * h));
					coords.add(new Coordinate(w, h));
					LineString linestring = geometryFactory.createLineString(
							coords.toArray(new Coordinate[coords.size()]));
					line = new LiteShape2(linestring, null, null, false);
				}
				return line;
			} else if (symbolizer instanceof PolygonSymbolizer) {
				if (polygon == null) {
					List<Coordinate> coords = new ArrayList<Coordinate>();
					coords.add(new Coordinate(0, 0));
					coords.add(new Coordinate(w, 0));
					coords.add(new Coordinate(w, h));
					coords.add(new Coordinate(0, h));
					coords.add(new Coordinate(0, 0));
					LinearRing ring = geometryFactory.createLinearRing(coords.toArray(new Coordinate[coords.size()]));
					Polygon p = geometryFactory.createPolygon(ring, null);
					polygon = new LiteShape2(p, null, null, false);
				}
				return polygon;
			} else if (symbolizer instanceof PointSymbolizer) {
				if (point == null) {
					Coordinate coord = new Coordinate(w / 2, h / 2);
					Point p = geometryFactory.createPoint(coord);
					point = new LiteShape2(p, null, null, false);
				}
				return point;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}

	@Override
	public Dimension getMaximumSize() {
		return new Dimension(width, height);
	}

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(width, height);
	}

}
