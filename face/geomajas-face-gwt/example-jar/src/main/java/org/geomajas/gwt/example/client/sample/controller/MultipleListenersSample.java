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

package org.geomajas.gwt.example.client.sample.controller;

import com.google.gwt.core.client.GWT;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.gwt.client.controller.listener.AbstractListener;
import org.geomajas.gwt.client.controller.listener.Listener;
import org.geomajas.gwt.client.controller.listener.ListenerEvent;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.PortalLayout;
import com.smartgwt.client.widgets.layout.Portlet;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.example.client.sample.i18n.SampleMessages;

/**
 * <p>
 * Sample that demonstrates the use of multiple listeners on the map.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class MultipleListenersSample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String TITLE = "MultipleListeners";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new MultipleListenersSample();
		}
	};

	private int column;

	/**
	 * @return The viewPanel Canvas
	 */
	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		// Create the map, using the wmsMap configuration (mapOsm.xml):
		final MapWidget map = new MapWidget("mapOsm", "gwtExample");

		VLayout mapLayout = new VLayout();
		mapLayout.setShowEdges(true);
		mapLayout.setHeight("50%");
		mapLayout.addMember(map);
		layout.addMember(mapLayout);

		final PortalLayout portalLayout = new PortalLayout(3);
		portalLayout.setWidth100();
		portalLayout.setHeight("50%");
		portalLayout.setShowColumnMenus(false);

		HLayout buttonLayout = new HLayout(10);
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setHeight(30);
		buttonLayout.setWidth100();
		Button add = new Button(MESSAGES.multipleListenersBtn());
		add.setLayoutAlign(VerticalAlignment.CENTER);
		add.setAutoFit(true);
		add.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				final Portlet portlet = new Portlet();
				portlet.setTitle(MESSAGES.multipleListenersPortletTitle());

				final Label label = new Label();
				label.setHeight(30);
				label.setContents(MESSAGES.multipleListenersPortletText() + " = ?");
				portlet.setHeight(70);
				portlet.addItem(label);
				portalLayout.addPortlet(portlet, (column++) % 3, 0);

				final Listener listener = new AbstractListener() {

					public void onMouseMove(ListenerEvent event) {
						label.setContents(MESSAGES.multipleListenersPortletText() + " = "
								+ event.getScreenPosition());
					}
				};
				map.addListener(listener);
				portlet.addCloseClickHandler(new CloseClickHandler() {

					public void onCloseClick(CloseClientEvent event) {
						map.removeListener(listener);
						if (map.getListeners() != null) {
							SC.say(MESSAGES.multipleListenersCount(map.getListeners().size()));
						}
						portlet.destroy();
					}
				});
				SC.say(MESSAGES.multipleListenersCount(map.getListeners().size()));
			}
		});
		buttonLayout.addMember(add);
		layout.addMember(buttonLayout);
		layout.addMember(portalLayout);
		return layout;
	}

	public String getDescription() {
		return MESSAGES.multipleListenersDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] {
				"classpath:org/geomajas/gwt/example/context/mapOsm.xml",
				"classpath:org/geomajas/gwt/example/base/layerOsm.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
