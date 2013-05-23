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
package org.geomajas.widget.searchandfilter.search.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.geometry.Geometry;

/**
 * Criterion with attribute criteria.
 *
 * @author Kristof Heirwegh
 */
public class GeometryCriterion implements Criterion {

	private static final long serialVersionUID = 100L;

	// TODO change to enums
	public static final int INTERSECTS = SearchByLocationRequest.QUERY_INTERSECTS;
	public static final int TOUCHES = SearchByLocationRequest.QUERY_TOUCHES;
	public static final int WITHIN = SearchByLocationRequest.QUERY_WITHIN;
	public static final int CONTAINS = SearchByLocationRequest.QUERY_CONTAINS;

	private List<String> serverLayerIds;

	private Geometry geometry;

	private int operator = INTERSECTS;

	public GeometryCriterion() {
		// no-args constructor as needed for GWT
	}

	/**
	 * Constructor using INTERSECTS as operator.
	 *
	 * @param serverLayerIds layer ids to search in
	 * @param geometry geometry to use for search
	 */
	public GeometryCriterion(List<String> serverLayerIds, Geometry geometry) {
		this.serverLayerIds = serverLayerIds;
		this.geometry = geometry;
	}

	/**
	 * Constructor using INTERSECTS as operator.
	 *
	 * @param serverLayerIds layer ids to search in
	 * @param geometry geometry to use for search
	 * @param operator operator to use for search
	 */
	public GeometryCriterion(List<String> serverLayerIds, Geometry geometry, int operator) {
		this.serverLayerIds = serverLayerIds;
		this.geometry = geometry;
		this.operator = operator;
	}

	// ----------------------------------------------------------

	public List<String> getServerLayerIds() {
		if (serverLayerIds == null) {
			serverLayerIds = new ArrayList<String>();
		}
		return serverLayerIds;
	}

	public void setServerLayerIds(List<String> serverLayerIds) {
		this.serverLayerIds = serverLayerIds;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public int getOperator() {
		return operator;
	}

	public void setOperator(int operator) {
		this.operator = operator;
	}

	@Override
	public boolean isValid() {
		return (serverLayerIds != null && serverLayerIds.size() > 0 && geometry != null && operator > 0 && operator < 5)
		;
	}

	@Override
	public void serverLayerIdVisitor(Set<String> layerIds) {
		layerIds.addAll(serverLayerIds);
	}

	@Override
	public List<Criterion> getCriteria() {
		return new ArrayList<Criterion>();
	}

	@Override
	public String getDisplayText() {
		StringBuilder sb = new StringBuilder();
		switch (operator) {
			case INTERSECTS:
				sb.append("INTERSECTS");
				break;
			case TOUCHES:
				sb.append("TOUCHES");
				break;
			case WITHIN:
				sb.append("WITHIN");
				break;
			case CONTAINS:
				sb.append("CONTAINS");
				break;
			default:
				sb.append("??");
				break;
		}
		sb.append(" ");
		sb.append(geometry.getGeometryType());
		return sb.toString();
	}

	@Override
	public String toString() {
		return getDisplayText();
	}

}
