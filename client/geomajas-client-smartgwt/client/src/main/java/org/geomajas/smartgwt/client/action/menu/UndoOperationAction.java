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

package org.geomajas.smartgwt.client.action.menu;

import org.geomajas.smartgwt.client.action.MenuAction;
import org.geomajas.smartgwt.client.controller.editing.EditController;
import org.geomajas.smartgwt.client.i18n.I18nProvider;
import org.geomajas.smartgwt.client.map.feature.FeatureTransaction;
import org.geomajas.smartgwt.client.map.feature.operation.AddCoordinateOp;
import org.geomajas.smartgwt.client.map.feature.operation.FeatureOperation;
import org.geomajas.smartgwt.client.util.WidgetLayout;
import org.geomajas.smartgwt.client.widget.MapWidget;
import org.geomajas.smartgwt.client.widget.MapWidget.RenderGroup;
import org.geomajas.smartgwt.client.widget.MapWidget.RenderStatus;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemIfFunction;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * Undo the last operation performed on the {@link FeatureTransaction}.
 * 
 * @author Pieter De Graef
 */
public class UndoOperationAction extends MenuAction implements MenuItemIfFunction {

	private MapWidget mapWidget;

	private EditController controller;

	/**
	 * @param mapWidget
	 *            The map on which editing is going on.
	 * @param controller
	 *            The actual child edit controller, not the parent!
	 */
	public UndoOperationAction(MapWidget mapWidget, EditController controller) {
		super(I18nProvider.getMenu().undoOperation(), WidgetLayout.iconUndo);
		this.mapWidget = mapWidget;
		this.controller = controller;
		setEnableIfCondition(this);
	}

	/**
	 * Undo the last operation performed on the {@link FeatureTransaction}.
	 * 
	 * @param event
	 *            The {@link MenuItemClickEvent} from clicking the action.
	 */
	public void onClick(MenuItemClickEvent event) {
		FeatureTransaction featureTransaction = mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		mapWidget.render(featureTransaction, RenderGroup.VECTOR, RenderStatus.DELETE);
		featureTransaction.undoLastOperation();
		mapWidget.render(featureTransaction, RenderGroup.VECTOR, RenderStatus.ALL);
		controller.cleanup();
	}

	// MenuItemIfFunction implementation:

	/**
	 * This function tries to find out whether or not this menu item should be enabled. Only if there are more then 1
	 * operations in the feature transaction operation queue, will this menu item be enabled.
	 */
	public boolean execute(Canvas target, Menu menu, MenuItem item) {
		FeatureTransaction featureTransaction = mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		if (featureTransaction != null) {
			boolean operationCount = featureTransaction.getOperationQueue().size() > 0;
			if (operationCount) {
				// The first addCoordinateOp may not be undone!
				FeatureOperation firstOperation = featureTransaction.getOperationQueue().get(0);
				if (firstOperation instanceof AddCoordinateOp) {
					if (featureTransaction.getNewFeatures()[0].getGeometry().getNumPoints() == 1) {
						return false;
					}
				}

				return true;
			}
		}
		return false;
	}
}
