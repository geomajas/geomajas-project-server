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
package org.geomajas.gwt.client.gfx.paintable.mapaddon;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.gwt.client.action.event.ToolbarActionDisabledEvent;
import org.geomajas.gwt.client.action.event.ToolbarActionEnabledEvent;
import org.geomajas.gwt.client.action.event.ToolbarActionHandler;
import org.geomajas.gwt.client.controller.AbstractGraphicsController;
import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.gfx.GraphicsContext;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.gfx.paintable.Image;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.MapWidget;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.user.client.Event;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * A convenience class for creating your own {@link MapAddon}s.<br /><br />
 * 
 * Creates a single addon out of an icon and/or background image and a controller. 
 * Use this class in combination with {@link MapAddonGroup}, 
 * if you want to place add ons on a single background image or simply to group them. <br /><br />
 * 
 * The dimensions of the background image are used in favor of those of the icon image:
 * <ul><li> 
 * If a background is added to this addon, its width and height are casted to 
 * an Integer and used as the dimensions for this {@link MapAddon}. </li>
 * <li>
 * If no background was added, the icons dimensions are casted to 
 * an Integer and used as the dimensions for this {@link MapAddon}.
 * </li></ul>
 * 
 * @author Emiel Ackermann
 * @since 1.10.0
 */
@Api
public class SingleMapAddon extends MapAddon {

	private Image background;
	private Image icon;
	protected MapWidget mapWidget;
	private GraphicsController controller;
	protected Object group;
	private boolean enabled = true;
	
	/**
	 * Creates a single map addon out of an icon, background image and a controller.<br /><br />
	 * The width and height of the <b>background</b> image are casted to an Integer and used.
	 * 
	 * @param id
	 * @param icon
	 * @param background can not be null
	 * @param mapWidget
	 * @param controller
	 */
	public SingleMapAddon(String id, Image icon, Image background, MapWidget mapWidget,  ToolbarAction action) {
		this(id, (int) background.getBounds().getWidth(), (int) background.getBounds().getHeight());
		this.icon = icon;
		this.background = background;
		this.mapWidget = mapWidget;
		this.controller = new ActionController(mapWidget, action);
	}

	/**
	 * Creates a single map addon out of an icon, background image and an action.<br /><br />
	 * The width and height of the <b>background</b> image are cast to an Integer and used.
	 * 
	 * @param id
	 * @param icon
	 * @param background can not be null
	 * @param mapWidget
	 * @param controller
	 */
	public SingleMapAddon(String id, Image icon, MapWidget mapWidget, ToolbarAction action) {
		this(id, (int) icon.getBounds().getWidth(), (int) icon.getBounds().getHeight());
		this.icon = icon;
		this.mapWidget = mapWidget;
		this.controller = new ActionController(mapWidget, action);
		this.enabled = !action.isDisabled();
	}

	/**
	 * Creates a single map addon out of an icon, background image and a controller.<br /><br />
	 * The width and height of the <b>background</b> image are cast to an Integer and used.
	 * 
	 * @param id
	 * @param icon
	 * @param background can not be null
	 * @param mapWidget
	 * @param controller
	 */
	public SingleMapAddon(String id, Image icon, Image background, MapWidget mapWidget, GraphicsController controller) {
		this(id, (int) background.getBounds().getWidth(), (int) background.getBounds().getHeight());
		this.icon = icon;
		this.background = background;
		this.mapWidget = mapWidget;
		this.controller = controller;
	}
	/**
	 * Creates a single map addon out of an icon and a controller.<br /><br />
	 * The width and height of the <b>icon</b> image are cast to an Integer and used.
	 * 
	 * @param id
	 * @param icon can not be null
	 * @param background
	 * @param mapWidget
	 * @param controller
	 */
	public SingleMapAddon(String id, Image icon, MapWidget mapWidget, GraphicsController controller) {
		this(id, (int) icon.getBounds().getWidth(), (int) icon.getBounds().getHeight());
		this.icon = icon;
		this.mapWidget = mapWidget;
		this.controller = controller;
	}
	/**
	 * Use this constructor if you want to create a {@link MapAddon} with an icon, 
	 * but without using the icon its dimensions.<br /><br />
	 * Used for the 'dragable' area of the {@link ZoomSlider}, for instance.
	 * 
	 * @param id
	 * @param icon
	 * @param width
	 * @param height
	 * @param mapWidget
	 * @param controller
	 */
	public SingleMapAddon(String id, Image icon, int width, int height,
			MapWidget mapWidget, GraphicsController controller) {
		this(id, width, height);
		this.icon = icon;
		this.mapWidget = mapWidget;
		this.controller = controller;
	}

	protected SingleMapAddon(String id, int width, int height) {
		super(id, width, height);
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void accept(PainterVisitor visitor, Object group, Bbox bounds, boolean recursive) {
		this.group = group;
		GraphicsContext vectorContext = mapWidget.getVectorContext();
		if (null != background) {
			vectorContext.drawImage(group, getId() + "Bg", background.getHref(),
					applyMargins(background.getBounds()), background.getStyle());
			vectorContext.setController(group, getId() + "Bg", controller, Event.MOUSEEVENTS);
			vectorContext.setCursor(group, getId() + "Bg", Cursor.POINTER.getValue());
		}
		if (null != icon) {
			drawImage(applyMargins(icon.getBounds()));
			vectorContext.setController(group, getId(), controller, Event.MOUSEEVENTS);
			vectorContext.setCursor(group, getId(), Cursor.POINTER.getValue());
		}
	}
	/**
	 * Get the {@link Image} that is the background of this addon.
	 * @return background
	 */
	public Image getBackground() {
		return background;
	}
	/**
	 * Set the {@link Image} that is the background of this addon.
	 * @param background
	 */
	public void setBackground(Image background) {
		this.background = background;
	}
	/**
	 * Get the {@link Image} that is the icon of this addon.
	 * @return icon
	 */
	public Image getIcon() {
		return icon;
	}
	/**
	 * Set the {@link Image} that is the icon of this addon.
	 * @param icon
	 */
	public void setIcon(Image icon) {
		this.icon = icon;
	}
	/**
	 * Get the {@link GraphicsController}.
	 * @return controller
	 */
	public GraphicsController getController() {
		return controller;
	}
	
	/**
	 * Set the {@link GraphicsController}.
	 * @param controller
	 */
	public void setController(GraphicsController controller) {
		this.controller = controller;
	}
	/**
	 * <p>Clones the given {@link Bbox} and applies the margins
	 * of this {@link MapAddon} and its parent (if present) on it.</p> 
	 * 
	 * @param bounds
	 * @param group 
	 * @return cloned bounds with the margins applied to it's x and y
	 */
	public Bbox applyMargins(Bbox bounds) {
		Bbox applied = (Bbox) bounds.clone();
		Coordinate c = getUpperLeftCorner();
		Double x = c.getX();
		Double y = c.getY();
		if (null != group && group instanceof MapAddon && group != this) {
			Coordinate pc = ((MapAddon) group).getUpperLeftCorner();
			x += pc.getX();
			y += pc.getY();
		}
		applied.setX(x + applied.getX());
		applied.setY(y + applied.getY());
		return applied;
	}	
	
	/**
	 * Get the positioned bounds of this addon. Get the enabled state of this addon.
	 * 
	 * @return positioned bounds
	 */
	public Bbox getAddonBounds() {
		return applyMargins(new Bbox(0, 0, getWidth(), getHeight()));
	}

	@Override
	public void onDraw() {
	}

	@Override
	public void onRemove() {
	}
	/**
	 * Get the enabled state of this addon.
	 * 
	 * @return enabled
	 * 			The enabled state of this addon.
	 */
	public boolean isEnabled() {
		return enabled;
	}
	
	/**
	 * Set the enabled state of this addon.
	 * 
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Draw the icon {@link Image} of this MapAddon.
	 * @param bounds 
	 */
	public void drawImage(Bbox bounds) {
		if (enabled) {
			mapWidget.getVectorContext().drawImage(group, getId(), icon.getHref(), bounds, icon.getStyle());
		} else {
			mapWidget.getVectorContext().drawImage(group, getId(), 
					makeDisabled(icon.getHref()), bounds, icon.getStyle());
		}
	}
		
	private String makeDisabled(String href) {
		int lastDot = href.lastIndexOf(".");
		if (lastDot >= 0) {
			return href.substring(0, lastDot) + "_Disabled" + href.substring(lastDot);
		} else {
			return href;
		}
	}

	/**
	 * Wraps a {@link ToolbarAction} into this {@link AbstractGraphicsController}.
	 * 
	 * @author Emiel Ackermann
	 */
	class ActionController extends AbstractGraphicsController implements ToolbarActionHandler {
		private ToolbarAction action;

		ActionController(MapWidget mapWidget, ToolbarAction action) {
			super(mapWidget);
			this.action = action;
			action.addToolbarActionHandler(this);
		}
		
		public void onMouseUp(MouseUpEvent event) {
			// execute the action
			action.onClick(new ClickEvent(null));
			event.stopPropagation();
		}

		public void onMouseDown(MouseDownEvent event) {
			// Don't propagate to the active controller on the map:
			event.stopPropagation();
		}

		public void onToolbarActionEnabled(ToolbarActionEnabledEvent event) {
			setEnabled(true);
			drawImage(applyMargins(icon.getBounds()));
		}

		public void onToolbarActionDisabled(ToolbarActionDisabledEvent event) {
			setEnabled(false);
			drawImage(applyMargins(icon.getBounds()));
		}
	}
	
	/**
	 * Get the group of this addon.
	 * 
	 * @return group
	 */
	public Object getGroup() {
		return group;
	}
	
	/**
	 * Get the mapWidget.
	 * 
	 * @return mapWidget
	 */
	public MapWidget getMapWidget() {
		return mapWidget;
	}
}
