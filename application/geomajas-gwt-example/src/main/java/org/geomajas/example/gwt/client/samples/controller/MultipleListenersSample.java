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

/**
 * <p>
 * Sample that demonstrates the use of multiple listeners on the map.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class MultipleListenersSample extends SamplePanel {

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
		final MapWidget map = new MapWidget("mapOsm", "gwt-samples");

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
		Button add = new Button(I18nProvider.getSampleMessages().multipleListenersBtn());
		add.setLayoutAlign(VerticalAlignment.CENTER);
		add.setAutoFit(true);
		add.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				final Portlet portlet = new Portlet();
				portlet.setTitle(I18nProvider.getSampleMessages().multipleListenersPortletTitle());

				final Label label = new Label();
				label.setHeight(30);
				label.setContents(I18nProvider.getSampleMessages().multipleListenersPortletText() + " = ?");
				portlet.setHeight(70);
				portlet.addItem(label);
				portalLayout.addPortlet(portlet, (column++) % 3, 0);

				final Listener listener = new AbstractListener() {

					public void onMouseMove(ListenerEvent event) {
						label.setContents(I18nProvider.getSampleMessages().multipleListenersPortletText() + " = "
								+ event.getScreenPosition());
					}
				};
				map.addListener(listener);
				portlet.addCloseClickHandler(new CloseClickHandler() {

					public void onCloseClick(CloseClientEvent event) {
						map.removeListener(listener);
						if (map.getListeners() != null) {
							SC.say(I18nProvider.getSampleMessages().multipleListenersCount(map.getListeners().size()));
						}
						portlet.destroy();
					}
				});
				SC.say(I18nProvider.getSampleMessages().multipleListenersCount(map.getListeners().size()));
			}
		});
		buttonLayout.addMember(add);
		layout.addMember(buttonLayout);
		layout.addMember(portalLayout);
		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().multipleListenersDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/example/gwt/client/samples/controller/MultipleListenersSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "WEB-INF/layerOsm.xml", "WEB-INF/mapOsm.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
