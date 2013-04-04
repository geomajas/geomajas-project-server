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

package org.geomajas.puregwt.client.widget;

import org.geomajas.annotation.Api;
import org.geomajas.puregwt.client.map.MapPresenter;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;

/**
 * {@link SimpleLayoutPanel} that wraps the map. This panel can be use in the GWT 2.0 layout system and will adopt to
 * resizes. Call {@link #setPresenter(MapPresenter)} before adding the panel to the general layout.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public class MapLayoutPanel extends SimpleLayoutPanel {

	private MapPresenter presenter;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/** Create a new map layout instance. */
	public MapLayoutPanel() {
	}

	/**
	 * Create a new map layout instance.
	 * 
	 * @param mapPresenter
	 *            Immediately apply a map onto this layout.
	 */
	public MapLayoutPanel(MapPresenter mapPresenter) {
		setPresenter(mapPresenter);
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	/**
	 * Set the map presenter. Call this method before adding this widget to the layout.
	 * 
	 * @param presenter
	 */
	public void setPresenter(MapPresenter presenter) {
		this.presenter = presenter;
		setWidget(presenter);
		onResize();
	}

	/**
	 * Get the map presenter.
	 * 
	 * @return The map presenter.
	 */
	public MapPresenter getMapPresenter() {
		return presenter;
	}

	@Override
	public void onResize() {
		if (presenter != null) {
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {

				@Override
				public void execute() {
					presenter.setSize(getElement().getOffsetWidth(), getElement().getOffsetHeight());
				}
			});
		}
		super.onResize();
	}
}