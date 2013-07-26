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

package org.geomajas.gwt.example.base.client.page.overview;

import java.util.Comparator;
import java.util.List;

import org.geomajas.gwt.example.base.client.ExampleBase;
import org.geomajas.gwt.example.base.client.resource.DataGridResource;
import org.geomajas.gwt.example.base.client.sample.ShowcaseSampleDefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * Sample view that displays all samples in a list.
 * 
 * @author Pieter De Graef
 */
public class SampleListView extends Composite implements HasSamples {

	/**
	 * UI binder interface this widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, SampleListView> {
	}

	private static final MyUiBinder UIBINDER = GWT.create(MyUiBinder.class);

	private static final DataGridResource RESOURCE = GWT.create(DataGridResource.class);

	private final ListHandler<ShowcaseSampleDefinition> sortHandler;

	private final ListDataProvider<ShowcaseSampleDefinition> dataProvider;

	@UiField
	protected DataGrid<ShowcaseSampleDefinition> grid;

	public SampleListView(List<ShowcaseSampleDefinition> data, final SampleOverviewPage overviewPage) {
		initWidget(UIBINDER.createAndBindUi(this));
		ProvidesKey<ShowcaseSampleDefinition> sampleKeyProvider = new ProvidesKey<ShowcaseSampleDefinition>() {

			public Object getKey(ShowcaseSampleDefinition item) {
				return item.getTitle();
			}
		};

		// We fill the grid through a list of SamplePanelFactory objects:
		dataProvider = new ListDataProvider<ShowcaseSampleDefinition>(sampleKeyProvider);
		dataProvider.addDataDisplay(grid);

		// Selection: show sample on click
		final SingleSelectionModel<ShowcaseSampleDefinition> selectionModel;
		selectionModel = new SingleSelectionModel<ShowcaseSampleDefinition>(sampleKeyProvider);
		grid.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

			public void onSelectionChange(SelectionChangeEvent event) {
				ExampleBase.showSample(selectionModel.getSelectedObject());
			}
		});

		// Add a sort handler:
		sortHandler = new ListHandler<ShowcaseSampleDefinition>(dataProvider.getList());
		grid.addColumnSortHandler(sortHandler);

		// Initialize the grid columns:
		initColumns();

		// Apply the entire data-set:
		setData(data);
	}

	public void setData(List<ShowcaseSampleDefinition> data) {
		dataProvider.getList().clear();
		dataProvider.getList().addAll(data);
		dataProvider.flush();
	}

	@UiFactory
	protected DataGrid<ShowcaseSampleDefinition> createGrid() {
		return new DataGrid<ShowcaseSampleDefinition>(1000, RESOURCE);
	}

	private void initColumns() {
		// Column1: Title
		final TextColumn<ShowcaseSampleDefinition> titleColumn = new TextColumn<ShowcaseSampleDefinition>() {

			public String getValue(ShowcaseSampleDefinition object) {
				return object.getTitle();
			}
		};
		titleColumn.setSortable(true);
		grid.addColumn(titleColumn, "Title");
		grid.setColumnWidth(titleColumn, 300, Unit.PX);
		sortHandler.setComparator(titleColumn, new Comparator<ShowcaseSampleDefinition>() {

			public int compare(ShowcaseSampleDefinition one, ShowcaseSampleDefinition two) {
				return titleColumn.getValue(one).compareTo(titleColumn.getValue(two));
			}
		});

		// Column2: Category
		final TextColumn<ShowcaseSampleDefinition> categoryColumn = new TextColumn<ShowcaseSampleDefinition>() {

			public String getValue(ShowcaseSampleDefinition object) {
				return object.getCategory();
			}
		};
		categoryColumn.setSortable(true);
		grid.addColumn(categoryColumn, "Category");
		grid.setColumnWidth(categoryColumn, 250, Unit.PX);
		sortHandler.setComparator(categoryColumn, new Comparator<ShowcaseSampleDefinition>() {

			public int compare(ShowcaseSampleDefinition one, ShowcaseSampleDefinition two) {
				return categoryColumn.getValue(one).compareTo(categoryColumn.getValue(two));
			}
		});

		// Column3: Description
		final TextColumn<ShowcaseSampleDefinition> descriptionColumn = new TextColumn<ShowcaseSampleDefinition>() {

			public String getValue(ShowcaseSampleDefinition object) {
				return object.getShortDescription();
			}
		};
		descriptionColumn.setSortable(true);
		grid.addColumn(descriptionColumn, "Description");
		sortHandler.setComparator(descriptionColumn, new Comparator<ShowcaseSampleDefinition>() {

			public int compare(ShowcaseSampleDefinition one, ShowcaseSampleDefinition two) {
				return descriptionColumn.getValue(one).compareTo(descriptionColumn.getValue(two));
			}
		});
	}
}