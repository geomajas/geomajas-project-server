/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.utility.smartgwt.client.wizard;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.widget.utility.smartgwt.client.wizard.WizardButton.ButtonType;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Default implementation of {@link WizardView}.
 * 
 * @author Jan De Moerloose
 * 
 * @param <DATA> type of data to view
 */
public class WizardWidget<DATA> extends VLayout implements WizardView<DATA> {

	private String title;

	private String helpText;

	private VLayout leftLayout;

	private VLayout pageBody;

	private HTMLFlow pageTitleDiv;

	private HTMLFlow loadingDiv;

	private List<WizardButton<DATA>> buttons;

	public WizardWidget(String title, String helpText) {
		this.title = title;
		buttons = new ArrayList<WizardButton<DATA>>();
		setStyleName("wizardWidget");
		setLayoutAlign(Alignment.CENTER);

		addMember(createTitle());

		HLayout body = new HLayout();
		leftLayout = new VLayout(10);
		leftLayout.setStyleName("wizardLeftLayout");
		leftLayout.setSize("220", "100%");
		leftLayout.setLayoutRightMargin(10);

		HTMLFlow explanation = new HTMLFlow("<div style='font-size:12px;'>" + helpText + "</div>");
		leftLayout.addMember(explanation);
		body.addMember(leftLayout);

		VLayout rightLayout = new VLayout();
		rightLayout.setLayoutRightMargin(5);
		rightLayout.addMember(createPageTop());
		pageBody = new VLayout();
		rightLayout.addMember(pageBody);
		rightLayout.addMember(createPageBottom());
		body.addMember(rightLayout);
		addMember(body);
	}

	public void addPageToView(WizardPage<DATA> page) {
		DefaultPageButton pageButton = new DefaultPageButton(page);
		leftLayout.addMember(pageButton);
		page.asWidget().setStyleName("wizardPageBody");
		page.asWidget().setSize("100%", "100%");
		pageBody.addMember(page.asWidget());
	}

	public void setCurrentPage(WizardPage<DATA> currentPage) {
		// Display correct title:
		pageTitleDiv.setContents(currentPage.getExplanation());
		markForRedraw();
	}

	public List<WizardButton<DATA>> getButtons() {
		return buttons;
	}

	public void setLoading(boolean loading) {
		if (loading) {
			pageTitleDiv.setVisible(false);
			loadingDiv.setVisible(true);
			setDisabled(true);
		} else {
			pageTitleDiv.setVisible(true);
			loadingDiv.setVisible(false);
			setDisabled(false);
		}
	}

	protected Canvas createTitle() {
		HLayout titleCanvas = new HLayout(5);
		titleCanvas.setSize("100%", "24px");
		titleCanvas.setStyleName("blockTitle");

		HTMLFlow titleDiv = new HTMLFlow(title);
		titleDiv.setSize("100%", "24px");
		titleDiv.setStyleName("blockTitleText");

		titleCanvas.addMember(titleDiv);
		return titleCanvas;
	}

	protected Canvas createLeftLayout() {
		HLayout body = new HLayout();
		VLayout leftLayout = new VLayout(10);
		leftLayout.setStyleName("wizardLeftLayout");
		leftLayout.setSize("220", "100%");
		leftLayout.setLayoutRightMargin(10);

		HTMLFlow explanation = new HTMLFlow("<div style='font-size:12px;'>" + helpText + "</div>");
		leftLayout.addMember(explanation);
		body.addMember(leftLayout);
		return body;
	}

	protected HLayout createPageTop() {
		HLayout layout = new HLayout(10);
		layout.setAlign(VerticalAlignment.CENTER);
		layout.setHeight(40);
		pageTitleDiv = new HTMLFlow();
		pageTitleDiv.setSize("100%", "40px");
		pageTitleDiv.setStyleName("wizardPageTitle");

		loadingDiv = new HTMLFlow();
		loadingDiv.setSize("100%", "40px");
		loadingDiv.setStyleName("wizardPageTitle");
		loadingDiv.setContents("Loading...");
		loadingDiv.setVisible(false);
		layout.addMember(pageTitleDiv);
		layout.addMember(loadingDiv);
		layout.addMember(new DefaultWizardButton(ButtonType.PREVIOUS));
		layout.addMember(new DefaultWizardButton(ButtonType.NEXT));
		layout.addMember(new DefaultWizardButton(ButtonType.CANCEL));
		layout.addMember(new DefaultWizardButton(ButtonType.FINISH));
		return layout;
	}

	protected HLayout createPageBottom() {
		HLayout layout = new HLayout(10);
		layout.setAlign(VerticalAlignment.CENTER);
		layout.setHeight(40);
		layout.addMember(new LayoutSpacer());
		layout.addMember(new DefaultWizardButton(ButtonType.PREVIOUS));
		layout.addMember(new DefaultWizardButton(ButtonType.NEXT));
		layout.addMember(new DefaultWizardButton(ButtonType.CANCEL));
		layout.addMember(new DefaultWizardButton(ButtonType.FINISH));
		return layout;
	}

	/**
	 * Default implementation of page button.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class DefaultPageButton extends HTMLFlow implements WizardButton<DATA> {

		private WizardPage<DATA> page;

		DefaultPageButton(WizardPage<DATA> page) {
			this.page = page;
			setContents(page.getTitle());
			setSize("100%", "30px");
			setStyleName("wizardButton");
			setCursor(Cursor.HAND);
			buttons.add(this);
		}

		public void setActive(boolean active) {
			if (active) {
				setStyleName("wizardButtonActive");
			} else {
				setStyleName("wizardButton");
			}
		}

		public void setEnabled(boolean enabled) {
			setDisabled(!enabled);
		}

		public WizardPage<DATA> getPage() {
			return page;
		}

		public ButtonType getType() {
			return ButtonType.PAGE;
		}

	}

	/**
	 * Default implementation of classical wizard button.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class DefaultWizardButton extends IButton implements WizardButton<DATA> {

		private ButtonType type;

		DefaultWizardButton(ButtonType type) {
			switch (type) {
				case CANCEL:
					setTitle("Cancel");
					break;
				case FINISH:
					setTitle("Finish");
					break;
				case NEXT:
					setTitle("Next");
					break;
				case PREVIOUS:
					setTitle("Previous");
					break;
			}
			this.type = type;
			setLayoutAlign(VerticalAlignment.CENTER);
			setEnabled(false);
			buttons.add(this);
		}

		public ButtonType getType() {
			return type;
		}

		public void setEnabled(boolean enabled) {
			setDisabled(!enabled);
		}

		public void setActive(boolean active) {
			// ignore
		}

		public WizardPage<DATA> getPage() {
			return null;
		}
	}

}
