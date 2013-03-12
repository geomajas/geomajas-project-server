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
package org.geomajas.puregwt.client.plugin.printing;

import org.geomajas.plugin.printing.client.widget.PrintPanel;
import org.geomajas.puregwt.client.ContentPanel;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.widget.MapLayoutPanel;
import org.geomajas.puregwt.widget.client.gadget.LegendDropDownGadget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Panel for printing sample.
 * 
 * @author Jan De Moerloose
 * 
 */
public class PrintingPanel extends ContentPanel {

	/**
	 * UI binder interface for this panel.
	 * 
	 * @author Jan De Moerloose
	 */
	interface MyUiBinder extends UiBinder<Widget, PrintingPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	@UiField
	protected SimplePanel printPanel;

	@UiField
	protected MapLayoutPanel mapPanel;

	public PrintingPanel(MapPresenter mapPresenter) {
		super(mapPresenter);
	}

	@Override
	public String getTitle() {
		return "Printing";
	}

	@Override
	public String getDescription() {
		return "Demonstrates printing capabilities";
	}

	@Override
	public Widget getContentWidget() {
		Widget widget = UI_BINDER.createAndBindUi(this);
		// Initialize the map, and return the layout:
		mapPresenter.initialize("puregwt-app", "mapPrinting");
		mapPresenter.addMapGadget(new LegendDropDownGadget());
		mapPanel.setPresenter(mapPresenter);
		printPanel.setWidget(new PrintPanel(mapPresenter, "puregwt-app"));
		return widget;
	}

}
