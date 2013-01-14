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
package org.geomajas.widget.searchandfilter.client.widget.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.gfx.paintable.GfxGeometry;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.client.util.GsfLayout;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchWidget.SaveRequestEvent;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchWidget.SaveRequestHandler;
import org.geomajas.widget.searchandfilter.search.dto.AndCriterion;
import org.geomajas.widget.searchandfilter.search.dto.Criterion;
import org.geomajas.widget.searchandfilter.search.dto.GeometryCriterion;
import org.geomajas.widget.searchandfilter.search.dto.OrCriterion;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Panel for editing search criteria for a combined search. This handles the combination of the criteria. The actual
 * criteria are handled by other {@link AbstractSearchPanel} implementations.
 *
 * @author Kristof Heirwegh
 * @since 1.0.0
 */
@Api
public class CombinedSearchPanel extends AbstractSearchPanel {

	private static final String GEOMETRY_IDENTIFIER = "CombinedSearchPanelGeometry";

	private final SearchAndFilterMessages messages = GWT.create(SearchAndFilterMessages.class);

	private DynamicForm operatorForm;
	private RadioGroupItem type;
	private ListGrid searchItems;
	private SelectItem selectSearch;
	private List<SearchWidget> searchWidgets = new ArrayList<SearchWidget>();
	private boolean alwaysShowElements = true;
	private boolean hideButtonsWhenAdding;
	private List<GfxGeometry> geometries = new ArrayList<GfxGeometry>();
	private final ShapeStyle selectionStyle;

	/**
	 * Create a combined search panel for a specific map widget.
	 *
	 * @param mapWidget map widget to search for
	 */
	@Api
	public CombinedSearchPanel(final MapWidget mapWidget) {
		super(mapWidget);
		selectionStyle = new ShapeStyle();
		selectionStyle.setFillColor("#FFFF00");
		selectionStyle.setFillOpacity(0.3f);
		selectionStyle.setStrokeColor("#B45F04");
		selectionStyle.setStrokeOpacity(0.9f);
		selectionStyle.setStrokeWidth(2f);

		VLayout layout = new VLayout(10);
		layout.setWidth(GsfLayout.combinedSearchPanelWidth);
		layout.setHeight(1);

		// -- operator type --
		operatorForm = new DynamicForm();
		operatorForm.setWidth100();
		operatorForm.setAlign(Alignment.LEFT);
		type = new RadioGroupItem();
		type.setTitle(messages.combinedSearchWidgetType());
		type.setValueMap(messages.combinedSearchWidgetOr(), messages.combinedSearchWidgetAnd());
		type.setValue(messages.combinedSearchWidgetAnd());
		type.setVertical(false);
		operatorForm.setFields(type);
		layout.addMember(operatorForm);

		// -- criteria grid --
		searchItems = new ListGrid();
		searchItems.setWidth100();
		searchItems.setHeight(1);
		searchItems.setShowAllRecords(true);
		searchItems.setBodyOverflow(Overflow.VISIBLE);
		searchItems.setOverflow(Overflow.VISIBLE);
		searchItems.setLeaveScrollbarGap(false);
		searchItems.setShowHeader(false);
		searchItems.setShowRecordComponents(true);
		searchItems.setShowRecordComponentsByCell(true);
		ListGridField nameField = new ListGridField(SearchListRecord.NAME_FIELD);
		nameField.setType(ListGridFieldType.TEXT);
		ListGridField removeField = new ListGridField(SearchListRecord.REMOVE_FIELD);
		removeField.setType(ListGridFieldType.IMAGE);
		removeField.setShowHover(true);
		removeField.setWidth(24);
		removeField.setHoverCustomizer(new HoverCustomizer() {
			public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
				return messages.combinedSearchWidgetRemove();
			}
		});
		removeField.setWidth(20);
		removeField.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent event) {
				searchItems.removeData(event.getRecord());
				updateDisplay();
			}
		});
		searchItems.setFields(nameField, removeField);
		updateDisplay();

		HLayout addBar = new HLayout(0);
		addBar.setAlign(Alignment.LEFT);
		addBar.setHeight(20);
		Img addImg = new Img(WidgetLayout.iconAdd, 16, 16);
		addImg.setTooltip(messages.combinedSearchWidgetAdd());
		DynamicForm searchForm = new DynamicForm();
		selectSearch = new SelectItem();
		selectSearch.setTitle(addImg.getInnerHTML());
		selectSearch.setWidth(200);
		selectSearch.setTooltip(messages.combinedSearchWidgetSelectSearch());
		selectSearch.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				if (null != event.getValue() && !"".equals(event.getValue())) {
					if (hideButtonsWhenAdding) {
						Canvas parent = getParentElement();
						if (parent instanceof SearchWidget) {
							((SearchWidget) parent).hideSearchButtons();
						}
					}
					final SearchWidget sw = getSearchWidget((String) event.getValue());
					sw.showForSave(new SaveRequestHandler() {
						public void onSaveRequested(SaveRequestEvent event) {
							searchItems.addData(new SearchListRecord(sw.getName(), event.getCriterion()));
							update();
						}

						public void onCancelRequested(SaveRequestEvent event) {
							update();
						}

						private void update() {
							updateDisplay();
							if (hideButtonsWhenAdding) {
								Canvas parent = getParentElement();
								if (parent instanceof SearchWidget) {
									((SearchWidget) parent).showSearchButtons();
								}
								selectSearch.setValue("");
							}
						}
					});
				}
				if (!hideButtonsWhenAdding) {
					event.cancel();
				}
			}
		});
		searchForm.setFields(selectSearch);
		addBar.addMember(searchForm);

		layout.addMember(searchItems);
		layout.addMember(addBar);

		addChild(layout);
	}

	/**
	 * Set whether all elements should always be displayed or only when significant.
	 * <p/>
	 * This affects the and/or choice and the list of criteria.
	 * <p/>
	 * The default value is true.
	 *
	 * @param alwaysShowElements should all elements always be visible?
	 */
	public void setAlwaysShowElements(boolean alwaysShowElements) {
		this.alwaysShowElements = alwaysShowElements;
		updateDisplay();
	}

	/**
	 * Set whether the search and reset buttons should be hidden while adding a criterion.
	 * <p/>
	 * The default value is false.
	 *
	 * @param hideButtonsWhenAdding should buttons be hidden while adding?
	 */
	public void setHideButtonsWhenAdding(boolean hideButtonsWhenAdding) {
		this.hideButtonsWhenAdding = hideButtonsWhenAdding;
	}

	@Override
	public void show() {
		updateDisplay();
		super.show();
	}

	public void updateDisplay() {
		int recordCount = searchItems.getRecords().length;
		if (alwaysShowElements || recordCount >= 1) {
			searchItems.show();
		} else {
			searchItems.hide();
		}
		if (alwaysShowElements || recordCount >= 2) {
			operatorForm.show();
		} else {
			operatorForm.hide();
		}
		updateGeometriesOnMap();
	}

	private void updateGeometriesOnMap() {
		// make list of geometries from search items
		List<Geometry> searchGeoms = new ArrayList<Geometry>();
		for (ListGridRecord record : searchItems.getRecords()) {
			SearchListRecord slr = (SearchListRecord) record;
			if (slr.getCritter() instanceof GeometryCriterion) {
				Geometry geometry = GeometryConverter.toGwt(((GeometryCriterion) slr.getCritter()).getGeometry());
				searchGeoms.add(geometry);
			}
		}
		// (re)draw the geometries
		for (int i = 0; i < searchGeoms.size(); i++) {
			GfxGeometry worldPaintable = null;
			if (i < geometries.size()) {
				worldPaintable = geometries.get(i);
				mapWidget.unregisterWorldPaintable(worldPaintable);
			} else {
				worldPaintable = new GfxGeometry(GEOMETRY_IDENTIFIER + "_" + (i + 1));
				worldPaintable.setStyle(selectionStyle);
				geometries.add(worldPaintable);
			}
			worldPaintable.setGeometry(searchGeoms.get(i));
			mapWidget.registerWorldPaintable(worldPaintable);
		}
		// unregister superfluous geometries
		for (int i = searchGeoms.size(); i < geometries.size(); i++) {
			mapWidget.unregisterWorldPaintable(geometries.get(i));
		}
	}

	private SearchWidget getSearchWidget(String value) {
		for (SearchWidget searchWidget : searchWidgets) {
			if (value.equals(searchWidget.getSearchWidgetId())) {
				return searchWidget;
			}
		}
		return SearchWidgetRegistry.getSearchWidgetInstance(value);
	}

	@Override
	public boolean validate() {
		if (searchItems.getRecords().length > 0) {
			return true;
		} else {
			SC.say(messages.combinedSearchWidgetNoValue());
			return false;
		}
	}

	@Override
	public Criterion getFeatureSearchCriterion() {
		List<Criterion> criteria = new ArrayList<Criterion>();
		for (ListGridRecord record : searchItems.getRecords()) {
			criteria.add(((SearchListRecord) record).getCritter());
		}
		if (messages.combinedSearchWidgetAnd().equals(type.getValue())) {
			AndCriterion crit = new AndCriterion();
			crit.setCriteria(criteria);
			return crit;
		} else {
			OrCriterion crit = new OrCriterion();
			crit.setCriteria(criteria);
			return crit;
		}
	}
	
	@Override
	public VectorLayer getFeatureSearchVectorLayer() {
		return null; // not relevant for combined search
	}

	@Override
	public void reset() {
		operatorForm.reset();
		searchItems.getDataAsRecordList().removeList(searchItems.getRecords());
		updateDisplay();
	}
	
	public void onDestroy() {
		searchItems.clear();
		updateGeometriesOnMap();
	}

	/**
	 * Which searches can you use to combine?
	 * <p>
	 * Use this method if you do not use the SearchWidgetRegistry or want to
	 * limit the number of widgets.
	 *
	 * @param searchWidgets search widgets
	 */
	@Api
	public void initializeList(Collection<SearchWidget> searchWidgets) {
		LinkedHashMap<String, String> values = new LinkedHashMap<String, String>();
		for (SearchWidget searchWidget : searchWidgets) {
			values.put(searchWidget.getSearchWidgetId(), searchWidget.getName());
		}
		selectSearch.setValueMap(values);
		this.searchWidgets.addAll(searchWidgets);
	}

	/**
	 * Which searches can you use to combine?
	 * <p>
	 * Use this method if you use the {@link SearchWidgetRegistry} and want to use all
	 * searches in it (do not forget to add them to the registry first).
	 */
	public void initializeListUseAll() {
		selectSearch.setValueMap(SearchWidgetRegistry.getSearchWidgetMapping());
	}

	@Override
	public void initialize(Criterion featureSearch) {
		List<Criterion> critters;
		if (featureSearch instanceof AndCriterion) {
			critters = ((AndCriterion) featureSearch).getCriteria();
			type.setValue(messages.combinedSearchWidgetAnd());
		} else if (featureSearch instanceof OrCriterion) {
			critters = ((OrCriterion) featureSearch).getCriteria();
			type.setValue(messages.combinedSearchWidgetOr());
		} else {
			return;
		} // not supported

		if (critters != null && critters.size() > 0) {
			for (Criterion cr : critters) {
				searchItems.addData(new SearchListRecord(SearchWidgetRegistry.getI18nTypeName(cr), cr));
				updateDisplay();
			}
		}
	}

	public static boolean canHandle(Criterion criterion) {
		return criterion != null && (criterion instanceof OrCriterion || criterion instanceof AndCriterion);
	}

	// ----------------------------------------------------------

	/**
	 * Record representation for grid.
	 */
	private static class SearchListRecord extends ListGridRecord {

		public static final String NAME_FIELD = "nameField";
		public static final String REMOVE_FIELD = "removeField";

		// private String name;
		private Criterion critter;

		public SearchListRecord(String name, Criterion criterion) {
			setSearchWidget(name, criterion);
		}

		public void setSearchWidget(String name, Criterion criterion) {
			this.critter = criterion;
			setAttribute(NAME_FIELD, name + ", " + criterion.getDisplayText());
			setAttribute(REMOVE_FIELD, WidgetLayout.iconRemove);
		}

		public Criterion getCritter() {
			return critter;
		}
	}
}
