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
package org.geomajas.plugin.rasterizing;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import org.geotools.geometry.jts.LiteShape2;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DirectLayer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.geotools.renderer.lite.StyledShapePainter;
import org.geotools.renderer.style.SLDStyleFactory;
import org.geotools.renderer.style.Style2D;
import org.geotools.styling.Style;
import org.geotools.util.NumberRange;
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

	private List<Geometry> geometries = new ArrayList<Geometry>();

	private Style style;

	private StyledShapePainter painter = new StyledShapePainter();

	private SLDStyleFactory styleFactory = new SLDStyleFactory();

	private final Logger log = LoggerFactory.getLogger(RasterDirectLayer.class);

	public GeometryDirectLayer(Style style) {
		this.style = style;
	}

	@Override
	public void draw(Graphics2D graphics, MapContent map, MapViewport viewport) {
		for (Geometry geometry : geometries) {
			LiteShape2 shape;
			try {
				shape = new LiteShape2(geometry, null, null, false, false);
				NumberRange<Double> range = NumberRange.create(0d, 100d);

				Style2D style2D = styleFactory.createStyle(null, style.featureTypeStyles().get(0).rules().get(0)
						.symbolizers().get(0), range);
				painter.paint(graphics, shape, style2D, 1d);
			} catch (Exception e) {
				log.error("could not draw " + getTitle(), e);
			}
		}
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

	public Style getStyle() {
		return style;
	}

	public void setStyle(Style style) {
		this.style = style;
	}

}
