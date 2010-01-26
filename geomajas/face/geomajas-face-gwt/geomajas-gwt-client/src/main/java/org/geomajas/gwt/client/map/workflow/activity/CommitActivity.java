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

import org.geomajas.command.CommandResponse;
import org.geomajas.extension.command.dto.PersistTransactionRequest;
import org.geomajas.extension.command.dto.PersistTransactionResponse;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.feature.FeatureTransaction;
import org.geomajas.gwt.client.map.workflow.MapModelWorkflowContext;
import org.geomajas.gwt.client.map.workflow.WorkflowContext;
import org.geomajas.gwt.client.map.workflow.WorkflowErrorHandler;
import org.geomajas.gwt.client.map.workflow.WorkflowException;

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

			GwtCommand command = new GwtCommand("command.feature.PersistTransaction");
			command.setCommandRequest(request);

			GwtCommandDispatcher.getInstance().execute(command, new CommandCallback() {

				public void execute(CommandResponse response) {
					if (response instanceof PersistTransactionResponse) {
						PersistTransactionResponse ptr = (PersistTransactionResponse) response;
						mapModel.applyFeatureTransaction(new FeatureTransaction(ft.getLayer(), ptr
								.getFeatureTransaction()));
					}
				}
			});
		}
		return context;
	}

	public WorkflowErrorHandler getErrorHandler() {
		return null;
	}
}
