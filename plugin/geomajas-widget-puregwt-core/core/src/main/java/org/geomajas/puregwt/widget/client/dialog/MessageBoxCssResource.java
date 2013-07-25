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

package org.geomajas.puregwt.widget.client.dialog;

import org.geomajas.annotation.Api;

import com.google.gwt.resources.client.CssResource;

/**
 * CSS resource bundle that contains styles used in pure Gwt Messagebox widget.
 * 
 * @author Kristof Heirwegh
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface MessageBoxCssResource extends CssResource {

	/**
	 * Get a CSS style class.
	 * @return
	 */
	String messageBoxContainer();
	/**
	 * Get a CSS style class.
	 * @return
	 */
	String messageBoxButton();
	/**
	 * Get a CSS style class.
	 * @return
	 */
	String messageBoxIcon();
	/**
	 * Get a CSS style class.
	 * @return
	 */
	String messageBoxContent();
	
	/**
	 * Get a CSS style class.
	 * @return
	 */
	String messageBoxIconHelp();
	/**
	 * Get a CSS style class.
	 * @return
	 */
	String messageBoxIconWarn();
	/**
	 * Get a CSS style class.
	 * @return
	 */
	String messageBoxIconError();
	/**
	 * Get a CSS style class.
	 * @return
	 */
	String messageBoxIconInfo();
	
}