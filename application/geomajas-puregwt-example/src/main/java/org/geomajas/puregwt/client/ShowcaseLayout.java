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

import org.geomajas.puregwt.client.general.IntroductionContentPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.TreeViewModel;

/**
 * Top layout for the Geomajas PureGWT showcase.
 * 
 * @author Pieter De Graef
 */
public class ShowcaseLayout extends Composite {

	/**
	 * UI binder interface for this showcase layout.
	 * 
	 * @author Pieter De Graef
	 */
	interface ShowcaseLayoutUiBinder extends UiBinder<Widget, ShowcaseLayout> {
	}

	private static final ShowcaseLayoutUiBinder UI_BINDER = GWT.create(ShowcaseLayoutUiBinder.class);

	@UiField
	protected ScrollPanel leftPanel;

	@UiField
	protected ScrollPanel contentPanel;

	private CellTree mainMenu;

	private ContentPanel content;

	public ShowcaseLayout(TreeViewModel treeModel) {
		initWidget(UI_BINDER.createAndBindUi(this));

		// Left panel - the show case tree view:
		mainMenu = new CellTree(treeModel, null);
		mainMenu.setAnimationEnabled(true);
		mainMenu.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		leftPanel.add(mainMenu);

		// Content panel:
		setContent(new IntroductionContentPanel());
	}

	public void setContent(final ContentPanel content) {
		if (this.content != null) {
			contentPanel.remove(this.content);
		}
		content.setSize("100%", "100%");
		contentPanel.add(content);

		// Necessary for IE:
		contentPanel.getElement().getFirstChildElement().getStyle().setWidth(100, Unit.PCT);
		contentPanel.getElement().getFirstChildElement().getStyle().setHeight(100, Unit.PCT);

		this.content = content;
		content.setVisible(true);
	}
}