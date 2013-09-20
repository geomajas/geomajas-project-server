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

package ${package}.client.gui;


import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.event.MapModelChangedEvent;
import org.geomajas.gwt.client.map.event.MapModelChangedHandler;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.FeatureListGrid;
import org.geomajas.gwt.client.widget.FeatureSearch;
import org.geomajas.gwt.client.widget.event.DefaultSearchHandler;

import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Layout for the search panel (see buttons top right on the map).
 * 
 * @author Pieter De Graef
 */
public class SearchPanel extends VLayout {

	private static final int PANEL_WIDTH = 580;

	private static final int PANEL_HEIGHT = 200;

	private static final int BTN_WIDTH = 24;

	private static final int TOP_OFFSET = 35;

	/** The layout containing the search and grid layouts. */
	private VLayout panelLayout;

	/** The layout containing the search widget. */
	private Canvas searchPanel;

	/** The layout containing the grid widget. */
	private Canvas gridPanel;

	/** The grid for displaying feature attributes. Used in the gridPanel. */
	private FeatureListGrid featureListGrid;

	/** The button that opens the search layout. */
	private IButton searchButton;

	/** The button that opens the grid layout. */
	private IButton gridButton;

	/** Clickable image that closes the whole layout again. */
	private Img closeBtn;

	// ------------------------------------------------------------------------
	// Constructor:
	// ------------------------------------------------------------------------

	/**
	 * The only constructor.
	 * 
	 * @param mapModel
	 *            The map's model. It knows what layers we can search in.
	 * @param parent
	 *            The map's parent widget. We use this to attach the search layout to.
	 */
	public SearchPanel(final MapModel mapModel, final Canvas parent) {
		// Set some parameters:
		setTop(TOP_OFFSET);
		setLeft(parent.getWidth() - BTN_WIDTH - 8);
		setWidth(BTN_WIDTH);
		setHeight(BTN_WIDTH + BTN_WIDTH + 8);
		setStyleName("search_panel_buttons");

		// Create the button GUI:
		createButtonGui();

		// Create the 2 panels:
		panelLayout = new VLayout();
		panelLayout.setBackgroundColor("#647386");
		panelLayout.setTop(TOP_OFFSET);
		panelLayout.setLeft(parent.getWidth() - PANEL_WIDTH);
		panelLayout.setHeight(PANEL_HEIGHT);
		panelLayout.setWidth(PANEL_WIDTH);
		panelLayout.setStyleName("search_panel");
		panelLayout.setVisible(false);

		gridPanel = createGridPanel(mapModel);
		searchPanel = createSearchPanel(mapModel);
		panelLayout.addChild(searchPanel);
		panelLayout.addChild(gridPanel);

		parent.addChild(panelLayout);

		// Add a handler that makes sure this widget is placed at the correct location when the parent widget resizes:
		parent.addResizedHandler(new ResizedHandler() {

			public void onResized(ResizedEvent event) {
				setTop(TOP_OFFSET);
				setLeft(parent.getWidth() - BTN_WIDTH - 8);
				panelLayout.setLeft(parent.getWidth() - PANEL_WIDTH);
			}
		});
	}

	// ------------------------------------------------------------------------
	// Public methods concerning showing and hiding the panels:
	// ------------------------------------------------------------------------

	/** Returns the panel that is currently open. If no panel is open, then null is returned. */
	public Canvas getOpenPanel() {
		if (searchPanel.isVisible()) {
			return searchPanel;
		} else if (gridPanel.isVisible()) {
			return gridPanel;
		}
		return null;
	}

	/**
	 * Is a certain panel open or not?
	 * 
	 * @param panel
	 *            The panel in question.
	 * @return Returns true or false.
	 */
	public boolean isOpen(Canvas panel) {
		return panel.isVisible();
	}

	/**
	 * Hide the open panel again - no matter which one is open. If no panel is open than nothing will happen.
	 */
	public void hidePanel() {
		Canvas panel = getOpenPanel();
		if (panel != null) {
			panel.setVisible(false);
			panelLayout.setVisible(false);
			gridButton.setSelected(false);
			searchButton.setSelected(false);
			closeBtn.setVisible(false);
			setHeight(52);
		}
	}

	/**
	 * Open either the search or the grid panel. (this piece of code could be better....)
	 * 
	 * @param panel
	 *            The search or the grid panel to show.
	 */
	public void showPanel(final Canvas panel) {
		if (panel == searchPanel) {
			gridPanel.setVisible(false);
			gridButton.setSelected(false);
			searchButton.setSelected(true);
		} else {
			searchPanel.setVisible(false);
			searchButton.setSelected(false);
			gridButton.setSelected(true);
		}
		panel.setVisible(true);
		panelLayout.setVisible(true);
		closeBtn.setVisible(true);
		setHeight(PANEL_HEIGHT);
	}

	// ------------------------------------------------------------------------
	// Private methods concerning GUI:
	// ------------------------------------------------------------------------

	/** Create the button GUI and attach the buttons to this widget. */
	private void createButtonGui() {
		// The search button opens the search widget:
		searchButton = new IButton();
		searchButton.setIcon(WidgetLayout.iconFind);
		searchButton.setSize(BTN_WIDTH + "px", BTN_WIDTH + "px");
		searchButton.setRadioGroup("panels");
		searchButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (isOpen(searchPanel)) {
					hidePanel();
				} else {
					showPanel(searchPanel);
				}
			}
		});

		// The grid button opens the feature list grid:
		gridButton = new IButton();
		gridButton.setIcon(WidgetLayout.iconTable);
		gridButton.setSize(BTN_WIDTH + "px", BTN_WIDTH + "px");
		gridButton.setRadioGroup("panels");
		gridButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (isOpen(gridPanel)) {
					hidePanel();
				} else {
					showPanel(gridPanel);
				}
			}
		});

		// A close button for closing. Only visible when a panel is open:
		closeBtn = new Img("[ISOMORPHIC]/images/close.png", BTN_WIDTH, BTN_WIDTH);
		closeBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				hidePanel();
			}
		});
		closeBtn.setCursor(Cursor.HAND);
		closeBtn.setVisible(false);

		// Add all buttons to this widget:
		addMember(searchButton);
		addMember(gridButton);
		addMember(new LayoutSpacer());
		addMember(closeBtn);
	}

	/** Create the layout for the search panel. Also connect it to the feature grid. */
	private Canvas createSearchPanel(final MapModel mapModel) {
		VLayout layout = new VLayout();
		layout.setSize("100%", "100%");
		layout.setVisible(false);
		layout.setStyleName("search_panel_inner");

		// Create a SearchWidget, based upon a map's model:
		final FeatureSearch searchWidget = new FeatureSearch(mapModel, true);

		// What to do when the result of a search comes in?
		// The DefaultSearchHandler will add all the features in the result to the given FeatureListGrid.
		searchWidget.addSearchHandler(new DefaultSearchHandler(featureListGrid) {

			// After the features have been added to the FeatureListGrid, make sure the tab with the grid is visible:
			public void afterSearch() {
				showPanel(gridPanel);
			}
		});

		// Limit the maximum number of features that a search may produce:
		searchWidget.setMaximumResultSize(20);
		searchWidget.setBackgroundColor("#F0F0F0");
		layout.addMember(searchWidget);

		mapModel.addMapModelChangedHandler(new MapModelChangedHandler() {

			// On map initialization: Select the countries layer in the search panel.
			public void onMapModelChanged(MapModelChangedEvent event) {
				VectorLayer layer = mapModel.getVectorLayers().get(0);
				searchWidget.setLayer(layer);
			}
		});

		return layout;
	}

	/** Create the panel containing the feature grid. This will display the search results. */
	private Canvas createGridPanel(MapModel mapModel) {
		VLayout layout = new VLayout();
		layout.setSize("100%", "100%");
		layout.setVisible(false);
		layout.setStyleName("search_panel_inner");

		// Create the FeatureListGrid - a table for displaying attributes:
		featureListGrid = new FeatureListGrid(mapModel);
		featureListGrid.setEditingEnabled(true);
		layout.addMember(featureListGrid);
		return layout;
	}
}