/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.printing.client.action.toolbar;

import org.geomajas.gwt.client.action.ConfigurableAction;
import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.printing.client.PrintingMessages;
import org.geomajas.plugin.printing.client.widget.PrintPreferencesCanvas;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Action to create a default PDF print of the map.
 * 
 * @author Jan De Moerloose
 * 
 */
public class PrintAction extends ToolbarAction implements ConfigurableAction {

	private PrintingMessages messages = GWT.create(PrintingMessages.class);

	private MapWidget mapWidget;

	public PrintAction(MapWidget mapWidget) {
		super("[ISOMORPHIC]/geomajas/osgeo/print.png", null);
		this.mapWidget = mapWidget;
		setTooltip(messages.printBtnTitle());
	}
	
	public void onClick(ClickEvent event) {
		PrintPreferencesCanvas canvas = new PrintPreferencesCanvas(mapWidget);
		canvas.setMargin(10);
		Window window = new Window();
		window.setTitle(messages.printPrefsTitle());
		window.addItem(canvas);
		window.centerInPage();
		window.setAutoSize(true);
		window.show();
	}

	public void configure(String key, String value) {
	}


}
