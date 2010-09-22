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

package org.geomajas.gwt.client.map.workflow.activity;

import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.FeatureTransaction;
import org.geomajas.gwt.client.map.workflow.MapModelWorkflowContext;
import org.geomajas.gwt.client.map.workflow.WorkflowContext;
import org.geomajas.gwt.client.map.workflow.WorkflowErrorHandler;
import org.geomajas.gwt.client.map.workflow.WorkflowException;

/**
 * <p>
 * This activity works on a {@link MapModelWorkflowContext} as {@link WorkflowContext} and checks the
 * {@link FeatureTransaction} to see if there are any geometries that are not valid. When an invalid geometry is found,
 * an exception is thrown.
 * </p>
 *
 * @author Pieter De Graef
 */
public class ValidationActivity implements Activity {

	/**
	 * Check for geometry validity.
	 *
	 * @param context
	 *            Expects a {@link MapModelWorkflowContext}.
	 */
	public WorkflowContext execute(WorkflowContext context) throws WorkflowException {
		if (context instanceof MapModelWorkflowContext) {
			MapModelWorkflowContext mmContext = (MapModelWorkflowContext) context;
			FeatureTransaction ft = mmContext.getFeatureTransaction();
			if (ft.getNewFeatures() != null) {
				for (Feature feature : ft.getNewFeatures()) {
					if (!feature.getGeometry().isValid()) {
						throw new WorkflowException(I18nProvider.getGlobal().validationActivityError());
					}
				}
			}
		}
		return context;
	}

	/**
	 * Return null. No specific error handler is necessary.
	 */
	public WorkflowErrorHandler getErrorHandler() {
		return null;
	}
}
