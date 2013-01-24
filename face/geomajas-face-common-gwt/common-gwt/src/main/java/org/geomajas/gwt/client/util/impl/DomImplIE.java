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

import org.geomajas.gwt.client.util.Dom;
import org.geomajas.gwt.client.util.Log;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Extends {@link DomImpl} for IE 6->8 browsers.
 * 
 * @author Jan De Moerloose
 * 
 */
public class DomImplIE extends DomImpl {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initVMLNamespace() {
		initVMLNamespaceForIE();
	}

	private native void initVMLNamespaceForIE()
	/*-{
	 if (!$doc.namespaces['vml']) {
		 $doc.namespaces.add('vml', 'urn:schemas-microsoft-com:vml');
		 var styleSheet = $doc.createStyleSheet();
		 styleSheet.addRule("vml\\:*", "behavior: url(#default#VML);");
	 }
	}-*/;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Element createElementNS(String ns, String tag, String id) {
		Element element;
		if (Dom.NS_HTML.equals(ns)) {
			element = DOM.createElement(tag);
		} else {
			element = DOM.createElement(ns + ":" + tag);
		}
		if (id != null) {
			DOM.setElementAttribute(element, "id", id);
		}
		Log.logDebug("Creating element " + id);
		return element;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setElementAttributeNS(String ns, Element element, String attr, String value) {
		element.setAttribute(ns + ":" + attr, value);
	}

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
	@Override
	public boolean isSvg() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInnerSvg(Element element, String svg) {
		throw new RuntimeException("SVG unsupported");
	}



}
