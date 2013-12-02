package org.geomajas.layer.geotools;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.geotools.data.AbstractDataStoreFactory;
import org.geotools.data.DataStore;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.opengis.feature.type.Name;

public class DummyJdbcFactory extends AbstractDataStoreFactory implements org.geotools.data.DataStoreFactorySpi {

	public class DummyJdbcDataStore extends ContentDataStore {
		protected List<Name> createTypeNames() throws IOException {
			return null;
		}
		protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
			return null;
		}
	}

	public String getDescription() {
		return "DummyJdbcFactory";
	}

	public Param[] getParametersInfo() {
		return new Param[] { new Param("testScope", Boolean.class, "Set to true for unit testing", true) };
	}

	public boolean canProcess(Map params) {
		if (!super.canProcess(params)) {
			return false; // was not in agreement with getParametersInfo
		}
		if (!(((String) params.get("testScope")).equalsIgnoreCase("true"))) {
			return (false);
		} else {
			return (true);
		}
	}

	public DataStore createDataStore(Map<String, Serializable> params) throws IOException {
		return new DummyJdbcDataStore();
	}

	public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException {
		return new DummyJdbcDataStore();
	}

}
