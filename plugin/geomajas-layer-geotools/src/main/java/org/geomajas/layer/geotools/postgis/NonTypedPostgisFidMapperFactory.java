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
package org.geomajas.layer.geotools.postgis;

import org.geotools.data.jdbc.fidmapper.BasicFIDMapper;
import org.geotools.data.jdbc.fidmapper.DefaultFIDMapperFactory;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.data.jdbc.fidmapper.MaxIncFIDMapper;
import org.geotools.data.postgis.fidmapper.PostGISAutoIncrementFIDMapper;

import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Level;

/**
 * ???
 *
 * @author check subversion
 */
public class NonTypedPostgisFidMapperFactory extends DefaultFIDMapperFactory {

	public NonTypedPostgisFidMapperFactory() {
		super();
	}

	public NonTypedPostgisFidMapperFactory(boolean returnFIDColumnsAsAttributes) {
		super(returnFIDColumnsAsAttributes);
	}

	/**
	 * Get the appropriate FIDMapper for the specified table. Overridden to
	 * return a non-typed mapper !!!!
	 *
	 * @param catalog
	 * @param schema
	 * @param tableName
	 * @param connection
	 *            the active database connection to get table key information
	 *
	 * @return the appropriate FIDMapper for the specified table.
	 *
	 * @throws IOException
	 *             if any error occurs.
	 */
	public FIDMapper getMapper(String catalog, String schema, String tableName, Connection connection)
			throws IOException {
		ColumnInfo[] colInfos = getPkColumnInfo(catalog, schema, tableName, connection);
		FIDMapper mapper = null;

		if (colInfos.length == 0) {
			mapper = buildNoPKMapper(schema, tableName, connection);
		} else if (colInfos.length > 1) {
			mapper = buildMultiColumnFIDMapper(schema, tableName, connection, colInfos);
		} else {
			ColumnInfo ci = colInfos[0];

			mapper = buildSingleColumnFidMapper(schema, tableName, connection, ci);
		}

		if (mapper == null) {
			mapper = buildLastResortFidMapper(schema, tableName, connection, colInfos);

			if (mapper == null) {
				String msg = "Cannot map primary key to a FID mapper, primary key columns are:\n"
						+ getColumnInfoList(colInfos);
				LOGGER.log(Level.SEVERE, msg);
				throw new IOException(msg);
			}
		}

		return mapper;
	}

	/**
	 * Builds a FID mapper based on a single column primary key. Default version
	 * tries the auto-increment way, then a mapping on an
	 * {@link MaxIncFIDMapper} type for numeric columns, and a plain
	 * {@link BasicFIDMapper} of text based columns.
	 *
	 * @param schema
	 * @param tableName
	 * @param connection
	 *            an open database connection.
	 * @param ci
	 *            the column information for the FID column.
	 *
	 * @return the appropriate FIDMapper.
	 */
	protected FIDMapper buildSingleColumnFidMapper(String schema, String tableName, Connection connection,
			ColumnInfo ci) {
		if (ci.isAutoIncrement()) {
			return new PostGISAutoIncrementFIDMapper(tableName, ci.getColName(), ci.getDataType(), true);
		} else if (isIntegralType(ci.getDataType())) {
			return new MaxIncFIDMapper(schema, tableName, ci.getColName(), ci.getDataType(),
					this.returnFIDColumnsAsAttributes);
		} else {
			return new BasicFIDMapper(ci.getColName(), ci.getSize(), this.returnFIDColumnsAsAttributes);
		}
	}
}