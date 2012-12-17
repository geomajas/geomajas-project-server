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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.gwt.client.util.DistanceFormat;
import org.geomajas.gwt.client.util.HtmlBuilder;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.event.GeometryEditStartEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStartHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStopEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStopHandler;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.gwt.client.gfx.InfoProvider;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.Window;

/**
 * Editing handler that shows a draggable/closeable window with geometry information.
 * 
 * @author Jan De Moerloose
 * 
 */
public class InfoDragLineHandler extends BaseDragLineHandler implements GeometryEditStartHandler,
		GeometryEditStopHandler {

	private HTMLFlow label;

	private Window window;

	private MapWidget map;

	private InfoProvider infoProvider = new DefaultInfoProvider();

	private String lengthA;

	private String lengthB;

	private static final String STYLE_NAME = "width:150px;border-width: 1px;" + "padding: 2px;" + "border-style:solid;"
			+ "text-align:left;" + "font-family: Arial,Verdana,sans-serif;" + "font-size: 11px;" + "border-color:gray;"
			+ "background-color:#FFFFFF";

	private static final String STYLE_VALUE = "width:150px;" + "border-width: 1px;" + "padding: 2px;"
			+ "border-style:solid;" + "font-family: Arial,Verdana,sans-serif;" + "font-size: 11px;"
			+ "text-align:left;" + "border-color:gray;" + "background-color:#FFFFFF";

	private static final String STYLE_TABLE = "width:300px;border-width: 0;" + "border-collapse:collapse;"
			+ "background-color:#FFFFFF";

	/**
	 * Construct a handler.
	 * 
	 * @param map the map
	 * @param editService the editing service
	 */
	public InfoDragLineHandler(MapWidget map, GeometryEditService editService) {
		super(editService);
		this.map = map;
	}

	public void register() {
		if (window == null) {
			createWindow();
		}
		super.register();
		registrations.add(editService.addGeometryEditStartHandler(this));
		registrations.add(editService.addGeometryEditStopHandler(this));
		// show initial state
		onDrag(null, null, null);
		window.show();
	}
	
	public void unregister() {
		window.hide();
		super.unregister();
	}

	public void setVisible(boolean visible) {
		window.setVisible(visible);
	}
	
	public boolean isVisible() {
		return window.isVisible();
	}

	public void setInfoProvider(InfoProvider infoProvider) {
		this.infoProvider = infoProvider;
		if (window != null) {
			window.setTitle(infoProvider.getTitle());
		}
	}
	
	public void setShowClose(boolean showClose) {
		window.setShowCloseButton(showClose);
	}

	@Override
	protected void onDrag(Coordinate dragPoint, Coordinate startA, Coordinate startB) {
		label.setContents(infoProvider.getHtml(editService.getGeometry(), dragPoint, startA, startB));
	}

	@Override
	protected void onDragStopped() {
		label.setContents(infoProvider.getHtml(editService.getGeometry(), null, null, null));
	}

	@Override
	public void onGeometryEditStop(GeometryEditStopEvent event) {
		window.hide();
	}

	@Override
	public void onGeometryEditStart(GeometryEditStartEvent event) {
		// show initial state
		onDrag(null, null, null);
		window.show();
	}

	protected double length(Coordinate[] edge) {
		return Math.hypot(edge[1].getX() - edge[0].getX(), edge[1].getY() - edge[0].getY());
	}

	private void createWindow() {
		window = new Window();
		window.hide();
		label = new HTMLFlow(infoProvider.getHtml(editService.getGeometry(), null, null, null));
		label.setAlign(Alignment.LEFT);
		window.setTitle(infoProvider.getTitle());
		window.setAutoSize(true);
		window.setCanDragReposition(true);
		window.setCanDragResize(true);
		window.addItem(label);
		window.setTop(25);
		window.setLeft(25);
		map.addChild(window);
	}

	/**
	 * Default implementation of {@link InfoProvider}.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class DefaultInfoProvider implements InfoProvider {

		@Override
		public String getHtml(Geometry geometry, Coordinate dragPoint, Coordinate startA, Coordinate startB) {
			List<String> rows = new ArrayList<String>();
			boolean empty = (geometry == null);
			rows.add(HtmlBuilder.trHtmlContent(HtmlBuilder.tdStyle(STYLE_NAME, "Type"),
					HtmlBuilder.tdStyle(STYLE_VALUE, (empty ? "" : geometry.getGeometryType()))));

			rows.add(HtmlBuilder.trHtmlContent(HtmlBuilder.tdStyle(STYLE_NAME, "Number of points"),
					HtmlBuilder.tdStyle(STYLE_VALUE, "" + (empty ? "" : GeometryService.getNumPoints(geometry)))));
			String area = (empty ? "" : DistanceFormat.asMapArea(map, GeometryService.getArea(geometry)));
			area = area.replaceAll("&sup2;", "²");
			rows.add(HtmlBuilder.trHtmlContent(HtmlBuilder.tdStyle(STYLE_NAME, "Area"),
					HtmlBuilder.tdStyle(STYLE_VALUE, area)));

			String length = (empty ? "" : DistanceFormat.asMapLength(map, GeometryService.getLength(geometry)));
			rows.add(HtmlBuilder.trHtmlContent(HtmlBuilder.tdStyle(STYLE_NAME, "Length"),
					HtmlBuilder.tdStyle(STYLE_VALUE, length)));
			lengthA = "";
			lengthB = "";
			if (startA != null) {
				double llen = length(new Coordinate[] { dragPoint, startA });
				lengthA = DistanceFormat.asMapLength(map, llen);
			}
			if (startB != null) {
				double rlen = length(new Coordinate[] { dragPoint, startB });
				lengthB = DistanceFormat.asMapLength(map, rlen);
			}
			rows.add(HtmlBuilder.trHtmlContent(HtmlBuilder.tdStyle(STYLE_NAME, "A"),
					HtmlBuilder.tdStyle(STYLE_VALUE, lengthA)));
			rows.add(HtmlBuilder.trHtmlContent(HtmlBuilder.tdStyle(STYLE_NAME, "B"),
					HtmlBuilder.tdStyle(STYLE_VALUE, lengthB)));
			String htmlContent = HtmlBuilder.tableStyleHtmlContent(STYLE_TABLE, rows.toArray(new String[rows.size()]));
			return htmlContent;
		}

		@Override
		public String getTitle() {
			return "Geometry information";
		}

	}

}
