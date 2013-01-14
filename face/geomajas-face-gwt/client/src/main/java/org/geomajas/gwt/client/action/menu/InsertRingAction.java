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

import org.geomajas.geometry.Coordinate;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.gwt.client.action.MenuAction;
import org.geomajas.gwt.client.controller.editing.ParentEditController;
import org.geomajas.gwt.client.controller.editing.EditController.EditMode;
import org.geomajas.gwt.client.gfx.MenuContext;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.FeatureTransaction;
import org.geomajas.gwt.client.map.feature.LazyLoadCallback;
import org.geomajas.gwt.client.map.feature.LazyLoader;
import org.geomajas.gwt.client.map.feature.TransactionGeomIndex;
import org.geomajas.gwt.client.map.feature.TransactionGeomIndexUtil;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.spatial.geometry.LinearRing;
import org.geomajas.gwt.client.spatial.geometry.MultiPolygon;
import org.geomajas.gwt.client.spatial.geometry.Polygon;
import org.geomajas.gwt.client.spatial.geometry.operation.AddRingOperation;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemIfFunction;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * Insert a new ring in the {@link Polygon} or {@link MultiPolygon} at a given index.
 * 
 * @author Pieter De Graef
 */
public class InsertRingAction extends MenuAction implements MenuItemIfFunction {

	private MapWidget mapWidget;

	private ParentEditController controller;

	private TransactionGeomIndex index;

	/**
	 * @param mapWidget
	 *            The map on which editing is going on.
	 * @param controller
	 *            The current parent editing controller active on the map.
	 */
	public InsertRingAction(MapWidget mapWidget, ParentEditController controller) {
		super(I18nProvider.getMenu().insertRing(), WidgetLayout.iconRingAdd);
		this.mapWidget = mapWidget;
		this.controller = controller;
		setEnableIfCondition(this);
	}

	/**
	 * Insert a new point in the geometry at a given index. The index is taken from the context menu event. This
	 * function will add a new empty interior ring in the polygon in question.
	 * 
	 * @param event
	 *            The {@link MenuItemClickEvent} from clicking the action.
	 */
	public void onClick(MenuItemClickEvent event) {
		final FeatureTransaction ft = mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		if (ft != null && index != null) {
			List<Feature> features = new ArrayList<Feature>();
			features.add(ft.getNewFeatures()[index.getFeatureIndex()]);
			LazyLoader.lazyLoad(features, GeomajasConstant.FEATURE_INCLUDE_GEOMETRY, new LazyLoadCallback() {

				public void execute(List<Feature> response) {
					controller.setEditMode(EditMode.INSERT_MODE);
					Geometry geometry = response.get(0).getGeometry();
					if (geometry instanceof Polygon) {
						geometry = addRing((Polygon) geometry);
					} else if (geometry instanceof MultiPolygon) {
						geometry = addRing((MultiPolygon) geometry);
					}
					ft.getNewFeatures()[index.getFeatureIndex()].setGeometry(geometry);
					controller.setGeometryIndex(index);
					controller.hideGeometricInfo();
				}
			});
		}
	}

	/**
	 * Implementation of the <code>MenuItemIfFunction</code> interface. This will determine if the menu action should be
	 * enabled or not. In essence, this action will be enabled when the context menu event occurred on the area of a
	 * polygon's exterior ring.
	 */
	public boolean execute(Canvas target, Menu menu, MenuItem item) {
		FeatureTransaction featureTransaction = mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		if (featureTransaction != null) {
			MenuContext graphics = mapWidget.getMenuContext();
			String targetId = graphics.getRightButtonName();
			if (targetId != null && TransactionGeomIndexUtil.isExteriorRing(targetId, true)) {
				index = TransactionGeomIndexUtil.getIndex(targetId);
				return true;
			}
		}
		return false;
	}

	// -------------------------------------------------------------------------
	// Private functions:
	// -------------------------------------------------------------------------

	private Polygon addRing(Polygon polygon) {
		LinearRing interiorRing = polygon.getGeometryFactory().createLinearRing(new Coordinate[] {});
		AddRingOperation op = new AddRingOperation(interiorRing);
		index.setInteriorRingIndex(polygon.getNumInteriorRing());
		index.setExteriorRing(false);
		return (Polygon) op.execute(polygon);
	}

	private MultiPolygon addRing(MultiPolygon multiPolygon) {
		if (index.getGeometryIndex() >= 0) {
			Polygon[] polygons = new Polygon[multiPolygon.getNumGeometries()];
			for (int n = 0; n < multiPolygon.getNumGeometries(); n++) {
				if (n == index.getGeometryIndex()) {
					polygons[n] = addRing((Polygon) multiPolygon.getGeometryN(n));
				} else {
					polygons[n] = (Polygon) multiPolygon.getGeometryN(n);
				}
			}
			return multiPolygon.getGeometryFactory().createMultiPolygon(polygons);
		}
		return multiPolygon;
	}
}
