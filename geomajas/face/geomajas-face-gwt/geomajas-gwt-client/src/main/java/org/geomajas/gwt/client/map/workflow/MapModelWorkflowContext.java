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

package org.geomajas.gwt.client.map.workflow;

import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.feature.FeatureTransaction;

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
