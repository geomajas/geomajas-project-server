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

package org.geomajas.gwt.client.gfx.paintable;

import org.geomajas.configuration.SymbolInfo;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.geometry.Geometry;

/**
 * <p>
 * A {@link Geometry} that can be drawn onto a
 * {@link org.geomajas.gwt.client.gfx.GraphicsContext}. It therefore the
 * {@link org.geomajas.gwt.client.gfx.Paintable} interface.
 * </p>
 *
 * @author Pieter De Graef
 */
public class GfxGeometry extends AbstractWorldPaintable {

	private ShapeStyle style;

	private SymbolInfo symbolInfo;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	public GfxGeometry(String id) {
		super(id);
	}

	public GfxGeometry(String id, Geometry geometry, ShapeStyle style) {
		super(id);
		this.style = style;
		original = geometry;
	}

	/**
	 * @param id
	 * @param geometry
	 * @param style
	 * @param symbolInfo Is only needed for Point and MultiPoint Geometries.
	 */
	public GfxGeometry(String id, Geometry geometry, ShapeStyle style, SymbolInfo symbolInfo) {
		super(id);
		this.style = style;
		this.symbolInfo = symbolInfo;
		original = geometry;
	}

	// -------------------------------------------------------------------------
	// Paintable implementation:
	// -------------------------------------------------------------------------

	/**
	 * Everything that can be drawn on the map, must be accessible by a
	 * PainterVisitor!
	 *
	 * @param visitor
	 *            A PainterVisitor object.
	 */
	public void accept(PainterVisitor visitor, Object group, Bbox bounds, boolean recursive) {
		visitor.visit(this, group);
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public void setStyle(ShapeStyle style) {
		this.style = style;
	}

	public ShapeStyle getStyle() {
		return style;
	}

	public Geometry getGeometry() {
		return (Geometry) getLocation();
	}

	public void setGeometry(Geometry geometry) {
		setOriginalLocation(geometry);
	}

	/**
	 * Only needed for Point and MultiPoint Geometries.
	 * @return
	 */
	public SymbolInfo getSymbolInfo() {
		return symbolInfo;
	}

	public void setSymbolInfo(SymbolInfo symbolInfo) {
		this.symbolInfo = symbolInfo;
	}
}
