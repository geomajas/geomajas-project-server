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

package org.geomajas.smartgwt.client.map.workflow.activity;

import org.geomajas.command.dto.PersistTransactionRequest;
import org.geomajas.command.dto.PersistTransactionResponse;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.smartgwt.client.map.MapModel;
import org.geomajas.smartgwt.client.map.feature.FeatureTransaction;
import org.geomajas.smartgwt.client.map.workflow.MapModelWorkflowContext;
import org.geomajas.smartgwt.client.map.workflow.WorkflowContext;
import org.geomajas.smartgwt.client.map.workflow.WorkflowErrorHandler;
import org.geomajas.smartgwt.client.map.workflow.WorkflowException;

/**
 * <p>
 * Activity that persists a {@link FeatureTransaction} to the server, and then applies the result - if successful - on
 * the MapModel. The MapModel, can be found in the given workflow context, as this class expects a
 * {@link MapModelWorkflowContext}.
 * </p>
 *
 * @author Pieter De Graef
 */
public class CommitActivity implements Activity {

	public WorkflowContext execute(WorkflowContext context) throws WorkflowException {
		if (context instanceof MapModelWorkflowContext) {
			MapModelWorkflowContext mmc = (MapModelWorkflowContext) context;
			final MapModel mapModel = mmc.getMapModel();
			final FeatureTransaction ft = mmc.getFeatureTransaction();

			PersistTransactionRequest request = new PersistTransactionRequest();
			request.setFeatureTransaction(ft.toDto());
			request.setCrs(mapModel.getCrs());

			GwtCommand command = new GwtCommand(PersistTransactionRequest.COMMAND);
			command.setCommandRequest(request);

			GwtCommandDispatcher.getInstance().execute(command,
					new AbstractCommandCallback<PersistTransactionResponse>() {

				public void execute(PersistTransactionResponse response) {
					mapModel.applyFeatureTransaction(new FeatureTransaction(ft.getLayer(),
							response.getFeatureTransaction()));
				}
			});
		}
		return context;
	}

	public WorkflowErrorHandler getErrorHandler() {
		return null;
	}
}
