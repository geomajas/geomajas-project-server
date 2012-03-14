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

package org.geomajas.puregwt.client;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.puregwt.client.feature.FeatureSelectionPanel;
import org.geomajas.puregwt.client.general.IntroductionContentPanel;
import org.geomajas.puregwt.client.general.MapFillPanel;
import org.geomajas.puregwt.client.general.NavigationOptionPanel;
import org.geomajas.puregwt.client.general.ResizeMapPanel;
import org.geomajas.puregwt.client.layer.LayerOrderPanel;
import org.geomajas.puregwt.client.layer.LayerVisibilityPanel;
import org.geomajas.puregwt.client.layer.TmsLayerPanel;
import org.geomajas.puregwt.client.rendering.DrawingInteractionPanel;
import org.geomajas.puregwt.client.rendering.ScreenSpaceRenderingPanel;
import org.geomajas.puregwt.client.rendering.WorldSpaceRenderingPanel;
import org.geomajas.puregwt.client.widget.LegendDisclosureGadgetPanel;
import org.geomajas.puregwt.client.widget.LayerLegendViewPanel;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.prefetch.RunAsyncCode;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.TreeViewModel;

/**
 * Left side tree view in the showcase. Contains the full list of examples.
 * 
 * @author Pieter De Graef
 */
public class ShowcaseTreeViewModel implements TreeViewModel {

	/** The top level categories. */
	private final ListDataProvider<Category> categories = new ListDataProvider<Category>();

	private final ContentPanelCell contentPanelCell = new ContentPanelCell();

	private SelectionModel<ContentPanel> selectionModel;
	
	private GeomajasGinjector geomajasInjector;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	public ShowcaseTreeViewModel(SelectionModel<ContentPanel> selectionModel, GeomajasGinjector geomajasInjector) {
		this.selectionModel = selectionModel;
		this.geomajasInjector = geomajasInjector;
		initializeTree();
	}

	// ------------------------------------------------------------------------
	// TreeViewModel implementation:
	// ------------------------------------------------------------------------

	public <T> NodeInfo<?> getNodeInfo(T value) {
		if (value == null) {
			// Return the top level categories.
			return new DefaultNodeInfo<Category>(categories, new CategoryCell());
		} else if (value instanceof Category) {
			// Return the examples within the category.
			Category category = (Category) value;
			return category.getNodeInfo();
		}
		return null;
	}

	public boolean isLeaf(Object value) {
		return value != null && !(value instanceof Category);
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	/**
	 * Initialize the top level categories in the tree.
	 */
	private void initializeTree() {
		List<Category> catList = categories.getList();

		// General:
		Category catGeneral = new Category("General");
		catList.add(catGeneral);
		catGeneral.addExample(new IntroductionContentPanel(), null);
		catGeneral.addExample(new NavigationOptionPanel(geomajasInjector.getMapPresenter()), null);
		catGeneral.addExample(new ResizeMapPanel(geomajasInjector.getMapPresenter()), null);
		catGeneral.addExample(new MapFillPanel(geomajasInjector.getMapPresenter()), null);

		// Layer Manipulation:
		Category catLayerMan = new Category("Layer Manipulation");
		catList.add(catLayerMan);
		catLayerMan.addExample(new TmsLayerPanel(geomajasInjector.getMapPresenter()), null);
		catLayerMan.addExample(new LayerVisibilityPanel(geomajasInjector.getMapPresenter()), null);
		catLayerMan.addExample(new LayerOrderPanel(geomajasInjector.getMapPresenter()), null);

		// Features:
		Category catFeature = new Category("Feature Manipulation");
		catList.add(catFeature);
		catFeature.addExample(new FeatureSelectionPanel(geomajasInjector.getMapPresenter()), null);

		// Drawing:
		Category catDrawing = new Category("Custom Drawing");
		catList.add(catDrawing);
		catDrawing.addExample(new ScreenSpaceRenderingPanel(geomajasInjector.getMapPresenter()), null);
		catDrawing.addExample(new WorldSpaceRenderingPanel(geomajasInjector.getMapPresenter()), null);
		catDrawing.addExample(new DrawingInteractionPanel(geomajasInjector.getMapPresenter()), null);

		// Widget plug-in:
		Category catWidget = new Category("Widgets");
		catList.add(catWidget);
		catWidget.addExample(new LayerLegendViewPanel(geomajasInjector.getMapPresenter()), null);
		catWidget.addExample(new LegendDisclosureGadgetPanel(geomajasInjector.getMapPresenter()), null);
	}

	// ------------------------------------------------------------------------
	// Private classes:
	// ------------------------------------------------------------------------

	/**
	 * The cell used to render categories.
	 * 
	 * @author Pieter De Graef
	 */
	private static class CategoryCell extends AbstractCell<Category> {

		public void render(Context context, Category value, SafeHtmlBuilder sb) {
			if (value != null) {
				sb.appendEscaped(value.getName());
			}
		}
	}

	/**
	 * The cell used to render examples.
	 * 
	 * @author Pieter De Graef
	 */
	private static class ContentPanelCell extends AbstractCell<ContentPanel> {

		@Override
		public void render(Context context, ContentPanel value, SafeHtmlBuilder sb) {
			if (value != null) {
				sb.appendEscaped(value.getTitle());
			}
		}
	}

	/**
	 * A top level category in the tree.
	 * 
	 * @author Pieter De Graef
	 */
	public class Category {

		private final ListDataProvider<ContentPanel> examples = new ListDataProvider<ContentPanel>();

		private final String name;

		private NodeInfo<ContentPanel> nodeInfo;

		private final List<RunAsyncCode> splitPoints = new ArrayList<RunAsyncCode>();

		public Category(String name) {
			this.name = name;
		}

		public void addExample(ContentPanel example, RunAsyncCode splitPoint) {
			examples.getList().add(example);
			if (splitPoint != null) {
				splitPoints.add(splitPoint);
			}
		}

		public String getName() {
			return name;
		}

		/**
		 * Get the node info for the examples under this category.
		 * 
		 * @return the node info
		 */
		public NodeInfo<ContentPanel> getNodeInfo() {
			if (nodeInfo == null) {
				nodeInfo = new DefaultNodeInfo<ContentPanel>(examples, contentPanelCell, selectionModel, null);
			}
			return nodeInfo;
		}

		/**
		 * Get the list of split points to prefetch for this category.
		 * 
		 * @return the list of classes in this category
		 */
		public Iterable<RunAsyncCode> getSplitPoints() {
			return splitPoints;
		}
	}
}