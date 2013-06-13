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

import org.geomajas.annotation.Api;

import com.google.gwt.resources.client.CssResource;

/**
 * CSS resource bundle that contains all generic styles used in pure gwt geomajas widgets.
 * 
 * @author Dosi Bingov
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface GeomajasWidgetCssResource extends CssResource {

	// ------------------------------------------------------------------------
	// TouchZoomWidget :
	// ------------------------------------------------------------------------

	/**
	 * Get a CSS style class.
	 * @return
	 */
	String touchZoomWidget();

	/**
	 * Get a CSS style class.
	 * @return
	 */
	String touchZoomOutBut();

	/**
	 * Get a CSS style class.
	 * @return
	 */
	String touchZoomInBut();

	/**
	 * Get a CSS style class.
	 * @return
	 */
	String touchZoomButNormal();

	/**
	 * Get a CSS style class.
	 * @return
	 */
	String touchZoomButTouched();
	
	// ------------------------------------------------------------------------
	// ExceptionDialog :
	// ------------------------------------------------------------------------

	/**
	 * Get a CSS style class.
	 * @return
	 */
	String exceptionDialogButtonPanel();
	
	/**
	 * Get a CSS style class.
	 * @return
	 */
	String exceptionDialogStackTracePanel();
	
	/**
	 * Get a CSS style class.
	 * @return
	 */
	String exceptionDialogMessageLabel();
	
	/**
	 * Get a CSS style class.
	 * @return
	 */
	String exceptionDialogCloseIcon();
	
	/**
	 * Get a CSS style class.
	 * @return
	 */
	String exceptionDialogTitle();
	
}