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

import com.metaparadigm.jsonrpc.AbstractSerializer;
import com.metaparadigm.jsonrpc.MarshallException;
import com.metaparadigm.jsonrpc.ObjectMatch;
import com.metaparadigm.jsonrpc.SerializerState;
import com.metaparadigm.jsonrpc.UnmarshallException;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ???
 *
 * @author check subversion
 */
public class GeometrySerializer extends AbstractSerializer {

	private final Logger log = LoggerFactory.getLogger(GeometrySerializer.class);

	private static String TYPE_POINT = "Point";

	private static String TYPE_MULTIPOINT = "MultiPoint";

	private static String TYPE_LINESTRING = "LineString";

	private static String TYPE_LINEARRING = "LinearRing";

	private static String TYPE_POLYGON = "Polygon";

	private static String TYPE_MULTILINESTRING = "MultiLineString";

	private static String TYPE_MULTIPOLYGON = "MultiPolygon";

	private static String ATTRIBUTE_TYPE = "type";

	private static String ATTRIBUTE_SRID = "srid";

	private static String ATTRIBUTE_PRECISION = "precision";

	private static String ATTRIBUTE_COORDINATES = "coordinates";

	private static final long serialVersionUID = 1;

	private static Class[] SERIALIZABLE_CLASSES = new Class[] {
			Point.class,
			LineString.class,
			Polygon.class,
			LinearRing.class,
	};

	private static Class[] JSON_CLASSES = new Class[] {JSONObject.class};

	public Class[] getSerializableClasses() {
		return SERIALIZABLE_CLASSES;
	}

	public Class[] getJSONClasses() {
		return JSON_CLASSES;
	}

	public boolean canSerialize(Class clazz, Class jsonClazz) {
		return (super.canSerialize(clazz, jsonClazz) ||
				((jsonClazz == null || jsonClazz == JSONObject.class) && Geometry.class
						.isAssignableFrom(clazz)));
	}

	public ObjectMatch tryUnmarshall(SerializerState state, Class clazz, Object o) throws UnmarshallException {
		JSONObject jso = (JSONObject) o;
		String type = jso.getString(ATTRIBUTE_TYPE);
		if (type == null) {
			throw new UnmarshallException("no type hint");
		}
		int srid = jso.getInt(ATTRIBUTE_SRID);
		if (srid <= 0) {
			throw new UnmarshallException("no srid");
		}
		int precision = jso.getInt(ATTRIBUTE_PRECISION);
		if (precision <= 0) {
			throw new UnmarshallException("no precision");
		}
		if (!(type.equals(TYPE_POINT) || type.equals(TYPE_LINESTRING) || type.equals(TYPE_POLYGON)
				|| type.equals(TYPE_LINEARRING) || type.equals(TYPE_MULTILINESTRING) || type
				.equals(TYPE_MULTIPOLYGON))) {
			throw new UnmarshallException(type + " is not a supported geometry");
		}
		JSONArray coordinates = jso.getJSONArray(ATTRIBUTE_COORDINATES);
		if (coordinates == null) {
			throw new UnmarshallException("coordinates missing");
		}
		return ObjectMatch.OKAY;
	}

	public Object unmarshall(SerializerState state, Class clazz, Object o) throws UnmarshallException {
		JSONObject jso = (JSONObject) o;
		String type = jso.getString(ATTRIBUTE_TYPE);
		if (type == null) {
			throw new UnmarshallException("no type hint");
		}
		int srid = jso.getInt(ATTRIBUTE_SRID);
		int precision = jso.getInt(ATTRIBUTE_PRECISION);
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(Math.pow(10, precision)), srid);

		Geometry geometry = null;
		if (type.equals(TYPE_POINT)) {
			geometry = createPoint(factory, jso);
		} else if (type.equals(TYPE_LINESTRING)) {
			geometry = createLineString(factory, jso);
		} else if (type.equals(TYPE_POLYGON)) {
			geometry = createPolygon(factory, jso);
		} else if (type.equals(TYPE_MULTILINESTRING)) {
			geometry = createMultiLineString(factory, jso);
		} else if (type.equals(TYPE_MULTIPOLYGON)) {
			geometry = createMultiPolygon(factory, jso);
		}
		return geometry;
	}

	private Polygon createPolygon(GeometryFactory factory, JSONObject jso) throws UnmarshallException {
		Polygon geometry = null;

		JSONObject jsoShell = jso.getJSONObject("shell");
		LinearRing shell = createLinearRing(factory, jsoShell);

		JSONArray holeArray = jso.getJSONArray("holes");
		LinearRing[] holes = new LinearRing[holeArray.length()];
		for (int i = 0; i < holeArray.length(); i++) {
			JSONObject jsoHole = holeArray.getJSONObject(i);
			holes[i] = createLinearRing(factory, jsoHole);
		}
		geometry = factory.createPolygon(shell, holes);
		return geometry;
	}

	private LinearRing createLinearRing(GeometryFactory factory, JSONObject jso) throws UnmarshallException {
		JSONArray coords = jso.getJSONArray(ATTRIBUTE_COORDINATES);
		Coordinate[] coordinates = new Coordinate[coords.length()];
		for (int i = 0; i < coords.length(); i++) {
			JSONObject nextCoord = coords.getJSONObject(i);
			if (nextCoord == null) {
				throw new UnmarshallException("inner coordinate missing");
			}
			coordinates[i] = new Coordinate(nextCoord.getDouble("x"), nextCoord.getDouble("y"));
		}
		coordinates = checkIfClosed(coordinates);
		LinearRing ring = factory.createLinearRing(coordinates);
		return ring;
	}

	private LineString createLineString(GeometryFactory factory, JSONObject jso) throws UnmarshallException {
		LineString geometry;
		JSONArray jsonCoords = jso.getJSONArray(ATTRIBUTE_COORDINATES);
		if (jsonCoords == null) {
			throw new UnmarshallException("coordinates missing");
		}
		Coordinate[] coordinates = new Coordinate[jsonCoords.length()];
		for (int i = 0; i < jsonCoords.length(); i++) {
			JSONObject nextCoord = jsonCoords.getJSONObject(i);
			if (nextCoord == null) {
				throw new UnmarshallException("inner coordinate missing");
			}
			coordinates[i] = new Coordinate(nextCoord.getDouble("x"), nextCoord.getDouble("y"));
		}
		geometry = new LineString(new CoordinateArraySequence(coordinates), factory);
		return geometry;
	}

	private Point createPoint(GeometryFactory factory, JSONObject jso) throws UnmarshallException {
		Point geometry;
		JSONArray jsonCoords = jso.getJSONArray(ATTRIBUTE_COORDINATES);
		if (jsonCoords == null) {
			throw new UnmarshallException("coordinates missing");
		}
		if (jsonCoords.length() != 1) {
			throw new UnmarshallException("wrong number of coordinates " + jsonCoords.length() + " for point");
		}
		JSONObject coord = jsonCoords.getJSONObject(0);
		if (coord == null) {
			throw new UnmarshallException("inner coordinate missing");
		}

		Coordinate coordinate = new Coordinate(coord.getDouble("x"), coord.getDouble("y"));
		geometry = new Point(new CoordinateArraySequence(new Coordinate[] {coordinate}), factory);
		return geometry;
	}

	private MultiPolygon createMultiPolygon(GeometryFactory factory, JSONObject jso)
			throws UnmarshallException {
		MultiPolygon geometry = null;
		JSONArray polyArray = jso.getJSONArray("polygons");
		Polygon[] polygons = new Polygon[polyArray.length()];
		for (int i = 0; i < polygons.length; i++) {
			polygons[i] = createPolygon(factory, polyArray.getJSONObject(i));
		}
		geometry = factory.createMultiPolygon(polygons);
		return geometry;
	}

	private Geometry createMultiLineString(GeometryFactory factory, JSONObject jso)
			throws UnmarshallException {
		MultiLineString geometry = null;
		JSONArray lineArray = jso.getJSONArray("lineStrings");
		LineString[] lineStrings = new LineString[lineArray.length()];
		for (int i = 0; i < lineArray.length(); i++) {
			lineStrings[i] = createLineString(factory, lineArray.getJSONObject(i));
		}
		geometry = factory.createMultiLineString(lineStrings);
		return geometry;
	}

	public Object marshall(SerializerState state, Object o) throws MarshallException {
		JSONObject obj = new JSONObject();
		Geometry geometry = (Geometry) o;

		if (geometry instanceof Point) {
			obj = fromPoint((Point) geometry);
		} else if (geometry instanceof LineString) {
			obj = fromLineString((LineString) geometry);
		} else if (geometry instanceof Polygon) {
			obj = fromPolygon((Polygon) geometry);
		} else if (geometry instanceof MultiPolygon) {
			obj = fromMultiPolygon((MultiPolygon) geometry);
		} else if (geometry instanceof MultiLineString) {
			obj = fromMultiLineString((MultiLineString) geometry);
		} else if (geometry instanceof MultiPoint) {
			obj = fromMultiPoint((MultiPoint) geometry);
		} else {
			throw new MarshallException("cannot marshal " + geometry.getClass());
		}
		return obj;
	}

	private void putBasics(JSONObject jso, Geometry geometry) {
		jso.put(ATTRIBUTE_TYPE, getType(geometry));
		jso.put(ATTRIBUTE_SRID, geometry.getSRID());
		PrecisionModel precisionmodel = geometry.getPrecisionModel();
		// floating or fixed, if floating put -1, if fixed the number of
		// decimals
		if (precisionmodel.isFloating()) {
			jso.put(ATTRIBUTE_PRECISION, -1);
		} else {
			int precision = (int) Math.log10(precisionmodel.getScale());
			jso.put(ATTRIBUTE_PRECISION, precision);
		}
	}

	private JSONObject fromPoint(Point p) {
		JSONObject jso = new JSONObject();
		JSONArray coordinates = new JSONArray();
		PrecisionModel precisionmodel = p.getPrecisionModel();
		coordinates.put(precisionmodel.makePrecise(p.getX()));
		coordinates.put(precisionmodel.makePrecise(p.getY()));
		putBasics(jso, p);
		jso.put(ATTRIBUTE_COORDINATES, coordinates);
		return jso;
	}

	private JSONObject fromMultiPoint(MultiPoint mp) {
		JSONObject jso = new JSONObject();
		JSONArray polys = new JSONArray();
		for (int i = 0; i < mp.getNumGeometries(); i++) {
			polys.put(fromPoint((Point) mp.getGeometryN(i)));
		}
		jso.put("points", polys);
		putBasics(jso, mp);
		return jso;
	}

	private JSONObject fromLineString(LineString ls) {
		PrecisionModel precisionmodel = ls.getPrecisionModel();
		JSONObject jso = new JSONObject();
		JSONArray coordinates = new JSONArray();
		for (int i = 0; i < ls.getCoordinates().length; i++) {
			coordinates.put(precisionmodel.makePrecise(ls.getCoordinates()[i].x));
			coordinates.put(precisionmodel.makePrecise(ls.getCoordinates()[i].y));
		}
		putBasics(jso, ls);
		jso.put(ATTRIBUTE_COORDINATES, coordinates);
		return jso;
	}

	private JSONObject fromPolygon(Polygon pg) {
		JSONObject jso = new JSONObject();
		JSONObject shell = fromLineString(pg.getExteriorRing());
		JSONArray holes = new JSONArray();
		for (int i = 0; i < pg.getNumInteriorRing(); i++) {
			holes.put(fromLineString(pg.getInteriorRingN(i)));
		}
		jso.put("shell", shell);
		jso.put("holes", holes);
		putBasics(jso, pg);
		return jso;
	}

	private JSONObject fromMultiPolygon(MultiPolygon mp) {
		JSONObject jso = new JSONObject();
		JSONArray polys = new JSONArray();
		for (int i = 0; i < mp.getNumGeometries(); i++) {
			polys.put(fromPolygon((Polygon) mp.getGeometryN(i)));
		}
		jso.put("polygons", polys);
		putBasics(jso, mp);
		return jso;
	}

	private JSONObject fromMultiLineString(MultiLineString ml) {
		JSONObject jso = new JSONObject();
		JSONArray polys = new JSONArray();
		for (int i = 0; i < ml.getNumGeometries(); i++) {
			polys.put(fromLineString((LineString) ml.getGeometryN(i)));
		}
		jso.put("lineStrings", polys);
		putBasics(jso, ml);
		return jso;
	}

	private String getType(Geometry geom) {
		if (geom instanceof Point) {
			return TYPE_POINT;
		} else if (geom instanceof LineString) {
			return TYPE_LINESTRING;
		} else if (geom instanceof Polygon) {
			return TYPE_POLYGON;
		} else if (geom instanceof MultiPolygon) {
			return TYPE_MULTIPOLYGON;
		} else if (geom instanceof MultiLineString) {
			return TYPE_MULTILINESTRING;
		} else if (geom instanceof MultiPoint) {
			return TYPE_MULTIPOINT;
		} else {
			log.error("getType() : type " + geom.getClass().getName() + " unknown");
			return "unknown";
		}
	}

	private Coordinate[] checkIfClosed(Coordinate[] coords) {
		int length = coords.length;
		if (coords[0].equals(coords[length - 1])) {
			return coords;
		}
		Coordinate[] newCoords = new Coordinate[length + 1];
		for (int i = 0; i < length; i++) {
			newCoords[i] = coords[i];
		}
		newCoords[length] = coords[0];
		return newCoords;
	}
}
