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

package org.geomajas.gwt.client.map.feature;

import java.util.Stack;

import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.map.feature.operation.FeatureOperation;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.Bbox;

/**
 * Feature transaction representation.
 *
 * @author Pieter De Graef
 */
public class FeatureTransaction implements Paintable {

	private Stack<FeatureOperation> operationQueue;

	private Feature[] oldFeatures;

	private Feature[] newFeatures;

	private VectorLayer layer;

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
			for (Feature newFeature : newFeatures) {
				op.execute(newFeature);
			}
		}
	}

	public void undoLastOperation() {
		if (operationQueue.size() > 0 && newFeatures != null && newFeatures.length > 0) {
			FeatureOperation op = operationQueue.pop();
			for (Feature newFeature : newFeatures) {
				op.undo(newFeature);
			}
		}
	}

	@Override
	public Object clone() { // NOSONAR super.clone() not supported by GWT
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
	 * @return feature transaction DTO
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

	/**
	 * Get the old/original features.
	 *
	 * @return array of features
	 */
	public Feature[] getOldFeatures() {
		return oldFeatures;
	}

	/**
	 * Set the old/original features.
	 *
	 * @param oldFeatures array of features
	 */
	public void setOldFeatures(Feature[] oldFeatures) {
		this.oldFeatures = oldFeatures;
	}

	/**
	 * Get the new/updated features.
	 *
	 * @return array of features
	 */
	public Feature[] getNewFeatures() {
		return newFeatures;
	}

	/**
	 * Set the new/updated features.
	 *
	 * @param newFeatures array of new features
	 */
	public void setNewFeatures(Feature[] newFeatures) {
		this.newFeatures = newFeatures;
	}

	/**
	 * Set the layer for the features.
	 *
	 * @return layer
	 */
	public VectorLayer getLayer() {
		return layer;
	}

	/**
	 * Set the layer for the features.
	 *
	 * @param layer layer
	 */
	public void setLayer(VectorLayer layer) {
		this.layer = layer;
	}

	/**
	 * Get the operation queue.
	 *
	 * @return stack of feature operations
	 */
	public Stack<FeatureOperation> getOperationQueue() {
		return operationQueue;
	}

	/**
	 * Set the operation queue.
	 *
	 * @param operationQueue stack of operations
	 */
	public void setOperationQueue(Stack<FeatureOperation> operationQueue) {
		this.operationQueue = operationQueue;
	}
}
