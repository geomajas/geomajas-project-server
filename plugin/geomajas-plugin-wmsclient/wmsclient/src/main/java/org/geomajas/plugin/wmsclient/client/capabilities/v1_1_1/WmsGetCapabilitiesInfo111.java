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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.plugin.wmsclient.client.capabilities.AbstractXmlNodeWrapper;
import org.geomajas.plugin.wmsclient.client.capabilities.WmsGetCapabilitiesInfo;
import org.geomajas.plugin.wmsclient.client.capabilities.WmsLayerInfo;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

/**
 * Implementation of the {@link WmsGetCapabilitiesInfo} for WMS version 1.1.1.
 * 
 * @author Pieter De Graef
 */
public class WmsGetCapabilitiesInfo111 extends AbstractXmlNodeWrapper implements WmsGetCapabilitiesInfo {

	private static final long serialVersionUID = 100L;

	private List<WmsLayerInfo> layers;

	public WmsGetCapabilitiesInfo111(Node node) {
		super(node);
	}

	public List<WmsLayerInfo> getLayers() {
		if (layers == null) {
			parse(getNode());
		}
		return layers;
	}

	protected void parse(Node node) {
		if (node instanceof Element) {
			Element element = (Element) node;
			NodeList layerNodes = element.getElementsByTagName("Layer");

			layers = new ArrayList<WmsLayerInfo>();
			for (int i = 0; i < layerNodes.getLength(); i++) {
				Node layerNode = layerNodes.item(i);
				if (layerNode.hasAttributes()) {
					WmsLayerInfo layer = new WmsLayerInfo111(layerNode);
					layers.add(layer);
				}
			}
		}
	}
}