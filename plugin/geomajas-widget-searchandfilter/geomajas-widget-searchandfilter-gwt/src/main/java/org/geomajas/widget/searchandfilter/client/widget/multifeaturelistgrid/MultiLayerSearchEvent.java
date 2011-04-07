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

import java.util.List;
import java.util.Map;

import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.VectorLayer;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @see SearchEvent.
 * @author Kristof Heirwegh
 */
public class MultiLayerSearchEvent extends GwtEvent<MultiLayerSearchHandler> {

	public static final Type<MultiLayerSearchHandler> TYPE = new Type<MultiLayerSearchHandler>();

	private Map<VectorLayer, List<Feature>> result;

	public MultiLayerSearchEvent(Map<VectorLayer, List<Feature>> result) {
		this.result = result;
	}

	public Map<VectorLayer, List<Feature>> getResult() {
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Type getAssociatedType() {
		return TYPE;
	}

	protected void dispatch(MultiLayerSearchHandler searchHandler) {
		searchHandler.onSearchDone(this);
	}
}
