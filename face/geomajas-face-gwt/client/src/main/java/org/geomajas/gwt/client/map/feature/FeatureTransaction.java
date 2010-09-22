/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.map.feature;

import java.util.Stack;

import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.map.feature.operation.FeatureOperation;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.Bbox;

/**
 * ???
 *
 * @author Pieter De Graef
 */
public class FeatureTransaction implements Paintable {

	private Stack<FeatureOperation> operationQueue;

	private Feature[] oldFeatures;

	private Feature[] newFeatures;

	private VectorLayer layer;

	private boolean geometryChanged;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	public FeatureTransaction(VectorLayer layer, Feature[] oldFeatures, Feature[] newFeatures) {
		operationQueue = new Stack<FeatureOperation>();
		this.layer = layer;
		this.oldFeatures = oldFeatures;
		this.newFeatures = newFeatures;
	}

	public FeatureTransaction(VectorLayer layer, org.geomajas.layer.feature.FeatureTransaction dto) {
		operationQueue = new Stack<FeatureOperation>();
		this.layer = layer;
		if (dto.getOldFeatures() != null) {
			oldFeatures = new Feature[dto.getOldFeatures().length];
			for (int i = 0; i < oldFeatures.length; i++) {
				oldFeatures[i] = new Feature(dto.getOldFeatures()[i], layer);
			}
		}
		if (dto.getNewFeatures() != null) {
			newFeatures = new Feature[dto.getNewFeatures().length];
			for (int i = 0; i < newFeatures.length; i++) {
				newFeatures[i] = new Feature(dto.getNewFeatures()[i], layer);
			}
		}
	}

	// -------------------------------------------------------------------------
	// Paintable implementation:
	// -------------------------------------------------------------------------

	public void accept(PainterVisitor visitor, Object group, Bbox bounds, boolean recursive) {
		visitor.visit(this, group);
	}

	public String getId() {
		return "featureTransaction";
	}

	// -------------------------------------------------------------------------
	// Class specific functions:
	// -------------------------------------------------------------------------

	/**
	 * Execute an operation on each of the features from the "newFeature" array. Also store the operation on a queue, so
	 * it is possible to undo all the changes afterwards.
	 *
	 * @param op
	 *            The operation to apply on each feature.
	 */
	public void execute(FeatureOperation op) {
		if (newFeatures != null && newFeatures.length > 0) {
			operationQueue.add(op);
			for (int i = 0; i < newFeatures.length; i++) {
				op.execute(newFeatures[i]);
			}
		}
	}

	public void undoLastOperation() {
		if (operationQueue.size() > 0 && newFeatures != null && newFeatures.length > 0) {
			FeatureOperation op = operationQueue.pop();
			for (int i = 0; i < newFeatures.length; i++) {
				op.undo(newFeatures[i]);
			}
		}
	}

	public Object clone() {
		// Clone only the features:
		Feature[] oldf = null;
		if (oldFeatures != null) {
			oldf = new Feature[oldFeatures.length];
			for (int i = 0; i < oldFeatures.length; i++) {
				oldf[i] = (Feature) oldFeatures[i].clone();
			}
		}
		Feature[] newf = null;
		if (newFeatures != null) {
			newf = new Feature[newFeatures.length];
			for (int i = 0; i < newFeatures.length; i++) {
				newf[i] = (Feature) newFeatures[i].clone();
			}
		}
		return new FeatureTransaction(layer, oldf, newf);
	}

	/**
	 * Transform this object into a DTO feature transaction.
	 *
	 * @return
	 */
	public org.geomajas.layer.feature.FeatureTransaction toDto() {
		org.geomajas.layer.feature.FeatureTransaction dto = new org.geomajas.layer.feature.FeatureTransaction();
		dto.setLayerId(layer.getServerLayerId());
		if (oldFeatures != null) {
			org.geomajas.layer.feature.Feature[] oldDto = new org.geomajas.layer.feature.Feature[oldFeatures.length];
			for (int i = 0; i < oldFeatures.length; i++) {
				oldDto[i] = oldFeatures[i].toDto();
			}
			dto.setOldFeatures(oldDto);
		}
		if (newFeatures != null) {
			org.geomajas.layer.feature.Feature[] newDto = new org.geomajas.layer.feature.Feature[newFeatures.length];
			for (int i = 0; i < newFeatures.length; i++) {
				newDto[i] = newFeatures[i].toDto();
			}
			dto.setNewFeatures(newDto);
		}
		return dto;
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public Feature[] getOldFeatures() {
		return oldFeatures;
	}

	public void setOldFeatures(Feature[] oldFeatures) {
		this.oldFeatures = oldFeatures;
	}

	public Feature[] getNewFeatures() {
		return newFeatures;
	}

	public void setNewFeatures(Feature[] newFeatures) {
		this.newFeatures = newFeatures;
	}

	public VectorLayer getLayer() {
		return layer;
	}

	public void setLayer(VectorLayer layer) {
		this.layer = layer;
	}

	public boolean isGeometryChanged() {
		return geometryChanged;
	}

	public void setGeometryChanged(boolean geometryChanged) {
		this.geometryChanged = geometryChanged;
	}

	public Stack<FeatureOperation> getOperationQueue() {
		return operationQueue;
	}

	public void setOperationQueue(Stack<FeatureOperation> operationQueue) {
		this.operationQueue = operationQueue;
	}
}
