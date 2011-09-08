package org.vaadin.gwtgraphics.client.impl.util;

import com.google.gwt.dom.client.Element;

/**
 * This class contains helpers used by the SVGImpl class.
 * 
 * @author Henri Kerola / IT Mill Ltd
 * 
 */
public abstract class SVGUtil {

	public static final String SVG_NS = "http://www.w3.org/2000/svg";

	public static final String XLINK_NS = "http://www.w3.org/1999/xlink";

	public static Element createSVGElementNS(String tag) {
		return createElementNS(SVG_NS, tag);
	}

	public static native Element createElementNS(String ns, String tag) /*-{
		return $doc.createElementNS(ns, tag);
	}-*/;

	public static void setAttributeNS(Element elem, String attr, int value) {
		setAttributeNS(null, elem, attr, "" + value);
	}

	public static void setAttributeNS(Element elem, String attr, String value) {
		setAttributeNS(null, elem, attr, value);
	}

	public static native void setAttributeNS(String uri, Element elem,
			String attr, String value) /*-{
		elem.setAttributeNS(uri, attr, value);
	}-*/;

	public static native void setClassName(Element element, String name) /*-{
		// See http://newsgroups.cryer.info/mozilla/dev.tech.svg/200803/080318666.html
		element.className.baseVal = name;
	}-*/;

	public static native SVGBBox getBBBox(Element element, boolean attached)
	/*-{
		var bbox = null;
		if (attached) {
			bbox = element.getBBox();
		} else {
			var ns = @org.vaadin.gwtgraphics.client.impl.util.SVGUtil::SVG_NS;
			var svg = $doc.createElementNS(ns, "svg");
			var parent = element.parentNode;
			svg.appendChild(element);
			$doc.documentElement.appendChild(svg);
			bbox = element.getBBox();
			$doc.documentElement.removeChild(svg);
			if (parent != null) {
				parent.appendChild(element);
			}
		}
		return bbox;
	}-*/;

}
