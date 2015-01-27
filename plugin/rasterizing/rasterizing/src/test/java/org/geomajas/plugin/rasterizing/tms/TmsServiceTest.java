package org.geomajas.plugin.rasterizing.tms;

import java.io.StringWriter;

import junit.framework.Assert;

import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.rasterizing.tms.ProfileType;
import org.geomajas.plugin.rasterizing.tms.TmsServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/rasterizing/rasterizing-service.xml", "/org/geomajas/testdata/beanContext.xml",
		"/org/geomajas/testdata/layerBeans.xml", "/org/geomajas/testdata/layerBeansMultiLine.xml",
		"/org/geomajas/testdata/layerBeansMultiPolygon.xml", "/org/geomajas/testdata/layerBeansPoint.xml",
		"/org/geomajas/testdata/layerBluemarble.xml",
		"/org/geomajas/plugin/rasterizing/DefaultRasterizedPipelines.xml",
		"/META-INF/geomajasWebContextRasterizing.xml" })
public class TmsServiceTest {

	@Autowired
	private TmsServiceImpl tmsService;

	@Test
	public void testWriteService() throws GeomajasException {
		StringWriter sw = new StringWriter();
		tmsService.writeService(sw);
		Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" + 
				"<TileMapService>\n" + 
				"    <Title>Geomajas Tile Map Service</Title>\n" + 
				"    <Abstract>Tile Map Service publication of the Geomajas vector layers</Abstract>\n" + 
				"    <TileMaps>\n" + 
				"        <TileMap href=\"http://test/tms/1.0.0/beans@EPSG:3857/beansStyleInfo\" profile=\"global-mercator\" srs=\"EPSG:3857\" title=\"beans\"/>\n" + 
				"        <TileMap href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo\" profile=\"global-geodetic\" srs=\"EPSG:4326\" title=\"beans\"/>\n" + 
				"        <TileMap href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo\" profile=\"local\" srs=\"EPSG:4326\" title=\"beans\"/>\n" + 
				"        <TileMap href=\"http://test/tms/1.0.0/lotsObeans@EPSG:3857/beansStyleInfo\" profile=\"global-mercator\" srs=\"EPSG:3857\" title=\"lotsObeans\"/>\n" + 
				"        <TileMap href=\"http://test/tms/1.0.0/lotsObeans@EPSG:4326/beansStyleInfo\" profile=\"global-geodetic\" srs=\"EPSG:4326\" title=\"lotsObeans\"/>\n" + 
				"        <TileMap href=\"http://test/tms/1.0.0/lotsObeans@EPSG:4326/beansStyleInfo\" profile=\"local\" srs=\"EPSG:4326\" title=\"lotsObeans\"/>\n" + 
				"        <TileMap href=\"http://test/tms/1.0.0/layerBeansMultiLine@EPSG:3857/layerBeansMultiLineStyleInfo\" profile=\"global-mercator\" srs=\"EPSG:3857\" title=\"layerBeansMultiLine\"/>\n" + 
				"        <TileMap href=\"http://test/tms/1.0.0/layerBeansMultiLine@EPSG:4326/layerBeansMultiLineStyleInfo\" profile=\"global-geodetic\" srs=\"EPSG:4326\" title=\"layerBeansMultiLine\"/>\n" + 
				"        <TileMap href=\"http://test/tms/1.0.0/layerBeansMultiLine@EPSG:4326/layerBeansMultiLineStyleInfo\" profile=\"local\" srs=\"EPSG:4326\" title=\"layerBeansMultiLine\"/>\n" + 
				"        <TileMap href=\"http://test/tms/1.0.0/layerBeansMultiPolygon@EPSG:3857/layerBeansMultiPolygonStyleInfo\" profile=\"global-mercator\" srs=\"EPSG:3857\" title=\"layerBeansMultiPolygon\"/>\n" + 
				"        <TileMap href=\"http://test/tms/1.0.0/layerBeansMultiPolygon@EPSG:4326/layerBeansMultiPolygonStyleInfo\" profile=\"global-geodetic\" srs=\"EPSG:4326\" title=\"layerBeansMultiPolygon\"/>\n" + 
				"        <TileMap href=\"http://test/tms/1.0.0/layerBeansMultiPolygon@EPSG:4326/layerBeansMultiPolygonStyleInfo\" profile=\"local\" srs=\"EPSG:4326\" title=\"layerBeansMultiPolygon\"/>\n" + 
				"        <TileMap href=\"http://test/tms/1.0.0/layerBeansPoint@EPSG:3857/layerBeansPointStyleInfo\" profile=\"global-mercator\" srs=\"EPSG:3857\" title=\"layerBeansPoint\"/>\n" + 
				"        <TileMap href=\"http://test/tms/1.0.0/layerBeansPoint@EPSG:4326/layerBeansPointStyleInfo\" profile=\"global-geodetic\" srs=\"EPSG:4326\" title=\"layerBeansPoint\"/>\n" + 
				"        <TileMap href=\"http://test/tms/1.0.0/layerBeansPoint@EPSG:4326/layerBeansPointStyleInfo\" profile=\"local\" srs=\"EPSG:4326\" title=\"layerBeansPoint\"/>\n" + 
				"    </TileMaps>\n" + 
				"</TileMapService>\n" + 
				"", sw.toString());
	}
	
	@Test
	public void testWriteTileMap() throws GeomajasException {
		StringWriter sw = new StringWriter();
		tmsService.writeTileMap("beans", "beansStyleInfo", ProfileType.GLOBAL_GEODETIC, sw);
		Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" + 
				"<TileMap tilemapservice=\"http://test/tms/1.0.0\" version=\"1.0.0\">\n" + 
				"    <Title>beans</Title>\n" + 
				"    <Abstract>Tile Map Service publication of the Geomajas layer beans</Abstract>\n" + 
				"    <SRS>EPSG:4326</SRS>\n" + 
				"    <BoundingBox maxy=\"90.0\" maxx=\"180.0\" miny=\"-90.0\" minx=\"-180.0\"/>\n" + 
				"    <Origin y=\"-90.0\" x=\"-180.0\"/>\n" + 
				"    <TileFormat mime-type=\"image/png\" extension=\"png\" height=\"256\" width=\"256\"/>\n" + 
				"    <TileSets profile=\"global-geodetic\">\n" + 
				"        <TileSet order=\"0\" units-per-pixel=\"0.703125\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/0\"/>\n" + 
				"        <TileSet order=\"1\" units-per-pixel=\"0.3515625\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/1\"/>\n" + 
				"        <TileSet order=\"2\" units-per-pixel=\"0.17578125\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/2\"/>\n" + 
				"        <TileSet order=\"3\" units-per-pixel=\"0.087890625\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/3\"/>\n" + 
				"        <TileSet order=\"4\" units-per-pixel=\"0.0439453125\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/4\"/>\n" + 
				"        <TileSet order=\"5\" units-per-pixel=\"0.02197265625\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/5\"/>\n" + 
				"        <TileSet order=\"6\" units-per-pixel=\"0.010986328125\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/6\"/>\n" + 
				"        <TileSet order=\"7\" units-per-pixel=\"0.0054931640625\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/7\"/>\n" + 
				"        <TileSet order=\"8\" units-per-pixel=\"0.00274658203125\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/8\"/>\n" + 
				"        <TileSet order=\"9\" units-per-pixel=\"0.001373291015625\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/9\"/>\n" + 
				"        <TileSet order=\"10\" units-per-pixel=\"6.866455078125E-4\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/10\"/>\n" + 
				"        <TileSet order=\"11\" units-per-pixel=\"3.4332275390625E-4\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/11\"/>\n" + 
				"        <TileSet order=\"12\" units-per-pixel=\"1.71661376953125E-4\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/12\"/>\n" + 
				"        <TileSet order=\"13\" units-per-pixel=\"8.58306884765625E-5\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/13\"/>\n" + 
				"        <TileSet order=\"14\" units-per-pixel=\"4.291534423828125E-5\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/14\"/>\n" + 
				"        <TileSet order=\"15\" units-per-pixel=\"2.1457672119140625E-5\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/15\"/>\n" + 
				"        <TileSet order=\"16\" units-per-pixel=\"1.0728836059570312E-5\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/16\"/>\n" + 
				"        <TileSet order=\"17\" units-per-pixel=\"5.364418029785156E-6\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/17\"/>\n" + 
				"        <TileSet order=\"18\" units-per-pixel=\"2.682209014892578E-6\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/18\"/>\n" + 
				"        <TileSet order=\"19\" units-per-pixel=\"1.341104507446289E-6\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/19\"/>\n" + 
				"        <TileSet order=\"20\" units-per-pixel=\"6.705522537231445E-7\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/20\"/>\n" + 
				"        <TileSet order=\"21\" units-per-pixel=\"3.3527612686157227E-7\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/21\"/>\n" + 
				"        <TileSet order=\"22\" units-per-pixel=\"1.6763806343078613E-7\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/22\"/>\n" + 
				"        <TileSet order=\"23\" units-per-pixel=\"8.381903171539307E-8\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/23\"/>\n" + 
				"        <TileSet order=\"24\" units-per-pixel=\"4.190951585769653E-8\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/24\"/>\n" + 
				"        <TileSet order=\"25\" units-per-pixel=\"2.0954757928848267E-8\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/25\"/>\n" + 
				"        <TileSet order=\"26\" units-per-pixel=\"1.0477378964424133E-8\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/26\"/>\n" + 
				"        <TileSet order=\"27\" units-per-pixel=\"5.238689482212067E-9\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/27\"/>\n" + 
				"        <TileSet order=\"28\" units-per-pixel=\"2.6193447411060333E-9\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/28\"/>\n" + 
				"        <TileSet order=\"29\" units-per-pixel=\"1.3096723705530167E-9\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/29\"/>\n" + 
				"        <TileSet order=\"30\" units-per-pixel=\"6.548361852765083E-10\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/30\"/>\n" + 
				"        <TileSet order=\"31\" units-per-pixel=\"3.2741809263825417E-10\" href=\"http://test/tms/1.0.0/beans@EPSG:4326/beansStyleInfo/31\"/>\n" + 
				"    </TileSets>\n" + 
				"</TileMap>\n" + 
				"", sw.toString());
	}

}
