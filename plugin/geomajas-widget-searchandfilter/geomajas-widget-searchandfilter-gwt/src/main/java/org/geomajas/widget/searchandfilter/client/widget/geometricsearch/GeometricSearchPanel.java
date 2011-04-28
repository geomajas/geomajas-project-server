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

import org.geomajas.gwt.client.gfx.paintable.GfxGeometry;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.client.util.SearchCommService;
import org.geomajas.widget.searchandfilter.client.util.DataCallback;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchPanel;
import org.geomajas.widget.searchandfilter.search.dto.Criterion;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.util.SC;
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
public class GeometricSearchPanel extends SearchPanel implements GeometryUpdateHandler {

	private final ShapeStyle selectionStyle;
	private final SearchAndFilterMessages messages = GWT.create(SearchAndFilterMessages.class);
	private final List<GeometricSearchMethod> searchMethods = new ArrayList<GeometricSearchMethod>();
	private final List<Geometry> geometries = new ArrayList<Geometry>();

	private TabSet tabs;
	private Geometry searchGeom;
	private GfxGeometry worldpaintable;

	/**
	 * @param mapWidget
	 */
	public GeometricSearchPanel(final MapWidget mapWidget) {
		super(mapWidget);
		selectionStyle = new ShapeStyle();
		selectionStyle.setFillColor("#FFFF00");
		selectionStyle.setFillOpacity(0.3f);
		selectionStyle.setStrokeColor("#B45F04");
		selectionStyle.setStrokeOpacity(0.9f);
		selectionStyle.setStrokeWidth(2f);

//		this.shapeStyleBuffer.setDashArray("2,1"); // not supported

		this.mapWidget = mapWidget;
		this.setTitle(messages.geometricSearchWidgetTitle());
		VLayout layout = new VLayout(0);
		layout.setWidth100();
		layout.setHeight100();
		tabs = new TabSet();
		tabs.setWidth100();
		tabs.setHeight100();
		layout.addMember(tabs);

		setWidth(400);
		setHeight(250);
		addChild(layout);
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
			searchMethod.initialize(mapWidget, this);
		}
	}

	// ----------------------------------------------------------

	@Override
	public boolean validate() {
		if (searchGeom == null) {
			SC.say(messages.geometricSearchWidgetTitle(), messages.geometricSearchWidgetNoGeometry());
			return false;
		} else if (SearchCommService.getVisibleServerLayerIds(mapWidget.getMapModel()).size() < 1) {
			SC.say(messages.geometricSearchWidgetTitle(), messages.geometricSearchWidgetNoLayers());
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Criterion getFeatureSearchCriterion() {
		return SearchCommService.buildGeometryCriterion(searchGeom, mapWidget);
	}

	@Override
	public void reset() {
		geometries.clear();
		searchGeom = null;
		updateGeomOnMap();
		for (GeometricSearchMethod m : searchMethods) {
			m.reset();
		}
	}

	@Override
	public void hide() {
		super.hide();
		// make sure everything is cleaned up, and controllers removed
		reset();
	}

	@Override
	public void initialize(Criterion featureSearch) {
		// can't do that because we don't know which method created the
		// geometry. Even more, it might be a merged geometry from several
		// methods.
		GWT.log("You cannot reinitialize the Geometric searchpanel!");
	}

	// ----------------------------------------------------------

	public void geometryUpdate(Geometry oldGeometry, Geometry newGeometry) {
		if (oldGeometry != null) {
			geometries.remove(oldGeometry);
		}
		if (newGeometry != null) {
			geometries.add(newGeometry);
		}

		if (geometries.size() == 0) {
			searchGeom = null;
			updateGeomOnMap();

		} else if (geometries.size() == 1) {
			searchGeom = geometries.get(0);
			updateGeomOnMap();

		} else {
			 SearchCommService.mergeGeometries(geometries, new DataCallback<Geometry>() {
				public void execute(Geometry result) {
					searchGeom = result;
					updateGeomOnMap();
				}
			});
		}
	}

	private void updateGeomOnMap() {
		if (worldpaintable == null) {
			worldpaintable = new GfxGeometry(GeometricSearchCreator.IDENTIFIER + "_SELECTION_GEOMETRY");
			worldpaintable.setStyle(selectionStyle);
		} else {
			mapWidget.unregisterWorldPaintable(worldpaintable);
		}

		if (searchGeom != null) {
			worldpaintable.setGeometry(searchGeom);
			mapWidget.registerWorldPaintable(worldpaintable);
		}
	}
}
