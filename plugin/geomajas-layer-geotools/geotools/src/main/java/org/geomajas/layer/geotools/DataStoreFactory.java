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
package org.geomajas.layer.geotools;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

/**
 * A simple utility factory which creates a GeoTools data store from a parameter map.
 * 
 * @author Jan De Moerloose
 */
public final class DataStoreFactory {

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
		Object url = parameters.get("url");
		if (url instanceof String) {
			parameters.put("url", ResourceUtils.getURL((String) url).toExternalForm());
		}
		DataStore store = DataStoreFinder.getDataStore(parameters);
		if (null == store) {
			StringBuilder availableStr = new StringBuilder();
			StringBuilder missingStr = new StringBuilder();
			Iterator<DataStoreFactorySpi> all = DataStoreFinder.getAllDataStores();
			Logger log = LoggerFactory.getLogger(DataStoreFactory.class);
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
		return store;
	}
}
