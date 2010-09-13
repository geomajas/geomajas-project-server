/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.layer.osm;

import com.vividsolutions.jts.geom.Envelope;
import junit.framework.Assert;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.service.GeoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/osmContext.xml" })
public class OsmLayerTest {

	@Autowired
	private Map<String, OsmLayer> layers;

	@Autowired
	private GeoService geoService;

	@Test
	public void testPaintOutOfBounds() throws Exception {
		double equator = OsmLayer.EQUATOR_IN_METERS;
		List<RasterTile> tiles = layers.get("osm").paint(geoService.getCrs("EPSG:900913"),
				new Envelope(-equator, equator, -equator, equator), 256 / equator);
		Assert.assertEquals(1, tiles.size());
		Assert.assertEquals("http://a.tile.openstreetmap.org/0/0/0.png", tiles.iterator().next().getUrl());
	}
}
