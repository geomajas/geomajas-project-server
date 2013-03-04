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

package org.geomajas.plugin.wmsclient.client.capabilities;

import com.google.gwt.xml.client.Node;

/**
 * Abstract definition for configuration objects that are based around an XML node.
 * 
 * @author Pieter De Graef
 */
public abstract class AbstractXmlNodeWrapper {

	private final Node node;

	public AbstractXmlNodeWrapper(Node node) {
		this.node = node;
	}

	protected abstract void parse(Node node);

	/**
	 * Get the XML node.
	 * 
	 * @return The XML node.
	 */
	public Node getNode() {
		return node;
	}

	protected String getValueRecursive(Node node) {
		if (node != null) {
			if (node.getNodeValue() != null) {
				return node.getNodeValue();
			}
			if (node.getFirstChild() != null) {
				return getValueRecursive(node.getFirstChild());
			}
		}
		return null;
	}

	protected double getValueRecursiveAsDouble(Node node) {
		String value = getValueRecursive(node);
		if (value != null) {
			try {
				return Double.parseDouble(value);
			} catch (Exception e) {
			}
		}
		return 0;
	}

	protected int getValueRecursiveAsInteger(Node node) {
		String value = getValueRecursive(node);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (Exception e) {
			}
		}
		return 0;
	}
}