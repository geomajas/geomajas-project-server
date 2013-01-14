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
