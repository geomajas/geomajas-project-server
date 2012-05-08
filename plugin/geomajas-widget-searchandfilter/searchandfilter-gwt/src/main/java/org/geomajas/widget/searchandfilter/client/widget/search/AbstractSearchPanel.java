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
package org.geomajas.widget.searchandfilter.client.widget.search;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.searchandfilter.client.widget.geometricsearch.GeometryUpdateHandler;
import org.geomajas.widget.searchandfilter.search.dto.Criterion;

import com.smartgwt.client.widgets.Canvas;

/**
 * @see SearchWidgetRegistry
 * @author Kristof Heirwegh
 * @since 1.0.0
 */
@Api(allMethods = true)
public abstract class AbstractSearchPanel extends Canvas {

	protected MapWidget mapWidget;
	protected GeometryUpdateHandler handler;
	private boolean canAddToFavourites = true;
	private boolean canFilterLayer;
	private boolean canReset = true;
	private boolean canCancel = true;

	/**
	 * Constructor.
	 *
	 * @param mapWidget map widget
	 */
	public AbstractSearchPanel(MapWidget mapWidget) {
		super();
		if (mapWidget == null) {
			throw new IllegalArgumentException("Please provide a mapWidget.");
		}
		this.mapWidget = mapWidget;
	}

	/**
	 * Called before getFeatureSearchCriterion().
	 * <p>
	 * Search will be cancelled if you return false.
	 * 
	 * @return true if a criterion can be returned.
	 */
	public abstract boolean validate();

	/**
	 * Should the "Add To Favourites" button be shown?
	 * 
	 * @return true when "add to favourites" button is visible
	 */
	public boolean canAddToFavourites() {
		return canAddToFavourites;
	}

	/**
	 * Indicate whether add to favourites is possible or not.
	 * 
	 * @param canAddToFavourites
	 *            new value
	 */
	public void setCanAddToFavourites(boolean canAddToFavourites) {
		this.canAddToFavourites = canAddToFavourites;
	}

	/**
	 * Should the "Filter layer" button be shown?
	 * 
	 * @return true when "Filter layer" button is visible
	 */
	public boolean canFilterLayer() {
		return canFilterLayer;
	}

	/**
	 * Indicate whether filter layer is possible or not.
	 * 
	 * @param canFilterLayer
	 *            new value
	 */
	public void setCanFilterLayer(boolean canFilterLayer) {
		this.canFilterLayer = canFilterLayer;
	}

	/**
	 * Should the "Reset" button be shown?
	 * 
	 * @return true when reset button is visible
	 */
	public boolean canReset() {
		return canReset;
	}

	/**
	 * Set whether the reset button can exist.
	 * 
	 * @param canReset
	 *            new value
	 */
	public void setCanReset(boolean canReset) {
		this.canReset = canReset;
	}

	/**
	 * Should the "Cancel" button be shown?
	 *
	 * @return true when cancel button is visible
	 */
	public boolean canCancel() {
		return canCancel;
	}

	/**
	 * Set whether the cancel button can exist.
	 *
	 * @param canCancel
	 *            new value
	 */
	public void setCanCancel(boolean canCancel) {
		this.canCancel = canCancel;
	}

	/**
	 * @return an object with the settings of your search, allowing the specifics of the search to be stored (in
	 *         favourites).
	 */
	public abstract Criterion getFeatureSearchCriterion();

	/**
	 * Get the vector layer which is to be searched.
	 * 
	 * @return the vector layer to search in.
	 */
	public abstract VectorLayer getFeatureSearchVectorLayer();

	/** {@inheritDoc} */
	public abstract void reset();

	/**
	 * Called to restore the settings previously requested through getSettings().
	 * 
	 * @param featureSearch settings
	 */
	public abstract void initialize(Criterion featureSearch);

	/**
	 * Get the map widget to which this search panel applies.
	 * 
	 * @return map widget
	 */
	public MapWidget getMapWidget() {
		return mapWidget;
	}
}
