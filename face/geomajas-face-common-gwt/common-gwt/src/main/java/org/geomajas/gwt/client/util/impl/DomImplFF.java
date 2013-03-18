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
 * Extends {@link DomImpl} for Firefox browsers.
 * 
 * @author Jan De Moerloose
 * 
 */
public class DomImplFF extends DomImpl {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInnerSvg(Element element, String svg) {
		svg = "<g xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">" + svg + "</g>";
		setInnerHTML(element, svg);
	}

	private native void setInnerHTML(Element element, String svg)
	/*-{
		 var fragment = new DOMParser().parseFromString(svg, "text/xml");
		 if(fragment) {
			 var children = fragment.childNodes;         
			 for(var i=0; i < children.length; i++) {    
				 element.appendChild (children[i]);
			 }
		 }
	 }-*/;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFireFox() {
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
		DOM.setStyleAttribute(element, "MozTransform", transform);
	}	

	/**
	 * {@inheritDoc}
	 */
	public void setTransformOrigin(Element element, String origin) {
		DOM.setStyleAttribute(element, "MozTransformOrigin", origin);
	}


}
