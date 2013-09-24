#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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

package ${package}.client;

import com.google.gwt.user.client.Cookies;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.layout.Layout;
import org.geomajas.gwt.client.command.CommunicationExceptionCallback;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.event.MapModelChangedEvent;
import org.geomajas.gwt.client.map.event.MapModelChangedHandler;
import org.geomajas.gwt.client.util.CrocEyeNotificationHandler;
import org.geomajas.gwt.client.util.Log;
import org.geomajas.gwt.client.util.Notify;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.OverviewMap;

import com.google.gwt.core.client.EntryPoint;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.widget.advancedviews.client.widget.ExpandingThemeWidget;
import org.geomajas.widget.advancedviews.configuration.client.ThemesInfo;
import org.geomajas.widget.featureinfo.client.widget.DockableWindow;
import org.geomajas.widget.layer.client.widget.CombinedLayertree;
import org.geomajas.widget.searchandfilter.client.util.GsfLayout;
import org.geomajas.widget.searchandfilter.client.util.SearchCommService;
import org.geomajas.widget.searchandfilter.client.widget.attributesearch.AttributeSearchCreator;
import org.geomajas.widget.searchandfilter.client.widget.attributesearch.AttributeSearchPanel;
import org.geomajas.widget.searchandfilter.client.widget.geometricsearch.FreeDrawingSearch;
import org.geomajas.widget.searchandfilter.client.widget.geometricsearch.GeometricSearchCreator;
import org.geomajas.widget.searchandfilter.client.widget.geometricsearch.GeometricSearchPanel;
import org.geomajas.widget.searchandfilter.client.widget.geometricsearch.GeometricSearchPanelCreator;
import org.geomajas.widget.searchandfilter.client.widget.geometricsearch.SelectionSearch;
import org.geomajas.widget.searchandfilter.client.widget.multifeaturelistgrid.MultiFeatureListGrid;
import org.geomajas.widget.searchandfilter.client.widget.search.CombinedSearchCreator;
import org.geomajas.widget.searchandfilter.client.widget.search.CombinedSearchPanel;
import org.geomajas.widget.searchandfilter.client.widget.search.DockableWindowSearchWidget;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchEvent;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchHandler;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchWidget;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchWidgetRegistry;
import org.geomajas.widget.utility.gwt.client.ribbon.RibbonBarLayout;
import org.geomajas.widget.utility.gwt.client.util.GuwLayout;

/**
 * Entry point and main class for GWT application. This class defines the layout and functionality of this application.
 *
 * @author geomajas-gwt-archetype
 */
public class Application implements EntryPoint {

	private Window searchResult;

	private MapWidget map;

	private OverviewMap overviewMap;

	private MultiFeatureListGrid searchResultGrid;

	public void onModuleLoad() {
		initializeConstants();

		// Build all layout blocks.
		Layout header = createHeader();

		Layout footer = createFooter();

		HLayout layoutRibbon = new HLayout();
		layoutRibbon.setAlign(Alignment.CENTER);

		VLayout layoutWest = new VLayout();
		layoutWest.addStyleName("applicationLayoutWest");
		VLayout layoutCenter = new VLayout();
		layoutCenter.addStyleName("applicationLayoutCenter");

		VLayout mainLayout = new VLayout();
		layoutRibbon.setHeight(85);
		layoutRibbon.setOverflow(Overflow.HIDDEN);
		// layoutNorth.addMember(layoutNorthWest);
		// layoutNorth.addMember(layoutNorthCenter);
		// layoutNorth.addMember(layoutNorthEast);
		HLayout middle = new HLayout();
		middle.setHeight("*");
		layoutWest.setWidth(300);
		middle.addMember(layoutWest);
		middle.addMember(layoutCenter);
		mainLayout.addMember(header);
		mainLayout.addMember(layoutRibbon);
		mainLayout.addMember(middle);
		mainLayout.addMember(footer);
		mainLayout.addStyleName("applicationLayoutBody");
		mainLayout.setWidth100();
		mainLayout.setHeight100();

		// ---------------------------------------------------------------------
		// Create the left-side (map and tabs):
		// ---------------------------------------------------------------------
		map = new MapWidget("mapMain", "app");
		GsfLayout.searchWindowParentElement = map;

		// -- Create Ribbon
		final RibbonBarLayout ribbonBar = new RibbonBarLayout(map, "app", "ribbon-bar");
		ribbonBar.setAlign(Alignment.CENTER);
		ribbonBar.setOverflow(Overflow.HIDDEN);
		ribbonBar.setStyleName("msRibbon");
		ribbonBar.setMembersMargin(0);
		ribbonBar.setHeight(100);

		// LayerTree layout:
		CombinedLayertree layerTree = getCombinedLayertree();

		layoutRibbon.addMember(ribbonBar);
		layoutWest.addMember(layerTree);
		layoutCenter.addMember(map);

		mainLayout.draw();

		initialize();
	}

	private CombinedLayertree getCombinedLayertree() {
//	    if (!map.getMapModel().getMapInfo().getWidgetInfo().containsKey(ClientLayerTreeInfo.IDENTIFIER)) {
//			ClientBranchNodeInfo rootNode = new ClientBranchNodeInfo();
//			for (Layer layer : map.getMapModel().getLayers()) {
//				ClientLayerNodeInfo node = new ClientLayerNodeInfo();
//				node.setLayerId(layer.getId());
//				rootNode.getTreeNodes().add(node);
//			}
//			ClientLayerTreeInfo clti = new ClientLayerTreeInfo();
//			clti.setTreeNode(rootNode);
//			map.getMapModel().getMapInfo().getWidgetInfo().put(ClientLayerTreeInfo.IDENTIFIER, clti);
//		}
		return new CombinedLayertree(map);
	}

	private void addThemeWidget() {
		if (map.getMapModel().getMapInfo().getWidgetInfo().containsKey(ThemesInfo.IDENTIFIER)) {
			ExpandingThemeWidget themes = new ExpandingThemeWidget(map);
			themes.setParentElement(map);
			themes.setSnapTo("BL");
			themes.setSnapOffsetTop(-50);
			themes.setSnapOffsetLeft(10);
			themes.setShowShadow(false);
		}
	}

	private Layout createFooter() {
		Layout footer = new Layout();
		footer.setHeight(10);
		footer.addStyleName("applicationLayoutHeaderBar");
		return footer;
	}

	private Layout createHeader() {
		VLayout headerLayout = new VLayout();
		headerLayout.setHeight(5 + 52 + 5);

		Layout topBlackBar = new Layout();
	    topBlackBar.addStyleName("applicationLayoutHeaderBar");
		topBlackBar.setHeight(5);

		HLayout header = new HLayout();
		header.addStyleName("applicationLayoutHeader");
		header.setHeight(52);

		Layout bottomBlackBar = new Layout();
		bottomBlackBar.addStyleName("applicationLayoutHeaderBar");
		bottomBlackBar.setHeight(5);

		headerLayout.addMember(topBlackBar);
		headerLayout.addMember(header);
		headerLayout.addMember(bottomBlackBar);

		return headerLayout;
	}

	private void initializeConstants() {// register Global layout stuff
		GuwLayout.ribbonBarInternalMargin = 2;
		GuwLayout.ribbonGroupInternalMargin = 4;

		GuwLayout.DropDown.ribbonBarDropDownButtonDescriptionIconSize = 24;

		GuwLayout.ribbonTabOverflow = Overflow.HIDDEN;

		GsfLayout.searchWindowPositionType = DockableWindowSearchWidget.SearchWindowPositionType.SNAPTO;
		GsfLayout.searchWindowPosSnapTo = "TR";
//		GsfLayout.searchWindowPosLeft = -25;

		SearchCommService.searchResultSize = 500;

		//Register crock eye notificator.
		Notify.getInstance().setHandler(CrocEyeNotificationHandler.getInstance());
		GwtCommandDispatcher.getInstance().setCommandExceptionCallback(CrocEyeNotificationHandler.getInstance());
		GwtCommandDispatcher.getInstance().setCommunicationExceptionCallback(new CommunicationExceptionCallback() {
			@Override
			public void onCommunicationException(Throwable error) {
//				Hide communication errors from the user, but report to server (try once)
				String msg = I18nProvider.getGlobal().commandCommunicationError() + ":${symbol_escape}n" + error.getMessage();
				Log.logError(msg, error);
			}
		});

		WidgetLayout.crocEyePositionLeft = 150;
		GuwLayout.DropDown.showDropDownImage = false;

		Cookies.setCookie("skin_name", "Enterprise");
	}

	public void initialize() {

		initializeSearches();
		map.getMapModel().addMapModelChangedHandler(new MapModelChangedHandler() {

			public void onMapModelChanged(MapModelChangedEvent event) {
				PanAndZoomSlider.addPanAndZoomSliderTo(map);
				addThemeWidget();
			}
		});
	}

	private void initializeSearches() {
		// ---------------------------------------------------------------------
		// Create dataGrid where result will be shown
		// ---------------------------------------------------------------------
		setSearchResult(new DockableWindow());
		getSearchResult().setTitle("Search results"); // MESSAGES.searchResults());
		getSearchResult().setWidth(650);
		getSearchResult().setHeight(300);
		getSearchResult().moveTo(50, 75);
		getSearchResult().setKeepInParentRect(true);
		getSearchResult().setCanDragResize(true);
		setSearchResultGrid(new MultiFeatureListGrid(map));
		getSearchResultGrid().setClearTabsetOnSearch(true);
		getSearchResultGrid().setShowDetailsOnSingleResult(true);
		getSearchResultGrid().setShowCsvExportAction(false);
		getSearchResult().addItem(getSearchResultGrid());

		// ---------------------------------------------------------------------
		// Create Searchpanels
		// ---------------------------------------------------------------------
		SearchWidgetRegistry.initialize(map, getSearchResultGrid(), false);
		SearchWidgetRegistry.put(new AttributeSearchCreator() {
			public SearchWidget createInstance(MapWidget mapWidget) {
				AttributeSearchPanel asp = new AttributeSearchPanel(mapWidget);
				asp.setCanAddToFavourites(false);
				return new DockableWindowSearchWidget(IDENTIFIER, getSearchWidgetName(), asp);
			}
		});
		SearchWidgetRegistry.put(new GeometricSearchCreator(new GeometricSearchPanelCreator() {

			public GeometricSearchPanel createInstance(MapWidget mapWidget) {
				GeometricSearchPanel gsp = new GeometricSearchPanel(mapWidget);
				gsp.setCanAddToFavourites(false);
				gsp.addSearchMethod(new FreeDrawingSearch());
				gsp.addSearchMethod(new SelectionSearch());
				return gsp;
			}
		}));
		SearchWidgetRegistry.put(new CombinedSearchCreator() {
			public SearchWidget createInstance(MapWidget mapWidget) {
				CombinedSearchPanel csp = new CombinedSearchPanel(mapWidget);
				csp.initializeListUseAll();
				csp.setCanAddToFavourites(false);
				return new DockableWindowSearchWidget(IDENTIFIER, getSearchWidgetName(), csp);
			}
		});

		// -- show resultgrid after search is finished
		SearchWidgetRegistry.addSearchHandler(new SearchHandler() {

			public void onSearchStart(SearchEvent event) {
			}

			public void onSearchDone(SearchEvent event) {
				// handled by featureListGrid, no need for us to do something
			}

			public void onSearchEnd(SearchEvent event) {
				if (!(getSearchResultGrid().isShowDetailsOnSingleResult() && event.isSingleResult())) {
					showSearchResultWindow();
				}
			}
		});
	}

	/**
	 * @param searchResultGrid
	 *            the searchResultGrid to set
	 */
	protected void setSearchResultGrid(MultiFeatureListGrid searchResultGrid) {
		this.searchResultGrid = searchResultGrid;
	}

	/**
	 * @return the searchResultGrid
	 */
	protected MultiFeatureListGrid getSearchResultGrid() {
		return searchResultGrid;
	}

	public void showSearchResultWindow() {
		searchResult.show();
		searchResult.bringToFront();
	}

	/**
	 * @return the searchResult
	 */
	protected Window getSearchResult() {
		return searchResult;
	}

	/**
	 * @param searchResult
	 *            the searchResult to set
	 */
	protected void setSearchResult(Window searchResult) {
		this.searchResult = searchResult;
	}

}
