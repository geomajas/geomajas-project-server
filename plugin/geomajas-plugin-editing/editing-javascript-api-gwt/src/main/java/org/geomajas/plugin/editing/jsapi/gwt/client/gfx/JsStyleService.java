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
package org.geomajas.plugin.editing.jsapi.gwt.client.gfx;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.plugin.editing.gwt.client.gfx.DefaultStyleService;
import org.geomajas.plugin.editing.gwt.client.gfx.PointSymbolizerShapeAndSize;
import org.geomajas.plugin.editing.gwt.client.gfx.StyleService;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.NoExport;

/**
 * JavaScript wrapper of {@link StyleService}.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 * 
 */
@Export("StyleService")
@ExportPackage("org.geomajas.plugin.editing.gfx")
@Api(allMethods = true)
public class JsStyleService implements Exportable, StyleService {

	private StyleService delegate;

	/**
	 * Default constructor (needed for exporter?).
	 */
	public JsStyleService() {
		delegate = new DefaultStyleService();
	}

	/**
	 * Delegating constructor.
	 * 
	 * @param delegate
	 */
	@NoExport
	public JsStyleService(StyleService delegate) {
		this.delegate = delegate;
	}

	/**
	 * @see #isCloseRingWhileInserting()
	 * @param closeRingWhileInserting
	 */
	public void setCloseRingWhileInserting(boolean closeRingWhileInserting) {
		delegate.setCloseRingWhileInserting(closeRingWhileInserting);
	}

	/**
	 * Should a second drag line be drawn when creating a linear ring ?
	 * 
	 * @return true when it should be drawn, false otherwise
	 */
	public boolean isCloseRingWhileInserting() {
		return delegate.isCloseRingWhileInserting();
	}

	/**
	 * Gets the {@link ShapeStyle} for vertex elements.
	 *
	 * @return shapestyle with configurable fill and style properties
	 */
	public ShapeStyle getVertexStyle() {
		return delegate.getVertexStyle();
	}

	/**
	 * Gets the {@link ShapeStyle} for edge tantative move.
	 *
	 * @return shapestyle with configurable fill and style properties
	 */
	public ShapeStyle getEdgeTentativeMoveStyle() {
		return delegate.getEdgeTentativeMoveStyle();
	}

	/**
	 * Gets the {@link ShapeStyle} for the background.
	 *
	 * @return shapestyle with configurable fill and style properties
	 */
	public ShapeStyle getBackgroundStyle() {
		return delegate.getBackgroundStyle();
	}

	/**
	 * Gets the {@link ShapeStyle} for background marked-for-deletion elements.
	 *
	 * @return shapestyle with configurable fill and style properties
	 */
	public ShapeStyle getBackgroundMarkedForDeletionStyle() {
		return delegate.getBackgroundMarkedForDeletionStyle();
	}

	/**
	 * Gets the {@link ShapeStyle} for vertex elements marked for deletion.
	 *
	 * @return shapestyle with configurable fill and style properties
	 */
	public ShapeStyle getVertexMarkForDeletionStyle() {
		return delegate.getVertexMarkForDeletionStyle();
	}

	/**
	 * Gets the {@link ShapeStyle} for vertex elements when hovering over them.
	 *
	 * @return shapestyle with configurable fill and style properties
	 */
	public ShapeStyle getVertexHoverStyle() {
		return delegate.getVertexHoverStyle();
	}

	/**
	 * Gets the {@link ShapeStyle} for vertex elements when selected.
	 *
	 * @return shapestyle with configurable fill and style properties
	 */
	public ShapeStyle getVertexSelectStyle() {
		return delegate.getVertexSelectStyle();
	}

	/**
	 * Gets the {@link ShapeStyle} for background when disabled.
	 *
	 * @return shapestyle with configurable fill and style properties
	 */
	public ShapeStyle getBackgroundDisabledStyle() {
		return delegate.getBackgroundDisabledStyle();
	}

	/**
	 * Gets the {@link ShapeStyle} for vertex elements when disabled.
	 *
	 * @return shapestyle with configurable fill and style properties
	 */
	public ShapeStyle getVertexDisabledStyle() {
		return delegate.getVertexDisabledStyle();
	}

	/**
	 * Gets the {@link ShapeStyle} for vertex elements when selected and hovering over.
	 *
	 * @return shapestyle with configurable fill and style properties
	 */
	public ShapeStyle getVertexSelectHoverStyle() {
		return delegate.getVertexSelectHoverStyle();
	}

	/**
	 * Gets the {@link ShapeStyle} for edge elements when selected and hovering over.
	 *
	 * @return shapestyle with configurable fill and style properties
	 */
	public ShapeStyle getEdgeSelectHoverStyle() {
		return delegate.getEdgeSelectHoverStyle();
	}

	/**
	 * Gets the {@link ShapeStyle} for vertex elements when snapped.
	 *
	 * @return shapestyle with configurable fill and style properties
	 */
	public ShapeStyle getVertexSnappedStyle() {
		return delegate.getVertexSnappedStyle();
	}

	/**
	 * Gets the {@link ShapeStyle} for edge elements when disabled.
	 *
	 * @return shapestyle with configurable fill and style properties
	 */
	public ShapeStyle getEdgeDisabledStyle() {
		return delegate.getEdgeDisabledStyle();
	}

	/**
	 * Gets the {@link ShapeStyle} for edge elements that are marked for deletion.
	 *
	 * @return shapestyle with configurable fill and style properties
	 */
	public ShapeStyle getEdgeMarkForDeletionStyle() {
		return delegate.getEdgeMarkForDeletionStyle();
	}

	/**
	 * Gets the {@link ShapeStyle} for edge elements.
	 *
	 * @return shapestyle with configurable fill and style properties
	 */
	public ShapeStyle getEdgeStyle() {
		return delegate.getEdgeStyle();
	}

	/**
	 * Gets the {@link ShapeStyle} for edge elements when hovered over.
	 *
	 * @return shapestyle with configurable fill and style properties
	 */
	public ShapeStyle getEdgeHoverStyle() {
		return delegate.getEdgeHoverStyle();
	}

	/**
	 * Gets the {@link ShapeStyle} for line elements.
	 *
	 * @return shapestyle with configurable fill and style properties
	 */
	public ShapeStyle getLineStringStyle() {
		return delegate.getLineStringStyle();
	}

	/**
	 * Gets the {@link ShapeStyle} for edge elements when selected.
	 *
	 * @return shapestyle with configurable fill and style properties
	 */
	public ShapeStyle getEdgeSelectStyle() {
		return delegate.getEdgeSelectStyle();
	}

	/**
	 * Gets the {@link ShapeStyle} for linearRing elements.
	 *
	 * @return shapestyle with configurable fill and style properties
	 */
	public ShapeStyle getLinearRingStyle() {
		return delegate.getLinearRingStyle();
	}

	/**
	 * Gets the {@link PointSymbolizerShapeAndSize} for the point on drawing.
	 *
	 * @return pointSymbolizerShapeAndSize with shape and size
	 */
	public PointSymbolizerShapeAndSize getPointSymbolizerShapeAndSize() {
		return delegate.getPointSymbolizerShapeAndSize();
	}

}
