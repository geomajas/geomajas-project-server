#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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

package ${package}.client.pages;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.LazyLoadCallback;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.FeatureListGrid;
import org.geomajas.gwt.client.widget.MapWidget;

import java.util.List;

/**
 * <p>
 * This class represents a tab that displays a {@link FeatureListGrid} widget together with 2 buttons to fill the grid.
 * </p>
 *
 * @author geomajas-gwt-archetype
 */
public class FeatureListGridPage extends AbstractTab {

	private FeatureListGrid table;

	public FeatureListGridPage(MapWidget map) {
		super("FeatureListGrid", map);

		// Create a horizontal layout for the buttons:
		HLayout buttonLayout = new HLayout();
		buttonLayout.setMembersMargin(5);
		buttonLayout.setHeight(25);

		// Create a button to show the "roads" objects into a FeatureGrid:
		IButton button1a = new IButton("Show roads");
		button1a.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				VectorLayer layer = (VectorLayer) getMap().getMapModel().getLayer("roadsLayer");
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
		button1a.setWidth(110);
		buttonLayout.addMember(button1a);
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
