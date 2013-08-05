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

package org.geomajas.smartgwt.example.client.sample.mapwidget;

import com.google.gwt.core.client.GWT;
import org.geomajas.configuration.client.UnitType;
import org.geomajas.smartgwt.example.base.SamplePanel;
import org.geomajas.smartgwt.example.base.SamplePanelFactory;
import org.geomajas.smartgwt.client.controller.PanController;
import org.geomajas.smartgwt.client.widget.MapWidget;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.smartgwt.example.client.sample.i18n.SampleMessages;

/**
 * <p>
 * Sample that shows how to switch between european and english units.
 * </p>
 * 
 * @author Frank Wynants
 */
public class UnitTypesSample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String TITLE = "UnitTypes";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new UnitTypesSample();
		}
	};

	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(10);

		HLayout mapLayout = new HLayout();
		mapLayout.setShowEdges(true);
		mapLayout.setHeight("60%");

		// Map with ID mapOsm is defined in the XML configuration. (mapOsm.xml)
		final MapWidget map = new MapWidget("mapOsm", "gwtExample");
		map.setController(new PanController(map));
		mapLayout.addMember(map);

		HLayout buttonLayout = new HLayout();
		buttonLayout.setMembersMargin(10);

		IButton butSwitch = new IButton(MESSAGES.switchUnitTypes());
		butSwitch.setWidth100();
		buttonLayout.addMember(butSwitch);

		butSwitch.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				if (map.getMapModel().getMapInfo().getDisplayUnitType() == UnitType.METRIC) {
					SC.say(MESSAGES.unitTypeEnglish());
					map.getMapModel().getMapInfo().setDisplayUnitType(UnitType.ENGLISH); // set English
				} else {
					SC.say(MESSAGES.unitTypeMetric());
					map.getMapModel().getMapInfo().setDisplayUnitType(UnitType.METRIC); // set Metric
				}
				map.setScalebarEnabled(false); // force an update of the scale-bar...
				map.setScalebarEnabled(true); // force an update of the scale-bar...
			}
		});

		layout.addMember(mapLayout);
		layout.addMember(buttonLayout);

		return layout;
	}

	public String getDescription() {
		return MESSAGES.unitTypesDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] {
				"classpath:org/geomajas/smartgwt/example/context/mapOsm.xml",
				"classpath:org/geomajas/smartgwt/example/base/layerOsm.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
