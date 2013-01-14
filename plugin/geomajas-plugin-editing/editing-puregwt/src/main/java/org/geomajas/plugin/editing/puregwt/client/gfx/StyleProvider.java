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

package org.geomajas.plugin.editing.puregwt.client.gfx;

import org.geomajas.configuration.FeatureStyleInfo;

/**
 * Provides styles to be used during the editing process. By default the color scheme is quite orange.
 * 
 * @author Pieter De Graef
 */
public class StyleProvider {

	private FeatureStyleInfo vertexStyle;

	private FeatureStyleInfo vertexHoverStyle;

	private FeatureStyleInfo vertexSelectStyle;

	private FeatureStyleInfo vertexDisabledStyle;

	private FeatureStyleInfo vertexSelectHoverStyle;

	private FeatureStyleInfo vertexMarkForDeletionStyle;

	private FeatureStyleInfo edgeStyle;

	private FeatureStyleInfo edgeHoverStyle;

	private FeatureStyleInfo edgeSelectStyle;

	private FeatureStyleInfo edgeDisabledStyle;

	private FeatureStyleInfo edgeSelectHoverStyle;

	private FeatureStyleInfo edgeMarkForDeletionStyle;

	private FeatureStyleInfo edgeInsertMoveStyle;

	private FeatureStyleInfo lineStringStyle;

	private FeatureStyleInfo linearRingStyle;

	private FeatureStyleInfo backgroundStyle;

	private FeatureStyleInfo backgroundMarkedForDeletionStyle;

	private FeatureStyleInfo backgroundDisabledStyle;

	private FeatureStyleInfo snappedVertexStyle;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/** Initializes all default styles. */
	public StyleProvider() {
		vertexStyle = new FeatureStyleInfo();
		vertexStyle.setFillColor("#FFFFFF");
		vertexStyle.setFillOpacity(1);
		vertexStyle.setStrokeColor("#CC6600");
		vertexStyle.setStrokeOpacity(1);
		vertexStyle.setStrokeWidth(1);

		vertexHoverStyle = new FeatureStyleInfo();
		vertexHoverStyle.setFillColor("#888888");
		vertexHoverStyle.setFillOpacity(1);
		vertexHoverStyle.setStrokeColor("#CC6600");
		vertexHoverStyle.setStrokeOpacity(1);
		vertexHoverStyle.setStrokeWidth(1);

		vertexSelectStyle = new FeatureStyleInfo();
		vertexSelectStyle.setFillColor("#666666");
		vertexSelectStyle.setFillOpacity(1);
		vertexSelectStyle.setStrokeColor("#FFFF33");
		vertexSelectStyle.setStrokeOpacity(1);
		vertexSelectStyle.setStrokeWidth(1);

		vertexDisabledStyle = new FeatureStyleInfo();
		vertexDisabledStyle.setFillColor("#999999");
		vertexDisabledStyle.setFillOpacity(1);
		vertexDisabledStyle.setStrokeColor("#666666");
		vertexDisabledStyle.setStrokeOpacity(1);
		vertexDisabledStyle.setStrokeWidth(1);

		vertexSelectHoverStyle = new FeatureStyleInfo();
		vertexSelectHoverStyle.setFillColor("#888888");
		vertexSelectHoverStyle.setFillOpacity(1);
		vertexSelectHoverStyle.setStrokeColor("#FFFF33");
		vertexSelectHoverStyle.setStrokeOpacity(1);
		vertexSelectHoverStyle.setStrokeWidth(1);

		vertexMarkForDeletionStyle = new FeatureStyleInfo();
		vertexMarkForDeletionStyle.setFillColor("#FF0000");
		vertexMarkForDeletionStyle.setFillOpacity(1);
		vertexMarkForDeletionStyle.setStrokeColor("#990000");
		vertexMarkForDeletionStyle.setStrokeOpacity(1);
		vertexMarkForDeletionStyle.setStrokeWidth(2);

		edgeStyle = new FeatureStyleInfo();
		edgeStyle.setFillColor("#FFFFFF");
		edgeStyle.setFillOpacity(0);
		edgeStyle.setStrokeColor("#FFAA00");
		edgeStyle.setStrokeOpacity(1);
		edgeStyle.setStrokeWidth(4);

		edgeHoverStyle = new FeatureStyleInfo();
		edgeHoverStyle.setFillColor("#FFFFFF");
		edgeHoverStyle.setFillOpacity(0);
		edgeHoverStyle.setStrokeColor("#FFCC33");
		edgeHoverStyle.setStrokeOpacity(1);
		edgeHoverStyle.setStrokeWidth(6);

		edgeSelectStyle = new FeatureStyleInfo();
		edgeSelectStyle.setFillColor("#FFFFFF");
		edgeSelectStyle.setFillOpacity(0);
		edgeSelectStyle.setStrokeColor("#CC8800");
		edgeSelectStyle.setStrokeOpacity(1);
		edgeSelectStyle.setStrokeWidth(6);

		edgeDisabledStyle = new FeatureStyleInfo();
		edgeDisabledStyle.setFillColor("#FFFFFF");
		edgeDisabledStyle.setFillOpacity(0);
		edgeDisabledStyle.setStrokeColor("#999999");
		edgeDisabledStyle.setStrokeOpacity(1);
		edgeDisabledStyle.setStrokeWidth(4);

		edgeSelectHoverStyle = new FeatureStyleInfo();
		edgeSelectHoverStyle.setFillColor("#FFFFFF");
		edgeSelectHoverStyle.setFillOpacity(0);
		edgeSelectHoverStyle.setStrokeColor("#FFCC33");
		edgeSelectHoverStyle.setStrokeOpacity(1);
		edgeSelectHoverStyle.setStrokeWidth(6);

		edgeMarkForDeletionStyle = new FeatureStyleInfo();
		edgeMarkForDeletionStyle.setFillColor("#FF0000");
		edgeMarkForDeletionStyle.setFillOpacity(0);
		edgeMarkForDeletionStyle.setStrokeColor("#990000");
		edgeMarkForDeletionStyle.setStrokeOpacity(1);
		edgeMarkForDeletionStyle.setStrokeWidth(6);

		edgeInsertMoveStyle = new FeatureStyleInfo();
		edgeInsertMoveStyle.setFillColor("#FFFFFF");
		edgeInsertMoveStyle.setFillOpacity(0);
		edgeInsertMoveStyle.setStrokeColor("#666666");
		edgeInsertMoveStyle.setStrokeOpacity(1);
		edgeInsertMoveStyle.setStrokeWidth(2);

		lineStringStyle = new FeatureStyleInfo();
		lineStringStyle.setFillColor("#FFFFFF");
		lineStringStyle.setFillOpacity(0);
		lineStringStyle.setStrokeColor("#FFFFFF");
		lineStringStyle.setStrokeOpacity(0);
		lineStringStyle.setStrokeWidth(0);

		linearRingStyle = new FeatureStyleInfo();
		linearRingStyle.setFillColor("#FFEE00");
		linearRingStyle.setFillOpacity(0);
		linearRingStyle.setStrokeColor("#FFAA00");
		linearRingStyle.setStrokeOpacity(1);
		linearRingStyle.setStrokeWidth(3);

		backgroundStyle = new FeatureStyleInfo();
		backgroundStyle.setFillColor("#FFCC00");
		backgroundStyle.setFillOpacity(0.35f);
		backgroundStyle.setStrokeColor("#FFAA00");
		backgroundStyle.setStrokeOpacity(0);
		backgroundStyle.setStrokeWidth(0);

		backgroundMarkedForDeletionStyle = new FeatureStyleInfo();
		backgroundMarkedForDeletionStyle.setFillColor("#FF0000");
		backgroundMarkedForDeletionStyle.setFillOpacity(0.35f);
		backgroundMarkedForDeletionStyle.setStrokeColor("#CC0000");
		backgroundMarkedForDeletionStyle.setStrokeOpacity(0);
		backgroundMarkedForDeletionStyle.setStrokeWidth(0);

		backgroundDisabledStyle = new FeatureStyleInfo();
		backgroundDisabledStyle.setFillColor("#999999");
		backgroundDisabledStyle.setFillOpacity(0.35f);
		backgroundDisabledStyle.setStrokeColor("#999999");
		backgroundDisabledStyle.setStrokeOpacity(0);
		backgroundDisabledStyle.setStrokeWidth(0);

		snappedVertexStyle = new FeatureStyleInfo();
		snappedVertexStyle.setFillColor("#888888");
		snappedVertexStyle.setFillOpacity(1);
		snappedVertexStyle.setStrokeColor("#CC0000");
		snappedVertexStyle.setStrokeOpacity(1);
		snappedVertexStyle.setStrokeWidth(1);
	}

	// ------------------------------------------------------------------------
	// StyleService implementation:
	// ------------------------------------------------------------------------

	public FeatureStyleInfo getEdgeMarkForDeletionStyle() {
		return edgeMarkForDeletionStyle;
	}

	public void setEdgeMarkForDeletionStyle(FeatureStyleInfo edgeMarkForDeletionStyle) {
		this.edgeMarkForDeletionStyle = edgeMarkForDeletionStyle;
	}

	public FeatureStyleInfo getVertexSelectHoverStyle() {
		return vertexSelectHoverStyle;
	}

	public void setVertexSelectHoverStyle(FeatureStyleInfo vertexSelectHoverStyle) {
		this.vertexSelectHoverStyle = vertexSelectHoverStyle;
	}

	public FeatureStyleInfo getEdgeSelectHoverStyle() {
		return edgeSelectHoverStyle;
	}

	public void setEdgeSelectHoverStyle(FeatureStyleInfo edgeSelectHoverStyle) {
		this.edgeSelectHoverStyle = edgeSelectHoverStyle;
	}

	public FeatureStyleInfo getVertexStyle() {
		return vertexStyle;
	}

	public void setVertexStyle(FeatureStyleInfo vertexStyle) {
		this.vertexStyle = vertexStyle;
	}

	public FeatureStyleInfo getVertexHoverStyle() {
		return vertexHoverStyle;
	}

	public void setVertexHoverStyle(FeatureStyleInfo vertexHoverStyle) {
		this.vertexHoverStyle = vertexHoverStyle;
	}

	public FeatureStyleInfo getVertexSelectStyle() {
		return vertexSelectStyle;
	}

	public void setVertexSelectStyle(FeatureStyleInfo vertexSelectStyle) {
		this.vertexSelectStyle = vertexSelectStyle;
	}

	public FeatureStyleInfo getVertexMarkForDeletionStyle() {
		return vertexMarkForDeletionStyle;
	}

	public void setVertexMarkForDeletionStyle(FeatureStyleInfo vertexMarkForDeletionStyle) {
		this.vertexMarkForDeletionStyle = vertexMarkForDeletionStyle;
	}

	public FeatureStyleInfo getEdgeStyle() {
		return edgeStyle;
	}

	public void setEdgeStyle(FeatureStyleInfo edgeStyle) {
		this.edgeStyle = edgeStyle;
	}

	public FeatureStyleInfo getEdgeHoverStyle() {
		return edgeHoverStyle;
	}

	public void setEdgeHoverStyle(FeatureStyleInfo edgeHoverStyle) {
		this.edgeHoverStyle = edgeHoverStyle;
	}

	public FeatureStyleInfo getEdgeSelectStyle() {
		return edgeSelectStyle;
	}

	public void setEdgeSelectStyle(FeatureStyleInfo edgeSelectStyle) {
		this.edgeSelectStyle = edgeSelectStyle;
	}

	public FeatureStyleInfo getLineStringStyle() {
		return lineStringStyle;
	}

	public void setLineStringStyle(FeatureStyleInfo lineStringStyle) {
		this.lineStringStyle = lineStringStyle;
	}

	public FeatureStyleInfo getLinearRingStyle() {
		return linearRingStyle;
	}

	public void setLinearRingStyle(FeatureStyleInfo linearRingStyle) {
		this.linearRingStyle = linearRingStyle;
	}

	public FeatureStyleInfo getBackgroundStyle() {
		return backgroundStyle;
	}

	public void setBackgroundStyle(FeatureStyleInfo backgroundStyle) {
		this.backgroundStyle = backgroundStyle;
	}

	public FeatureStyleInfo getEdgeTentativeMoveStyle() {
		return edgeInsertMoveStyle;
	}

	public void setEdgeInsertMoveStyle(FeatureStyleInfo edgeInsertMoveStyle) {
		this.edgeInsertMoveStyle = edgeInsertMoveStyle;
	}

	public FeatureStyleInfo getVertexDisabledStyle() {
		return vertexDisabledStyle;
	}

	public void setVertexDisabledStyle(FeatureStyleInfo vertexDisabledStyle) {
		this.vertexDisabledStyle = vertexDisabledStyle;
	}

	public FeatureStyleInfo getEdgeDisabledStyle() {
		return edgeDisabledStyle;
	}

	public void setEdgeDisabledStyle(FeatureStyleInfo edgeDisabledStyle) {
		this.edgeDisabledStyle = edgeDisabledStyle;
	}

	public FeatureStyleInfo getBackgroundDisabledStyle() {
		return backgroundDisabledStyle;
	}

	public void setBackgroundDisabledStyle(FeatureStyleInfo backgroundDisabledStyle) {
		this.backgroundDisabledStyle = backgroundDisabledStyle;
	}

	public FeatureStyleInfo getBackgroundMarkedForDeletionStyle() {
		return backgroundMarkedForDeletionStyle;
	}

	public void setBackgroundMarkedForDeletionStyle(FeatureStyleInfo backgroundMarkedForDeletionStyle) {
		this.backgroundMarkedForDeletionStyle = backgroundMarkedForDeletionStyle;
	}

	public FeatureStyleInfo getVertexSnappedStyle() {
		return snappedVertexStyle;
	}

	public void setSnappedVertexStyle(FeatureStyleInfo snappedVertexStyle) {
		this.snappedVertexStyle = snappedVertexStyle;
	}
}