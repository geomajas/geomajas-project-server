/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package mypackage.client.pages;

import java.util.List;

import org.geomajas.global.GeomajasConstant;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.LazyLoadCallback;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.FeatureListGrid;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * <p>
 * This class represents a tab that displays a {@link FeatureGrid} widget together with 2 buttons to fill the grid.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class FeatureListGridPage extends AbstractTab {

	private FeatureListGrid table;

	public FeatureListGridPage(MapWidget map) {
		super("FeatureListGrid", map);

		// Create a horizontal layout for the buttons:
		HLayout buttonLayout = new HLayout();
		buttonLayout.setMembersMargin(5);
		buttonLayout.setHeight(25);

		// Create a button to show the "structures" objects into a FeatureGrid:
		IButton button1 = new IButton("Show countries");
		button1.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				VectorLayer layer = (VectorLayer) getMap().getMapModel().getLayer("countries110mLayer");
				table.setLayer(layer);
				layer.getFeatureStore().getFeatures(GeomajasConstant.FEATURE_INCLUDE_ALL, new LazyLoadCallback() {

					// Add all the features currently in the layer's FeatureStore to the grid:
					public void execute(List<Feature> response) {
						for (Feature feature : response) {
							table.addFeature(feature);
						}
					}
				});
			}
		});
		button1.setWidth(120);
		buttonLayout.addMember(button1);
		mainLayout.addMember(buttonLayout);

		// Create the FeatureGrid that shows alpha-numerical attributes of features:
		table = new FeatureListGrid(map.getMapModel());
		table.setEditingEnabled(true);
		mainLayout.addMember(table);
	}

	public void initialize() {
	}

	public FeatureListGrid getTable() {
		return table;
	}
}
