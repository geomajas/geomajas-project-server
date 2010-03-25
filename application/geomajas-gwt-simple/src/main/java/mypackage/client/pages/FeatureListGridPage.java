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

package mypackage.client.pages;

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

import java.util.List;

/**
 * <p>
 * Example page using the FeatureListTable, and showing some test-buttons.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class FeatureListGridPage extends AbstractTestPage {

	private FeatureListGrid table;

	// private boolean selectionFilter;

	public FeatureListGridPage(MapWidget map) {
		super("FeatureListGrid", map);

		HLayout buttonLayout = new HLayout();
		buttonLayout.setMembersMargin(5);
		buttonLayout.setHeight(25);
		IButton button1 = new IButton("Show structures");
		button1.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				VectorLayer layer = (VectorLayer) getMap().getMapModel().getLayer("structuresLayer");
				table.setLayer(layer);
				layer.getFeatureStore().getFeatures(GeomajasConstant.FEATURE_INCLUDE_ATTRIBUTES,
						new LazyLoadCallback() {
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

		IButton button1a = new IButton("Show roads");
		button1a.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				VectorLayer layer = (VectorLayer) getMap().getMapModel().getLayer("roadsLayer");
				table.setLayer(layer);
				layer.getFeatureStore().getFeatures(GeomajasConstant.FEATURE_INCLUDE_ATTRIBUTES,
						new LazyLoadCallback() {
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

		IButton button2 = new IButton("Clear Table");
		button2.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				table.empty();
			}
		});
		button2.setWidth(100);
		buttonLayout.addMember(button2);

		// BUTTON3 : toggle ID in table
		IButton button3 = new IButton("Toggle idInTable");
		button3.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				table.setIdInTable(!table.isIdInTable());
			}
		});
		button3.setWidth(120);
		buttonLayout.addMember(button3);

		// BUTTON4 : toggle showAllAttributes
		IButton button4 = new IButton("Toggle showAllAttributes");
		button4.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				table.setAllAttributesDisplayed(!table.isAllAttributesDisplayed());
			}
		});
		button4.setWidth(175);
		buttonLayout.addMember(button4);

		// BUTTON5 : toggle double click
		IButton button5 = new IButton("Toggle editing");
		button5.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				table.setEditingEnabled(!table.isEditingEnabled());
			}
		});
		button5.setWidth(130);
		buttonLayout.addMember(button5);

		mainLayout.addMember(buttonLayout);

		table = new FeatureListGrid(map.getMapModel());
		table.setEditingEnabled(true);
		mainLayout.addMember(table);
	}

	/**
	 * To add buttons to your test page, use this method.
	 * 
	 * @param text button text
	 * @param handler click handler
	 */
	protected void addButton(String text, ClickHandler handler) {
		IButton button = new IButton(text);
		button.addClickHandler(handler);
		button.setWidth(180);
		mainLayout.addMember(button);
	}

	public void initialize() {
		table.setShowImageAttributeOnHover(true);
	}

	public FeatureListGrid getTable() {
		return table;
	}
}
