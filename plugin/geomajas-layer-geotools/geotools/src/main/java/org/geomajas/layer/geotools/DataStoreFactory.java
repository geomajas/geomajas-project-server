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
package org.geomajas.layer.geotools;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.geomajas.annotation.Api;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.jdbc.JDBCDataStore;
import org.geotools.data.jdbc.fidmapper.DefaultFIDMapperFactory;
import org.geotools.data.jdbc.fidmapper.FIDMapperFactory;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

/**
 * A simple utility factory which creates a GeoTools data store from a parameter map.
 * 
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api
public final class DataStoreFactory {

	/**
	 * Boolean parameter that indicates that so-called typed feature ids should be used (feature id prefixed with
	 * typename). Defaults to "true". Add this parameter with value "false" to the parameter map to let first generation
	 * JDBC datastores generate non-typed fids.
	 * @since 1.10.0
	 */
	@Api
	public static final String USE_TYPED_FIDS = "useTypedFids";
	
	private static final Map<Map<String, Object>, DataStore> DATASTORE_CACHE = 
			new ConcurrentHashMap<Map<String, Object>, DataStore>();

	/**
	 * Protect construction.
	 */
	private DataStoreFactory() {
	}

	/**
	 * Creates a suitable {@link DataStore} for the specified parameters.
	 * 
	 * @param parameters list of GeoTools parameters.
	 * @return data store, never null
	 * @throws IOException could not create data store
	 */
	public static DataStore create(Map<String, Object> parameters) throws IOException {
		Object url = parameters.get(ShapefileDataStoreFactory.URLP.key);
		Logger log = LoggerFactory.getLogger(DataStoreFactory.class);
		if (url instanceof String) {
			parameters.put(ShapefileDataStoreFactory.URLP.key, ResourceUtils.getURL((String) url).toExternalForm());
		}
		if (DATASTORE_CACHE.containsKey(parameters)) {
			return DATASTORE_CACHE.get(parameters);
		}
		DataStore store = DataStoreFinder.getDataStore(parameters);
		Object typed = parameters.get(USE_TYPED_FIDS);
		if (typed instanceof String) {
			Boolean t = Boolean.valueOf((String) typed);
			if (store instanceof JDBCDataStore) {
				JDBCDataStore jdbcStore = (JDBCDataStore) store;
				FIDMapperFactory fidMapperFactory = jdbcStore.getFIDMapperFactory();
				if (fidMapperFactory instanceof DefaultFIDMapperFactory) {
					((DefaultFIDMapperFactory) fidMapperFactory).setReturningTypedFIDMapper(t);
				}
			} else if (!t) {
				if (store != null) {
					log.warn("Non-typed FIDs are only supported by first-generation JDBC datastores, "
							+ "using default fid format for datastore class " + store.getClass().getName());
				}
			}
		}
		if (null == store) {
			StringBuilder availableStr = new StringBuilder();
			StringBuilder missingStr = new StringBuilder();
			Iterator<DataStoreFactorySpi> all = DataStoreFinder.getAllDataStores();
			while (all.hasNext()) {
				DataStoreFactorySpi factory = all.next();
				if (!factory.isAvailable()) {
					log.warn("Datastore factory " + factory.getDisplayName() + "(" + factory.getDescription()
							+ ") is not available");
					if (missingStr.length() != 0) {
						missingStr.append(",");
					}
					missingStr.append(factory.getDisplayName());
				} else {
					if (availableStr.length() != 0) {
						availableStr.append(",");
					}
					availableStr.append(factory.getDisplayName());
				}
			}
			throw new IOException(
					"No datastore found. Possible causes are missing factory or missing library for your datastore"
							+ " (e.g. database driver).\nCheck the isAvailable() method of your"
							+ " DataStoreFactory class to find out which libraries are needed.\n"
							+ "Unavailable factories : " + missingStr + "\n" + "Available factories : " + availableStr
							+ "\n");
		}
		DATASTORE_CACHE.put(parameters, store);
		return store;
	}

}
