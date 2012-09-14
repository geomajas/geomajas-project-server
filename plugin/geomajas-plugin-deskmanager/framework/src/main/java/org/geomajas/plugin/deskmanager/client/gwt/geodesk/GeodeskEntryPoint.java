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
 * @author Oliver May
 *
 */
public class GeodeskEntryPoint implements EntryPoint {
	// FIXME: move keys to Actions
	public static final String MOUSELOCATION_KEY = "MouseLocationColumn";

	public static final String REFRESHLAYERS_KEY = "RefreshLayersButton";

	public static final String SCALESELECT_KEY = "ScaleSelectColumn";

	private static final String SEARCH_FAVORIET_ACTION_KEY = "SearchFavoriet";

	private static final String SEARCH_GECOMBINEERD_ACTION_KEY = "SearchGecombineerd";

	private static final String SEARCH_RUIMTELIJK_ACTION_KEY = "SearchRuimtelijk";

	private static final String SEARCH_VRIJ_ACTION_KEY = "SearchVrij";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 */
	public void onModuleLoad() {
		initialize();
	}

	public void initialize() {
		ToolbarRegistry.put(SEARCH_FAVORIET_ACTION_KEY, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new FavouritesSearchAction();
			}
		});

		ToolbarRegistry.put(SEARCH_GECOMBINEERD_ACTION_KEY, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new CombinedSearchAction();
			}
		});

		ToolbarRegistry.put(SEARCH_RUIMTELIJK_ACTION_KEY, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new SpatialSearchAction();
			}
		});

		ToolbarRegistry.put(SEARCH_VRIJ_ACTION_KEY, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new FreeSearchAction();
			}
		});

		FeatureDetailWidgetFactory.setDefaultVectorFeatureDetailWidgetBuilder(new DefaultFeatureInfoCanvasBuilder());

		RibbonColumnRegistry.put(MOUSELOCATION_KEY, new RibbonColumnCreator() {

			public RibbonColumn create(List<ClientToolInfo> tools, MapWidget mapWidget) {
				return new MouseLocationRibbonColumn(mapWidget);
			}
		});
		RibbonColumnRegistry.put(SCALESELECT_KEY, new RibbonColumnCreator() {

			public RibbonColumn create(List<ClientToolInfo> tools, MapWidget mapWidget) {
				return new ScaleSelectRibbonColumn(mapWidget);
			}
		});
		RibbonColumnRegistry.put(REFRESHLAYERS_KEY, new RibbonColumnCreator() {

			public RibbonColumn create(List<ClientToolInfo> tools, MapWidget mapWidget) {
				RibbonColumn rc = new RibbonButton(new RefreshLayersAction(mapWidget));
				return rc;
			}
		});
	}

}
