/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.service.manager;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.geomajas.configuration.AbstractAttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.plugin.deskmanager.DeskmanagerException;
import org.geomajas.service.GeoService;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.Transaction;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Implementation of the ShapeFileService.
 * 
 * @author Kristof Heirwegh
 * @author Frank Wynants
 * @author Oliver May
 */
@Component
public class ShapeFileServiceImpl implements ShapeFileService {

	private final Logger log = LoggerFactory.getLogger(ShapeFileServiceImpl.class);

	private static final long serialVersionUID = 1L;

	private static final String DELIM = ",";

	private static final String SUBDELIM = ":";

	@Autowired
	private VectorLayerService layerService;

	@Resource(name = "postGisDatastore")
	private DataStore dataStore;

	@Autowired
	private GeoService geoService;

	@Resource(name = "dynamicLayersApplication")
	private ClientApplicationInfo defaultGeodesk;

	public boolean importShapeFile(String shpFileName, String layerName) {
		log.info("Importing Shapefile using Geotools: " + shpFileName);
		DataStore sourceStore = null;
		Transaction tr = null;

		try {
			File shpFile = new File(shpFileName);
			sourceStore = toApplicationCrs((ShapefileDataStore) FileDataStoreFinder.getDataStore(shpFile));
			SimpleFeatureSource featureSource = sourceStore.getFeatureSource(sourceStore.getTypeNames()[0]);

			Set<String> schemes = new HashSet<String>();
			for (String s : dataStore.getTypeNames()) {
				schemes.add(s);
			}

			String action = "create";
			if (schemes.contains(layerName)) {
				action = "update";
			} else {
				// Build the feature type for the database
				SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
				builder.init(sourceStore.getSchema(sourceStore.getTypeNames()[0]));
				// builder.init(SimpleFeatureTypeBuilder.retype(sourceStore.getSchema(),
				// geoService.getCrs2(defaultCrs)));
				builder.setName(layerName);

				dataStore.createSchema(builder.buildFeatureType());
			}

			tr = new DefaultTransaction(action);
			String typename = layerName;
			SimpleFeatureStore fs = (SimpleFeatureStore) dataStore.getFeatureSource(typename); // 2.7.0
			fs.setTransaction(tr);
			fs.removeFeatures(Filter.INCLUDE);
			fs.addFeatures(featureSource.getFeatures());
			tr.commit();

		} catch (Exception e) {
			log.warn("Failed adding features from ShapeFile to database", e);
			return false;

		} finally {
			try {
				if (sourceStore != null) {
					sourceStore.dispose();
				}
				if (tr != null) {
					tr.close();
					// do not dispose dataStore, it is also used by geoTools layers
				}
			} catch (Exception e2) { // ignore
			}
		}
		return true;
	}

	private DataStore toApplicationCrs(ShapefileDataStore sourceStore) throws LayerException, IOException,
			FactoryException {

		CoordinateReferenceSystem sourceCrs = sourceStore.getSchema().getGeometryDescriptor()
				.getCoordinateReferenceSystem();
		CoordinateReferenceSystem targetCrs = geoService.getCrs2(defaultGeodesk.getMaps().get(0).getCrs());
		boolean lenient = true; // allow for some error due to different datums
		MathTransform transform = CRS.findMathTransform(sourceCrs, targetCrs, lenient);

		SimpleFeatureType featureType = SimpleFeatureTypeBuilder.retype(sourceStore.getSchema(), targetCrs);

		DataStore datastore = new MemoryDataStore(sourceStore.getFeatureSource().getFeatures());

		SimpleFeatureIterator it = datastore.getFeatureSource(datastore.getTypeNames()[0]).getFeatures().features();

		try {
			//Reproject geometr
			while (it.hasNext()) {
				SimpleFeature feature = it.next();
				Geometry geometry = (Geometry) feature.getDefaultGeometry();
				Geometry geometry2 = JTS.transform(geometry, transform);
				feature.setDefaultGeometry(geometry2);
			}

		} catch (TransformException e) {
			log.warn(e.getLocalizedMessage(), e);
		}
		return datastore;
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	// see GBE-321
	public void toShapeFile(File shapeFile, VectorLayer layer, List<InternalFeature> features) throws Exception {
		if (features.size() == 0) {
			return;
		}

		List<PrimitiveAttributeInfo> attInfos = getPrimitiveAttributeInfo(layer);
		SimpleFeatureType type = createFeatureType(layer.getId(), layer.getLayerInfo().getFeatureInfo()
				.getGeometryType().getName(), features.get(0).getGeometry().getGeometryType(), features.get(0)
				.getGeometry().getSRID(), attInfos);
		DefaultFeatureCollection collection = new DefaultFeatureCollection();
		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);
		for (InternalFeature feature : features) {
			featureBuilder.add(feature.getGeometry());
			Map<String, Attribute> atts = feature.getAttributes();
			for (PrimitiveAttributeInfo pai : attInfos) {
				featureBuilder.add(atts.get(pai.getName()).getValue());
			}
			SimpleFeature sf = featureBuilder.buildFeature(feature.getId());
			collection.add(sf);
		}

		buildShapeFile(shapeFile, layer, collection, type);
	}

	// ----------------------------------------------------------

	private void buildShapeFile(File shapeFile, VectorLayer layer, SimpleFeatureCollection collection,
			SimpleFeatureType type) throws Exception, DeskmanagerException {
		Map<String, Serializable> params = new HashMap<String, Serializable>();
		params.put("url", shapeFile.toURI().toURL());
		params.put("create spatial index", Boolean.FALSE);

		ShapefileDataStoreFactory sfDataStoreFactory = new ShapefileDataStoreFactory();
		ShapefileDataStore sfDataStore = (ShapefileDataStore) sfDataStoreFactory.createNewDataStore(params);
		sfDataStore.createSchema(type);
		sfDataStore.forceSchemaCRS(layerService.getCrs(layer));

		String typeName = sfDataStore.getTypeNames()[0];
		SimpleFeatureSource sfSource = sfDataStore.getFeatureSource(typeName);
		if (sfSource instanceof SimpleFeatureStore) {
			SimpleFeatureStore sfs = (SimpleFeatureStore) sfSource;
			Transaction transaction = new DefaultTransaction("create");
			sfs.setTransaction(transaction);
			try {
				sfs.addFeatures(collection);
				transaction.commit();

			} catch (Exception e) {
				e.printStackTrace();
				transaction.rollback();
				throw e;

			} finally {
				transaction.close();
			}
		} else {
			throw new DeskmanagerException(DeskmanagerException.CANNOT_CREATE_SHAPEFILE_FEATURESTORE);
		}
	}

	@SuppressWarnings("deprecation")
	// see GBE-321
	private SimpleFeatureType createFeatureType(String layerName, String geometryName, String geometryType, int srid,
			List<PrimitiveAttributeInfo> atts) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(geometryName);
		sb.append(SUBDELIM);
		sb.append(geometryType);
		sb.append(SUBDELIM);
		sb.append("srid=");
		sb.append(srid);
		for (PrimitiveAttributeInfo pai : atts) {
			sb.append(DELIM);
			sb.append(pai.getLabel());
			sb.append(SUBDELIM);
			sb.append(toGeotoolsType(pai.getType()));
		}
		SimpleFeatureType sft = DataUtilities.createType(layerName, sb.toString());
		return sft;
	}

	@SuppressWarnings("deprecation")
	// see GBE-321
	private List<PrimitiveAttributeInfo> getPrimitiveAttributeInfo(VectorLayer layer) {
		List<PrimitiveAttributeInfo> res = new ArrayList<PrimitiveAttributeInfo>();

		Collection<AbstractAttributeInfo> ais = layer.getLayerInfo().getFeatureInfo().getAttributesMap().values();
		for (AbstractAttributeInfo ai : ais) {
			if (ai instanceof PrimitiveAttributeInfo && !((PrimitiveAttributeInfo) ai).isHidden()) {
				res.add((PrimitiveAttributeInfo) ai);
			}
		}
		return res;
	}

	private String toGeotoolsType(PrimitiveType type) {
		switch (type) {
			case CURRENCY:
			case DOUBLE:
				return "Double";
			case FLOAT:
				return "Float";
			case STRING:
			case URL:
			case IMGURL:
				return "String";
			case INTEGER:
			case LONG:
			case SHORT:
				return "Integer";
			case BOOLEAN:
				return "Boolean";
			case DATE:
				return "Date";

			default:
				log.warn("Unsupported Shapefile Type: " + type.toString());
				return "String";
		}
	}
}
