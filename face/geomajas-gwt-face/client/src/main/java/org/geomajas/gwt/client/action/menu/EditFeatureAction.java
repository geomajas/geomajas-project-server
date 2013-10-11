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
import org.geomajas.gwt.client.controller.editing.LineStringEditController;
import org.geomajas.gwt.client.controller.editing.MultiLineStringEditController;
import org.geomajas.gwt.client.controller.editing.MultiPolygonEditController;
import org.geomajas.gwt.client.controller.editing.ParentEditController;
import org.geomajas.gwt.client.controller.editing.PointEditController;
import org.geomajas.gwt.client.controller.editing.PolygonEditController;
import org.geomajas.gwt.client.controller.editing.EditController.EditMode;
import org.geomajas.gwt.client.i18n.MenuMessages;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.FeatureTransaction;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;
import org.geomajas.gwt.client.widget.MapWidget.RenderStatus;
import org.geomajas.layer.LayerType;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemIfFunction;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * Menu action that starts editing a selected feature on the map.
 * 
 * @author Pieter De Graef
 */
public class EditFeatureAction extends MenuAction implements MenuItemIfFunction {

	private MapWidget mapWidget;

	private ParentEditController controller;

	private Feature feature;

	/**
	 * Constructor for the menu action to edit a selected feature on the map.
	 * 
	 * @param mapWidget
	 *            The <code>MapWidget</code> on which editing is in progress.
	 * @param controller
	 *            The current parent editing controller active on the map.
	 */
	public EditFeatureAction(MapWidget mapWidget, ParentEditController controller) {
		super(((MenuMessages) GWT.create(MenuMessages.class)).editFeature(),
				WidgetLayout.iconVectorEdit);
		this.mapWidget = mapWidget;
		this.controller = controller;
		setEnableIfCondition(this);
	}

	/**
	 * Activate editing, and set the correct child editing controller on the parent editing controller.
	 */
	public void onClick(MenuItemClickEvent event) {
		if (feature != null && feature.isSelected()) {
			FeatureTransaction ft = mapWidget.getMapModel().getFeatureEditor().startEditing(
					new Feature[] { feature.clone() }, new Feature[] { feature.clone() });
			mapWidget.render(ft, RenderGroup.VECTOR, RenderStatus.ALL);
			VectorLayer vLayer = feature.getLayer();
			if (vLayer.getLayerInfo().getLayerType() == LayerType.POINT) {
				controller.setController(new PointEditController(mapWidget, controller));
			} else if (vLayer.getLayerInfo().getLayerType() == LayerType.MULTIPOINT) {
				mapWidget.getMapModel().getFeatureEditor().stopEditing();
				mapWidget.render(ft, RenderGroup.VECTOR, RenderStatus.DELETE);
				SC.warn("Editing of MultiPoint layers is not supported yet....");
			} else if (vLayer.getLayerInfo().getLayerType() == LayerType.LINESTRING) {
				controller.setController(new LineStringEditController(mapWidget, controller));
			} else if (vLayer.getLayerInfo().getLayerType() == LayerType.MULTILINESTRING) {
				controller.setController(new MultiLineStringEditController(mapWidget, controller));
			} else if (vLayer.getLayerInfo().getLayerType() == LayerType.POLYGON) {
				controller.setController(new PolygonEditController(mapWidget, controller));
			} else if (vLayer.getLayerInfo().getLayerType() == LayerType.MULTIPOLYGON) {
				controller.setController(new MultiPolygonEditController(mapWidget, controller));
			}
			controller.setEditMode(EditMode.DRAG_MODE);
		}
	}

	/**
	 * Implementation of the <code>MenuItemIfFunction</code> interface. This will determine if the menu action should be
	 * enabled or not. In essence, this action will be enabled if a vector-layer is selected that allows the updating of
	 * existing features.
	 */
	public boolean execute(Canvas target, Menu menu, MenuItem item) {
		int count = mapWidget.getMapModel().getNrSelectedFeatures();
		if (count == 1) {
			for (VectorLayer layer : mapWidget.getMapModel().getVectorLayers()) {
				if (layer.getSelectedFeatures().size() == 1) {
					// It's already selected, so we assume the feature is fully loaded:
					feature = layer.getFeatureStore().getPartialFeature(layer.getSelectedFeatures().iterator().next());
					return true;
				}
			}
			return true;
		}
		return false;
	}
}
