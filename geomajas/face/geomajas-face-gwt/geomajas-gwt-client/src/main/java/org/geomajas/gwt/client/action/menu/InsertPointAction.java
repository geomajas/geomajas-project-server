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

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.action.MenuAction;
import org.geomajas.gwt.client.gfx.MenuGraphicsContext;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.feature.FeatureTransaction;
import org.geomajas.gwt.client.map.feature.TransactionGeomIndex;
import org.geomajas.gwt.client.map.feature.TransactionGeomIndexUtil;
import org.geomajas.gwt.client.map.feature.operation.InsertCoordinateOp;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemIfFunction;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * Insert a new point in the geometry at a given index. This action will take the target from the context menu event,
 * and create an index from it.
 * 
 * @author Pieter De Graef
 */
public class InsertPointAction extends MenuAction implements MenuItemIfFunction {

	private MapWidget mapWidget;

	private TransactionGeomIndex index;

	private Coordinate coordinate;

	/**
	 * Create the menu action for inserting points in a geometry.
	 * 
	 * @param mapWidget
	 *            The map on which editing is going on.
	 */
	public InsertPointAction(MapWidget mapWidget) {
		super(I18nProvider.getMenu().insertPoint(), "[ISOMORPHIC]/geomajas/vertex-create.png");
		this.mapWidget = mapWidget;
		setEnableIfCondition(this);
	}

	/**
	 * Insert a new point in the geometry at a given index. This action will take the target from the context menu
	 * event, and create an index from it. Then insert a new coordinate with the position equal to the context menu
	 * event's position at the index.
	 * 
	 * @param event
	 *            The {@link MenuItemClickEvent} from clicking the action.
	 */
	public void onClick(MenuItemClickEvent event) {
		FeatureTransaction ft = mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		if (ft != null && index != null && coordinate != null) {
			mapWidget.render(ft, "delete");
			InsertCoordinateOp op = new InsertCoordinateOp(index, coordinate);
			ft.execute(op);
			mapWidget.render(ft, "all");
		}
	}

	/**
	 * Implementation of the <code>MenuItemIfFunction</code> interface. This will determine if the menu action should be
	 * enabled or not. In essence, this action will be enabled when the context menu event occurred on an edge of the
	 * painted <code>FeatureTransaction</code>.
	 */
	public boolean execute(Canvas target, Menu menu, MenuItem item) {
		FeatureTransaction featureTransaction = mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		if (featureTransaction != null) {
			MenuGraphicsContext graphics = mapWidget.getGraphics();
			coordinate = mapWidget.getMapModel().getMapView().getWorldViewTransformer().viewToWorld(
					graphics.getRightButtonCoordinate());
			String targetId = graphics.getRightButtonTarget();
			if (targetId != null && TransactionGeomIndexUtil.isEdge(targetId)) {
				index = TransactionGeomIndexUtil.getIndex(targetId);
				return true;
			}
		}
		return false;
	}
}
