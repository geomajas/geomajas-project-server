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

package org.geomajas.widget.searchandfilter.gwt.example.client.pages;

import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.searchandfilter.client.widget.multifeaturelistgrid.MultiFeatureListGrid;

/**
 * <p>
 * This class represents a tab that displays a {@link MultiFeatureListGrid}
 * widget together with 2 buttons to fill the grid.
 * </p>
 *
 * @author geomajas-gwt-archetype
 */
public class MultiFeatureListGridPage extends AbstractTab {

	private MultiFeatureListGrid table;

	public MultiFeatureListGridPage(MapWidget map) {
		super("FeatureListGrid", map);

		// Create the FeatureGrid that shows alpha-numerical attributes of
		// features:
		table = new MultiFeatureListGrid(map);
		table.setClearTabsetOnSearch(false);
		table.setShowDetailsOnSingleResult(true);
		mainLayout.addMember(table);
	}

	public void initialize() {
	}

	public MultiFeatureListGrid getTable() {
		return table;
	}
}
