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

package org.geomajas.smartgwt.client.map.workflow;

import org.geomajas.smartgwt.client.map.MapModel;
import org.geomajas.smartgwt.client.map.feature.FeatureTransaction;

/**
 * <p>
 * Context object for the default workflow for handling feature transactions on a MapModel.
 * </p>
 *
 * @author Pieter De Graef
 */
public class MapModelWorkflowContext implements WorkflowContext {

	private boolean stopProcess;

	private MapModel mapModel;

	// -------------------------------------------------------------------------
	// WorkflowContext implementation:
	// -------------------------------------------------------------------------

	/**
	 * Set the seed data. This class expects the object to be a {@link MapModel}.
	 */
	public void setSeedData(Object seedObject) {
		if (seedObject instanceof MapModel) {
			mapModel = (MapModel) seedObject;
		}
	}

	public void setStopProcess(boolean stopProcess) {
		this.stopProcess = stopProcess;
	}

	public boolean stopProcess() {
		return stopProcess;
	}

	// -------------------------------------------------------------------------
	// Getters:
	// -------------------------------------------------------------------------

	public MapModel getMapModel() {
		return mapModel;
	}

	public FeatureTransaction getFeatureTransaction() {
		if (mapModel != null) {
			return mapModel.getFeatureEditor().getFeatureTransaction();
		}
		return null;
	}
}
