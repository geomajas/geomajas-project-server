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
import com.vividsolutions.jts.util.Assert;
import junit.framework.TestCase;
import org.geomajas.geometry.Geometry;
import org.geomajas.service.DtoConverterService;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DtoGeometrySerializerTest extends TestCase {

	private DtoGeometrySerializer dtoSerializer;

	private GeometrySerializer jtsSerializer;

	private DtoConverterService converter;

	public DtoGeometrySerializerTest() {
		dtoSerializer = new DtoGeometrySerializer();
		jtsSerializer = new GeometrySerializer();

		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				new String[] {"org/geomajas/spring/geomajasContext.xml", "org/geomajas/spring/emptyApplication.xml"});
		converter = applicationContext.getBean("service.DtoConverterService", DtoConverterService.class);
	}

	public void testPoint() throws MarshallException {
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(10000.0), 31300);
		CoordinateArraySequence coords = new CoordinateArraySequence(new Coordinate[] {new Coordinate(12.3456,
				34567.3456)});
		Point p = new Point(coords, factory);
		JSONObject jtsJson = (JSONObject) jtsSerializer.marshall(null, p);
		Geometry dto = converter.toDto(p);
		JSONObject dtoJson = (JSONObject) dtoSerializer.marshall(null, dto);
		Assert.equals(jtsJson.toString(), dtoJson.toString());
	}

	public void testLineString() throws MarshallException {
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(10000.0), 31300);
		CoordinateArraySequence coords = new CoordinateArraySequence(new Coordinate[] {new Coordinate(12.0, 34.23),
				new Coordinate(12.000, 54.555), new Coordinate(-0.01, 0.0)});
		LineString p = new LineString(coords, factory);
		JSONObject jtsJson = (JSONObject) jtsSerializer.marshall(null, p);
		Geometry dto = converter.toDto(p);
		JSONObject dtoJson = (JSONObject) dtoSerializer.marshall(null, dto);
		Assert.equals(jtsJson.toString(), dtoJson.toString());
	}

	public void testPolygon() throws MarshallException {
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(10000.0), 31300);
		CoordinateArraySequence coords = new CoordinateArraySequence(new Coordinate[] {new Coordinate(12.0, 34.23),
				new Coordinate(12.000, 54.555), new Coordinate(7, 8), new Coordinate(12.0, 34.23)});
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

	public void testMultiPolygon() throws MarshallException {
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(10000.0), 31300);
		CoordinateArraySequence coords = new CoordinateArraySequence(new Coordinate[] {new Coordinate(12.0, 34.23),
				new Coordinate(12.000, 54.555), new Coordinate(7, 8), new Coordinate(12.0, 34.23)});
		LinearRing ring = new LinearRing(coords, factory);
		Polygon p = new Polygon(ring, new LinearRing[] {}, factory);
		MultiPolygon m = new MultiPolygon(new Polygon[] {p}, factory);
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

	public void testMultiLineString() throws MarshallException {
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(10000.0), 31300);
		CoordinateArraySequence coords = new CoordinateArraySequence(new Coordinate[] {new Coordinate(12.0, 34.23),
				new Coordinate(12.000, 54.555), new Coordinate(-0.01, 0.0)});
		LineString l = new LineString(coords, factory);
		MultiLineString m = new MultiLineString(new LineString[] {l}, factory);
		JSONObject jtsJson = (JSONObject) jtsSerializer.marshall(null, m);
		Geometry dto = converter.toDto(m);
		JSONObject dtoJson = (JSONObject) dtoSerializer.marshall(null, dto);
		Assert.equals(jtsJson.toString(), dtoJson.toString());
	}
}
