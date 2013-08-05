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

import org.geomajas.geometry.Coordinate;
import org.geomajas.smartgwt.client.action.MenuAction;
import org.geomajas.smartgwt.client.gfx.MenuContext;
import org.geomajas.smartgwt.client.i18n.I18nProvider;
import org.geomajas.smartgwt.client.map.feature.FeatureTransaction;
import org.geomajas.smartgwt.client.map.feature.TransactionGeomIndex;
import org.geomajas.smartgwt.client.map.feature.TransactionGeomIndexUtil;
import org.geomajas.smartgwt.client.map.feature.operation.InsertCoordinateOp;
import org.geomajas.smartgwt.client.util.WidgetLayout;
import org.geomajas.smartgwt.client.widget.MapWidget;

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
		super(I18nProvider.getMenu().insertPoint(), WidgetLayout.iconVertexCreate);
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
			mapWidget.render(ft, MapWidget.RenderGroup.VECTOR, MapWidget.RenderStatus.DELETE);
			InsertCoordinateOp op = new InsertCoordinateOp(index, coordinate);
			ft.execute(op);
			mapWidget.render(ft, MapWidget.RenderGroup.VECTOR, MapWidget.RenderStatus.ALL);
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
			MenuContext graphics = mapWidget.getMenuContext();
			coordinate = mapWidget.getMapModel().getMapView().getWorldViewTransformer().viewToWorld(
					graphics.getRightButtonCoordinate());
			String targetId = graphics.getRightButtonName();
			if (targetId != null && TransactionGeomIndexUtil.isEdge(targetId)) {
				index = TransactionGeomIndexUtil.getIndex(targetId);
				return true;
			}
		}
		return false;
	}
}
