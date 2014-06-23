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
package org.geomajas.plugin.deskmanager.service.manager;

import com.vividsolutions.jts.geom.Geometry;
import org.geomajas.configuration.AbstractAttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.geotools.DataStoreFactory;
import org.geomajas.plugin.deskmanager.DeskmanagerException;
import org.geomajas.service.GeoService;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.Transaction;
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
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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

	@Autowired(required = false)
	@Qualifier("dataSourceDbType")
	private String dataSourceDbType = "postgis";

	@Autowired(required = false)
	@Qualifier("dataSourceNamespace")
	private String dataSourceNamespace = "postgis";

	@Resource(name = "dataSource")
	private DataSource dataSource;

	@Autowired
	private GeoService geoService;

	@Resource(name = "dynamicLayersApplication")
	private ClientApplicationInfo defaultGeodesk;

	public boolean importShapeFile(String shpFileName, String layerName) {
		log.info("Importing Shapefile using Geotools: " + shpFileName);
		Transaction tr = new DefaultTransaction("transaction");
		ShapefileDataStore sourceStore = null;
		try {
			DataStore dataStore = createDataStore();

			//Read shapefile
			File shpFile = new File(shpFileName);
			sourceStore = (ShapefileDataStore) FileDataStoreFinder.getDataStore(shpFile);
			SimpleFeatureSource featureSource = sourceStore.getFeatureSource(
					sourceStore.getTypeNames()[0]);

			//Find math transform
			CoordinateReferenceSystem sourceCrs = featureSource.getSchema().getCoordinateReferenceSystem();
			CoordinateReferenceSystem targetCrs = geoService.getCrs2(defaultGeodesk.getMaps().get(0).getCrs());
			boolean lenient = true; // allow for some error due to different datums
			MathTransform transform = CRS.findMathTransform(sourceCrs, targetCrs, lenient);


			//Create schema if it does not exist.
			if (!Arrays.asList(dataStore.getTypeNames()).contains(layerName)) {
				// Build the feature type for the database
				SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
				builder.init(featureSource.getSchema());
				builder.setName(layerName);
				dataStore.createSchema(builder.buildFeatureType());
			}

			//Create reader and writer
			FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dataStore.getFeatureWriterAppend(layerName, tr);
			SimpleFeatureIterator reader = featureSource.getFeatures().features();

			try {
				//Copy all features, and convert geometries.
				while (reader.hasNext()) {
					SimpleFeature original = reader.next();
					SimpleFeature copy = writer.next();


					copy.setAttributes(convertAttributes(original.getAttributes(), sourceStore.getStringCharset()));
					Geometry geometry2 = JTS.transform((Geometry) original.getDefaultGeometry(), transform);
					copy.setDefaultGeometry(geometry2);
					writer.write();
				}
				tr.commit();
			} catch (Exception e) {
				log.warn("Failed adding features from ShapeFile to database", e);
				tr.rollback();
				return false;
			} finally {
				writer.close();
				reader.close();
				tr.close();
			}
		} catch (Exception e) {
			log.warn("Failed adding features from ShapeFile to database", e);
			return false;
		} finally {
			try {
				if (sourceStore != null) {
					sourceStore.dispose();
				}
			} catch (Exception e2) { // ignore
			}
		}
		return true;
	}

	private DataStore createDataStore() throws IOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(JDBCDataStoreFactory.DATASOURCE.key, dataSource);
		// these are apparently required but not used
		params.put(JDBCDataStoreFactory.DATABASE.key, "some_database");
		params.put(JDBCDataStoreFactory.USER.key, "some_user");
		params.put(JDBCDataStoreFactory.PASSWD.key, "some_password");
		params.put(JDBCDataStoreFactory.HOST.key, "some host");
		params.put(JDBCDataStoreFactory.PORT.key, "0");
		params.put(JDBCDataStoreFactory.NAMESPACE.key, dataSourceNamespace);
		params.put(JDBCDataStoreFactory.DBTYPE.key, dataSourceDbType);

		return DataStoreFactory.create(params);
	}

	private List<Object> convertAttributes(List<Object> attributes, Charset stringCharset)
			throws UnsupportedEncodingException {
		List<Object> convertedAttributes = new ArrayList<Object>(attributes.size());
		for (Object attribute : attributes) {
			if (attribute instanceof String) {
				convertedAttributes.add(attribute.toString().replaceAll("\\\\", "\\\\\\\\"));
			} else {
				convertedAttributes.add(attribute);
			}
		}
		return convertedAttributes;
	}

	@SuppressWarnings({"rawtypes", "deprecation" })
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

	@Override
	public String getShpFileName(String dir) {
		File fDir = new File(dir);
		File[] filesInDir = fDir.listFiles();
		for (File file : filesInDir) {
			if (file.getName().toLowerCase().endsWith(".shp")) {
				return file.getAbsolutePath();
			}
		}
		return null;
	}

	@Override
	public boolean unzip(String zipFileName, String tmpDir) {
		Enumeration<?> entries;
		ZipFile zipFile = null;

		try {
			zipFile = new ZipFile(zipFileName);
			entries = zipFile.entries();

			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				copyInputStream(zipFile.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(tmpDir
						+ "/" + entry.getName())));
			}

			zipFile.close();
		} catch (IOException e) {
			log.warn("Failed unzipping shapefile archive.", e);
			return false;
		} finally {
			try {
				if (zipFile != null) {
					zipFile.close();
				}
			} catch (Exception e) { // ignore
			}
		}
		return true;
	}

	@Override
	public List<Geometry> extractGeometriesFromShapeFile(String parentDir, String toCrs) throws Exception {
		// check if there is a shp available
		String shpFileName = getShpFileName(parentDir);
		List<Geometry> geometries = new ArrayList<Geometry>();
		if (shpFileName == null) {
			throw new Exception("No shapeFile available in directory " + parentDir);
		}
		return fromShpToGeometries(shpFileName, toCrs);
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

	private List<Geometry> fromShpToGeometries(String shpFileName, String toCrs) {
		log.info("Extracting Geometries from Shapefile using Geotools: " + shpFileName);
		ShapefileDataStore sourceStore = null;
		try {
			//Read shapefile
			File shpFile = new File(shpFileName);
			sourceStore = (ShapefileDataStore) FileDataStoreFinder.getDataStore(shpFile);
			SimpleFeatureSource featureSource = sourceStore.getFeatureSource(
					sourceStore.getTypeNames()[0]);

			//Find math transform
			CoordinateReferenceSystem sourceCrs = featureSource.getSchema().getCoordinateReferenceSystem();
			CoordinateReferenceSystem targetCrs = geoService.getCrs2(toCrs);
			boolean lenient = true; // allow for some error due to different datums
			MathTransform transform = CRS.findMathTransform(sourceCrs, targetCrs, lenient);

			SimpleFeatureIterator reader = featureSource.getFeatures().features();

			List<Geometry> geometries = new ArrayList<Geometry>();
			try {
				while (reader.hasNext()) {
					SimpleFeature original = reader.next();
					Geometry geometry2 = JTS.transform((Geometry) original.getDefaultGeometry(), transform);
					geometries.add(geometry2);
				}
			} catch (Exception e) {
				log.warn("Failed reading features from ShapeFile", e);
			} finally {
				//writer.close();
				reader.close();
			}
			return geometries;
		} catch (Exception e) {
			log.warn("Failed collecting geometries from ShapeFile", e);

		} finally {
			try {
				if (sourceStore != null) {
					sourceStore.dispose();
				}
			} catch (Exception e2) { // ignore
			}
		}
		return null;
	}

	/**
	 * Copys an inputstream to an outputstream
	 *
	 * @param in The inputstream to read from
	 * @param out The outputstream to write to
	 * @throws IOException
	 */
	private static void copyInputStream(InputStream in, OutputStream out) throws IOException {
		try {
			byte[] buffer = new byte[1024];
			int len;

			while ((len = in.read(buffer)) >= 0) {
				out.write(buffer, 0, len);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			// caveat: the method that opens the stream should also close it
			in.close();
			out.close();
		}
	}
}
