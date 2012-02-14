package org.geomajas.sld.editor.client.view;

import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.client.presenter.RulePresenter;
import org.geomajas.sld.client.view.ViewUtil;
import org.geomajas.sld.editor.client.i18n.SldEditorMessages;
import org.geomajas.sld.editor.client.widget.IncompleteFilterInfo;
import org.geomajas.sld.editor.client.widget.IncompleteRuleInfo;
import org.geomajas.sld.filter.FilterTypeInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class RuleView extends ViewImpl implements RulePresenter.MyView {

	private VLayout ruleDetailContainer;

	private VLayout symbolPane;

	private VLayout filterPane;

	private RuleInfo currentRule;

	private Label noRuleSelectedMessage;

	private final EventBus eventBus;

	private final ViewUtil viewUtil;

	private final SldEditorMessages sldEditorMessages;

	public RuleView(final EventBus eventBus, final ViewUtil viewUtil, final SldEditorMessages sldEditorMessages) {
		this.eventBus = eventBus;
		this.viewUtil = viewUtil;
		this.sldEditorMessages = sldEditorMessages;
		ruleDetailContainer = new VLayout(5);
		ruleDetailContainer.setMinHeight(200);
		// ruleDetailContainer.setHeight("*"); // TODO: needed???
		ruleDetailContainer.setWidth("100%");
		ruleDetailContainer.setMinWidth(100);
		ruleDetailContainer.setLayoutTopMargin(10);
		ruleDetailContainer.setLayoutLeftMargin(5);
		ruleDetailContainer.setLayoutBottomMargin(5);
		ruleDetailContainer.setGroupTitle(sldEditorMessages.ruleDetailContainerTitle());
		ruleDetailContainer.setIsGroup(true);

		noRuleSelectedMessage = new Label("<i>Geen stijl geselecteerd!</i>");
		noRuleSelectedMessage.setAlign(Alignment.CENTER);
		ruleDetailContainer.addMember(noRuleSelectedMessage);
		noRuleSelectedMessage.hide();

		TabSet topTabSet = new TabSet();
		topTabSet.setTabBarPosition(Side.TOP);

		symbolPane = new VLayout();
		symbolPane.setMembersMargin(5);
		symbolPane.setMargin(5);

		filterPane = new VLayout();

		Tab tTab1 = new Tab(sldEditorMessages.symbologyTabTitle());

		tTab1.setPane(symbolPane);

		Tab tTab2 = new Tab(sldEditorMessages.filterTabTitle());
		filterPane = new VLayout();
		filterPane.setMembersMargin(5);
		filterPane.setMargin(5);

		tTab2.setPane(filterPane);

		topTabSet.addTab(tTab1);
		topTabSet.addTab(tTab2);

		ruleDetailContainer.addMember(topTabSet);
	}
	


	@Override
	public void setInSlot(Object slot, Widget content) {
		if (slot == RulePresenter.TYPE_SYMBOL_CONTENT) {
			setSymbolContent((Canvas)content);
		} else if (slot == RulePresenter.TYPE_FILTER_CONTENT) {
			setFilterContent((Canvas)content);
		} else {
			super.setInSlot(slot, content);
		}
	}
	
	private void setFilterContent(Canvas content) {
		for (Canvas member : filterPane.getMembers()) {
			member.removeFromParent();
		}
		if (content != null) {
			content.setWidth100();
			content.setHeight100();
			filterPane.addMember(content);
		}
	}

	private void setSymbolContent(Canvas content) {
		for (Canvas member : symbolPane.getMembers()) {
			member.removeFromParent();
		}
		if (content != null) {
			content.setWidth100();
			content.setHeight100();
			symbolPane.addMember(content);
		}
	}

	public Widget asWidget() {
		return ruleDetailContainer;
	}

}
