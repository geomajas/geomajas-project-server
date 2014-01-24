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

package org.geomajas.internal.service;

import junit.framework.Assert;

import org.geomajas.global.GeomajasException;
import org.geomajas.internal.layer.tile.InternalTileImpl;
import org.geomajas.layer.tile.VectorTile;
import org.geomajas.layer.tile.VectorTile.VectorTileContentType;
import org.geomajas.service.DtoConverterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Tests for conversion of tiles.
 * 
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml" })
public class TileConverterTest {

	@Autowired
	private DtoConverterService converterService;

	@Test
	public void testToDto() throws GeomajasException {
		InternalTileImpl internalTile = new InternalTileImpl(0, 0, 0, new Envelope(0, 0, 10, 10), 0);
		internalTile.setContentType(VectorTileContentType.STRING_CONTENT);
		VectorTile tile = converterService.toDto(internalTile);
		Assert.assertNotNull(tile);
	}
}
