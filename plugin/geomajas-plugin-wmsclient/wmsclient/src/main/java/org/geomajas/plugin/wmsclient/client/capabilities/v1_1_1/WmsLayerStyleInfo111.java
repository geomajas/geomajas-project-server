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
import org.geomajas.plugin.wmsclient.client.capabilities.WmsLayerStyleInfo;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

/**
 * Implementation of the {@link WmsLayerStyleInfo} for WMS version 1.1.1.
 * 
 * @author Pieter De Graef
 */
public class WmsLayerStyleInfo111 extends AbstractXmlNodeWrapper implements WmsLayerStyleInfo {

	private static final long serialVersionUID = 100L;

	private String name;

	private String title;

	private String abstractt;

	private WmsLayerLegendUrlInfo legendUrl;

	public WmsLayerStyleInfo111(Node node) {
		super(node);
	}

	public String getName() {
		if (name == null) {
			parse(getNode());
		}
		return name;
	}

	public String getTitle() {
		if (title == null) {
			parse(getNode());
		}
		return title;
	}

	public String getAbstract() {
		if (abstractt == null) {
			parse(getNode());
		}
		return abstractt;
	}

	public WmsLayerLegendUrlInfo getLegendUrl() {
		if (legendUrl == null) {
			parse(getNode());
		}
		return legendUrl;
	}

	// ------------------------------------------------------------------------
	// AbstractNodeInfo implementation:
	// ------------------------------------------------------------------------

	protected void parse(Node node) {
		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			String nodeName = child.getNodeName();
			if ("Name".equalsIgnoreCase(nodeName)) {
				name = getValueRecursive(child);
			} else if ("Title".equalsIgnoreCase(nodeName)) {
				title = getValueRecursive(child);
			} else if ("Abstract".equalsIgnoreCase(nodeName)) {
				abstractt = getValueRecursive(child);
			} else if ("LegendURL".equalsIgnoreCase(nodeName)) {
				legendUrl = createLegendInfo(child);
			}
		}
	}
	
	protected WmsLayerLegendUrlInfo createLegendInfo(Node node) {
		return new WmsLayerLegendUrlInfo111(node);
	}
}