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
package org.geomajas.plugin.deskmanager.client.gwt.geodesk.action;

import org.geomajas.gwt.client.action.ConfigurableAction;
import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.i18n.GeodeskMessages;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.util.Icon;
import org.geomajas.widget.searchandfilter.client.widget.geometricsearch.GeometricSearchCreator;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchWidgetRegistry;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * @author Oliver May
 * 
 */
public class SpatialSearchAction extends ToolbarAction implements ConfigurableAction {

	private static final GeodeskMessages MESSAGES = GWT.create(GeodeskMessages.class);
	public static final String IDENTIFIER = GeometricSearchCreator.IDENTIFIER;

	public SpatialSearchAction() {
		super(Icon.searchRuimtelijk, MESSAGES.searchRuimtelijkTitle(), MESSAGES.searchRuimtelijkTooltip());
	}

	public void configure(String key, String value) {
	}

	public void onClick(ClickEvent event) {
		SearchWidgetRegistry.getSearchWidgetInstance(GeometricSearchCreator.IDENTIFIER).showForSearch();
	}

}
