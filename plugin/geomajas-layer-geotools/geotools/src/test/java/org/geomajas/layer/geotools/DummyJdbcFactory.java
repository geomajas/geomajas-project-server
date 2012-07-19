package org.geomajas.layer.geotools;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.sql.DataSource;

import org.geotools.data.AbstractDataStoreFactory;
import org.geotools.data.DataStore;
import org.geotools.data.jdbc.JDBCDataStore;
import org.geotools.data.jdbc.JDBCDataStoreConfig;
import org.geotools.data.jdbc.QueryData;
import org.geotools.data.jdbc.attributeio.AttributeIO;
import org.opengis.feature.type.AttributeDescriptor;

public class DummyJdbcFactory extends AbstractDataStoreFactory implements org.geotools.data.DataStoreFactorySpi {

	public class DummyJdbcDataStore extends JDBCDataStore {

		protected DummyJdbcDataStore(DataSource dataSource, JDBCDataStoreConfig config) throws IOException {
			super(dataSource, config);
		}

		@Override
		protected AttributeIO getGeometryAttributeIO(AttributeDescriptor type, QueryData queryData) throws IOException {
			return null;
		}

	}

	@Override
	public String getDescription() {
		return "DummyJdbcFactory";
	}

	@Override
	public Param[] getParametersInfo() {
		return new Param[] { new Param("testScope", Boolean.class, "Set to true for unit testing", true) };
	}

	@Override
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

	@Override
	public DataStore createDataStore(Map<String, Serializable> params) throws IOException {
		return new DummyJdbcDataStore(null, new JDBCDataStoreConfig());
	}

	@Override
	public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException {
		return new DummyJdbcDataStore(null, new JDBCDataStoreConfig());
	}

}
