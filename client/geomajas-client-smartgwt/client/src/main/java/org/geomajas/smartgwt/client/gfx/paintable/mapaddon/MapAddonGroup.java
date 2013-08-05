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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.smartgwt.client.gfx.PainterVisitor;
import org.geomajas.smartgwt.client.gfx.paintable.Image;
import org.geomajas.smartgwt.client.spatial.Bbox;
import org.geomajas.smartgwt.client.widget.MapWidget;

/**
 * <p>Groups other {@link MapAddon}s together that need a shared background image.
 * Can not contain a controller of its own, to avoid conflicting behaviour.</p>
 * <p> The customized PanController in the showcase is an example of one 
 * {@link MapAddonGroup} with four {@link SingleMapAddon}s containing the arrows.</p>
 * 
 * @author Emiel Ackermann
 * @since 1.10.0
 */
@Api
public class MapAddonGroup extends MapAddon {

	private List<MapAddon> addons;
	private MapWidget mapWidget;
	private Image background;
	/**
	 * Creates a group for the given addons out of the given background.
	 * 
	 * @param id
	 * @param addons
	 * @param background
	 * @param mapWidget
	 */
	public MapAddonGroup(String id, List<MapAddon> addons, Image background, MapWidget mapWidget) {
		this(id, background, mapWidget);
		this.addons = addons;
	}
	
	/**
	 * Creates a empty group. Addons can be added using {@link MapAddonGroup#addAddon(MapAddon)}.
	 * @param id
	 * @param background
	 * @param mapWidget
	 */
	public MapAddonGroup(String id, Image background, MapWidget mapWidget) {
		this(id, (int) background.getBounds().getWidth(), (int) background.getBounds().getHeight(), mapWidget);
		this.background = background;
	}
	
	/**
	 * Set the dimensions with this constructor, if no background image is required for the group.
	 * 
	 * @param id
	 * @param width
	 * @param height
	 */
	public MapAddonGroup(String id, int width, int height, MapWidget mapWidget) {
		super(id, width, height);
		this.mapWidget = mapWidget;
	}
	@Override
	public void accept(PainterVisitor visitor, Object group, Bbox bounds,
			boolean recursive) {
		mapWidget.getVectorContext().drawGroup(group, this);
		if (null != background) {
			mapWidget.getVectorContext().drawImage(this, getId() + "Bg", background.getHref(), 
					applyMargins(background.getBounds()), background.getStyle());
		}
		for (MapAddon addon : addons) {
			addon.accept(visitor, this, bounds, recursive);
		}
	}
	
	/**
	 * 
	 * Clones the given {@link Bbox} and applies the margins on it. 
	 * @param bounds
	 * @return
	 */
	private Bbox applyMargins(Bbox bounds) {
		Bbox applied = (Bbox) bounds.clone();
		Coordinate c = getUpperLeftCorner();
		applied.setX(applied.getX() + c.getX());
		applied.setY(applied.getY() + c.getY());
		return applied;
	}
	/**
	 * Unregisters itself and all its addons.
	 */
	public void unregisterAll() {
		for (MapAddon addon : addons) {
			mapWidget.unregisterMapAddon(addon);
		}
		mapWidget.unregisterMapAddon(this);
	}
	/**
	 * Add an {@link MapAddon}.
	 * 
	 * @param addon
	 */
	public void addAddon(MapAddon addon) {
		if (null == addons) {
			addons = new ArrayList<MapAddon>();
		}
		addons.add(addon);
	}
	@Override
	public void setMapSize(int mapWidth, int mapHeight) {
		super.setMapSize(mapWidth, mapHeight);
		for (MapAddon addon : addons) {
			addon.setMapSize(mapWidth, mapHeight);
		}
	}
	
	/**
	 * Removes given addon from group.
	 * 
	 * @param addon
	 * @return boolean given by {@link List#remove(Object)}
	 * @throws IllegalArgumentException if no addons are present.
	 */
	public boolean removeAddon(MapAddon addon) {
		if (null == addons) {
			throw new IllegalArgumentException("This group does not contain any addons yet.");
		}
		return addons.remove(addon);
	}
	
	/**
	 * Returns {@link MapAddon} with given id if found. Otherwise null is returned.
	 * 
	 * @param id
	 * @return null if no addon with given id was found
	 */
	public MapAddon getAddon(String id) {
		MapAddon found = null;
		for (MapAddon addon : addons) {
			if (addon.getId().equals(id)) {
				found = addon;
				break;
			}
		}
		return found;
	}

	@Override
	public void onDraw() {
		for (MapAddon addon : addons) {
			addon.onDraw();
		}
	}

	@Override
	public void onRemove() {
		for (MapAddon addon : addons) {
			addon.onRemove();
		}		
	}
}
