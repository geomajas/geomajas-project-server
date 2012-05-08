/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.geocoder.client.event;

import com.google.gwt.event.shared.GwtEvent;
import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringAlternative;

import java.util.List;

/**
 * Event which is used when the geocoder returned alternatives.
 * <p/>
 * Purpose it to allow the use to choose which one to select.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public class SelectAlternativeEvent extends GwtEvent<SelectAlternativeHandler> {

	private MapWidget mapWidget;
	private List<GetLocationForStringAlternative> alternatives;

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	public Type getAssociatedType() {
		return SelectAlternativeHandler.TYPE;
	}

	/** {@inheritDoc} */
	protected void dispatch(SelectAlternativeHandler handler) {
		handler.onSelectAlternative(this);
	}

	/**
	 * Constructor which passes the map and alternatives.
	 *
	 * @param mapWidget map
	 * @param alternatives alternatives
	 */
	public SelectAlternativeEvent(MapWidget mapWidget, List<GetLocationForStringAlternative> alternatives) {
		this.mapWidget = mapWidget;
		this.alternatives = alternatives;
	}

	/**
	 * Get map widget the geocoder applies to.
	 *
	 * @return map widget
	 */
	@Api
	public MapWidget getMapWidget() {
		return mapWidget;
	}

	/**
	 * Get alternatives.
	 *
	 * @return alternatives
	 * @since 1.0.0
	 */
	@Api
	public List<GetLocationForStringAlternative> getAlternatives() {
		return alternatives;
	}
}
