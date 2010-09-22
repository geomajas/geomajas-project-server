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

import org.geomajas.gwt.client.action.MenuAction;
import org.geomajas.gwt.client.gfx.MenuContext;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.feature.FeatureTransaction;
import org.geomajas.gwt.client.map.feature.TransactionGeomIndex;
import org.geomajas.gwt.client.map.feature.TransactionGeomIndexUtil;
import org.geomajas.gwt.client.map.feature.operation.RemoveRingOp;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;
import org.geomajas.gwt.client.widget.MapWidget.RenderStatus;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemIfFunction;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * Remove an existing ring from a Polygon or MultiPolygon at a given index.
 * 
 * @author Pieter De Graef
 */
public class RemoveRingAction extends MenuAction implements MenuItemIfFunction {

	private MapWidget mapWidget;

	private TransactionGeomIndex index;

	/**
	 * @param mapWidget
	 *            The map on which editing is going on.
	 */
	public RemoveRingAction(MapWidget mapWidget) {
		super(I18nProvider.getMenu().removeRing(), "[ISOMORPHIC]/geomajas/osgeo/ring-delete.png");
		this.mapWidget = mapWidget;
		setEnableIfCondition(this);
	}

	/**
	 * Remove an existing ring from a Polygon or MultiPolygon at a given index.
	 * 
	 * @param event
	 *            The {@link MenuItemClickEvent} from clicking the action.
	 */
	public void onClick(MenuItemClickEvent event) {
		FeatureTransaction ft = mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		if (ft != null && index != null) {
			mapWidget.render(ft, RenderGroup.VECTOR, RenderStatus.DELETE);
			RemoveRingOp op = new RemoveRingOp(index);
			ft.execute(op);
			mapWidget.render(ft, RenderGroup.VECTOR, RenderStatus.ALL);
		}
	}

	/**
	 * Implementation of the <code>MenuItemIfFunction</code> interface. This will determine if the menu action should be
	 * enabled or not. In essence, this action will be enabled when the context menu event occurred on the area of a
	 * polygon's interior ring.
	 */
	public boolean execute(Canvas target, Menu menu, MenuItem item) {
		FeatureTransaction featureTransaction = mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		if (featureTransaction != null) {
			MenuContext graphics = mapWidget.getMenuContext();
			String targetId = graphics.getRightButtonName();
			if (targetId != null && TransactionGeomIndexUtil.isInteriorRing(targetId, true)) {
				index = TransactionGeomIndexUtil.getIndex(targetId);
				return true;
			}
		}
		return false;
	}
}
