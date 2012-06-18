/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Element;
import org.geomajas.global.GeomajasConstant;

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
public class Dom extends com.google.gwt.user.client.DOM {

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
	 * @param id base id
	 * @param suffixes suffixes to add
	 * @return id
	 */
	public static String assembleId(String id, String... suffixes) {
		StringBuilder sb = new StringBuilder();
		if (null != id) {
			sb.append(id);
		}
		for (String s : suffixes) {
			sb.append(Dom.ID_SEPARATOR);
			sb.append(s);
		}
		return sb.toString();
	}

	/**
	 * Disassemble an DOM id, removing suffixes.
	 * 
	 * @param id base id
	 * @param suffixes suffixes to remove
	 * @return id
	 */
	public static String disAssembleId(String id, String... suffixes) {
		int count = 0;
		for (String s : suffixes) {
			count += s.length() + Dom.ID_SEPARATOR.length();
		}
		return id.substring(0, id.length() - count);
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
	 * @param ns The name-space to be used in the element creation.
	 * @param tag The tag-name to be used in the element creation.
	 * @return Returns a newly created DOM element in the given name-space.
	 */
	public static Element createElementNS(String ns, String tag, String id) {
		Element element;
		if (NS_HTML.equals(ns)) {
			element = createElement(tag);
		} else {
			if (isIE()) {
				element = createElement(ns + ":" + tag);
			} else {
				element = createNameSpaceElement(ns, tag);
			}
		}
		if (id != null) {
			setElementAttribute(element, "id", id);
		}
		Log.logDebug("Creating element " + id);
		return element;
	}

	/**
	 * <p>
	 * Creates a new DOM element in the given name-space. If the name-space is HTML, a normal element will be created.
	 * </p>
	 * <p>
	 * There is an exception when using Internet Explorer! For Internet Explorer a new element will be created of type
	 * "namespace:tag".
	 * </p>
	 * 
	 * @param ns The name-space to be used in the element creation.
	 * @param tag The tag-name to be used in the element creation.
	 * @return Returns a newly created DOM element in the given name-space.
	 */
	public static Element createElementNS(String ns, String tag) {
		return createElementNS(ns, tag, null);
	}

	/**
	 * Creates an HTML element with the given id.
	 * 
	 * @param tagName the HTML tag of the element to be created
	 * @return the newly-created element
	 */
	public static Element createElement(String tagName, String id) {
		Log.logDebug("Creating element " + id);
		Element element = createElement(tagName).cast();
		setElementAttribute(element, "id", id);
		return element;
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
	 * @param ns The name-space to be used in the element creation.
	 * @param element The element to which the attribute is to be set.
	 * @param attr The name of the attribute.
	 * @param value The new value for the attribute.
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
	 * @return true or false - yes or no.
	 */
	public static boolean isIE() {
		return getUserAgent().contains("msie");
	}

	/**
	 * Is the browser supporting SVG?
	 * 
	 * @return true or false - yes or no.
	 */
	public static boolean isSvg() {
		return !getUserAgent().contains("msie") || getUserAgent().contains("msie 9.0");
	}

	/**
	 * Is the user currently using FireFox?
	 * 
	 * @return true or false - yes or no.
	 */
	public static boolean isFireFox() {
		return getUserAgent().contains("firefox");
	}

	/**
	 * Is the user currently using Chrome?
	 * 
	 * @return true or false - yes or no.
	 */
	public static boolean isChrome() {
		return getUserAgent().contains("chrome");
	}

	/**
	 * Is the user currently using Safari?
	 * 
	 * @return true or false - yes or no.
	 */
	public static boolean isSafari() {
		return getUserAgent().contains("safari");
	}

	/**
	 * Is the user currently using a Webkit based browser (such as Chrome or Safari)?
	 * 
	 * @return true or false - yes or no.
	 */
	public static boolean isWebkit() {
		return getUserAgent().contains("webkit");
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
	 * @param element The element onto which to set the SVG string.
	 * @param svg The string of SVG to set on the element.
	 */
	public static void setInnerSvg(Element element, String svg) {
		svg = "<g xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">" + svg + "</g>";
		if (isFireFox()) {
			setFireFoxInnerHTML(element, svg);
		} else if (isWebkit()) {
			setWebkitInnerHTML(element, svg);
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

	private static native void setWebkitInnerHTML(Element element, String svg)
	/*-{
		 var fragment = new DOMParser().parseFromString(svg,"text/xml");	
		 if(fragment) {
			 var children = fragment.childNodes;         
			 for(var i=0; i < children.length; i++) {
				 var node = @org.geomajas.gwt.client.util.Dom::cloneSvgElement(Lcom/google/gwt/user/client/Element;)
						 (children[i]);
				 element.appendChild (node);
			 }
		 }
	 }-*/;

	/**
	 * Clone a single SVG element.
	 * 
	 * @param source The source SVG element.
	 * @return Returns the clone.
	 */
	public static Element cloneSvgElement(Element source) {
		if (source == null || source.getNodeName() == null) {
			return null;
		}
		if ("#text".equals(source.getNodeName())) {
			return Document.get().createTextNode(source.getNodeValue()).cast();
		}
		Element clone = createElementNS(Dom.NS_SVG, source.getNodeName(), Dom.createUniqueId());
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

	/**
	 * Convert a URL to an absolute URL. This assumes the page is at the application root.
	 * <p/>
	 * It converts relative URLs by prepending the HostPageBaseURL and converts classpath resources to use the
	 * resource dispatcher.
	 *
	 * @param url URL to convert
	 * @return converted URL
	 */
	public static String makeUrlAbsolute(String url) {
		String href = url;
		if (href.indexOf(':') <= 0) {
			// SVG in Chrome can't handle relative paths (the xml:base attribute has not yet been tested):
			href = GWT.getHostPageBaseURL() + href;
		} else if (href.startsWith(GeomajasConstant.CLASSPATH_URL_PREFIX)) {
			href = GWT.getHostPageBaseURL() + "d/resource/" +
					href.substring(GeomajasConstant.CLASSPATH_URL_PREFIX.length());
		}
		return href;
	}
}