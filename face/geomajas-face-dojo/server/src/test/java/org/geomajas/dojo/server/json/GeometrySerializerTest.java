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
import junit.framework.TestCase;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Serialization test for Geometry dtos to/from JSON.
 *
 * @author Jan De Moerloose
 * @author Pieter De Graef
 */
public class GeometrySerializerTest extends TestCase {

	public void testPoint() throws MarshallException {
		GeometrySerializer ser = new GeometrySerializer();
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(100.0), 31300);
		CoordinateArraySequence coords = new CoordinateArraySequence(new Coordinate[] {new Coordinate(
				12.3456, 34567.3456)});
		Point p = new Point(coords, factory);
		JSONObject jo = (JSONObject) ser.marshall(null, p);
		assertEquals("Point", jo.get("type").toString());
		assertEquals("31300", jo.get("srid").toString());
		assertEquals("2", jo.get("precision").toString());
		assertEquals("[12.35,34567.35]", jo.get("coordinates").toString());
	}

	public void testLineString() throws MarshallException {
		GeometrySerializer ser = new GeometrySerializer();
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(100.0), 31300);
		CoordinateArraySequence coords = new CoordinateArraySequence(new Coordinate[] {
				new Coordinate(12.0, 34.23), new Coordinate(12.000, 54.555), new Coordinate(-0.01, -0.0)});
		LineString p = new LineString(coords, factory);
		JSONObject jo = (JSONObject) ser.marshall(null, p);
		assertEquals("LineString", jo.get("type").toString());
		assertEquals("31300", jo.get("srid").toString());
		assertEquals("2", jo.get("precision").toString());
		assertEquals("[12,34.23,12,54.56,-0.01,0]", jo.get("coordinates").toString());
	}

	public void testPolygon() throws MarshallException {
		GeometrySerializer ser = new GeometrySerializer();
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(100.0), 31300);
		CoordinateArraySequence coords = new CoordinateArraySequence(new Coordinate[] {
				new Coordinate(12.0, 34.23), new Coordinate(12.000, 54.555), new Coordinate(7, 8),
				new Coordinate(12.0, 34.23)});
		LinearRing ring = new LinearRing(coords, factory);
		Polygon p = new Polygon(ring, new LinearRing[] {}, factory);
		JSONObject jo = (JSONObject) ser.marshall(null, p);
		assertEquals("Polygon", jo.get("type").toString());
		assertEquals("[]", jo.get("holes").toString());
		assertEquals("31300", jo.get("srid").toString());
		assertEquals("2", jo.get("precision").toString());
		JSONObject shell = jo.getJSONObject("shell");
		assertEquals("LineString", shell.get("type").toString());
		assertEquals("31300", shell.get("srid").toString());
		assertEquals("2", shell.get("precision").toString());
		assertEquals("[12,34.23,12,54.56,7,8,12,34.23]", shell.get("coordinates").toString());
	}

	public void testMultiPolygon() throws MarshallException {
		GeometrySerializer ser = new GeometrySerializer();
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(100.0), 31300);
		CoordinateArraySequence coords = new CoordinateArraySequence(new Coordinate[] {
				new Coordinate(12.0, 34.23), new Coordinate(12.000, 54.555), new Coordinate(7, 8),
				new Coordinate(12.0, 34.23)});
		LinearRing ring = new LinearRing(coords, factory);
		Polygon p = new Polygon(ring, new LinearRing[] {}, factory);
		MultiPolygon m = new MultiPolygon(new Polygon[] {p}, factory);
		JSONObject jo = (JSONObject) ser.marshall(null, m);
		assertEquals("MultiPolygon", jo.get("type").toString());
		assertEquals("31300", jo.get("srid").toString());
		assertEquals("2", jo.get("precision").toString());
		JSONArray polygons = jo.getJSONArray("polygons");
		JSONObject polygon = polygons.getJSONObject(0);
		assertEquals("Polygon", polygon.get("type").toString());
		assertEquals("[]", polygon.get("holes").toString());
		assertEquals("31300", polygon.get("srid").toString());
		assertEquals("2", polygon.get("precision").toString());
		JSONObject shell = polygon.getJSONObject("shell");
		assertEquals("LineString", shell.get("type").toString());
		assertEquals("31300", shell.get("srid").toString());
		assertEquals("2", shell.get("precision").toString());
		assertEquals("[12,34.23,12,54.56,7,8,12,34.23]", shell.get("coordinates").toString());
	}

	public void testMultiLineString() throws MarshallException {
		GeometrySerializer ser = new GeometrySerializer();
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(100.0), 31300);
		CoordinateArraySequence coords = new CoordinateArraySequence(new Coordinate[] {
				new Coordinate(12.0, 34.23), new Coordinate(12.000, 54.555), new Coordinate(-0.01, -0.0)});
		LineString l = new LineString(coords, factory);
		MultiLineString m = new MultiLineString(new LineString[] {l}, factory);
		JSONObject jo = (JSONObject) ser.marshall(null, m);
		assertEquals("MultiLineString", jo.get("type").toString());
		assertEquals("31300", jo.get("srid").toString());
		assertEquals("2", jo.get("precision").toString());
		JSONArray lineStrings = jo.getJSONArray("lineStrings");
		JSONObject lineString = lineStrings.getJSONObject(0);
		assertEquals("LineString", lineString.get("type").toString());
		assertEquals("31300", lineString.get("srid").toString());
		assertEquals("2", lineString.get("precision").toString());
		assertEquals("[12,34.23,12,54.56,-0.01,0]", lineString.get("coordinates").toString());
	}
}
