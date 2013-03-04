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
import org.geomajas.plugin.wmsclient.client.capabilities.WmsOnlineResourceInfo;

import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;

/**
 * Implementation of the {@link WmsOnlineResourceInfo} for WMS version 1.1.1.
 * 
 * @author Pieter De Graef
 */
public class WmsOnlineResourceInfo111 extends AbstractXmlNodeWrapper implements WmsOnlineResourceInfo {

	private static final long serialVersionUID = 100L;

	private String type;

	private String xLink;

	private String href;

	public WmsOnlineResourceInfo111(Node node) {
		super(node);
	}

	public String getType() {
		if (type == null) {
			parse(getNode());
		}
		return type;
	}

	public String getXLink() {
		if (xLink == null) {
			parse(getNode());
		}
		return xLink;
	}

	public String getHref() {
		if (href == null) {
			parse(getNode());
		}
		return href;
	}

	// ------------------------------------------------------------------------
	// AbstractNodeInfo implementation:
	// ------------------------------------------------------------------------

	protected void parse(Node node) {
		NamedNodeMap attributes = node.getAttributes();
		type = getValueRecursive(attributes.getNamedItem("xlink:type"));
		xLink = getValueRecursive(attributes.getNamedItem("xmlns:xlink"));
		href = getValueRecursive(attributes.getNamedItem("xlink:href"));
	}
}