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

package org.geomajas.plugin.wmsclient.client.capabilities.v1_1_1;

import org.geomajas.plugin.wmsclient.client.capabilities.AbstractXmlNodeWrapper;
import org.geomajas.plugin.wmsclient.client.capabilities.WmsLayerLegendUrlInfo;
import org.geomajas.plugin.wmsclient.client.capabilities.WmsOnlineResourceInfo;

import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

/**
 * Implementation of the {@link WmsLayerLegendUrlInfo} for WMS version 1.1.1.
 * 
 * @author Pieter De Graef
 */
public class WmsLayerLegendUrlInfo111 extends AbstractXmlNodeWrapper implements WmsLayerLegendUrlInfo {

	private static final long serialVersionUID = 100L;

	private int width;

	private int height;

	private String format;

	private WmsOnlineResourceInfo onlineResource;

	public WmsLayerLegendUrlInfo111(Node node) {
		super(node);
	}

	public int getWidth() {
		if (width == 0) {
			parse(getNode());
		}
		return width;
	}

	public int getHeight() {
		if (height == 0) {
			parse(getNode());
		}
		return height;
	}

	public String getFormat() {
		if (format == null) {
			parse(getNode());
		}
		return format;
	}

	public WmsOnlineResourceInfo getOnlineResource() {
		if (onlineResource == null) {
			parse(getNode());
		}
		return onlineResource;
	}

	// ------------------------------------------------------------------------
	// AbstractNodeInfo implementation:
	// ------------------------------------------------------------------------

	protected void parse(Node node) {
		NamedNodeMap attributes = node.getAttributes();
		width = getValueRecursiveAsInteger(attributes.getNamedItem("width"));
		height = getValueRecursiveAsInteger(attributes.getNamedItem("height"));

		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			String nodeName = child.getNodeName();
			if ("Format".equalsIgnoreCase(nodeName)) {
				format = getValueRecursive(child);
			} else if ("OnlineResource".equalsIgnoreCase(nodeName)) {
				onlineResource = createOnlineResource(child);
			}
		}
	}

	protected WmsOnlineResourceInfo createOnlineResource(Node node) {
		return new WmsOnlineResourceInfo111(node);
	}
}