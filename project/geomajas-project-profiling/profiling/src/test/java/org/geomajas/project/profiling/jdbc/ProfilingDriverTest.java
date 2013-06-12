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

package org.geomajas.project.profiling.jdbc;

import org.geomajas.project.profiling.jmx.GroupData;
import org.geomajas.project.profiling.service.ProfilingContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Verify that profiling driver proxies and profiles.
 *
 * @author Joachim Van der Auwera
 */
public class ProfilingDriverTest {

	private Connection connection;
	private ProfilingContainer profilingContainer;

	@Before
	public void getConnection() throws Exception {
		Class.forName("org.geomajas.project.profiling.jdbc.ProfilingDriver");
		Class.forName("org.hsqldb.jdbcDriver");

		profilingContainer = new ProfilingContainer();
		profilingContainer.start();

		ProfilingDriver.addListener(new ProfilingListener() {
			@Override
			public void register(String group, long durationMillis) {
				profilingContainer.register(group, durationMillis);
			}
		});

		connection = DriverManager.getConnection("profiling:jdbc:hsqldb:mem:testdb", "sa", "");
	}

	@After
	public void closeConnection() throws Exception {
		sql("SHUTDOWN");
		connection.close();
	}

	private ResultSet sql(String sql) throws Exception {
		Statement st = connection.createStatement();
		st.execute(sql);
		return st.getResultSet();
	}


	@Test
	public void testProfilingDriver() throws Exception {
		// test registering connection
		Thread.sleep(100); // give profiling container time to summarize data
		List<GroupData> groupsData = profilingContainer.getGroupData();
		assertThat(groupsData).hasSize(1);
		assertThat(groupsData.get(0).getGroup()).isEqualTo("Driver.connect");
		assertThat(groupsData.get(0).getInvocationCount()).isEqualTo(1);
		profilingContainer.clear();


		// test profiling a statement

		sql("CREATE TABLE bla (\n" +
				"VERSION INTEGER,\n" +
				"NAME VARCHAR(255)\n" +
				");");
		sql("INSERT INTO bla (NAME, VERSION) values ('zzz', 8)");

		ResultSet resultSet = sql("SELECT * from bla");
		assertThat(resultSet.next()).isTrue();
		assertThat(resultSet.getString("NAME")).isEqualTo("zzz");
		assertThat(resultSet.getInt("VERSION")).isEqualTo(8);

		Thread.sleep(100); // give profiling container time to summarize data
		groupsData = profilingContainer.getGroupData();
		assertThat(groupsData).hasSize(3);
		assertThat(groupsData.toString()).contains(
				"[GroupContainer{group='Connection.createStatement', OneContainer{invocationCount=3");
		assertThat(groupsData.toString()).contains(
				"GroupContainer{group='Statement.execute', OneContainer{invocationCount=3");
		assertThat(groupsData.toString()).contains(
				"GroupContainer{group='Statement.getResultSet', OneContainer{invocationCount=3");
		profilingContainer.clear();


		// test profiling a prepared statement

		PreparedStatement ps = connection.prepareStatement("SELECT NAME, VERSION as V from bla");
		ps.execute();
		ps.execute();
		resultSet = ps.getResultSet();
		assertThat(resultSet.next()).isTrue();
		assertThat(resultSet.getString("NAME")).isEqualTo("zzz");
		assertThat(resultSet.getInt("V")).isEqualTo(8);

		Thread.sleep(100); // give profiling container time to summarize data
		groupsData = profilingContainer.getGroupData();
		assertThat(groupsData).hasSize(3);
		assertThat(groupsData.toString()).contains(
				"[GroupContainer{group='Connection.prepareStatement', OneContainer{invocationCount=1");
		assertThat(groupsData.toString()).contains(
				"GroupContainer{group='PreparedStatement.execute', OneContainer{invocationCount=2");
		assertThat(groupsData.toString()).contains(
				"GroupContainer{group='PreparedStatement.getResultSet', OneContainer{invocationCount=1");
		profilingContainer.clear();
	}

}
