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
package org.geomajas.plugin.rasterizing.layer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.geomajas.configuration.AbstractAttributeInfo;
import org.geomajas.configuration.GeometryAttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.SyntheticAttributeInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.layer.LayerType;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.InternalFeature;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.DataFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * FeatureCollection backed by a list of {@link InternalFeature} objects.
 * 
 * @author Jan De Moerloose
 * 
 */
public class InternalFeatureCollection extends DataFeatureCollection {

	private List<InternalFeature> internalFeatures;

	private ReferencedEnvelope bounds;

	/**
	 * Construct a collection with specified params.
	 * 
	 * @param layer layer
	 * @param mapCrs crs
	 * @param internalFeatures backing list
	 * @param bounds bounds
	 */
	public InternalFeatureCollection(VectorLayer layer, CoordinateReferenceSystem mapCrs,
			List<InternalFeature> internalFeatures, ReferencedEnvelope bounds) {
		this.internalFeatures = internalFeatures;
		this.bounds = bounds;
		schema = createFeatureType(layer, mapCrs);
	}

	private SimpleFeatureType createFeatureType(VectorLayer layer, CoordinateReferenceSystem mapCrs) {
		SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
		VectorLayerInfo info = layer.getLayerInfo();
		builder.setName(info.getFeatureInfo().getDataSourceName());
		builder.setCRS(mapCrs);
		for (AbstractAttributeInfo attrInfo : info.getFeatureInfo().getAttributes()) {
			if (attrInfo instanceof PrimitiveAttributeInfo) {
				PrimitiveAttributeInfo prim = (PrimitiveAttributeInfo) attrInfo;
				switch (prim.getType()) {
					case BOOLEAN:
						builder.add(prim.getName(), Boolean.class);
						break;
					case CURRENCY:
						builder.add(prim.getName(), BigDecimal.class);
						break;
					case DATE:
						builder.add(prim.getName(), Date.class);
						break;
					case DOUBLE:
						builder.add(prim.getName(), Double.class);
						break;
					case FLOAT:
						builder.add(prim.getName(), Float.class);
						break;
					case INTEGER:
						builder.add(prim.getName(), Integer.class);
						break;
					case LONG:
						builder.add(prim.getName(), Long.class);
						break;
					case SHORT:
						builder.add(prim.getName(), Short.class);
						break;
					case STRING:
					case URL:
					case IMGURL:
						builder.add(prim.getName(), String.class);
						break;
					default:
						throw new IllegalStateException("Unknown primitive attribute type " + prim.getType());
				}
			} else if (attrInfo instanceof SyntheticAttributeInfo) {
				SyntheticAttributeInfo synth = (SyntheticAttributeInfo) attrInfo;
				// can't determine type, using object
				builder.add(synth.getName(), Object.class);
			}
		}
		// add the geometry attribute
		GeometryAttributeInfo geom = info.getFeatureInfo().getGeometryType();
		builder.add(geom.getName(), getGeometryBinding(info.getLayerType()), mapCrs);
		builder.setDefaultGeometry(geom.getName());
		return builder.buildFeatureType();
	}

	private Class getGeometryBinding(LayerType layerType) {
		switch (layerType) {
			case LINESTRING:
				return Geometry.class;
			case MULTILINESTRING:
				return MultiLineString.class;
			case MULTIPOINT:
				return MultiPoint.class;
			case MULTIPOLYGON:
				return MultiPolygon.class;
			case POINT:
				return Point.class;
			case POLYGON:
				return Polygon.class;
			default:
				return Geometry.class;
		}
	}

	@Override
	public SimpleFeatureIterator features() {
		return new WrappingIterator();
	}

	@Override
	public ReferencedEnvelope getBounds() {
		return bounds;
	}

	@Override
	public int getCount() throws IOException {
		return internalFeatures.size();
	}

	/**
	 * Iterator that reuses the same geotools feature over and over again to wrap the {@link InternalFeature}s. Trying
	 * to avoid geotools object creation to increase performance.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public class WrappingIterator implements SimpleFeatureIterator {

		private Iterator<InternalFeature> it = internalFeatures.iterator();

		private WrappedFeature feature = new WrappedFeature(schema);

		@Override
		public boolean hasNext() {
			return it.hasNext();
		}

		@Override
		public SimpleFeature next() throws NoSuchElementException {
			InternalFeature internalFeature = it.next();
			feature.setId(internalFeature.getId());
			for (String name : internalFeature.getAttributes().keySet()) {
				Attribute<?> attribute = internalFeature.getAttributes().get(name);
				if (attribute.isPrimitive()) {
					feature.setAttribute(name, attribute.getValue());
				}
			}
			feature.setDefaultGeometry(internalFeature.getGeometry());
			return feature;
		}

		@Override
		public void close() {
		}

	}

	/**
	 * Geotools feature with mutable id for reuse.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public class WrappedFeature extends SimpleFeatureImpl {

		public WrappedFeature(SimpleFeatureType featureType) {
			super(new Object[featureType.getAttributeCount()], featureType, new FeatureIdImpl(""), false);
		}

		public void setId(String fid) {
			((FeatureIdImpl) id).setID(fid);
		}

	}

}
