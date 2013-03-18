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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Extends {@link DomImpl} for Webkit browsers (Safari, Chrome).
 * 
 * @author Jan De Moerloose
 * 
 */
public class DomImplWebKit extends DomImpl {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isChrome() {
		return getUserAgent().contains("chrome");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSafari() {
		return getUserAgent().contains("safari");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWebkit() {
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isTransformationSupported() {
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setTransform(Element element, String transform) {
		DOM.setStyleAttribute(element, "WebkitTransform", transform);
	}	

	/**
	 * {@inheritDoc}
	 */
	public void setTransformOrigin(Element element, String origin) {
		DOM.setStyleAttribute(element, "WebkitTransformOrigin", origin);
	}

}
