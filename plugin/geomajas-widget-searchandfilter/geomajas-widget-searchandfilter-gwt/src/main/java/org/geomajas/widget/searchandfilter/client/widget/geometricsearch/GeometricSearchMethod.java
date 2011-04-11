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

import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.Canvas;

/**
 * This is a facade which contains all components and methods for a geometric
 * search. This allows for generic/new implementations to be added to the
 * GeometricSearch Window.
 * 
 * @author Kristof Heirwegh
 */
public interface GeometricSearchMethod {

	/**
	 * @param map
	 *            The map will be passed to your class at construction time so
	 *            you can interact with the mapWidget.
	 */
	void initialize(MapWidget map);

	/**
	 * The name of your searchwidget. Will be used as tabname.
	 * Don't forget to i18n it.
	 * @return
	 */
	String getTitle();

	/**
	 * @return A canvas with the components (buttons / inputboxes / ... ) that
	 *         you need for your search
	 */
	Canvas getSearchCanvas();

	/**
	 * When the user activates the search, this method will be called to
	 * retrieve the geometry.
	 * <p>
	 * Can (should) be <code>null</code> if the search is not correctly
	 * configured by the user. (eg. the user did not use your search).
	 * <p>
	 * If you have multiple geometries you need to first merge them (You can use
	 * the CommService convenience class for this)
	 *
	 * @return geometry to search with
	 */
	Geometry getGeometry();

	/**
	 * This method will be called when the widget is reset, or the user
	 * activates the reset-button. You are expected to restore all properties to
	 * their default values. If getGeometry is called, null should be returned.
	 */
	void reset();

}