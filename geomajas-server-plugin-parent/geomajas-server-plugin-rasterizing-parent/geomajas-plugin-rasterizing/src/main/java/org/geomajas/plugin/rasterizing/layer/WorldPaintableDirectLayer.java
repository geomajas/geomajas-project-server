/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing.layer;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.geomajas.geometry.Bbox;
import org.geomajas.internal.layer.feature.InternalFeatureImpl;
import org.geomajas.layer.LayerException;
import org.geomajas.plugin.rasterizing.command.dto.WorldEllipseInfo;
import org.geomajas.plugin.rasterizing.command.dto.WorldGeometryInfo;
import org.geomajas.plugin.rasterizing.command.dto.WorldImageInfo;
import org.geomajas.plugin.rasterizing.command.dto.WorldPaintableInfo;
import org.geomajas.plugin.rasterizing.command.dto.WorldRectangleInfo;
import org.geomajas.plugin.rasterizing.command.dto.WorldTextInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.SymbolizerTypeInfo;
import org.geomajas.sld.TextSymbolizerInfo;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DirectLayer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.renderer.label.LabelCacheImpl;
import org.geotools.renderer.lite.LabelCache;
import org.geotools.renderer.lite.RendererUtilities;
import org.geotools.renderer.lite.StyledShapePainter;
import org.geotools.renderer.style.PolygonStyle2D;
import org.geotools.renderer.style.SLDStyleFactory;
import org.geotools.renderer.style.Style2D;
import org.geotools.styling.PointPlacement;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.geotools.util.NumberRange;
import org.opengis.filter.FilterFactory;
import org.opengis.referencing.operation.MathTransform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Geotools layer responsible for rendering arbitrary paintables (defined in world space).
 * 
 * @author Jan De Moerloose
 * 
 */
public class WorldPaintableDirectLayer extends DirectLayer {

	private List<WorldPaintableInfo> paintables = new ArrayList<WorldPaintableInfo>();

	private StyledShapePainter painter = new StyledShapePainter();

	private SLDStyleFactory styleFactory = new SLDStyleFactory();

	private FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);

	private WorldPaintableLayerFactory factory;

	private final Logger log = LoggerFactory.getLogger(WorldPaintableDirectLayer.class);

	public WorldPaintableDirectLayer(List<WorldPaintableInfo> paintables, WorldPaintableLayerFactory factory) {
		this.paintables = paintables;
		this.factory = factory;
	}

	@Override
	public void draw(Graphics2D graphics, MapContent map, MapViewport viewport) {
		LabelCache labelCache = new LabelCacheImpl();
		labelCache.start();
		labelCache.startLayer("direct");
		MathTransform worldtoScreen = createAffineTransform(viewport);
		for (WorldPaintableInfo paintable : paintables) {
			try {
				NumberRange<Double> range = NumberRange.create(0d, 10E20d);
				Geometry geometry = null;
				Geometry screenGeom = null;
				Style2D style2D = null;
				if (paintable.getGeometrySymbolizerInfo() != null) {
					Symbolizer geometrySymbolizer = convertGeometry(paintable.getGeometrySymbolizerInfo());
					style2D = styleFactory.createStyle(null, geometrySymbolizer, range);
					if (paintable instanceof WorldRectangleInfo) {
						WorldRectangleInfo w = (WorldRectangleInfo) paintable;
						Bbox bounds = w.getBbox();
						geometry = JTS.toGeometry(new Envelope(bounds.getX(), bounds.getMaxX(), bounds.getY(), bounds
								.getMaxY()));
					} else if (paintable instanceof WorldGeometryInfo) {
						WorldGeometryInfo g = (WorldGeometryInfo) paintable;
						geometry = factory.getConverterService().toInternal(g.getGeometry());
					}
					if (geometry != null) {
						screenGeom = JTS.transform(geometry, worldtoScreen);
						LiteShape2 shape = new LiteShape2(screenGeom, null, null, false);
						painter.paint(graphics, shape, style2D, 1d);
					}
					if (paintable instanceof WorldEllipseInfo) {
						if (style2D instanceof PolygonStyle2D) {
							// geotools can't draw circle, draw it ourselves
							WorldEllipseInfo c = (WorldEllipseInfo) paintable;
							Bbox bounds = c.getBbox();
							geometry = JTS.toGeometry(new Envelope(bounds.getX(), bounds.getMaxX(), bounds.getY(),
									bounds.getMaxY()));
							screenGeom = (Polygon) JTS.transform(geometry, worldtoScreen);
							Envelope env = screenGeom.getEnvelopeInternal();
							Shape shape = new Ellipse2D.Double(env.getMinX(), env.getMinY(), env.getWidth(),
									env.getHeight());
							Graphics2D g = (Graphics2D) graphics.create();
							PolygonStyle2D ps = (PolygonStyle2D) style2D;
							if (ps.getFill() != null) {
								graphics.setPaint(ps.getFill());
								graphics.setComposite(ps.getFillComposite());
								graphics.fill(shape);
							}

							if (ps.getContour() != null) {
								graphics.setPaint(ps.getContour());
								graphics.setStroke(ps.getStroke());
								graphics.setComposite(ps.getContourComposite());
								graphics.draw(shape);
							}
						}
					}
				}
				if (paintable instanceof WorldImageInfo) {
					WorldImageInfo im = (WorldImageInfo) paintable;
					Bbox bounds = im.getBbox();
					geometry = JTS.toGeometry(new Envelope(bounds.getX(), bounds.getMaxX(), bounds.getY(), bounds
							.getMaxY()));
					screenGeom = JTS.transform(geometry, worldtoScreen);
					Resource resource = factory.getResourceService().find(im.getUrl());
					if (resource != null) {
						if (resource.isReadable()) {
							Envelope screenEnvelope = screenGeom.getEnvelopeInternal();
							BufferedImage buff = ImageIO.read(resource.getInputStream());
							// find transform between image bounds and screen bounds
							double scaleX = screenEnvelope.getWidth() / buff.getWidth();
							double scaleY = screenEnvelope.getHeight() / buff.getHeight();
							AffineTransform transform = new AffineTransform();
							double tx = screenGeom.getEnvelopeInternal().getMinX();
							double ty = screenGeom.getEnvelopeInternal().getMinY();
							transform.translate(tx, ty);
							transform.scale(scaleX, scaleY);
							Graphics2D g = (Graphics2D) graphics.create();
							if (style2D instanceof PolygonStyle2D) {
								PolygonStyle2D ps = (PolygonStyle2D) style2D;
								if (ps.getFillComposite() != null) {
									g.setComposite(ps.getFillComposite());
								}
							}
							g.drawImage(buff, transform, null);
						}
					}
				}
				if (paintable.getLabelSymbolizerInfo() != null) {
					// we have a label, set the text on the symbolizer
					TextSymbolizer labelSymbolizer = convertText(paintable.getLabelSymbolizerInfo());
					labelSymbolizer.setLabel(filterFactory.literal(paintable.getLabel()));
					Coordinate anchor = null;
					// find a good position
					if (geometry != null) {
						// use the geoservice for geometries/rectangles/ellipses/images
						InternalFeatureImpl f = new InternalFeatureImpl();
						f.setGeometry(geometry);
						anchor = factory.getGeoService().calcDefaultLabelPosition(f);
						// displace to center !!!
						if (labelSymbolizer.getLabelPlacement() instanceof PointPlacement) {
							((PointPlacement) labelSymbolizer.getLabelPlacement()).getAnchorPoint().setAnchorPointX(
									filterFactory.literal(0.5));
							((PointPlacement) labelSymbolizer.getLabelPlacement()).getAnchorPoint().setAnchorPointY(
									filterFactory.literal(0.5));
						}
					} else if (paintable instanceof WorldTextInfo) {
						// use the upper-left corner for text
						WorldTextInfo t = (WorldTextInfo) paintable;
						anchor = new Coordinate(t.getAnchor().getX(), t.getAnchor().getY());
						// displace because anchor is upper-left corner !!!
						if (labelSymbolizer.getLabelPlacement() instanceof PointPlacement) {
							((PointPlacement) labelSymbolizer.getLabelPlacement()).getAnchorPoint().setAnchorPointY(
									filterFactory.literal(1.0));
						}
					}
					if (anchor != null) {
						Point worldAnchor = new GeometryFactory().createPoint(anchor);
						Point screenAnchor = (Point) JTS.transform(worldAnchor, worldtoScreen);
						LiteShape2 anchorPoint = new LiteShape2(screenAnchor, null, null, false);
						labelCache.put("direct", labelSymbolizer, null, anchorPoint, range);
					}
				}
			} catch (Exception e) { // NOSONAR
				log.error("could not draw paintable " + paintable.getLabel() + " in layer " + getTitle(), e);
			}
		}
		labelCache.endLayer("direct", graphics, viewport.getScreenArea());
		labelCache.end(graphics, viewport.getScreenArea());
	}

	private Symbolizer convertGeometry(SymbolizerTypeInfo info) throws LayerException {
		RuleInfo rule = new RuleInfo();
		rule.getSymbolizerList().add(info);
		return factory.getStyleConverterService().convert(rule).symbolizers().get(0);
	}

	private TextSymbolizer convertText(TextSymbolizerInfo info) throws LayerException {
		RuleInfo rule = new RuleInfo();
		rule.getSymbolizerList().add(info);
		return (TextSymbolizer) factory.getStyleConverterService().convert(rule).symbolizers().get(0);
	}

	private MathTransform createAffineTransform(MapViewport viewport) {
		return ProjectiveTransform.create(RendererUtilities.worldToScreenTransform(viewport.getBounds(),
				viewport.getScreenArea()));
	}

	@Override
	public void dispose() {
		super.preDispose();
		super.dispose();
	}

	@Override
	public ReferencedEnvelope getBounds() {
		return null;
	}

}
