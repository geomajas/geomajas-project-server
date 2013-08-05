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

package org.geomajas.smartgwt.client.gfx.paintable.mapaddon;

import org.geomajas.geometry.Coordinate;
import org.geomajas.annotation.Api;
import org.geomajas.smartgwt.client.gfx.Paintable;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;

/**
 * <p>
 * Definition of an add-on that can be placed on a map, on a fixed location. Implementation need to specify an exact
 * width and height. This is necessary to be placed at the correct location on the map in screen space.
 * </p>
 * <p>
 * All MapAddons are by default aligned top-left. It is possible to change this by specifying a horizontal or vertical
 * alignment, in addition to a horizontal or vertical margin.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.8.0
 */
@Api
public abstract class MapAddon implements Paintable {

	private String id;

	protected int width;

	protected int height;

	private Coordinate upperLeftCorner = new Coordinate(0, 0);

	private Alignment alignment = Alignment.LEFT;

	private VerticalAlignment verticalAlignment = VerticalAlignment.TOP;

	private int horizontalMargin;

	private int verticalMargin;
	
	private boolean repaintOnMapViewChange;

	// -------------------------------------------------------------------------
	// Protected constructor:
	// -------------------------------------------------------------------------

	/**
	 * When creating your own MapAddons, always use this constructor! All
	 * MapAddons are in essence paintable objects on the map and therefore require a unique identifier. For correct
	 * placing the width and height are also required.
	 * 
	 * @param id
	 *            The unique identifier.
	 * @param width
	 *            The width of the MapAddon on the map.
	 * @param height
	 *            The height of the MapAddon on the map.
	 * @since 1.8.0
	 */
	@Api
	public MapAddon(String id, int width, int height) {
		this.id = id;
		this.width = width;
		this.height = height;
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/**
	 * Return the unique identifier for this map add-on.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Apply a new width and height for the map onto whom this add-on is drawn. This method is triggered automatically
	 * when the map resizes.
	 * 
	 * @param mapWidth
	 *            The map's new width.
	 * @param mapHeight
	 *            The map's new height.
	 */
	public void setMapSize(int mapWidth, int mapHeight) {
		double x = horizontalMargin;
		double y = verticalMargin;

		// Calculate horizontal position:
		switch (alignment) {
			case LEFT:
				break;
			case CENTER:
				x = Math.round((mapWidth - width) / 2);
				break;
			case RIGHT:
				x = mapWidth - width - horizontalMargin;
		}

		// Calculate vertical position:
		switch (verticalAlignment) {
			case TOP:
				break;
			case CENTER:
				y = Math.round((mapHeight - height) / 2);
				break;
			case BOTTOM:
				y = mapHeight - height - verticalMargin;
		}

		upperLeftCorner = new Coordinate(x, y);
	}

	/**
	 * Called when this map add-on is drawn for the first time.
	 */
	public abstract void onDraw();

	/**
	 * Called when this map add-on is removed from the map.
	 */
	public abstract void onRemove();

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	/**
	 * Get the current horizontal alignment.
	 */
	public Alignment getAlignment() {
		return alignment;
	}

	/**
	 * Set a new horizontal alignment.
	 * 
	 * @param alignment
	 *            The new alignment.
	 */
	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}

	/**
	 * Get the current vertical alignment for this map add-on.
	 */
	public VerticalAlignment getVerticalAlignment() {
		return verticalAlignment;
	}

	/**
	 * Set a new vertical alignment.
	 * 
	 * @param verticalAlignment
	 *            Apply this vertical alignment.
	 */
	public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
		this.verticalAlignment = verticalAlignment;
	}

	/**
	 * Get a horizontal margin. Depending on the horizontal alignment, this margin will be applied to the left or right
	 * of this map add-on.
	 * 
	 * @return The current horizontal margin, expressed in pixels.
	 */
	public int getHorizontalMargin() {
		return horizontalMargin;
	}

	/**
	 * Set a horizontal margin. Depending on the horizontal alignment, this margin will be applied to the left or right
	 * of this map add-on.
	 * 
	 * @param horizontalMargin
	 *            The new horizontal margin, expressed in pixels.
	 */
	public void setHorizontalMargin(int horizontalMargin) {
		this.horizontalMargin = horizontalMargin;
	}

	/**
	 * Get a vertical margin. Depending on the vertical alignment, this margin will be applied to the top or bottom of
	 * this map add-on.
	 * 
	 * @return The current vertical margin, expressed in pixels.
	 */
	public int getVerticalMargin() {
		return verticalMargin;
	}

	/**
	 * Set a vertical margin. Depending on the vertical alignment, this margin will be applied to the top or bottom of
	 * this map add-on.
	 * 
	 * @param verticalMargin
	 *            The new vertical margin, expressed in pixels.
	 */
	public void setVerticalMargin(int verticalMargin) {
		this.verticalMargin = verticalMargin;
	}

	/**
	 * Get the width of this map add-on, expressed in pixels.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Get the height of this map add-on, expressed in pixels.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Get the upper left corner of this map add-on. Depends on the horizontal and vertical alignments and margins.
	 */
	public Coordinate getUpperLeftCorner() {
		return upperLeftCorner;
	}

	
	public boolean isRepaintOnMapViewChange() {
		return repaintOnMapViewChange;
	}

	
	public void setRepaintOnMapViewChange(boolean repaintOnMapViewChange) {
		this.repaintOnMapViewChange = repaintOnMapViewChange;
	}
	
	
}
