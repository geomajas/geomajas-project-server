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
package org.geomajas.widget.searchandfilter.client.widget.multifeaturelistgrid;

import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.widget.searchandfilter.client.util.Callback;

/**
 * <p>
 * When the ExportToCsv Button is clicked in the MultiFeatureListGrid this
 * handler will be called, with the currently active layer.
 * </p>
 * <p>
 * It is up to the handler to do the actual export as the MultiFeatureListGrid
 * has no knowledge of the filters or even commands that were used to retrieve
 * the features.
 * </p>
 *
 * @author Kristof Heirwegh
 */
public interface ExportToCsvHandler {

	void execute(VectorLayer layer);
	void execute(VectorLayer layer, Callback onFinished);

}
