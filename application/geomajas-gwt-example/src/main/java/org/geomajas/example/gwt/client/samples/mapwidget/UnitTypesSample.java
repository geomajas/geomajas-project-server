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

package org.geomajas.example.gwt.client.samples.mapwidget;

import org.geomajas.configuration.client.UnitType;
import org.geomajas.example.gwt.client.samples.base.SamplePanel;
import org.geomajas.example.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.example.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows how to switch between european and english units.
 * </p>
 * 
 * @author Frank Wynants
 */
public class UnitTypesSample extends SamplePanel {

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
		final MapWidget map = new MapWidget("mapOsm", "gwt-samples");
		map.setController(new PanController(map));
		mapLayout.addMember(map);

		HLayout buttonLayout = new HLayout();
		buttonLayout.setMembersMargin(10);

		IButton butSwitch = new IButton(I18nProvider.getSampleMessages().switchUnitTypes());
		butSwitch.setWidth100();
		buttonLayout.addMember(butSwitch);

		butSwitch.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				if (map.getMapModel().getMapInfo().getDisplayUnitType() == UnitType.METRIC) {
					SC.say(I18nProvider.getSampleMessages().unitTypeEnglish());
					map.getMapModel().getMapInfo().setDisplayUnitType(UnitType.ENGLISH); // set English
				} else {
					SC.say(I18nProvider.getSampleMessages().unitTypeMetric());
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
		return I18nProvider.getSampleMessages().unitTypesDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/example/gwt/client/samples/mapwidget/UnitTypesSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "WEB-INF/layerOsm.xml",
				"WEB-INF/mapOsm.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
