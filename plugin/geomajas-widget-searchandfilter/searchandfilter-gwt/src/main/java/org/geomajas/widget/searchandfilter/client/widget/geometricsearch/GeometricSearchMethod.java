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
package org.geomajas.widget.searchandfilter.client.widget.geometricsearch;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.ExpectAlternatives;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.Canvas;

/**
 * This is a facade which contains all components and methods for a geometric
 * search. This allows for generic/new implementations to be added to the
 * GeometricSearch Window.
 * <p/>
 * Do not implement, extend {@link AbstractGeometricSearchMethod} instead.
 * 
 * @author Kristof Heirwegh
 * @since 1.0.0
 */
@Api(allMethods = true)
@ExpectAlternatives
public interface GeometricSearchMethod {

	/**
	 * Initialize the search method.
	 *
	 * @param map
	 *            The map will be passed to your class at construction time so
	 *            you can interact with the mapWidget.
	 * @param handler handler to update the geometry
	 */
	void initialize(MapWidget map, GeometryUpdateHandler handler);

	/**
	 * The name of your search widget. Will be used as tab name.
	 * Don't forget to i18n it.
	 *
	 * @return title
	 */
	String getTitle();

	/**
	 * @return A canvas with the components (buttons / input boxes / ... ) that
	 *         you need for your search
	 */
	Canvas getSearchCanvas();

	/**
	 * This method will be called when the widget is reset, or the user
	 * activates the reset-button. You are expected to restore all properties to
	 * their default values.
	 */
	void reset();
}