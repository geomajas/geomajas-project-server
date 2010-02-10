package org.geomajas.gwt.client;

import org.geomajas.gwt.client.samples.IntroductionTab;
import org.geomajas.gwt.client.samples.base.SamplePanel;
import org.geomajas.gwt.client.samples.base.SampleTree;
import org.geomajas.gwt.client.samples.base.SampleTreeNode;
import org.geomajas.gwt.client.widget.LocaleSelect;

import com.google.gwt.core.client.EntryPoint;
import com.smartgwt.client.types.TabBarControls;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.LeafClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafClickHandler;

public class GeomajasSamples implements EntryPoint {

	private TabSet mainTabSet;

	private SampleTree sampleTree;

	public void onModuleLoad() {

		// Setup the overall layout:
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
		main.setLayoutMargin(5);
		main.setStyleName("tabSetContainer");

		HLayout hLayout = new HLayout();
		hLayout.setWidth100();
		hLayout.setHeight100();

		VLayout leftTreeLayout = new VLayout();
		leftTreeLayout.setHeight100();
		leftTreeLayout.setWidth(200);
		leftTreeLayout.setShowResizeBar(true);

		sampleTree = new SampleTree();
		sampleTree.addLeafClickHandler(new LeafClickHandler() {

			public void onLeafClick(LeafClickEvent event) {
				TreeNode node = event.getLeaf();
				showSample(node);
			}
		});

		leftTreeLayout.addMember(sampleTree);
		hLayout.addMember(leftTreeLayout);

		mainTabSet = new TabSet();
		mainTabSet.setWidth100();
		mainTabSet.setHeight100();

		LayoutSpacer layoutSpacer = new LayoutSpacer();
		layoutSpacer.setWidth(5);
		LocaleSelect localeSelect = new LocaleSelect();
		mainTabSet
				.setTabBarControls(TabBarControls.TAB_SCROLLER, TabBarControls.TAB_PICKER, layoutSpacer, localeSelect);

		hLayout.addMember(mainTabSet);
		main.addMember(hLayout);
		main.draw();
		showIntroductionTab();
	}

	private void showSample(TreeNode node) {
		if (node instanceof SampleTreeNode) {
			SampleTreeNode treeNode = (SampleTreeNode) node;
			SamplePanel panel = treeNode.getSamplePanel();

			Tab tab = null;
			String tabId = panel.getId() + "_tab";
			tab = mainTabSet.getTab(tabId);
			if (tab == null) {
				tab = new Tab();
				tab.setID(tabId);
				String imgHTML = Canvas.imgHTML(treeNode.getIcon(), 16, 16);
				tab.setTitle("<span>" + imgHTML + "&nbsp;" + treeNode.getName() + "</span>");
				tab.setPane(panel);
				tab.setCanClose(true);
				mainTabSet.addTab(tab);
				panel.initialize();
			}
			mainTabSet.selectTab(tab);
		}
	}

	private void showIntroductionTab() {
		IntroductionTab sample = new IntroductionTab();
		Tab tab = new Tab();
		tab.setID(sample.getId() + "_tab");
		String imgHTML = Canvas.imgHTML(sample.getIcon(), 16, 16);
		tab.setTitle("<span>" + imgHTML + "&nbsp;" + sample.getTitle() + "</span>");
		tab.setPane(sample.getViewPanel());
		mainTabSet.addTab(tab);
		sample.initialize();
	}
}
