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

package org.geomajas.gwt.client.gfx;

import org.geomajas.gwt.client.util.Dom;
import org.geomajas.gwt.client.map.render.VectorTilePresenter.TileView;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.VectorObject;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Element;

/**
 * Vector object that represents a tile for a vector layer. As this class is an extension of the {@link VectorObject}
 * super class, it can be add to a VectorObjectContainer for rendering. The <code>setContent</code> method will provide
 * the actual content for this tile in the form of an SVG or VML string.
 * 
 * @author Pieter De Graef
 */
public class VectorTileObject extends VectorObject implements TileView {

	/**
	 * Set the actual tile contents. This string will probably come from the server which has the capability to create
	 * SVG/VML string representing a tile.
	 * 
	 * @param content
	 *            The actual SVG or VML string to be parsed and applied in this tile. It contains the actual rendering.
	 */
	public void setContent(String content) {
		if (Dom.isIE()) {
			throw new IllegalArgumentException("VectorTileGroup - rendering VML tiles: Not implemented.");
		} else {
			setInnerSvg(getElement(), content);
		}
	}

	protected Class<? extends VectorObject> getType() {
		return Group.class;
	}

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
		if (Dom.isFireFox()) {
			setFireFoxInnerHTML(element,
					"<g xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">" + svg
							+ "</g>");
		} else if (Dom.isWebkit()) {
			setWebkitInnerHTML(element,
					"<g xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">" + svg
							+ "</g>");
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
		var node = @org.geomajas.gwt.client.gfx.VectorTileObject::clone(Lcom/google/gwt/user/client/Element;)
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
	private static Element clone(Element source) {
		if (source == null || source.getNodeName() == null) {
			return null;
		}
		if ("#text".equals(source.getNodeName())) {
			return Document.get().createTextNode(source.getNodeValue()).cast();
		}
		Element clone = createElementNS(Dom.NS_SVG, source.getNodeName());
		cloneAttributes(source, clone);
		for (int i = 0; i < source.getChildCount(); i++) {
			Element child = source.getChild(i).cast();
			clone.appendChild(clone(child));
		}

		return clone;
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
	 * @param ns
	 *            The name-space to be used in the element creation.
	 * @param tag
	 *            The tag-name to be used in the element creation.
	 * @return Returns a newly created DOM element in the given name-space.
	 */
	public static Element createElementNS(String ns, String tag) {
		if (ns.equals(Dom.NS_HTML)) {
			return Dom.createElement(tag);
		} else {
			if (Dom.isIE()) {
				return Dom.createElement(ns + ":" + tag);
			} else {
				return createNameSpaceElement(ns, tag);
			}
		}
	}

	private static native Element createNameSpaceElement(String ns, String tag)
	/*-{
	 return $doc.createElementNS(ns, tag);
	}-*/;

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

	@Override
	protected void drawTransformed() {
		if (hasScale() || hasTranslation()) {
			throw new UnsupportedOperationException("can't transform VectorTileObject");
		}
	}
}