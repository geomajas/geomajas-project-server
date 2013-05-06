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
package org.geomajas.plugin.deskmanager.client.gwt.manager.blueprints;

import java.util.HashMap;

import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.deskmanager.client.gwt.common.GdmLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.ManagerTab;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.EditSessionEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.EditSessionHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.Whiteboard;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.ManagerCommandService;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Frontend presentation of a selectable list of blueprints, and a panel that contains configuration options of the
 * selected blueprint.
 * 
 * @author Kristof Heirwegh
 * @author Oliver May
 */
public class Blueprints extends VLayout implements EditSessionHandler, ManagerTab {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	private IButton buttonNew;

	private BlueprintGrid grid;

	private BlueprintDetail detail;

	private static final int MARGIN = 20;

	public Blueprints() {
		super(MARGIN);

		detail = new BlueprintDetail();

		VLayout topContainer = new VLayout(5);
		topContainer.setShowResizeBar(true);
		topContainer.setMinHeight(200);
		topContainer.setHeight("60%");
		topContainer.setLayoutBottomMargin(5);

		buttonNew = new IButton(MESSAGES.newBlueprintButtonText());
		buttonNew.setWidth(buttonNew.getTitle().length() * GdmLayout.buttonFontWidth + GdmLayout.buttonOffset);
		buttonNew.setIcon(WidgetLayout.iconAdd);
		buttonNew.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Window w = new ChooseUserAppNameWindow(new DataCallback<HashMap<String, Object>>() {

					public void execute(HashMap<String, Object> result) {
						if (result != null && result.containsKey("userApplication") && result.containsKey("public") 
								&& result.containsKey("name")) {
							//TODO: i18n
							ManagerCommandService.createNewBlueprint(result.get("userApplication").toString(), 
									(Boolean) result.get("public"), result.get("name").toString());
						}
					}
				});
				w.show();
			}
		});

		grid = new BlueprintGrid();
		grid.addSelectionChangedHandler(detail);

		topContainer.addMember(buttonNew);
		topContainer.addMember(grid);

		// ----------------------------------------------------------

		detail.setMinHeight(200);
		detail.setHeight("40%");
		detail.setLayoutTopMargin(5);

		addMember(topContainer);
		addMember(detail);

		Whiteboard.registerHandler(this);
	}

	public void readData() {
		grid.readData();
	}

	public void destroy() {
		Whiteboard.unregisterHandler(this);
		super.destroy();
	}

	// -- EditSessionHandler--------------------------------------------------------

	public void onEditSessionChange(EditSessionEvent ese) {
		if (ese.isParentOfRequestee(detail)) {
			grid.setDisabled(ese.isSessionStart());
			buttonNew.setDisabled(ese.isSessionStart());
			// detail handles event itself so no need to do it here
		}
	}
}
