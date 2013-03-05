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
package org.geomajas.gwt.client.util.impl;

import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.Element;

/**
 * Extends {@link DomImpl} for IE9 browser.
 * 
 * @author Jan De Moerloose
 * 
 */
public class DomImplIE9 extends DomImpl {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isIE() {
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isOrHasChild(Element parent, Element child) {
		return noContainsIsOrHasChild(parent, child);
	}

	/**
	 * Uses compareDocumentPosition to check parenthood as contains() does not exist for SVG in IE9.
	 * @param parent
	 * @param child
	 * @return
	 */
	public native boolean noContainsIsOrHasChild(Node parent, Node child) /*-{
		// For more information about compareDocumentPosition, see:
		// http://www.quirksmode.org/blog/archives/2006/01/contains_for_mo.html
		return (parent === child) || !!(parent.compareDocumentPosition(child) & 16);
	}-*/;	

}
