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

package org.geomajas.puregwt.example.base.client.resource;

import com.google.gwt.resources.client.CssResource;

/**
 * CSS resource bundle that contains all generic styles used within this web application.
 * 
 * @author Pieter De Graef
 */
public interface ShowcaseCssResource extends CssResource {

	@ClassName("clickable")
	String clickable();

	@ClassName("header")
	String header();

	@ClassName("subHeader")
	String subHeader();

	@ClassName("sampleBlockPanel")
	String sampleBlockPanel();

	@ClassName("sampleBlockTitle")
	String sampleBlockTitle();

	@ClassName("sampleBlockDescription")
	String sampleBlockDescription();

	@ClassName("sampleBlockCategory")
	String sampleBlockCategory();

	// ------------------------------------------------------------------------
	// Often used styles for the samples:
	// ------------------------------------------------------------------------

	@ClassName("sampleLayout")
	String sampleLayout();

	@ClassName("sampleLeftLayout")
	String sampleLeftLayout();

	@ClassName("sampleContentLayout")
	String sampleContentLayout();

	@ClassName("sampleRow")
	String sampleRow();

	@ClassName("sampleHasBorder")
	String sampleHasBorder();

	// ------------------------------------------------------------------------
	// Styles for the transparent panels:
	// ------------------------------------------------------------------------

	@ClassName("panelTopLeft")
	String panelTopLeft();

	@ClassName("panelTopRight")
	String panelTopRight();

	@ClassName("panelBottomLeft")
	String panelBottomLeft();

	@ClassName("panelBottomRight")
	String panelBottomRight();

	@ClassName("panelTop")
	String panelTop();

	@ClassName("panelRight")
	String panelRight();

	@ClassName("panelBottom")
	String panelBottom();

	@ClassName("panelLeft")
	String panelLeft();

	@ClassName("panelMiddle")
	String panelMiddle();
}