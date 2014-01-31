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
package org.geomajas.rest.server;

import org.apache.commons.io.IOUtils;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureStore;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Parses and produces a shapefile.
 * 
 * @author Oliver May
 *
 */
public class ShpParser {

	public Object read(Object input) throws IOException {
		return null;
	}

	public void write(SimpleFeatureCollection obj, OutputStream output, SimpleFeatureType sft,
			String layerId) throws IOException {
		File shpFile = File.createTempFile("shpFile", ".shp");

		ShapefileDataStoreFactory fact = new ShapefileDataStoreFactory();

		Map<String, Serializable> params = new HashMap<String, Serializable>();
		params.put(ShapefileDataStoreFactory.URLP.key, shpFile.toURI().toURL());
		params.put(ShapefileDataStoreFactory.CREATE_SPATIAL_INDEX.key, Boolean.TRUE);

		ShapefileDataStore shpDataStore = (ShapefileDataStore) fact.createNewDataStore(params);

		shpDataStore.createSchema(sft);

		SimpleFeatureStore store = (SimpleFeatureStore) shpDataStore.getFeatureSource(shpDataStore.getTypeNames()[0]);

		Transaction transaction = new DefaultTransaction("create");
		store.setTransaction(transaction);
		store.addFeatures(obj);
		transaction.commit();

		ZipOutputStream os = new ZipOutputStream(output);

		final String fileName = shpFile.getName().substring(0, shpFile.getName().lastIndexOf("."));
		File[] shpFiles = new File(shpFile.getParent()).listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.contains(fileName);
			}
		});

		for (File file : shpFiles) {
			os.putNextEntry(new ZipEntry(layerId + file.getName().substring(file.getName().lastIndexOf("."))));
			IOUtils.copy(new FileInputStream(file), os);
			file.delete();
		}
		os.close();
		output.flush();
	}

}
