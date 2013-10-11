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
import org.geomajas.gwt.client.gfx.MenuContext;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.feature.FeatureTransaction;
import org.geomajas.gwt.client.map.feature.TransactionGeomIndex;
import org.geomajas.gwt.client.map.feature.TransactionGeomIndexUtil;
import org.geomajas.gwt.client.map.feature.operation.RemoveRingOp;
import org.geomajas.gwt.client.util.WidgetLayout;
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
		super(I18nProvider.getMenu().removeRing(), WidgetLayout.iconRingDelete);
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
