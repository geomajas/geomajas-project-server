/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.common.proxy;

import java.util.List;

import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.tile.RasterTile;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Mock layer class for testing.
 * 
 * @author Jan De Moerloose
 * 
 */
public class MockProxyLayer implements ProxyLayerSupport {

	private String id;

	private LayerAuthentication authentication;

	private RasterLayerInfo info;

	public MockProxyLayer(String id) {
		this.id = id;
		authentication = new LayerAuthentication();
		authentication.setUser("user-" + id);
		authentication.setPassword("password-" + id);
		authentication.setRealm("realm-" + id);
		info = new RasterLayerInfo();
		info.setMaxExtent(new Bbox(0, 0, 100, 100));
	}

	@Override
	public List<RasterTile> paint(CoordinateReferenceSystem boundsCrs, Envelope bounds, double scale)
			throws GeomajasException {
		return null;
	}

	@Override
	public RasterLayerInfo getLayerInfo() {
		return info;
	}

	@Override
	public CoordinateReferenceSystem getCrs() {
		return null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public ProxyAuthentication getProxyAuthentication() {
		return authentication;
	}

	@Override
	public boolean isUseCache() {
		return true;
	}

}
