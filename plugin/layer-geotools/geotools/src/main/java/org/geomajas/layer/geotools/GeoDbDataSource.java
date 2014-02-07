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
package org.geomajas.layer.geotools;

import geodb.GeoDB;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

/**
 * This class takes an existing h2 datasource and turns it into a {@link GeoDB} datasource which can be directly used by
 * {@link GeoToolsLayer}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GeoDbDataSource implements FactoryBean<DataSource> {

	private DataSource dataSource;

	private Resource script;

	public Resource getScript() {
		return script;
	}

	public void setScript(Resource script) {
		this.script = script;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}


	@PostConstruct
	void postConstruct() throws SQLException, IOException {
		Connection connection = dataSource.getConnection();
		GeoDB.InitGeoDB(connection);
		if (script != null) {
			String sql = IOUtils.toString(script.getInputStream());
			Statement statement = connection.createStatement();
			statement.execute(sql);
		}
		connection.close();
	}

	@Override
	public DataSource getObject() throws Exception {
		return dataSource;
	}

	@Override
	public Class<?> getObjectType() {
		return DataSource.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
