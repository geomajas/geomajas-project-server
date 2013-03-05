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

package org.geomajas.gwt.client.util;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.util.impl.DomImpl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

/**
 * <p>
 * Extension of the GWT DOM class, to support elements and attributes with specific name-spaces. These name-spaces will
 * generally be used for SVG support in geomajas.<br/>
 * When using these functions, note that they behave differently in Internet Explorer! Also, there is an initialization
 * function for VML (used only in IE). Call this function before attempting to use VML on a page.
 * </p>
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * @since 1.1.0
 */
@Api(allMethods = true)
public class Dom extends com.google.gwt.user.client.DOM {
	
	private static final DomImpl IMPL = GWT.create(DomImpl.class);

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
		IMPL.initVMLNamespace();
	}

	/**
	 * Assemble an DOM id.
	 * 
	 * @param id base id
	 * @param suffixes suffixes to add
	 * @return id
	 */
	public static String assembleId(String id, String... suffixes) {
		return IMPL.assembleId(id, suffixes);
	}

	/**
	 * Disassemble an DOM id, removing suffixes.
	 * 
	 * @param id base id
	 * @param suffixes suffixes to remove
	 * @return id
	 */
	public static String disAssembleId(String id, String... suffixes) {
		return IMPL.disAssembleId(id, suffixes);
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
	public static Element createElementNS(String ns, String tag, String id) {
		return IMPL.createElementNS(ns, tag, id);
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
		return IMPL.createElementNS(ns, tag);
	}

	/**
	 * Creates an HTML element with the given id.
	 * 
	 * @param tagName the HTML tag of the element to be created
	 * @return the newly-created element
	 */
	public static Element createElement(String tagName, String id) {
		return IMPL.createElement(tagName, id);
	}

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
		IMPL.setElementAttributeNS(ns, element, attr, value);
	}

	/**
	 * Is the user currently running Internet Explorer?
	 * 
	 * @return true or false - yes or no.
	 */
	public static boolean isIE() {
		return IMPL.isIE();
	}

	/**
	 * Is the browser supporting SVG?
	 * 
	 * @return true or false - yes or no.
	 */
	public static boolean isSvg() {
		return IMPL.isSvg();
	}

	/**
	 * Is the user currently using FireFox?
	 * 
	 * @return true or false - yes or no.
	 */
	public static boolean isFireFox() {
		return IMPL.isFireFox();
	}

	/**
	 * Is the user currently using Chrome?
	 * 
	 * @return true or false - yes or no.
	 */
	public static boolean isChrome() {
		return IMPL.isChrome();
	}

	/**
	 * Is the user currently using Safari?
	 * 
	 * @return true or false - yes or no.
	 */
	public static boolean isSafari() {
		return IMPL.isSafari();
	}

	/**
	 * Is the user currently using a Webkit based browser (such as Chrome or Safari)?
	 * 
	 * @return true or false - yes or no.
	 */
	public static boolean isWebkit() {
		return IMPL.isWebkit();
	}

	/**
	 * Similar method to the "setInnerHTML", but specified for setting SVG. Using the regular setInnerHTML, it is not
	 * possible to set a string of SVG on an object. This method can do that. On the other hand, this method is better
	 * not used for setting normal HTML as an element's innerHTML.
	 * 
	 * @param element The element onto which to set the SVG string.
	 * @param svg The string of SVG to set on the element.
	 */
	public static void setInnerSvg(Element element, String svg) {
		IMPL.setInnerSvg(element, svg);
	}

	/**
	 * Clone a single SVG element.
	 * 
	 * @param source The source SVG element.
	 * @return Returns the clone.
	 */
	public static Element cloneSvgElement(Element source) {
		return IMPL.cloneSvgElement(source);
	}

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
		return IMPL.makeUrlAbsolute(url);
	}
	
	/**
	 * Determine whether one element is equal to, or the child of, another.
	 * 
	 * @param parent the potential parent element
	 * @param child the potential child element
	 * @return <code>true</code> if the relationship holds
	 */
	public static boolean isOrHasChild(Element parent, Element child) {
		return IMPL.isOrHasChild(parent, child);
	}
	
}