#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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
package ${package}.client;

import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.TransformGeometryRequest;
import org.geomajas.command.dto.TransformGeometryResponse;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.controller.AbstractGraphicsController;
import org.geomajas.gwt.client.map.MapView.ZoomOption;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.gwt.client.spatial.geometry.Point;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.gwt.client.widget.MapWidget;

import com.google.gwt.core.client.Callback;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.Position.Coordinates;
import com.google.gwt.geolocation.client.PositionError;

/**
 * Controller to zoom to the current location, fetched from browser.
 *
 * @author Oliver May
 *
 */
public class ZoomToLocationController extends AbstractGraphicsController {

	public ZoomToLocationController(MapWidget mapWidget) {
		super(mapWidget);
	}

	public void onMouseUp(MouseUpEvent event) {
//		StyleElement se = (StyleElement) Document.get().getElementById("inlineStyle");
//		se.setInnerText(".msRibbon { background: green; }");

		final Geolocation geo = Geolocation.getIfSupported();
		if (geo != null) {
			geo.getCurrentPosition(new Callback<Position, PositionError>() {

				@Override
				public void onSuccess(final Position result) {
					Coordinates coord = result.getCoordinates();
					TransformGeometryRequest req = new TransformGeometryRequest();
					GeometryFactory gf = new GeometryFactory(4326, 1);
					Point point = gf.createPoint(new Coordinate(coord.getLongitude(), coord.getLatitude()));

					req.setGeometry(GeometryConverter.toDto(point));
					req.setSourceCrs("EPSG:4326");
					req.setTargetCrs(mapWidget.getMapModel().getCrs());

					GwtCommand command = new GwtCommand(TransformGeometryRequest.COMMAND);
					command.setCommandRequest(req);

					GwtCommandDispatcher.getInstance().execute(command, new CommandCallback<CommandResponse>() {

						@Override
						public void execute(CommandResponse response) {
							if (response.getErrors().isEmpty()) {
								org.geomajas.geometry.Geometry geom = ((TransformGeometryResponse) response)
										.getGeometry();
								double accuracy = result.getCoordinates().getAccuracy();

								Bbox box = new Bbox(geom.getCoordinates()[0].getX() - (accuracy / 2), geom
										.getCoordinates()[0].getY() - (accuracy / 2), accuracy, accuracy);
								mapWidget.getMapModel().getMapView().applyBounds(box, ZoomOption.LEVEL_FIT);
							}
						}
					});

				}

				@Override
				public void onFailure(PositionError reason) {
					// TODO Auto-generated method stub

				}
			});
		}
		event.stopPropagation();
	}

	public void onMouseDown(MouseDownEvent event) {
		// Don't propagate to the active controller on the map:
		event.stopPropagation();
	}
}