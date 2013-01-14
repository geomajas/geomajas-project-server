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
package org.geomajas.widget.searchandfilter.client.widget.attributesearch;

import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.client.widget.search.DockableWindowSearchWidget;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchWidget;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchWidgetCreator;

import com.google.gwt.core.client.GWT;

/**
 * Creator (or factory) pattern needed because GWT cannot instantiate classes
 * starting from name (as String).
 *
 * @author Kristof Heirwegh
 */
public class AttributeSearchCreator implements SearchWidgetCreator {

	public static final String IDENTIFIER = "AttributeSearch";

	private final SearchAndFilterMessages messages = GWT.create(SearchAndFilterMessages.class);

	public String getSearchWidgetId() {
		return IDENTIFIER;
	}

	public String getSearchWidgetName() {
		return messages.attributeSearchWidgetTitle();
	}

	public SearchWidget createInstance(MapWidget mapWidget) {
		return new DockableWindowSearchWidget(IDENTIFIER, getSearchWidgetName(), new AttributeSearchPanel(mapWidget));
	}
}
