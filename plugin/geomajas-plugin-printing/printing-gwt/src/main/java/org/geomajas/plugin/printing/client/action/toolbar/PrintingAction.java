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
package org.geomajas.plugin.printing.client.action.toolbar;

import org.geomajas.gwt.client.action.ConfigurableAction;
import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.KeepInScreenWindow;
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
 */
public class PrintingAction extends ToolbarAction implements ConfigurableAction {

	private static final PrintingMessages MESSAGES = GWT.create(PrintingMessages.class);

	private MapWidget mapWidget;

	/**
	 * Create action for given map widget.
	 *
	 * @param mapWidget map widget
	 */
	public PrintingAction(MapWidget mapWidget) {
		super(WidgetLayout.iconPrint, null);
		this.mapWidget = mapWidget;
		setTooltip(MESSAGES.printBtnTitle());
	}

	/** {@inheritDoc} */
	public void onClick(ClickEvent event) {
		PrintPreferencesCanvas canvas = new PrintPreferencesCanvas(mapWidget);
		canvas.setMargin(WidgetLayout.marginSmall);
		Window window = new KeepInScreenWindow();
		window.setTitle(MESSAGES.printPrefsTitle());
		window.addItem(canvas);
		window.centerInPage();
		window.setAutoSize(true);
		window.show();
	}

	/** {@inheritDoc} */
	public void configure(String key, String value) {
		// nothing to configure
	}

}
