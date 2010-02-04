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
package org.geomajas.layer.geotools;

import java.io.IOException;
import java.util.Map;

import org.geomajas.layer.geotools.postgis.NonTypedPostgisFidMapperFactory;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.jdbc.JDBC1DataStore;
import org.geotools.data.jdbc.JDBCDataStore;
import org.geotools.data.postgis.PostgisDataStore;
import org.springframework.util.ResourceUtils;

/**
 * A simple utility factory for injecting a {@link DataStore} via Spring.
 * 
 * @author Jan De Moerloose
 * 
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
	 * @param parameters
	 *            list of Geotools parameters.
	 * @return
	 * @throws IOException
	 */
	public static DataStore create(Map<String, String> parameters) throws IOException {
		String url = parameters.get("url");
		if (url != null) {
			parameters.put("url", ResourceUtils.getURL(url).toExternalForm());
		}
		DataStore store = DataStoreFinder.getDataStore(parameters);
		if (store instanceof PostgisDataStore) {
			PostgisDataStore jdbcStore = (PostgisDataStore) store;
			jdbcStore.setFIDMapperFactory(new NonTypedPostgisFidMapperFactory(false));
		} else if (store instanceof JDBCDataStore) {
			JDBCDataStore jdbcStore = (JDBCDataStore) store;
			jdbcStore.setFIDMapperFactory(new NonTypedFidMapperFactory());
		}
		if (store instanceof JDBC1DataStore) {
			store = new ExtendedDataStore((JDBC1DataStore) store);
		}
		return store;
	}
}
