/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.geotools;

import com.vividsolutions.jts.geom.Envelope;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureWriter;
import org.geotools.data.LockingManager;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.DefaultSQLBuilder;
import org.geotools.data.jdbc.JDBC1DataStore;
import org.geotools.data.jdbc.JDBCDataStoreConfig;
import org.geotools.data.jdbc.QueryData;
import org.geotools.data.jdbc.SQLBuilder;
import org.geotools.data.jdbc.attributeio.AttributeIO;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.data.jdbc.fidmapper.FIDMapperFactory;
import org.geotools.feature.SchemaException;
import org.geotools.filter.SQLEncoderException;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;

import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Extended data store for GeoTools.
 *
 * @author Jan De Moerloose
 * @author Pieter De Graef
 */
public class ExtendedDataStore extends JDBC1DataStore implements DataStore {

	private JDBC1DataStore delegate;

	public ExtendedDataStore(JDBC1DataStore delegate) throws IOException {
		super(new JDBCDataStoreConfig(delegate.getNameSpace().toString(), "public", Long.MAX_VALUE));
		this.delegate = delegate;
	}

	public Map<String, Collection<String>> getRelatedFeatures(int spatialRelation, String table1, String table2,
			Filter filter1, Filter filter2) throws IOException {
		StringBuffer sqlBuffer = new StringBuffer();
		try {
			DefaultSQLBuilder builder = (DefaultSQLBuilder) getSqlBuilder(table1);
			// no way we can get at 2 tables ????
			builder.sqlWhere(sqlBuffer, (org.geotools.filter.Filter) filter1);
			return Collections.EMPTY_MAP;
		} catch (SQLEncoderException e) {
			throw new IOException("Could not encode sql for relationship query : " + e.getMessage());
		}
	}

	public boolean allSameOrder(String[] requestedNames, SimpleFeatureType ft) {
		return delegate.allSameOrder(requestedNames, ft);
	}

	public void createSchema(SimpleFeatureType featureType) throws IOException {
		delegate.createSchema(featureType);
	}

	public boolean equals(Object arg0) {
		return delegate.equals(arg0);
	}

	public Envelope getEnvelope(String typeName) {
		return delegate.getEnvelope(typeName);
	}

	public FeatureReader getFeatureReader(SimpleFeatureType requestType, org.geotools.filter.Filter filter,
			Transaction transaction) throws IOException {
		return delegate.getFeatureReader(requestType, filter, transaction);
	}

	public FeatureReader getFeatureReader(Query query, Transaction trans) throws IOException {
		return delegate.getFeatureReader(query, trans);
	}

	public FeatureSource getFeatureSource(String typeName) throws IOException {
		return delegate.getFeatureSource(typeName);
	}

	public FeatureWriter getFeatureWriter(String typeName, org.geotools.filter.Filter filter, Transaction transaction)
			throws IOException {
		return delegate.getFeatureWriter(typeName, filter, transaction);
	}

	public FeatureWriter getFeatureWriter(String typeName, Transaction transaction) throws IOException {
		return delegate.getFeatureWriter(typeName, transaction);
	}

	public FeatureWriter getFeatureWriterAppend(String typeName, Transaction transaction) throws IOException {
		return delegate.getFeatureWriterAppend(typeName, transaction);
	}

	public FIDMapper getFIDMapper(String tableName) throws IOException {
		return delegate.getFIDMapper(tableName);
	}

	public FIDMapperFactory getFIDMapperFactory() {
		return delegate.getFIDMapperFactory();
	}

	public LockingManager getLockingManager() {
		return delegate.getLockingManager();
	}

	public URI getNameSpace() {
		return delegate.getNameSpace();
	}

	public SimpleFeatureType getSchema(String typeName) throws IOException {
		return delegate.getSchema(typeName);
	}

	public SQLBuilder getSqlBuilder(String typeName) throws IOException {
		return delegate.getSqlBuilder(typeName);
	}

	public String getSqlNameEscape() {
		return delegate.getSqlNameEscape();
	}

	public int getTransactionIsolation() {
		return delegate.getTransactionIsolation();
	}

	public String[] getTypeNames() throws IOException {
		return delegate.getTypeNames();
	}

	public FeatureSource getView(Query query) throws IOException, SchemaException {
		return delegate.getView(query);
	}

	public int hashCode() {
		return delegate.hashCode();
	}

	public void setFIDMapper(String featureTypeName, FIDMapper fidMapper) {
		delegate.setFIDMapper(featureTypeName, fidMapper);
	}

	public void setFIDMapperFactory(FIDMapperFactory fmFactory) throws UnsupportedOperationException {
		delegate.setFIDMapperFactory(fmFactory);
	}

	public void setTransactionIsolation(int value) {
		delegate.setTransactionIsolation(value);
	}

	public String toString() {
		return delegate.toString();
	}

	public void updateSchema(String typeName, SimpleFeatureType featureType) throws IOException {
		delegate.updateSchema(typeName, featureType);
	}

	@Override
	protected Connection createConnection() throws SQLException {
		throw new SQLException("Not allowed to create a connection");
	}

	@Override
	protected AttributeIO getGeometryAttributeIO(AttributeDescriptor descriptor, QueryData queryData)
			throws IOException {
		throw new IOException("Not allowed to get a geometry IO attribute");
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

}
