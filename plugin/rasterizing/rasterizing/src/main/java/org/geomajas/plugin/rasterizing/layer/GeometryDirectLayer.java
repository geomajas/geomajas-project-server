/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing.layer;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DirectLayer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.renderer.lite.RendererUtilities;
import org.geotools.renderer.lite.StyledShapePainter;
import org.geotools.renderer.style.SLDStyleFactory;
import org.geotools.renderer.style.Style2D;
import org.geotools.styling.Style;
import org.geotools.util.NumberRange;
import org.opengis.referencing.operation.MathTransform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Geotools layer responsible for rendering arbitrary geometries (defined in world space).
 * 
 * @author Jan De Moerloose
 * 
 */
public class GeometryDirectLayer extends DirectLayer {
	
	/**
	 * Name of geometry attribute.
	 */
	public static final String DEFAULT_GEOMETRY_NAME = "the_geom";

	private List<Geometry> geometries = new ArrayList<Geometry>();

	private Style style;

	private StyledShapePainter painter = new StyledShapePainter();

	private SLDStyleFactory styleFactory = new SLDStyleFactory();
	
	private Class<? extends Geometry> binding;

	private final Logger log = LoggerFactory.getLogger(GeometryDirectLayer.class);

	public GeometryDirectLayer(Style style, Class<? extends Geometry> binding) {
		super();
		this.style = style;
		this.binding = binding;
	}

	@Override
	public void draw(Graphics2D graphics, MapContent map, MapViewport viewport) {
		MathTransform worldtoScreen = createAffineTransform(viewport);
		for (Geometry geometry : geometries) {
			LiteShape2 shape;
			try {
				Geometry screenGeom = JTS.transform(geometry, worldtoScreen);
				shape = new LiteShape2(screenGeom, null, null, false);
				NumberRange<Double> range = NumberRange.create(0d, 100d);

				Style2D style2D = styleFactory.createStyle(null, style.featureTypeStyles().get(0).rules().get(0)
						.symbolizers().get(0), range);
				painter.paint(graphics, shape, style2D, 1d);
			} catch (Exception e) { // NOSONAR
				log.error("could not draw " + getTitle(), e);
			}
		}
	}

	private MathTransform createAffineTransform(MapViewport viewport) {
		return ProjectiveTransform.create(RendererUtilities.worldToScreenTransform(viewport.getBounds(),
				viewport.getScreenArea()));
	}

	@Override
	public ReferencedEnvelope getBounds() {
		return null;
	}

	public List<Geometry> getGeometries() {
		return geometries;
	}

	public void setGeometries(List<Geometry> geometries) {
		this.geometries = geometries;
	}
	
	public Class<? extends Geometry> getBinding() {
		return binding;
	}

	public Style getStyle() {
		return style;
	}

	public void setStyle(Style style) {
		this.style = style;
	}

}
