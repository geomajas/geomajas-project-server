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
public class JsStyleService implements Exportable {

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

	public ShapeStyle getVertexStyle() {
		return delegate.getVertexStyle();
	}

	public ShapeStyle getEdgeTentativeMoveStyle() {
		return delegate.getEdgeTentativeMoveStyle();
	}

	public ShapeStyle getBackgroundStyle() {
		return delegate.getBackgroundStyle();
	}

	public ShapeStyle getBackgroundMarkedForDeletionStyle() {
		return delegate.getBackgroundMarkedForDeletionStyle();
	}

	public ShapeStyle getVertexMarkForDeletionStyle() {
		return delegate.getVertexMarkForDeletionStyle();
	}

	public ShapeStyle getVertexHoverStyle() {
		return delegate.getVertexHoverStyle();
	}

	public ShapeStyle getVertexSelectStyle() {
		return delegate.getVertexSelectStyle();
	}

	public ShapeStyle getBackgroundDisabledStyle() {
		return delegate.getBackgroundDisabledStyle();
	}

	public ShapeStyle getVertexDisabledStyle() {
		return delegate.getVertexDisabledStyle();
	}

	public ShapeStyle getVertexSelectHoverStyle() {
		return delegate.getVertexSelectHoverStyle();
	}

	public ShapeStyle getEdgeSelectHoverStyle() {
		return delegate.getEdgeSelectHoverStyle();
	}

	public ShapeStyle getVertexSnappedStyle() {
		return delegate.getVertexSnappedStyle();
	}

	public ShapeStyle getEdgeDisabledStyle() {
		return delegate.getEdgeDisabledStyle();
	}

	public ShapeStyle getEdgeMarkForDeletionStyle() {
		return delegate.getEdgeMarkForDeletionStyle();
	}

	public ShapeStyle getEdgeStyle() {
		return delegate.getEdgeStyle();
	}

	public ShapeStyle getEdgeHoverStyle() {
		return delegate.getEdgeHoverStyle();
	}

	public ShapeStyle getLineStringStyle() {
		return delegate.getLineStringStyle();
	}

	public ShapeStyle getEdgeSelectStyle() {
		return delegate.getEdgeSelectStyle();
	}

	public ShapeStyle getLinearRingStyle() {
		return delegate.getLinearRingStyle();
	}

}
