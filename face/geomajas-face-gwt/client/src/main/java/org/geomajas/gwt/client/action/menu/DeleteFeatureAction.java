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
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemIfFunction;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * Menu action that deletes a selected feature on the map.
 * 
 * @author Pieter De Graef
 */
public class DeleteFeatureAction extends MenuAction implements MenuItemIfFunction {

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
	public DeleteFeatureAction(MapWidget mapWidget, ParentEditController controller) {
		super(I18nProvider.getMenu().deleteFeature(), WidgetLayout.iconVectorRemove);
		this.mapWidget = mapWidget;
		this.controller = controller;
		setEnableIfCondition(this);
	}

	/**
	 * Prepare a FeatureTransaction for deletion of the selected feature, and ask if the user wants to continue. If
	 * he/she does, the feature will be deselected and then the FeatureTransaction will be committed.
	 */
	public void onClick(MenuItemClickEvent event) {
		if (feature != null && feature.isSelected()) {
			SC.confirm(
					I18nProvider.getGlobal().confirmDeleteFeature(feature.getLabel(), feature.getLayer().getLabel()),
					new BooleanCallback() {

						public void execute(Boolean value) {
							if (value) {
								feature.getLayer().deselectFeature(feature);
								mapWidget.getMapModel().getFeatureEditor()
										.startEditing(new Feature[] { feature }, null);
								SaveEditingAction action = new SaveEditingAction(mapWidget, controller);
								action.onClick(null);
							}
						}
					});
		}
	}

	/**
	 * Implementation of the <code>MenuItemIfFunction</code> interface. This will determine if the menu action should be
	 * enabled or not. In essence, this action will be enabled if a vector-layer is selected that allows the deleting of
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
