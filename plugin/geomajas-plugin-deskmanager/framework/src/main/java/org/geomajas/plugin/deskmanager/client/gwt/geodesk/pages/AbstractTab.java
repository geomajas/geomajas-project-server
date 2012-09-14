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
package org.geomajas.plugin.deskmanager.client.gwt.geodesk.pages;

import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

/**
 * Basic implementation for a tab. The tabs are added bottom left.
 * 
 * @author Jan De Moerloose
 */
public abstract class AbstractTab extends Tab {

	private final MapWidget map;

	private VLayout mainLayout;

	AbstractTab(String title, MapWidget map) {
		super(title);
		this.map = map;
		mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();
		setPane(mainLayout);
	}

	public abstract void initialize();

	public MapWidget getMap() {
		return map;
	}

	public VLayout getMainLayout() {
		return mainLayout;
	}
}
