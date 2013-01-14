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
package org.geomajas.widget.searchandfilter.client.widget.searchfavourites;

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.client.util.CriterionUtil;
import org.geomajas.widget.searchandfilter.client.util.DataCallback;
import org.geomajas.widget.searchandfilter.client.util.FavouritesCommService;
import org.geomajas.widget.searchandfilter.client.widget.attributesearch.AttributeSearchCreator;
import org.geomajas.widget.searchandfilter.client.widget.attributesearch.AttributeSearchPanel;
import org.geomajas.widget.searchandfilter.client.widget.search.CombinedSearchCreator;
import org.geomajas.widget.searchandfilter.client.widget.search.CombinedSearchPanel;
import org.geomajas.widget.searchandfilter.client.widget.search.FavouritesController.FavouriteChangeHandler;
import org.geomajas.widget.searchandfilter.client.widget.search.FavouritesController.FavouriteEvent;
import org.geomajas.widget.searchandfilter.client.widget.search.AbstractSearchPanel;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchWidget;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchWidget.SaveRequestEvent;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchWidget.SaveRequestHandler;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchWidgetRegistry;
import org.geomajas.widget.searchandfilter.search.dto.Criterion;
import org.geomajas.widget.searchandfilter.search.dto.SearchFavourite;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * A simple system of searchFavourites. Favourites can be either private or
 * shared (with everybody).
 * 
 * @author Kristof Heirwegh
 * @since 1.0.0
 */
@Api
public class SearchFavouritesListPanel extends AbstractSearchPanel implements FavouriteChangeHandler {

	private static final SearchAndFilterMessages MESSAGES = GWT.create(SearchAndFilterMessages.class);

	private SearchWidget parent;
	private ListGrid favouriteItems;

	public SearchFavouritesListPanel(final MapWidget mapWidget) {
		super(mapWidget);

		// -- grid --
		favouriteItems = new FavouritesListGrid();
		favouriteItems.setWidth100();
		favouriteItems.setHeight100();
		favouriteItems.setCanExpandRecords(true);
		favouriteItems.setCanExpandMultipleRecords(false);
		favouriteItems.setShowAllRecords(true);
		favouriteItems.setShowRecordComponents(false);
		favouriteItems.setShowRecordComponentsByCell(false);
		favouriteItems.setShowRollOverCanvas(true);
		favouriteItems.setSelectionType(SelectionStyle.SINGLE);
		favouriteItems.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				if (parent != null) {
					if (favouriteItems.getSelectedRecord() != event.getRecord()) {
						favouriteItems.selectRecord(event.getRecord());
					}
					parent.startSearch();
				}
			}
		});
		ListGridField nameField = new ListGridField(FavouriteListRecord.NAME_FIELD,
				MESSAGES.searchFavouritesListWidgetFavourites());
		nameField.setType(ListGridFieldType.TEXT);

		favouriteItems.setFields(nameField);

		// ----------------------------------------------------------

		setWidth(350);
		setHeight(250);
		addChild(favouriteItems);

		initializeList();
		SearchWidgetRegistry.addFavouriteChangeHandler(this);
	}

	@Override
	public boolean validate() {
		if (favouriteItems.getSelectedRecord() != null) {
			return true;
		} else {
			SC.say(MESSAGES.searchFavouritesListWidgetNoSelection());
			return false;
		}
	}

	@Override
	public Criterion getFeatureSearchCriterion() {
		FavouriteListRecord flr = (FavouriteListRecord) favouriteItems.getSelectedRecord();
		if (flr != null) {
			return flr.getFavourite().getCriterion();
		} else {
			return null;
		}
	}
	
	@Override
	public VectorLayer getFeatureSearchVectorLayer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reset() {
		favouriteItems.deselectAllRecords();
	}

	@Override
	public void hide() {
		SearchWidgetRegistry.removeFavouriteChangeHandler(this);
	}

	@Override
	public void initialize(Criterion featureSearch) {
		GWT.log("You cannot reinitialize the favourites search panel!");
	}

	@Override
	public boolean canAddToFavourites() {
		return false;
	}

	@Override
	public boolean canReset() {
		return false;
	}

	public void setSearchWidget(SearchWidget searchWidget) {
		this.parent = searchWidget;
	}

	// ----------------------------------------------------------

	/**
	 * get favourites from store
	 */
	private void initializeList() {
		FavouritesCommService.getSearchFavourites(new DataCallback<List<SearchFavourite>>() {
			public void execute(List<SearchFavourite> result) {
				favouriteItems.getDataAsRecordList().removeList(favouriteItems.getRecords());
				for (SearchFavourite sf : result) {
					favouriteItems.addData(new FavouriteListRecord(sf));
				}
			}
		});
	}

	private void updateLayerFilters() {
		CriterionUtil.clearLayerFilters(mapWidget);
		for (ListGridRecord lgr : favouriteItems.getRecords()) {
			FavouriteListRecord flr = (FavouriteListRecord) lgr;
			if (flr.isFilterActivated()) {
				CriterionUtil.setLayerFilter(mapWidget, flr.getFavourite().getCriterion());
			}
		}
	}

	// ----------------------------------------------------------

	/**
	 * Custom ListGrid.
	 * 
	 * @author Kristof Heirwegh
	 */
	private class FavouritesListGrid extends ListGrid {

		private static final String BTN_DELETE_IMG = "[SKIN]/actions/remove.png";
		private static final String BTN_SEARCH_IMG = "[ISOMORPHIC]/geomajas/silk/find.png";
		private static final String BTN_FILTER_IMG = "[ISOMORPHIC]/geomajas/smartgwt/filter.png";

		private HLayout rollOverTools;
		private ImgButton filterBtn;
		private FavouriteListRecord rollOverRecord;

		@Override
		protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {
			rollOverRecord = (FavouriteListRecord) getRecord(rowNum);

			if (rollOverTools == null) {
				rollOverTools = new HLayout();
				rollOverTools.setSnapTo("TR");
				rollOverTools.setWidth(40);
				rollOverTools.setHeight(22);
				ImgButton searchBtn = new ImgButton();
				searchBtn.setShowDown(false);
				searchBtn.setShowRollOver(false);
				searchBtn.setLayoutAlign(Alignment.CENTER);
				searchBtn.setSrc(BTN_SEARCH_IMG);
				searchBtn.setPrompt(MESSAGES.searchWidgetSearch());
				searchBtn.setHeight(16);
				searchBtn.setWidth(16);
				searchBtn.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						favouriteItems.deselectAllRecords();
						favouriteItems.selectRecord(rollOverRecord);
						parent.startSearch();
					}
				});
				filterBtn = new ImgButton();
				filterBtn.setActionType(SelectionType.CHECKBOX);
				filterBtn.setShowDown(false);
				filterBtn.setShowRollOver(false);
				filterBtn.setSrc(BTN_FILTER_IMG);
				filterBtn.setLayoutAlign(Alignment.CENTER);
				filterBtn.setPrompt(MESSAGES.searchFavouritesListWidgetFilter());
				filterBtn.setHeight(16);
				filterBtn.setWidth(16);
				filterBtn.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						rollOverRecord.setFilterActivated(!rollOverRecord.isFilterActivated());
						updateLayerFilters();
					}
				});
				rollOverTools.addMember(filterBtn);
				rollOverTools.addMember(searchBtn);
			}

			if (rollOverRecord != null) {
				filterBtn.setSelected(rollOverRecord.isFilterActivated());
			}
			return rollOverTools;
		}

		@Override
		protected Canvas getExpansionComponent(final ListGridRecord record) {
			final FavouriteListRecord flr = (FavouriteListRecord) record;
			final SearchFavourite fav = flr.getFavourite();
			VLayout layout = new VLayout(5);
			layout.setWidth100();
			layout.setPadding(5);
			VLayout group = new VLayout(5);
			group.setBackgroundColor("white");
			group.setPadding(5);
			group.setIsGroup(true);
			group.setWidth100();
			group.setGroupTitle(MESSAGES.favouritesControllerAddGroupTitle());

			final DynamicForm form = new DynamicForm();
			final TextItem nameItem = new TextItem();
			nameItem.setWidth(190);
			nameItem.setTitle(MESSAGES.favouritesControllerAddName());
			nameItem.setTooltip(MESSAGES.favouritesControllerAddNameTooltip());
			nameItem.setRequired(true);
			final CheckboxItem sharedItem = new CheckboxItem();
			sharedItem.setTitle(MESSAGES.favouritesControllerAddShared());
			sharedItem.setTooltip(MESSAGES.favouritesControllerAddSharedTooltip());
			final TextItem lastEditItem = new TextItem();
			lastEditItem.setWidth(190);
			lastEditItem.setDisabled(true);
			lastEditItem.setTitle(MESSAGES.searchFavouritesListWidgetLastChangeBy());
			final DateItem lastEditDateItem = new DateItem();
			lastEditDateItem.setDisabled(true);
			lastEditDateItem.setUseTextField(true);
			lastEditDateItem.setTitle(MESSAGES.searchFavouritesListWidgetLastChange());
			form.setFields(nameItem, sharedItem, lastEditItem, lastEditDateItem);

			HLayout buttonlayout = new HLayout(10);
			buttonlayout.setHeight(20);
			buttonlayout.setWidth100();
			HLayout frmButtonlayout = new HLayout(10);
			frmButtonlayout.setHeight(20);
			frmButtonlayout.setWidth100();

			final IButton saveButton = new IButton(MESSAGES.searchFavouritesListWidgetSave());
			saveButton.setIcon(WidgetLayout.iconSave);
			saveButton.setAutoFit(true);
			saveButton.setShowDisabledIcon(false);
			saveButton.setDisabled(true);
			saveButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					SearchFavourite oldFav = flr.getFavourite();
					SearchFavourite newFav = (SearchFavourite) oldFav.clone();
					newFav.setName(nameItem.getValueAsString());
					newFav.setShared(sharedItem.getValueAsBoolean());
					flr.setFavourite(newFav);
					SearchWidgetRegistry.getFavouritesController().onChangeRequested(
							new FavouriteEvent(oldFav, newFav, SearchFavouritesListPanel.this));
				}
			});

			final IButton cancelButton = new IButton(MESSAGES.searchFavouritesListWidgetCancel());
			cancelButton.setIcon(WidgetLayout.iconCancel);
			cancelButton.setAutoFit(true);
			cancelButton.setDisabled(true);
			cancelButton.setShowDisabledIcon(false);
			cancelButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					FavouritesListGrid.this.collapseRecord(record);
					FavouritesListGrid.this.expandRecord(record);
				}
			});

			form.addItemChangedHandler(new ItemChangedHandler() {
				public void onItemChanged(ItemChangedEvent event) {
					if (saveButton.isDisabled()) {
						saveButton.setDisabled(false);
						cancelButton.setDisabled(false);
					}
				}
			});

			IButton editCritButton = new IButton(MESSAGES.searchFavouritesListWidgetEditFilter());
			editCritButton.setIcon(WidgetLayout.iconEdit);
			editCritButton.setAutoFit(true);
			editCritButton.setShowDisabledIcon(false);
			editCritButton.setTooltip(MESSAGES.searchFavouritesListWidgetEditFilterTooltip());
			editCritButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					SearchWidget sw = null;
					if (AttributeSearchPanel.canHandle(fav.getCriterion())) {
						sw = SearchWidgetRegistry.getSearchWidgetInstance(AttributeSearchCreator.IDENTIFIER);
					}
					if (sw == null && CombinedSearchPanel.canHandle(fav.getCriterion())) {
						sw = SearchWidgetRegistry.getSearchWidgetInstance(CombinedSearchCreator.IDENTIFIER);
					}
					if (sw == null) {
						SC.say(MESSAGES.searchFavouritesListWidgetSearchWindowNotFound());
						return;
					}

					sw.showForSave(new SaveRequestHandler() {
						public void onSaveRequested(SaveRequestEvent event) {
							SearchFavourite oldFav = flr.getFavourite();
							SearchFavourite newFav = (SearchFavourite) oldFav.clone();
							newFav.setCriterion(event.getCriterion());
							flr.setFavourite(newFav);
							SearchWidgetRegistry.getFavouritesController().onChangeRequested(
									new FavouriteEvent(oldFav, newFav, SearchFavouritesListPanel.this));
						}

						public void onCancelRequested(SaveRequestEvent event) {
						}
					});
					sw.initialize(fav.getCriterion());
				}
			});

			IButton deleteButton = new IButton(MESSAGES.searchFavouritesListWidgetDelete());
			deleteButton.setIcon(BTN_DELETE_IMG);
			deleteButton.setAutoFit(true);
			deleteButton.setShowDisabledIcon(false);
			deleteButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					SC.ask(MESSAGES.searchFavouritesListWidgetDelete(),
							MESSAGES.searchFavouritesListWidgetDeleteMessage(), new BooleanCallback() {
								public void execute(Boolean value) {
									if (value) {
										favouriteItems.collapseRecord(record);
										SearchWidgetRegistry.getFavouritesController().onDeleteRequested(
												new FavouriteEvent(flr.getFavourite(), null,
														SearchFavouritesListPanel.this));
									}
								}
							});
				}
			});

			LayoutSpacer lsrFrm = new LayoutSpacer();
			lsrFrm.setWidth("*");

			// ----------------------------------------------------------

			frmButtonlayout.addMember(lsrFrm);
			frmButtonlayout.addMember(saveButton);
			frmButtonlayout.addMember(cancelButton);
			group.addMember(form);
			group.addMember(frmButtonlayout);

			buttonlayout.addMember(editCritButton);
			buttonlayout.addMember(deleteButton);

			layout.addMember(group);
			layout.addMember(buttonlayout);

			// ----------------------------------------------------------
			// -- set record values --
			// ----------------------------------------------------------

			nameItem.setValue(fav.getName());
			sharedItem.setValue(fav.isShared());
			lastEditItem.setValue(fav.getLastChangeBy());
			lastEditDateItem.setValue(fav.getLastChange());
			editCritButton.setDisabled(!AttributeSearchPanel.canHandle(fav.getCriterion())
					&& !CombinedSearchPanel.canHandle(fav.getCriterion()));

			return layout;
		}
	}

	/**
	 * Used by Grid.
	 *
	 * @author Kristof Heirwegh
	 */
	private static class FavouriteListRecord extends ListGridRecord {

		public static final String ID_FIELD = "idField";
		public static final String NAME_FIELD = "nameField";

		private SearchFavourite favourite;
		private boolean filterActivated;

		public FavouriteListRecord(SearchFavourite fav) {
			setFavourite(fav);
		}

		public void setFavourite(SearchFavourite fav) {
			this.favourite = fav;
			setAttribute(ID_FIELD, fav.getId());
			setAttribute(NAME_FIELD, fav.getName());
			filterActivated = CriterionUtil.isActiveLayerFilter(fav.getCriterion());
		}

		public SearchFavourite getFavourite() {
			return favourite;
		}

		public void setFilterActivated(boolean state) {
			if (filterActivated != state) {
				filterActivated = state;
			}
		}

		public boolean isFilterActivated() {
			return filterActivated;
		}
	}

	// ----------------------------------------------------------
	// -- FavouriteChangeHandler --
	// ----------------------------------------------------------

	public void onAdd(FavouriteEvent event) {
		FavouriteListRecord flr = new FavouriteListRecord(event.getNewFavourite());
		favouriteItems.addData(flr);
	}

	public void onDelete(FavouriteEvent event) {
		FavouriteListRecord flr = (FavouriteListRecord) favouriteItems.getDataAsRecordList().find(
				FavouriteListRecord.ID_FIELD, event.getOldFavourite().getId());
		if (flr != null) {
			favouriteItems.removeData(flr);
		}
	}

	public void onChange(FavouriteEvent event) {
		FavouriteListRecord flr = (FavouriteListRecord) favouriteItems.getDataAsRecordList().find(
				FavouriteListRecord.ID_FIELD, event.getNewFavourite().getId());
		if (flr != null) {
			flr.setFavourite(event.getNewFavourite());
			favouriteItems.collapseRecord(flr);
			favouriteItems.expandRecord(flr);
		}
	}
}
