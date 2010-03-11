/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.util;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Element;
import com.smartgwt.client.util.SC;

/**
 * <p>
 * Extension of the GWT DOM class, to support elements and attributes with specific name-spaces. These name-spaces will
 * generally be used for SVG support in geomajas.<br/>
 * When using these functions, note that they behave differently in Internet Explorer! Also, there is an initialization
 * function for VML (used only in IE). Call this function before attempting to use VML on a page.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class DOM extends com.google.gwt.user.client.DOM {

	/**
	 * XLINK name-space. Used in attributes, referencing other elements.
	 */
	public static final String NS_XLINK = "http://www.w3.org/1999/xlink";

	/**
	 * General SVG name-space.
	 */
	public static final String NS_SVG = "http://www.w3.org/2000/svg";

	/**
	 * General VML name-space.
	 */
	public static final String NS_VML = "vml";

	/**
	 * General HTML name-space.
	 */
	public static final String NS_HTML = "html";

	/**
	 * Separator character for DOM element id's.
	 */
	public static final String ID_SEPARATOR = "-";

	/**
	 * Initialization of the VML name-spaces. In order to be able to use VML in a page, this function has to be called
	 * first!
	 */
	public static void initVMLNamespace() {
		if (isIE()) {
			initVMLNamespaceForIE();
		}
	}
	
	/**
	 * Assemble an DOM id.
	 * 
	 * @param id
	 * @param suffixes
	 * @return id
	 */
	public static String assembleId(String id, String... suffixes) {
		for (String s : suffixes) {
			id += DOM.ID_SEPARATOR + s;
		}
		return id;
	}


	private static native void initVMLNamespaceForIE()
	/*-{
	 if (!$doc.namespaces['vml']) {
		 $doc.namespaces.add('vml', 'urn:schemas-microsoft-com:vml');
		 var styleSheet = $doc.createStyleSheet();
		 styleSheet.addRule("vml\\:*", "behavior: url(#default#VML);");
	 }
	}-*/;

	/**
	 * <p>
	 * Creates a new DOM element in the given name-space. If the name-space is HTML, a normal element will be created.
	 * </p>
	 * <p>
	 * There is an exception when using Internet Explorer! For Internet Explorer a new element will be created of type
	 * "namespace:tag".
	 * </p>
	 * 
	 * @param ns
	 *            The name-space to be used in the element creation.
	 * @param tag
	 *            The tag-name to be used in the element creation.
	 * @return Returns a newly created DOM element in the given name-space.
	 */
	public static Element createElementNS(String ns, String tag) {
		if (ns == NS_HTML) {
			return createElement(tag);
		} else {
			if (isIE()) {
				return createElement(ns + ":" + tag);
			} else {
				return createNameSpaceElement(ns, tag);
			}
		}
	}

	private static native Element createNameSpaceElement(String ns, String tag)
	/*-{
	 return $doc.createElementNS(ns, tag);
	}-*/;

	/**
	 * <p>
	 * Adds a new attribute in the given name-space to an element.
	 * </p>
	 * <p>
	 * There is an exception when using Internet Explorer! For Internet Explorer the attribute of type "namespace:attr"
	 * will be set.
	 * </p>
	 * 
	 * @param ns
	 *            The name-space to be used in the element creation.
	 * @param element
	 *            The element to which the attribute is to be set.
	 * @param attr
	 *            The name of the attribute.
	 * @param value
	 *            The new value for the attribute.
	 */
	public static void setElementAttributeNS(String ns, Element element, String attr, String value) {
		if (isIE()) {
			element.setAttribute(ns + ":" + attr, value);
		} else {
			setNameSpaceAttribute(ns, element, attr, value);
		}
	}

	private static native void setNameSpaceAttribute(String ns, Element element, String attr, String value)
	/*-{
	 element.setAttributeNS(ns, attr, value);
	}-*/;

	/**
	 * Is the user currently running Internet Explorer?
	 * 
	 * @return
	 */
	public static boolean isIE() {
		return SC.isIE();
	}

	/**
	 * Is the user currently using FireFox?
	 * 
	 * @return
	 */
	public static boolean isFireFox() {
		return getUserAgent().contains("firefox");
	}

	/**
	 * Is the user currently using Chrome?
	 * 
	 * @return
	 */
	public static boolean isChrome() {
		return getUserAgent().contains("chrome");
	}

	private static native String getUserAgent()
	/*-{
	 return navigator.userAgent.toLowerCase();
	}-*/;

	/**
	 * Similar method to the "setInnerHTML", but specified for setting SVG. Using the regular setInnerHTML, it is not
	 * possible to set a string of SVG on an object. This method can do that. On the other hand, this method is better
	 * not used for setting normal HTML as an element's innerHTML.
	 * 
	 * @param element
	 *            The element onto which to set the SVG string.
	 * @param svg
	 *            The string of SVG to set on the element.
	 */
	public static void setInnerSvg(Element element, String svg) {
		svg = "<g xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">"
				+ svg + "</g>";
		if (isFireFox()) {
			setFireFoxInnerHTML(element, svg);
		} else if (isChrome()) {
			setChromeInnerHTML(element, svg);
		}
	}

	private static native void setFireFoxInnerHTML(Element element, String svg)
	/*-{
		 var fragment = new DOMParser().parseFromString(svg, "text/xml");
		 if(fragment) {
			 var children = fragment.childNodes;         
			 for(var i=0; i < children.length; i++) {    
				 element.appendChild (children[i]);
			 }
		 }
	 }-*/;

	private static native void setChromeInnerHTML(Element element, String svg)
	/*-{
		 var fragment = new DOMParser().parseFromString(svg,"text/xml");	
		 if(fragment) {
			 var children = fragment.childNodes;         
			 for(var i=0; i < children.length; i++) {
				 var node = @org.geomajas.gwt.client.util.DOM::cloneSvgElement(Lcom/google/gwt/user/client/Element;)
						 (children[i]);
				 element.appendChild (node);
			 }
		 }
	 }-*/;

	/**
	 * Clone a single SVG element.
	 * 
	 * @param source
	 *            The source SVG element.
	 * @return Returns the clone.
	 */
	public static Element cloneSvgElement(Element source) {
		if (source == null || source.getNodeName() == null) {
			return null;
		}
		if ("#text".equals(source.getNodeName())) {
			return Document.get().createTextNode(source.getNodeValue()).cast();
		}
		Element clone = DOM.createElementNS(DOM.NS_SVG, source.getNodeName());
		cloneAttributes(source, clone);
		for (int i = 0; i < source.getChildCount(); i++) {
			Element child = source.getChild(i).cast();
			clone.appendChild(cloneSvgElement(child));
		}

		return clone;
	}

	private static native void cloneAttributes(Element source, Element target)
	/*-{
		  if (source != null && target != null) {
			  for (var i=0; i<source.attributes.length; i++) {
				  var attribute = source.attributes.item(i);
				  if (attribute.value != null && attribute.value.length > 0) {
					  var atClone = null;
					  try {
						  atClone = $doc.createAttributeNS(attribute.namespaceURI, attribute.name);
					  } catch (e) {
						  atClone = $doc.createAttribute(attribute.name);
					  }
					 atClone.value = attribute.value;
					 target.setAttributeNode(atClone);
				  }
			  }
		  }
	 }-*/;
}