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
import org.geomajas.gwt.client.util.WidgetLayout;
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
		super(I18nProvider.getMenu().saveEditing(), WidgetLayout.iconSaveAlt);
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
				mapWidget.render(featureTransaction, RenderGroup.VECTOR, RenderStatus.DELETE);
			}
			mapModel.getFeatureEditor().stopEditing();
		}
	}
}
