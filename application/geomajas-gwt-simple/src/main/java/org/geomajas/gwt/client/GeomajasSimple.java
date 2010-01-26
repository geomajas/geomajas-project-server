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

package org.geomajas.gwt.client;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.pages.AbstractTestPage;
import org.geomajas.gwt.client.pages.ButtonPage;
import org.geomajas.gwt.client.pages.FeatureListGridPage;
import org.geomajas.gwt.client.pages.SearchPage;
import org.geomajas.gwt.client.simple.i18n.Simple;
import org.geomajas.gwt.client.widget.ActivityMonitor;
import org.geomajas.gwt.client.widget.LayerTree;
import org.geomajas.gwt.client.widget.Legend;
import org.geomajas.gwt.client.widget.LoadingScreen;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.OverviewMap;
import org.geomajas.gwt.client.widget.ScaleSelect;
import org.geomajas.gwt.client.widget.Toolbar;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.smartgwt.client.core.KeyIdentifier;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.util.KeyCallback;
import com.smartgwt.client.util.Page;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;

/**
 * ???
 * 
 * @author check subversion
 */
public class GeomajasSimple implements EntryPoint {

	private MapWidget map;

	private OverviewMap overviewMap;

	private Legend legend;

	private TabSet tabSet = new TabSet();

	private List<AbstractTestPage> tabs = new ArrayList<AbstractTestPage>();

	public GeomajasSimple() {
	}

	public void onModuleLoad() {
		I18nProvider.setLookUp(GWT.<ConstantsWithLookup>create(Simple.class));
		String name = GWT.getModuleName();
		if (!"org.geomajas.gwt.GeomajasSimple".equals(name)) {
			return;
		}

		// Debug console (ctrl-d)
		if (!GWT.isScript()) {
			KeyIdentifier debugKey = new KeyIdentifier();
			debugKey.setCtrlKey(true);
			debugKey.setKeyName("D");

			Page.registerKey(debugKey, new KeyCallback() {

				public void execute(String keyName) {
					SC.showConsole();
				}
			});
		}

		HLayout layout = new HLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMargin(10);
		layout.setMembersMargin(10);

		// Create the left-side:
		map = new MapWidget("sampleFeaturesMap");
		final Toolbar toolbar = new Toolbar(map);
		toolbar.setButtonSize(Toolbar.BUTTON_SIZE_BIG);

		map.getMapModel().addMapModelHandler(new MapModelHandler() {

			public void onMapModelChange(MapModelEvent event) {
				toolbar.addToolbarSeparator();
				ScaleSelect scale = new ScaleSelect(map.getMapModel().getMapView(), map.getPixelLength());
				scale.setScales(0.01, 0.001, 0.0001);
				toolbar.addMember(scale);
			}
		});

		VLayout mapLayout = new VLayout();
		mapLayout.setShowEdges(true);
		mapLayout.setShowResizeBar(true);
		mapLayout.setResizeBarTarget("mytabs");
		mapLayout.addMember(toolbar);
		mapLayout.addMember(map);
		mapLayout.setHeight("65%");
		tabSet.setTabBarPosition(Side.TOP);
		tabSet.setWidth100();
		tabSet.setHeight("35%");
		tabSet.setID("mytabs");

		VLayout leftLayout = new VLayout();
		leftLayout.addMember(mapLayout);
		leftLayout.addMember(tabSet);

		layout.addMember(leftLayout);

		// Create the right-side:
		overviewMap = new OverviewMap("sampleOverviewMap", map, true, true);
		LayerTree layerTree = new LayerTree(map);
		layerTree.setHeight("*");
		legend = new Legend(map.getMapModel());

		VLayout rightLayout = new VLayout();
		rightLayout.setSize("250px", "100%");
		rightLayout.setShowEdges(true);

		ActivityMonitor monitor = new ActivityMonitor();

		rightLayout.addMember(monitor);
		rightLayout.addMember(overviewMap);
		rightLayout.addMember(layerTree);
		rightLayout.addMember(legend);

		layout.addMember(rightLayout);

		// Add test-pages here:
		FeatureListGridPage page1 = new FeatureListGridPage(map);
		addTab(new SearchPage(map, tabSet, page1.getTable()));
		addTab(page1);
		addTab(new ButtonPage(map, overviewMap));

		// Finally draw everything:
		layout.draw();
		
		// Install a loading screen:
		LoadingScreen loadScreen = new LoadingScreen(map, "Simple GWT application using Geomajas 1.5.2");
		loadScreen.draw();

		// Then initialize:
		initialize();
	}

	private void addTab(AbstractTestPage tab) {
		tabSet.addTab(tab);
		tabs.add(tab);
	}

	private void initialize() {
		map.initialize();
		legend.setHeight(200);
		overviewMap.setHeight(200);

		for (AbstractTestPage tab : tabs) {
			tab.initialize();
		}
	}
}
