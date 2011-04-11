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
package org.geomajas.widget.searchandfilter.client.widget.geometricsearch;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.client.util.CommService;
import org.geomajas.widget.searchandfilter.client.util.DataCallback;
import org.geomajas.widget.searchandfilter.client.widget.multifeaturelistgrid.MultiFeatureListGrid;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

/**
 * Geometric Search Widget. Contains framework + search functionality. But does
 * not create the geometries itself.
 * <p>
 * You add specific searchmethods through addSearchMethod().
 * 
 * @author Kristof Heirwegh
 */
public class GeometricSearchWidget extends Canvas {

	private static final String BTN_SEARCH_IMG = "[ISOMORPHIC]/geomajas/silk/find.png";
	private static final String BTN_RESET_IMG = "[ISOMORPHIC]/geomajas/silk/undo.png";
	private static final String BTN_PROCESSING = "[ISOMORPHIC]/geomajas/ajax-loader.gif";

	private final SearchAndFilterMessages messages = GWT.create(SearchAndFilterMessages.class);
	private final List<GeometricSearchMethod> searchMethods = new ArrayList<GeometricSearchMethod>();
	private TabSet tabs;
	private IButton searchBtn;
	private IButton resetBtn;
	private MultiFeatureListGrid targetGrid;

	/**
	 * @param mapWidget
	 * @param target the listgrid where the result will be shown
	 */
	public GeometricSearchWidget(final MapWidget mapWidget, final MultiFeatureListGrid target) {
		if (mapWidget == null || target == null) {
			throw new IllegalArgumentException("All parameters are required");
		}

		this.setTitle(messages.geometricSearchWidgetTitle());
		this.targetGrid = target;
		VLayout layout = new VLayout(5);
		layout.setWidth(300);
		layout.setHeight(250);
		tabs = new TabSet();
		tabs.setWidth100();
		tabs.setHeight100();
		layout.addMember(tabs);

		HLayout buttonBar = new HLayout(10);
		searchBtn = new IButton(messages.geometricSearchWidgetStartSearch());
		searchBtn.setIcon(BTN_SEARCH_IMG);
		searchBtn.setAutoFit(true);
		searchBtn.setDisabled(true);
		searchBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onSearch();
			}
		});
		resetBtn = new IButton(messages.geometricSearchWidgetReset());
		resetBtn.setIcon(BTN_RESET_IMG);
		resetBtn.setAutoFit(true);
		resetBtn.setDisabled(true);
		resetBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onReset();
			}
		});
		buttonBar.addMember(searchBtn);
		buttonBar.addMember(resetBtn);
		layout.addMember(buttonBar);

		addChild(layout);
		setMargin(5);
	}

	public void addSearchMethod(GeometricSearchMethod searchMethod) {
		addSearchMethod(searchMethod, -1);
	}

	public void addSearchMethod(GeometricSearchMethod searchMethod, int position) {
		if (searchMethod == null) {
			throw new IllegalArgumentException("Please provide a searchMethod.");
		}

		if (searchMethods.contains(searchMethod)) {
			return;
		} else {
			searchMethods.add(searchMethod);
			Tab tab = new Tab(searchMethod.getTitle());
			tab.setPane(searchMethod.getSearchCanvas());
			if (position > -1) {
				tabs.addTab(tab, position);
			} else {
				tabs.addTab(tab);
			}
			if (searchBtn.isDisabled()) {
				searchBtn.setDisabled(false);
				resetBtn.setDisabled(false);
			}
		}
	}

	// ----------------------------------------------------------

	private void onReset() {
		for (GeometricSearchMethod m : searchMethods) {
			m.reset();
		}
	}

	private void onSearch() {
		// -- get geoms
		List<Geometry> geoms = new ArrayList<Geometry>();
		for (GeometricSearchMethod m : searchMethods) {
			if (m.getGeometry() != null) {
				geoms.add(m.getGeometry());
			}
		}

		// -- check & merge to one if needed
		if (geoms.size() == 0) {
			SC.say(messages.geometricSearchWidgetTitle(), messages.geometricSearchWidgetNoGeometry());
		} else if (geoms.size() == 1) {
			handleSearch(geoms.get(0));
		} else {
			CommService.mergeGeometries(geoms, new DataCallback<Geometry>() {
				public void execute(Geometry result) {
					// TODO Auto-generated method stub
					handleSearch(result);
				}
			});
		}
	}

	private void handleSearch(Geometry geom) {
		// TODO
		if (!targetGrid.willShowSingleResult(null)) {
			targetGrid.focus();
			targetGrid.bringToFront();
		}
	}
}
