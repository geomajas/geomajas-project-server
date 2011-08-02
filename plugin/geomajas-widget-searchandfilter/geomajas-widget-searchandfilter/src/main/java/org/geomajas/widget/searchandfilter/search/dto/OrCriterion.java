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
package org.geomajas.widget.searchandfilter.search.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Logic Criterion grouping criteria by OR.
 * 
 * @author Kristof Heirwegh
 */
public class OrCriterion implements Criterion {

	private static final long serialVersionUID = 100L;

	private List<Criterion> criteria;

	// ----------------------------------------------------------

	public List<Criterion> getCriteria() {
		if (criteria == null) {
			criteria = new ArrayList<Criterion>();
		}
		return criteria;
	}

	public void setCriteria(List<Criterion> criteria) {
		this.criteria = criteria;
	}

	public boolean isValid() {
		if (criteria != null  && criteria.size() > 0) {
			for (Criterion critter : criteria) {
				if (!critter.isValid()) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public void serverLayerIdVisitor(Set<String> layerIds) {
		for (Criterion critter : criteria) {
			critter.serverLayerIdVisitor(layerIds);
		}
	}
}
