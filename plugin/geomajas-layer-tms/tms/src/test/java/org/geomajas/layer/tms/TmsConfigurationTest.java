/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.tms;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.geomajas.layer.tms.configuration.TileFormatInfo;
import org.geomajas.layer.tms.configuration.TileMapInfo;
import org.geomajas.layer.tms.configuration.TileSetInfo;
import org.geomajas.layer.tms.configuration.TileSetsInfo;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for the TMS configuration service.
 * 
 * @author Pieter De Graef
 */
public class TmsConfigurationTest {

	private static final double DELTA = 0.00001;

	@Test
	public void testLayerCapa1Unmarshall() throws JAXBException, IOException {
		// Create a JaxB unmarshaller:
		JAXBContext context = JAXBContext.newInstance(TileMapInfo.class);
		Unmarshaller um = context.createUnmarshaller();

		// Create an input stream that points to a TMS layer configuration (xml):
		InputStream is = getClass().getResourceAsStream("/org/geomajas/layer/tms/tileMapCapa1.xml");

		// Unmarshall the InputStream into a TileMapInfo object:
		TileMapInfo tileMap = (TileMapInfo) um.unmarshal(is);

		// Test basic parameters:
		Assert.assertNotNull(tileMap);
		Assert.assertEquals("abstract", tileMap.getAbstractTxt());
		Assert.assertEquals("srs", tileMap.getSrs());
		Assert.assertEquals("title", tileMap.getTitle());
		Assert.assertEquals("version", tileMap.getVersion());

		// Test bounding box:
		Assert.assertEquals(1, tileMap.getBoundingBox().getMinX(), DELTA);
		Assert.assertEquals(2, tileMap.getBoundingBox().getMinY(), DELTA);
		Assert.assertEquals(3, tileMap.getBoundingBox().getMaxX(), DELTA);
		Assert.assertEquals(4, tileMap.getBoundingBox().getMaxY(), DELTA);

		// Test origin:
		Assert.assertEquals(5, tileMap.getOrigin().getX(), DELTA);
		Assert.assertEquals(6, tileMap.getOrigin().getY(), DELTA);

		// Test TileFormat:
		Assert.assertEquals(7, tileMap.getTileFormat().getWidth());
		Assert.assertEquals(8, tileMap.getTileFormat().getHeight());
		Assert.assertEquals("extension", tileMap.getTileFormat().getExtension());
		Assert.assertEquals("mimetype", tileMap.getTileFormat().getMimeType());

		// Test Tile sets:
		Assert.assertEquals("profile", tileMap.getTileSets().getProfile());
		Assert.assertEquals(2, tileMap.getTileSets().getTileSets().size());

		// First TileSet:
		TileSetInfo tileSet = tileMap.getTileSets().getTileSets().get(0);
		Assert.assertEquals(9, tileSet.getUnitsPerPixel(), DELTA);
		Assert.assertEquals(0, tileSet.getOrder(), DELTA);
		Assert.assertEquals("href1", tileSet.getHref());

		// Second TileSet:
		tileSet = tileMap.getTileSets().getTileSets().get(1);
		Assert.assertEquals(10, tileSet.getUnitsPerPixel(), DELTA);
		Assert.assertEquals(1, tileSet.getOrder(), DELTA);
		Assert.assertEquals("href2", tileSet.getHref());
	}

	public void testMarshall() throws JAXBException {
		// Create a JaxB unmarshaller:
		JAXBContext context = JAXBContext.newInstance(TileMapInfo.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		TileMapInfo tileMap = new TileMapInfo();
		tileMap.setVersion("1.0.0");
		tileMap.setTitle("tms-vlaanderen");

		TileFormatInfo tileFormat = new TileFormatInfo();
		tileFormat.setExtension("jpg");
		tileFormat.setHeight(256);
		tileFormat.setWidth(256);
		tileFormat.setMimeType("image/jpeg");
		tileMap.setTileFormat(tileFormat);

		TileSetsInfo tileSets = new TileSetsInfo();
		tileSets.setProfile("raster");
		TileSetInfo tileSet = new TileSetInfo();
		tileSet.setHref("0");
		tileSet.setOrder(0);
		tileSet.setUnitsPerPixel(0.25000000000000);
		List<TileSetInfo> tileSetList = new ArrayList<TileSetInfo>();
		tileSetList.add(tileSet);
		tileMap.setTileSets(tileSets);
		tileSets.setTileSets(tileSetList);

		marshaller.marshal(tileMap, System.out);
	}
}