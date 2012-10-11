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
package org.geomajas.plugin.deskmanager.client.gwt.geodesk;

import java.util.List;

import org.geomajas.configuration.client.ClientToolInfo;
import org.geomajas.gwt.client.action.ToolCreator;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.action.toolbar.ToolbarRegistry;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.action.CombinedSearchAction;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.action.FavouritesSearchAction;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.action.FreeSearchAction;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.action.SpatialSearchAction;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.action.ribbon.RefreshLayersAction;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.ribbon.MouseLocationRibbonColumn;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.ribbon.ScaleSelectRibbonColumn;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.widget.featureinfo.DefaultFeatureInfoCanvasBuilder;
import org.geomajas.widget.featureinfo.client.widget.factory.FeatureDetailWidgetFactory;
import org.geomajas.widget.utility.common.client.ribbon.RibbonColumn;
import org.geomajas.widget.utility.gwt.client.ribbon.RibbonButton;
import org.geomajas.widget.utility.gwt.client.ribbon.RibbonColumnRegistry;
import org.geomajas.widget.utility.gwt.client.ribbon.RibbonColumnRegistry.RibbonColumnCreator;

import com.google.gwt.core.client.EntryPoint;


/**
 * Entry point for the geodesks, this loads all necessary widgets. For starting up an actual geodesk application, see 
 * {@link GeodeskApplication}.
 * 
 * @author Oliver May
 *
 */
public class GeodeskEntryPoint implements EntryPoint {
	private static final String MOUSE_LOCATION_RIBBON_COLUMN_IDENTIFIER = "MouseLocationRibbonColumn";
	private static final String SCLE_SELECT_RIBBON_COLUMN_IDENTIFIER = "ScaleSelectRibbonColumn";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 */
	public void onModuleLoad() {
		initialize();
	}

	public void initialize() {
		ToolbarRegistry.put(FavouritesSearchAction.IDENTIFIER, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new FavouritesSearchAction();
			}
		});

		ToolbarRegistry.put(CombinedSearchAction.IDENTIFIER, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new CombinedSearchAction();
			}
		});

		ToolbarRegistry.put(SpatialSearchAction.IDENTIFIER, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new SpatialSearchAction();
			}
		});

		ToolbarRegistry.put(FreeSearchAction.IDENTIFIER, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new FreeSearchAction();
			}
		});

		FeatureDetailWidgetFactory.setDefaultVectorFeatureDetailWidgetBuilder(new DefaultFeatureInfoCanvasBuilder());

		RibbonColumnRegistry.put(MOUSE_LOCATION_RIBBON_COLUMN_IDENTIFIER, new RibbonColumnCreator() {

			public RibbonColumn create(List<ClientToolInfo> tools, MapWidget mapWidget) {
				return new MouseLocationRibbonColumn(mapWidget);
			}
		});
		RibbonColumnRegistry.put(SCLE_SELECT_RIBBON_COLUMN_IDENTIFIER, new RibbonColumnCreator() {

			public RibbonColumn create(List<ClientToolInfo> tools, MapWidget mapWidget) {
				return new ScaleSelectRibbonColumn(mapWidget);
			}
		});
		RibbonColumnRegistry.put(RefreshLayersAction.IDENTIFIER, new RibbonColumnCreator() {

			public RibbonColumn create(List<ClientToolInfo> tools, MapWidget mapWidget) {
				RibbonColumn rc = new RibbonButton(new RefreshLayersAction(mapWidget));
				return rc;
			}
		});
	}

}
