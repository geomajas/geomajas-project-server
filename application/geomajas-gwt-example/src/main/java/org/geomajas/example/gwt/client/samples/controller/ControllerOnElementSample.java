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

package org.geomajas.example.gwt.client.samples.controller;

import org.geomajas.example.gwt.client.samples.base.SamplePanel;
import org.geomajas.example.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.example.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.controller.AbstractGraphicsController;
import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.gfx.GraphicsContext;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows how to add a customer controller to the map that can be dragged around by the user.
 * </p>
 * 
 * @author Frank Wynants
 * @author Pieter De Graef
 */
public class ControllerOnElementSample extends SamplePanel {

	public static final String TITLE = "ControllerOnElement";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new ControllerOnElementSample();
		}
	};

	public Canvas getViewPanel() {

		// Drawing information:
		final Bbox rectangle = new Bbox(100, 100, 300, 100);
		final ShapeStyle style = new ShapeStyle("#66AA00", 0.7f, "#667700", 0.8f, 3);
		final ShapeStyle hoverStyle = new ShapeStyle("#66AA00", 0.5f, "#667700", 0.7f, 3);

		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		// Map with ID wmsMap is defined in the XML configuration. (mapWms.xml)
		final MapWidget map = new MapWidget("mapOsm", "gwt-samples");

		// Create the custom controller:
		final GraphicsController customController = new AbstractGraphicsController(map) {

			public void onMouseOver(MouseOverEvent event) {
				// When the mouse hovers over the image, make it transparent:
				map.getVectorContext().drawRectangle(map.getGroup(RenderGroup.SCREEN), "rectangle", rectangle,
						hoverStyle);
			}

			public void onMouseOut(MouseOutEvent event) {
				// When the mouse moves away from the image, make it visible again:
				map.getVectorContext().drawRectangle(map.getGroup(RenderGroup.SCREEN), "rectangle", rectangle, style);
			}

			public void onMouseDown(MouseDownEvent event) {
				map.getVectorContext().drawRectangle(map.getGroup(RenderGroup.SCREEN), "rectangle", rectangle,
						new ShapeStyle("#AA0000", 0.8f, "#990000", 1.0f, 3));
			}

			public void onMouseUp(MouseUpEvent event) {
				map.getVectorContext().drawRectangle(map.getGroup(RenderGroup.SCREEN), "rectangle", rectangle,
						hoverStyle);
			}
		};

		// After map initialization we draw an image on the map:
		map.getMapModel().addMapModelHandler(new MapModelHandler() {

			public void onMapModelChange(MapModelEvent event) {
				GraphicsContext graphics = map.getVectorContext();
				graphics.drawRectangle(map.getGroup(RenderGroup.SCREEN), "rectangle", rectangle, style);
				map.getVectorContext().setController(map.getGroup(RenderGroup.SCREEN), "rectangle", customController);
			}
		});

		layout.addMember(map);
		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().controllerOnElementDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/example/gwt/client/samples/controller/ControllerOnElementSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "WEB-INF/layerOsm.xml", "WEB-INF/mapOsm.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
