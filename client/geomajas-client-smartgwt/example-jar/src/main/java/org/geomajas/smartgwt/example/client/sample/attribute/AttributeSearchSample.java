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

package org.geomajas.smartgwt.example.client.sample.attribute;

import com.google.gwt.core.client.GWT;
import org.geomajas.smartgwt.example.base.SamplePanel;
import org.geomajas.smartgwt.example.base.SamplePanelFactory;
import org.geomajas.smartgwt.client.widget.FeatureListGrid;
import org.geomajas.smartgwt.client.widget.FeatureSearch;
import org.geomajas.smartgwt.client.widget.MapWidget;
import org.geomajas.smartgwt.client.widget.event.DefaultSearchHandler;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import org.geomajas.smartgwt.example.client.sample.i18n.SampleMessages;

/**
 * <p>
 * Sample that tests security on attribute level.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class AttributeSearchSample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String TITLE = "AttributeFilter";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new AttributeSearchSample();
		}
	};

	public Canvas getViewPanel() {
		final VLayout layout = new VLayout();
		layout.setMembersMargin(10);
		layout.setWidth100();
		layout.setHeight100();

		// Map with ID beansMap is defined in the XML configuration. (contains any type of attribute)
		final MapWidget map = new MapWidget("mapBeansAssociation", "gwtExample");
		map.setVisible(false);
		layout.addMember(map);
		map.init();

		// Create a layout with a FeatureListGrid in it:
		final FeatureListGrid grid = new FeatureListGrid(map.getMapModel());
		grid.setShowEdges(true);

		// Create a search widget that displays it's results in the FeatureListGrid:
		final FeatureSearch search = new FeatureSearch(map.getMapModel(), true);
		search.addSearchHandler(new DefaultSearchHandler(grid) {

			public void afterSearch() {
			}
		});
		
		// Create a layout for the search, and add a title in it:
		VLayout searchLayout = new VLayout();
		searchLayout.setHeight("40%");
		searchLayout.setShowEdges(true);
		searchLayout.setMembersMargin(10);
		
		ToolStrip title = new ToolStrip();
		title.setWidth100();
		Label titleLabel = new Label(MESSAGES.search2InnerTitle());
		titleLabel.setWidth100();
		title.addSpacer(10);
		title.addMember(titleLabel);
		searchLayout.addMember(title);
		searchLayout.addMember(search);
		searchLayout.setShowResizeBar(true);

		// Add the search and the grid to the layout:
		layout.addMember(searchLayout);
		layout.addMember(grid);

		return layout;
	}

	public String getDescription() {
		return MESSAGES.search2Description();
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/smartgwt/example/context/layerBeans.xml",
				"classpath:org/geomajas/smartgwt/example/context/mapBeans.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
