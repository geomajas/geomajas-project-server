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
package org.geomajas.layer.hibernate;

import org.hibernate.dialect.HSQLDialect;
import org.hibernate.usertype.UserType;
import org.hibernatespatial.SpatialDialect;

/**
 * HSQL implementation of spatial dialect. Has only one capability : geometry
 * user type.
 * 
 * @author Jan De Moerloose
 */
public class HSQLSpatialDialect extends HSQLDialect implements SpatialDialect {

	public String getDbGeometryTypeName() {
		return "VARCHAR";
	}

	public UserType getGeometryUserType() {
		return new HSQLGeometryUserType();
	}

	public String getSpatialAggregateSQL(String columnName, int aggregation) {
		throw new IllegalArgumentException(
				"Spatial aggregation is not known by this dialect");
	}

	public String getSpatialFilterExpression(String columnName) {
		throw new IllegalArgumentException(
				"Spatial filtering is not known by this dialect");
	}

	public String getSpatialRelateSQL(String columnName, int spatialRelation,
			boolean hasFilter) {
		throw new IllegalArgumentException(
				"Spatial relation is not known by this dialect");
	}

	public boolean isTwoPhaseFiltering() {
		return false;
	}

}
