/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.dojo.server.json;

import junit.framework.TestCase;

import org.geomajas.geometry.Geometry;
import org.geomajas.global.GeomajasException;
import org.geomajas.service.DtoConverterService;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.metaparadigm.jsonrpc.MarshallException;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import com.vividsolutions.jts.util.Assert;

/**
 * Serializer test for the DTO geometry objects.
 *
 * @author Pieter De Graef
 */
public class DtoGeometrySerializerTest extends TestCase {

	private DtoGeometrySerializer dtoSerializer;

	private GeometrySerializer jtsSerializer;

	private DtoConverterService converter;

	public DtoGeometrySerializerTest() {
		dtoSerializer = new DtoGeometrySerializer();
		jtsSerializer = new GeometrySerializer();

		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] {
				"org/geomajas/spring/geomajasContext.xml", "org/geomajas/spring/emptyApplication.xml" });
		converter = applicationContext.getBean("service.DtoConverterService", DtoConverterService.class);
	}

	public void testPoint() throws MarshallException, GeomajasException {
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(10000.0), 31300);
		CoordinateArraySequence coords = new CoordinateArraySequence(new Coordinate[] { new Coordinate(12.3456,
				34567.3456) });
		Point p = new Point(coords, factory);
		JSONObject jtsJson = (JSONObject) jtsSerializer.marshall(null, p);
		Geometry dto = converter.toDto(p);
		JSONObject dtoJson = (JSONObject) dtoSerializer.marshall(null, dto);
		Assert.equals(jtsJson.toString(), dtoJson.toString());
	}

	public void testLineString() throws MarshallException, GeomajasException {
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(10000.0), 31300);
		CoordinateArraySequence coords = new CoordinateArraySequence(new Coordinate[] { new Coordinate(12.0, 34.23),
				new Coordinate(12.000, 54.555), new Coordinate(-0.01, 0.0) });
		LineString p = new LineString(coords, factory);
		JSONObject jtsJson = (JSONObject) jtsSerializer.marshall(null, p);
		Geometry dto = converter.toDto(p);
		JSONObject dtoJson = (JSONObject) dtoSerializer.marshall(null, dto);
		Assert.equals(jtsJson.toString(), dtoJson.toString());
	}

	public void testPolygon() throws MarshallException, GeomajasException {
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(10000.0), 31300);
		CoordinateArraySequence coords = new CoordinateArraySequence(new Coordinate[] { new Coordinate(12.0, 34.23),
				new Coordinate(12.000, 54.555), new Coordinate(7, 8), new Coordinate(12.0, 34.23) });
		LinearRing ring = new LinearRing(coords, factory);
		Polygon p = new Polygon(ring, null, factory);
		Geometry dto = converter.toDto(p);

		JSONObject jtsJson = (JSONObject) jtsSerializer.marshall(null, p);
		JSONObject dtoJson = (JSONObject) dtoSerializer.marshall(null, dto);
		Assert.equals(jtsJson.toString().length(), dtoJson.toString().length());
		Assert.equals(jtsJson.get("type").toString(), dtoJson.get("type").toString());
		Assert.equals(jtsJson.get("holes").toString(), dtoJson.get("holes").toString());
		Assert.equals(jtsJson.get("srid").toString(), dtoJson.get("srid").toString());
		Assert.equals(jtsJson.get("precision").toString(), dtoJson.get("precision").toString());

		JSONObject jtsShell = jtsJson.getJSONObject("shell");
		JSONObject dtoShell = dtoJson.getJSONObject("shell");
		Assert.equals(jtsShell.get("type").toString(), dtoShell.get("type").toString());
		Assert.equals(jtsShell.get("srid").toString(), dtoShell.get("srid").toString());
		Assert.equals(jtsShell.get("precision").toString(), dtoShell.get("precision").toString());
		Assert.equals(jtsShell.get("coordinates").toString(), dtoShell.get("coordinates").toString());
	}

	public void testMultiPolygon() throws MarshallException, GeomajasException {
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(10000.0), 31300);
		CoordinateArraySequence coords = new CoordinateArraySequence(new Coordinate[] { new Coordinate(12.0, 34.23),
				new Coordinate(12.000, 54.555), new Coordinate(7, 8), new Coordinate(12.0, 34.23) });
		LinearRing ring = new LinearRing(coords, factory);
		Polygon p = new Polygon(ring, new LinearRing[] {}, factory);
		MultiPolygon m = new MultiPolygon(new Polygon[] { p }, factory);
		Geometry dto = converter.toDto(m);

		JSONObject jtsJson = (JSONObject) jtsSerializer.marshall(null, m);
		JSONObject dtoJson = (JSONObject) dtoSerializer.marshall(null, dto);
		Assert.equals(jtsJson.toString().length(), dtoJson.toString().length());
		Assert.equals(jtsJson.get("type").toString(), dtoJson.get("type").toString());
		Assert.equals(jtsJson.get("srid").toString(), dtoJson.get("srid").toString());
		Assert.equals(jtsJson.get("precision").toString(), dtoJson.get("precision").toString());

		JSONObject jtsPolygon = jtsJson.getJSONArray("polygons").getJSONObject(0);
		JSONObject dtoPolygon = dtoJson.getJSONArray("polygons").getJSONObject(0);
		Assert.equals(jtsPolygon.get("type").toString(), dtoPolygon.get("type").toString());
		Assert.equals(jtsPolygon.get("holes").toString(), dtoPolygon.get("holes").toString());
		Assert.equals(jtsPolygon.get("srid").toString(), dtoPolygon.get("srid").toString());
		Assert.equals(jtsPolygon.get("precision").toString(), dtoPolygon.get("precision").toString());

		JSONObject jtsShell = jtsPolygon.getJSONObject("shell");
		JSONObject dtoShell = dtoPolygon.getJSONObject("shell");
		Assert.equals(jtsShell.get("type").toString(), dtoShell.get("type").toString());
		Assert.equals(jtsShell.get("srid").toString(), dtoShell.get("srid").toString());
		Assert.equals(jtsShell.get("precision").toString(), dtoShell.get("precision").toString());
		Assert.equals(jtsShell.get("coordinates").toString(), dtoShell.get("coordinates").toString());
	}

	public void testMultiLineString() throws MarshallException, GeomajasException {
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(10000.0), 31300);
		CoordinateArraySequence coords = new CoordinateArraySequence(new Coordinate[] { new Coordinate(12.0, 34.23),
				new Coordinate(12.000, 54.555), new Coordinate(-0.01, 0.0) });
		LineString l = new LineString(coords, factory);
		MultiLineString m = new MultiLineString(new LineString[] { l }, factory);
		JSONObject jtsJson = (JSONObject) jtsSerializer.marshall(null, m);
		Geometry dto = converter.toDto(m);
		JSONObject dtoJson = (JSONObject) dtoSerializer.marshall(null, dto);
		Assert.equals(jtsJson.toString(), dtoJson.toString());
	}
}
