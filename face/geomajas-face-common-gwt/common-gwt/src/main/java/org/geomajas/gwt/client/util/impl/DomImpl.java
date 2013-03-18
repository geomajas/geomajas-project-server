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

import org.geomajas.global.GeomajasConstant;
import org.geomajas.gwt.client.util.Dom;
import org.geomajas.gwt.client.util.Log;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * <p>
 * Default implementation of {@link Dom} delegate.
 * </p>
 * 
 * @author Jan De Moerloose
 */
public class DomImpl {

	/**
	 * Initialization of the VML name-spaces. In order to be able to use VML in a page, this function has to be called
	 * first!
	 */
	public void initVMLNamespace() {
		throw new RuntimeException("VML unsupported");
	}

	/**
	 * Assemble an DOM id.
	 * 
	 * @param id base id
	 * @param suffixes suffixes to add
	 * @return id
	 */
	public String assembleId(String id, String... suffixes) {
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
	public String disAssembleId(String id, String... suffixes) {
		int count = 0;
		for (String s : suffixes) {
			count += s.length() + Dom.ID_SEPARATOR.length();
		}
		return id.substring(0, id.length() - count);
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
	public Element createElementNS(String ns, String tag, String id) {
		Element element;
		if (Dom.NS_HTML.equals(ns)) {
			element = DOM.createElement(tag);
		} else {
			element = createNameSpaceElement(ns, tag);
		}
		if (id != null) {
			DOM.setElementAttribute(element, "id", id);
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
	public Element createElementNS(String ns, String tag) {
		return createElementNS(ns, tag, null);
	}

	/**
	 * Creates an HTML element with the given id.
	 * 
	 * @param tagName the HTML tag of the element to be created
	 * @return the newly-created element
	 */
	public Element createElement(String tagName, String id) {
		Log.logDebug("Creating element " + id);
		Element element = DOM.createElement(tagName).cast();
		DOM.setElementAttribute(element, "id", id);
		return element;
	}

	private native Element createNameSpaceElement(String ns, String tag)
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
	public void setElementAttributeNS(String ns, Element element, String attr, String value) {
		setNameSpaceAttribute(ns, element, attr, value);
	}

	private native void setNameSpaceAttribute(String ns, Element element, String attr, String value)
	/*-{
	 element.setAttributeNS(ns, attr, value);
	}-*/;

	/**
	 * Is currently using Internet Explorer ? Consider using deferred binding instead of this flag to minimize your
	 * download and support future browser versions.
	 * 
	 * @return true or false - yes or no.
	 */
	public boolean isIE() {
		return false;
	}

	/**
	 * Is the browser supporting SVG? Consider using deferred binding instead of this flag to minimize your download and
	 * support future browser versions.
	 * 
	 * @return true or false - yes or no.
	 */
	public boolean isSvg() {
		return true;
	}

	/**
	 * Is the user currently using Firefox ? Consider using deferred binding instead of this flag to minimize your
	 * download and support future browser versions.
	 * 
	 * @return true or false - yes or no.
	 */
	public boolean isFireFox() {
		return false;
	}

	/**
	 * Is the user currently using Chrome? Consider using deferred binding instead of this flag to minimize your
	 * download and support future browser versions.
	 * 
	 * @return true or false - yes or no.
	 */
	public boolean isChrome() {
		return false;
	}

	/**
	 * Is the user currently using Safari? Consider using deferred binding instead of this flag to minimize your
	 * download and support future browser versions.
	 * 
	 * @return true or false - yes or no.
	 */
	public boolean isSafari() {
		return false;
	}

	/**
	 * Is the user currently using a Webkit based browser (such as Chrome or Safari)? Consider using deferred binding
	 * instead of this flag to minimize your download and support future browser versions.
	 * 
	 * @return true or false - yes or no.
	 */
	public boolean isWebkit() {
		return false;
	}
	
	protected native String getUserAgent()
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
	public void setInnerSvg(Element element, String svg) {
		svg = "<g xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">" + svg + "</g>";
		setInnerHTML(element, svg);
	}

	private native void setInnerHTML(Element element, String svg)
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
	public Element cloneSvgElement(Element source) {
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

	private native void cloneAttributes(Element source, Element target)
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
	 * It converts relative URLs by prepending the HostPageBaseURL and converts classpath resources to use the resource
	 * dispatcher.
	 * 
	 * @param url URL to convert
	 * @return converted URL
	 */
	public String makeUrlAbsolute(String url) {
		String href = url;
		if (href.indexOf(':') <= 0) {
			// SVG in Chrome can't handle relative paths (the xml:base attribute has not yet been tested):
			href = GWT.getHostPageBaseURL() + href;
		} else if (href.startsWith(GeomajasConstant.CLASSPATH_URL_PREFIX)) {
			href = GWT.getHostPageBaseURL() + "d/resource/"
					+ href.substring(GeomajasConstant.CLASSPATH_URL_PREFIX.length());
		}
		return href;
	}

	/**
	 * Determine whether one element is equal to, or the child of, another.
	 * 
	 * @param parent the potential parent element
	 * @param child the potential child element
	 * @return <code>true</code> if the relationship holds
	 */
	public boolean isOrHasChild(Element parent, Element child) {
		return DOM.isOrHasChild(parent, child);
	}
	
	/**
	 * Is the browser supporting transformations? Not all browsers support alternatives, so this check can be necessary.
	 * 
	 * @return true or false - yes or no.
	 * @since 1.3.0
	 */
	public boolean isTransformationSupported() {
		return false;
	}
	
	/**
	 * Set the CSS3 transform property (e.g. 'scale(2)').
	 * 
	 * @param element
	 * @param transform
	 * @since 1.3.0
	 */
	public void setTransform(Element element, String transform) {
		throw new RuntimeException("Transformation unsupported");
	}

	/**
	 * Set the CSS3 transform-origin property (defaults to center, e.g. '10 px 20 px').
	 * 
	 * @param element
	 * @param origin
	 * @since 1.3.0
	 */
	public void setTransformOrigin(Element element, String origin) {
		throw new RuntimeException("Transformation origin unsupported");
	}

}