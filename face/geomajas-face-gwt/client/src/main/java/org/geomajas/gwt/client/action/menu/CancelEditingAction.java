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

import org.geomajas.gwt.client.action.MenuAction;
import org.geomajas.gwt.client.controller.editing.ParentEditController;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.feature.FeatureTransaction;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;
import org.geomajas.gwt.client.widget.MapWidget.RenderStatus;

import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * Action that stops the editing and also removes any {@link org.geomajas.gwt.client.map.feature.FeatureTransaction}
 * from the {@link org.geomajas.gwt.client.map.feature.FeatureEditor}.
 *
 * @author Pieter De Graef
 */
public class CancelEditingAction extends MenuAction {

	private MapWidget mapWidget;

	private ParentEditController controller;

	/**
	 * Constructor for the cancel editing action.
	 *
	 * @param mapWidget
	 *            The <code>MapWidget</code> on which editing is in progress.
	 * @param controller
	 *            The current parent editing controller active on the map.
	 */
	public CancelEditingAction(MapWidget mapWidget, ParentEditController controller) {
		super(I18nProvider.getMenu().cancelEditing(), WidgetLayout.iconQuit);
		this.mapWidget = mapWidget;
		this.controller = controller;
	}

	/**
	 * Cancels editing, and also removes the FeatureTransaction object from the map.
	 *
	 * @param event
	 *            The {@link MenuItemClickEvent} from clicking the action.
	 */
	public void onClick(MenuItemClickEvent event) {
		FeatureTransaction featureTransaction = mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		if (featureTransaction != null) {
			controller.cleanup();
			mapWidget.render(featureTransaction,  RenderGroup.VECTOR, RenderStatus.DELETE);
			mapWidget.getMapModel().getFeatureEditor().stopEditing();
		}
	}
}
