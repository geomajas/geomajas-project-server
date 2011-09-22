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

package org.geomajas.plugin.editing.client.gfx;

import org.geomajas.gwt.client.gfx.style.ShapeStyle;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public class DefaultStyleService implements StyleService {

	private ShapeStyle vertexStyle = new ShapeStyle("#FFFFFF", 1, "#CC6600", 1, 1);

	private ShapeStyle vertexHoverStyle = new ShapeStyle("#888888", 1, "#CC6600", 1, 1);

	private ShapeStyle vertexSelectStyle = new ShapeStyle("#666666", 1, "#FFFF33", 1, 1);

	private ShapeStyle vertexSelectHoverStyle = new ShapeStyle("#888888", 1, "#FFFF33", 1, 1);

	private ShapeStyle vertexMarkForDeletionStyle = new ShapeStyle("#FF0000", 1, "#990000", 1, 2);

	private ShapeStyle edgeStyle = new ShapeStyle("#FFFFFF", 0, "#FFAA00", 1, 4);

	private ShapeStyle edgeHoverStyle = new ShapeStyle("#FFFFFF", 0, "#FFCC33", 1, 4);

	private ShapeStyle edgeSelectStyle = new ShapeStyle("#FFFFFF", 0, "#CC8800", 1, 5);

	private ShapeStyle edgeSelectHoverStyle = new ShapeStyle("#FFFFFF", 0, "#FFCC33", 1, 5);

	private ShapeStyle edgeMarkForDeletionStyle = new ShapeStyle("#FF0000", 0, "#990000", 1, 5);

	private ShapeStyle lineStringStyle = new ShapeStyle("#FFFFFF", 0, "#FFFFFF", 0, 0);

	private ShapeStyle linearRingStyle = new ShapeStyle("#FFEE00", 0, "#FFAA00", 1, 3);

	private ShapeStyle backgroundStyle = new ShapeStyle("#FFCC00", 0.5f, "#FFAA00", 0, 0);

	public ShapeStyle getEdgeMarkForDeletionStyle() {
		return edgeMarkForDeletionStyle;
	}

	public void setEdgeMarkForDeletionStyle(ShapeStyle edgeMarkForDeletionStyle) {
		this.edgeMarkForDeletionStyle = edgeMarkForDeletionStyle;
	}

	public ShapeStyle getVertexSelectHoverStyle() {
		return vertexSelectHoverStyle;
	}

	public void setVertexSelectHoverStyle(ShapeStyle vertexSelectHoverStyle) {
		this.vertexSelectHoverStyle = vertexSelectHoverStyle;
	}

	public ShapeStyle getEdgeSelectHoverStyle() {
		return edgeSelectHoverStyle;
	}

	public void setEdgeSelectHoverStyle(ShapeStyle edgeSelectHoverStyle) {
		this.edgeSelectHoverStyle = edgeSelectHoverStyle;
	}

	public ShapeStyle getVertexStyle() {
		return vertexStyle;
	}

	public void setVertexStyle(ShapeStyle vertexStyle) {
		this.vertexStyle = vertexStyle;
	}

	public ShapeStyle getVertexHoverStyle() {
		return vertexHoverStyle;
	}

	public void setVertexHoverStyle(ShapeStyle vertexHoverStyle) {
		this.vertexHoverStyle = vertexHoverStyle;
	}

	public ShapeStyle getVertexSelectStyle() {
		return vertexSelectStyle;
	}

	public void setVertexSelectStyle(ShapeStyle vertexSelectStyle) {
		this.vertexSelectStyle = vertexSelectStyle;
	}

	public ShapeStyle getVertexMarkForDeletionStyle() {
		return vertexMarkForDeletionStyle;
	}

	public void setVertexMarkForDeletionStyle(ShapeStyle vertexMarkForDeletionStyle) {
		this.vertexMarkForDeletionStyle = vertexMarkForDeletionStyle;
	}

	public ShapeStyle getEdgeStyle() {
		return edgeStyle;
	}

	public void setEdgeStyle(ShapeStyle edgeStyle) {
		this.edgeStyle = edgeStyle;
	}

	public ShapeStyle getEdgeHoverStyle() {
		return edgeHoverStyle;
	}

	public void setEdgeHoverStyle(ShapeStyle edgeHoverStyle) {
		this.edgeHoverStyle = edgeHoverStyle;
	}

	public ShapeStyle getEdgeSelectStyle() {
		return edgeSelectStyle;
	}

	public void setEdgeSelectStyle(ShapeStyle edgeSelectStyle) {
		this.edgeSelectStyle = edgeSelectStyle;
	}

	public ShapeStyle getLineStringStyle() {
		return lineStringStyle;
	}

	public void setLineStringStyle(ShapeStyle lineStringStyle) {
		this.lineStringStyle = lineStringStyle;
	}

	public ShapeStyle getLinearRingStyle() {
		return linearRingStyle;
	}

	public void setLinearRingStyle(ShapeStyle linearRingStyle) {
		this.linearRingStyle = linearRingStyle;
	}

	public ShapeStyle getBackgroundStyle() {
		return backgroundStyle;
	}

	public void setBackgroundStyle(ShapeStyle backgroundStyle) {
		this.backgroundStyle = backgroundStyle;
	}
}