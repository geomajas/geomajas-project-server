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

package org.geomajas.puregwt.client.resource;

import com.google.gwt.resources.client.CssResource;

/**
 * CSS resource bundle that contains all generic styles used in pure gwt geomajas widgets.
 * 
 * @author Dosi Bingov
 * 
 */
public interface GeomajasWidgetCssResource extends CssResource {

	// ------------------------------------------------------------------------
	// TouchZoomWidget :
	// ------------------------------------------------------------------------

	@ClassName("touchZoomWidget")
	String touchZoomWidget();

	@ClassName("touchZoomOutBut")
	String touchZoomOutBut();

	@ClassName("touchZoomInBut")
	String touchZoomInBut();

	@ClassName("touchZoomButNormal")
	String touchZoomButNormal();

	@ClassName("touchZoomButTouched")
	String touchZoomButTouched();
}