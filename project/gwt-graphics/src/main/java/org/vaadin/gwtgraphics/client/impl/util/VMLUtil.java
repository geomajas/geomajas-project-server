package org.vaadin.gwtgraphics.client.impl.util;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;

/**
 * This class contains helpers used by the VMLImpl class.
 * 
 * @author Henri Kerola / IT Mill Ltd
 * 
 */
public abstract class VMLUtil {

	public static final String VML_NS_PREFIX = "vml";

	public static final String VML_ELEMENT_CLASSNAME = "vml-element";

	public static Element createVMLElement(String name) {
		Element element = Document.get().createElement(
				VML_NS_PREFIX + ":" + name);
		element.setClassName(VML_ELEMENT_CLASSNAME);
		return element;
	}

	public static Element getOrCreateChildElementWithTagName(Element element,
			String name) {
		Element e = getChildElementWithTagName(element, name);
		if (e != null) {
			return e;
		}
		return element.appendChild(createVMLElement(name));
	}

	public static String getPropertyOfFirstChildElementWithTagName(
			Element element, String name, String attr) {
		Element e = getChildElementWithTagName(element, name);
		if (e != null) {
			return e.getPropertyString(attr);
		}
		return "";
	}

	public static boolean hasChildElementWithTagName(Element element,
			String name) {
		Element e = getChildElementWithTagName(element, name);
		return e != null;
	}

	private static Element getChildElementWithTagName(Element element,
			String name) {
		NodeList<Node> nodes = element.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.getItem(i);
			if (node.getNodeName().equals(name)) {
				return Element.as(node);
			}
		}
		return null;
	}

	public native static String getTagName(Element element) /*-{
		return element.tagName;
	}-*/;
}
