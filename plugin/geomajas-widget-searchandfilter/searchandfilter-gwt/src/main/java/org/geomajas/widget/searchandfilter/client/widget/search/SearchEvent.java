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
package org.geomajas.widget.searchandfilter.client.widget.search;

import java.util.List;
import java.util.Map;

import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.widget.searchandfilter.search.dto.Criterion;

/**
 * Search event.
 *
 * @see SearchWidgetRegistry
 * @see org.geomajas.gwt.client.widget.event.SearchEvent
 * @author Kristof Heirwegh
 */
public class SearchEvent {

	/**
	 * Criterion used/to use for the search
	 */
	private Criterion criterion;

	private Map<VectorLayer, List<Feature>> result;

	private Boolean singleResult;

	// ----------------------------------------------------------

	public SearchEvent() {
	}

	public Map<VectorLayer, List<Feature>> getResult() {
		return result;
	}

	/**
	 * Returns true if the search resulted in a single result in a single layer.
	 * false in all other cases. Also returns false when search has not yet been
	 * run.
	 * 
	 * @return
	 */
	public boolean isSingleResult() {
		if (result == null) {
			return false;
		} else {
			if (singleResult == null) {
				if (result.size() == 1 && result.values().iterator().next().size() == 1) {
					singleResult = true;
				} else {
					singleResult = false;
				}
			}
			return singleResult;
		}
	}

	public Criterion getCriterion() {
		return criterion;
	}

	public void setCriterion(Criterion criterion) {
		this.criterion = criterion;
	}

	public void setResult(Map<VectorLayer, List<Feature>> result) {
		this.result = result;
	}
}
