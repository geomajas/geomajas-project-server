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
import org.geomajas.plugin.wmsclient.client.capabilities.WmsLayerMetadataUrlInfo;
import org.geomajas.plugin.wmsclient.client.capabilities.WmsOnlineResourceInfo;

import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

/**
 * Implementation of the {@link WmsLayerMetadataUrlInfo} for WMS version 1.1.1.
 * 
 * @author Pieter De Graef
 */
public class WmsLayerMetadataUrlInfo111 extends AbstractXmlNodeWrapper implements WmsLayerMetadataUrlInfo {

	private static final long serialVersionUID = 100L;

	private String type;

	private String format;

	private WmsOnlineResourceInfo onlineResource;

	public WmsLayerMetadataUrlInfo111(Node node) {
		super(node);
	}

	public String getFormat() {
		if (format == null) {
			parse(getNode());
		}
		return format;
	}

	public String getType() {
		if (type == null) {
			parse(getNode());
		}
		return type;
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
		type = getValueRecursive(attributes.getNamedItem("type"));

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