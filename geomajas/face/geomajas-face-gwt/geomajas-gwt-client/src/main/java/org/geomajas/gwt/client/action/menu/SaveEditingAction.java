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

package org.geomajas.gwt.client.action.menu;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt.client.action.MenuAction;
import org.geomajas.gwt.client.controller.editing.ParentEditController;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.feature.FeatureTransaction;
import org.geomajas.gwt.client.map.workflow.MapModelWorkflowContext;
import org.geomajas.gwt.client.map.workflow.SequenceProcessor;
import org.geomajas.gwt.client.map.workflow.WorkflowContext;
import org.geomajas.gwt.client.map.workflow.WorkflowErrorHandler;
import org.geomajas.gwt.client.map.workflow.WorkflowProcessor;
import org.geomajas.gwt.client.map.workflow.activity.Activity;
import org.geomajas.gwt.client.map.workflow.activity.CommitActivity;
import org.geomajas.gwt.client.map.workflow.activity.ValidationActivity;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;
import org.geomajas.gwt.client.widget.MapWidget.RenderStatus;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * Action that persists the editing to the server.
 *
 * @author Pieter De Graef
 */
public class SaveEditingAction extends MenuAction {

	private MapWidget mapWidget;

	private MapModel mapModel;

	private ParentEditController controller;

	/**
	 * Constructor for the save editing action. When this constructor is used, this action will not only save the
	 * FeatureTransaction, but also clean it's painted version up from the map.
	 *
	 * @param mapWidget
	 *            The <code>MapWidget</code> on which editing is in progress.
	 * @param controller
	 *            The current parent editing controller active on the map. Can be null (if this action is executed from
	 *            somewhere else)
	 */
	public SaveEditingAction(MapWidget mapWidget, ParentEditController controller) {
		this(mapWidget.getMapModel());
		this.mapWidget = mapWidget;
		this.controller = controller;
	}

	/**
	 * Constructor for the save editing action.If this constructor is used, no visual changes on the map are possible.
	 * In other words, a painter FeatureTransaction on the map will remain.
	 *
	 * @param mapModel
	 *            The <code>MapModel</code> on which editing is in progress.
	 */
	public SaveEditingAction(MapModel mapModel) {
		super(I18nProvider.getMenu().saveEditing(), "[ISOMORPHIC]/geomajas/save.png");
		this.mapModel = mapModel;
	}

	/**
	 * Saves editing, and also removes the {@link FeatureTransaction} object from the map.
	 *
	 * @param event
	 *            The {@link MenuItemClickEvent} from clicking the action.
	 */
	public void onClick(MenuItemClickEvent event) {
		FeatureTransaction featureTransaction = mapModel.getFeatureEditor().getFeatureTransaction();
		if (featureTransaction != null) {
			List<Activity> activities = new ArrayList<Activity>();
			activities.add(new ValidationActivity());
			activities.add(new CommitActivity());
			WorkflowProcessor processor = new SequenceProcessor(new MapModelWorkflowContext());
			processor.setDefaultErrorHandler(new WorkflowErrorHandler() {

				public void handleError(WorkflowContext context, Throwable throwable) {
					SC.warn(I18nProvider.getGlobal().saveEditingAborted() + throwable.getMessage());
				}
			});
			processor.setActivities(activities);
			processor.doActivities(mapModel);

			// Cleaning up duties: controller and MapWidget (if they're present)
			if (controller != null) {
				controller.cleanup();
			}
			if (mapWidget != null) {
				mapWidget.render(featureTransaction, RenderGroup.SCREEN, RenderStatus.DELETE);
			}
			mapModel.getFeatureEditor().stopEditing();
		}
	}
}
