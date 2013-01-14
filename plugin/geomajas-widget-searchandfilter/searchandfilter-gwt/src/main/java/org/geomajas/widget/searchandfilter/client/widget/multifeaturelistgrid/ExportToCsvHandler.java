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
package org.geomajas.widget.searchandfilter.client.widget.multifeaturelistgrid;

import org.geomajas.gwt.client.map.layer.VectorLayer;

/**
 * <p>
 * When the ExportToCsv Button is clicked in the {@link MultiFeatureListGrid} this
 * handler will be called, with the currently active layer.
 * </p>
 * <p>
 * It is up to the handler to do the actual export as the {@link MultiFeatureListGrid}
 * has no knowledge of the filters or even commands that were used to retrieve
 * the features.
 * </p>
 *
 * @author Kristof Heirwegh
 */
public interface ExportToCsvHandler {

	/**
	 * Do export to CSV.
	 *
	 * @param layer layer to export
	 */
	void execute(VectorLayer layer);

	/**
	 * Do export to CSV.
	 *
	 * @param layer layer to export
	 */
	void execute(VectorLayer layer, Runnable onFinished);

}
