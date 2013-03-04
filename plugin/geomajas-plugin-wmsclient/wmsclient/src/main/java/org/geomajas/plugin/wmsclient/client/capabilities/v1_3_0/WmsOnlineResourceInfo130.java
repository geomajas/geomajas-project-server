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

package org.geomajas.plugin.wmsclient.client.capabilities.v1_3_0;

import org.geomajas.plugin.wmsclient.client.capabilities.v1_1_1.WmsOnlineResourceInfo111;

import com.google.gwt.xml.client.Node;

/**
 * Default {@link org.geomajas.plugin.wmsclient.client.capabilities.WmsOnlineResourceInfo} implementation for WMS
 * 1.3.0.
 * 
 * @author Pieter De Graef
 */
public class WmsOnlineResourceInfo130 extends WmsOnlineResourceInfo111 {

	private static final long serialVersionUID = 100L;

	public WmsOnlineResourceInfo130(Node node) {
		super(node);
	}
}