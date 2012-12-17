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
package org.geomajas.plugin.editing.gwt.client.handler;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.gfx.paintable.Rectangle;
import org.geomajas.gwt.client.gfx.paintable.Text;
import org.geomajas.gwt.client.gfx.style.FontStyle;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.map.event.MapViewChangedEvent;
import org.geomajas.gwt.client.map.event.MapViewChangedHandler;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.WorldViewTransformer;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;
import org.geomajas.gwt.client.widget.MapWidget.RenderStatus;
import org.geomajas.plugin.editing.client.service.GeometryEditService;

/**
 * Editing handler that draws labels on the drag lines.
 * 
 * @author Jan De Moerloose
 * 
 */
public class LabelDragLineHandler extends BaseDragLineHandler implements MapViewChangedHandler {

	private MapWidget map;

	private static final int WIDTH = 18;

	private static final int HEIGHT = 18;

	private LabelRenderer aRenderer;

	private LabelRenderer bRenderer;

	/**
	 * Construct a handler.
	 * 
	 * @param map the map
	 * @param editService the editing service
	 */
	public LabelDragLineHandler(MapWidget map, GeometryEditService editService) {
		super(editService);
		this.map = map;
		aRenderer = new LabelRenderer("drag-label-a", "A");
		bRenderer = new LabelRenderer("drag-label-b", "B");
	}

	/**
	 * Register handlers.
	 */
	public void register() {
		super.register();
		registrations.add(map.getMapModel().getMapView().addMapViewChangedHandler(this));
	}

	/**
	 * Register handlers.
	 */
	public void unregister() {
		onDragStopped();
		super.unregister();
	}

	@Override
	protected void onDrag(Coordinate dragPoint, Coordinate startA, Coordinate startB) {
		if (startA != null) {
			aRenderer.setCoordinates(startA, dragPoint);
			aRenderer.setVisible(true);
			aRenderer.draw();
		} else {
			aRenderer.setVisible(false);
			aRenderer.draw();
		}
		if (startB != null) {
			bRenderer.setCoordinates(startB, dragPoint);
			bRenderer.setVisible(true);
			bRenderer.draw();
		} else {
			bRenderer.setVisible(false);
			bRenderer.draw();
		}
	}

	@Override
	protected void onDragStopped() {
		aRenderer.setVisible(false);
		bRenderer.setVisible(false);
		aRenderer.draw();
		bRenderer.draw();
	}

	@Override
	public void onMapViewChanged(MapViewChangedEvent event) {
		aRenderer.draw();
		bRenderer.draw();
	}

	/**
	 * Renders a single label.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	private class LabelRenderer {

		private Rectangle rectangle;

		private Text text;

		private WorldViewTransformer wtf;

		private boolean visible;

		private Coordinate start;

		private Coordinate stop;

		public LabelRenderer(String id, String content) {
			rectangle = new Rectangle(id + "-rect");
			rectangle.setStyle(new ShapeStyle("#FFFFFF", 0.9f, "#000000", 0.9f, 1));
			text = new Text(id + "-text");
			text.setStyle(new FontStyle("#000000", 12, "Arial", "normal", "normal"));
			text.setContent(content);
			wtf = map.getMapModel().getMapView().getWorldViewTransformer();
		}

		public void setCoordinates(Coordinate start, Coordinate stop) {
			this.start = start;
			this.stop = stop;
		}

		public void draw() {
			if (!visible) {
				delete();
			} else {
				double scale = map.getMapModel().getMapView().getCurrentScale();
				double llen = scale * length(new Coordinate[] { start, stop });
				if (llen < WIDTH) {
					delete();
				} else {
					Coordinate c = wtf.worldToView(middle(new Coordinate[] { start, stop }));
					int x = (int) (c.getX() - WIDTH / 2);
					int y = (int) (c.getY() - HEIGHT / 2);
					rectangle.setBounds(new Bbox(x, y, WIDTH, HEIGHT));
					text.setPosition(new Coordinate(x + 4, y + 2));
					map.render(rectangle, RenderGroup.SCREEN, RenderStatus.ALL);
					map.render(text, RenderGroup.SCREEN, RenderStatus.ALL);
				}
			}
		}

		private void delete() {
			map.render(rectangle, RenderGroup.SCREEN, RenderStatus.DELETE);
			map.render(text, RenderGroup.SCREEN, RenderStatus.DELETE);
		}

		private double length(Coordinate[] edge) {
			return Math.hypot(edge[1].getX() - edge[0].getX(), edge[1].getY() - edge[0].getY());
		}

		private Coordinate middle(Coordinate[] c) {
			return new Coordinate(0.5 * (c[0].getX() + c[1].getX()), 0.5 * (c[0].getY() + c[1].getY()));
		}

		public void setVisible(boolean visible) {
			this.visible = visible;
		}

	}

}
