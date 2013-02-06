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

import org.geomajas.plugin.deskmanager.client.gwt.common.UserApplicationRegistry;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.AbstractConfigurationLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.SaveButtonBar;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.BlueprintEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.BlueprintSelectionHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.ManagerCommandService;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveBlueprintRequest;
import org.geomajas.plugin.deskmanager.domain.dto.BlueprintDto;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Kristof Heirwegh
 */
public class BlueprintSettings extends AbstractConfigurationLayout implements BlueprintSelectionHandler {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);
	
	private static final DateTimeFormat DATE_FORMATTER = DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_SHORT);

	private static final int FORMITEM_WIDTH = 300;

	private BlueprintDto blueprint;

	private DynamicForm form;

	private TextItem blueprintName;

	private SelectItem clientApplicationName;

	private StaticTextItem lastEditBy;

	private StaticTextItem lastEditDate;

	private CheckboxItem active;

	private CheckboxItem geodesksActive;

	private CheckboxItem publiek;

	private CheckboxItem limitToLoketTerritory;

	private CheckboxItem limitToUserTerritory;

	private boolean containsNonPublicLayers;

	public BlueprintSettings() {
		super();
		setMargin(5);

		SaveButtonBar buttonBar = new SaveButtonBar(this);
		addMember(buttonBar);

		// ----------------------------------------------------------

		form = new DynamicForm();
		form.setAutoWidth();
		form.setAutoFocus(true); /* Set focus on first field */
		form.setNumCols(4);
		form.setDisabled(true);

		blueprintName = new TextItem("blueprintName");
		blueprintName.setTitle(MESSAGES.blueprintSettingsNameBlueprint());
		blueprintName.setRequired(true);
		blueprintName.setWidth(FORMITEM_WIDTH);
		blueprintName.setWrapTitle(false);

		clientApplicationName = new SelectItem();
		clientApplicationName.setTitle(MESSAGES.blueprintSettingsClientApplicationName());
		clientApplicationName.setWidth(FORMITEM_WIDTH);
		clientApplicationName.setRequired(true);
		clientApplicationName.setWrapTitle(false);
		clientApplicationName.setValueMap(UserApplicationRegistry.getInstance().getUserApplicationNames());

		lastEditBy = new StaticTextItem("lastEditBy");
		lastEditBy.setTitle(MESSAGES.settingsLatestChangeBy());
		lastEditBy.setWidth(FORMITEM_WIDTH);
		lastEditBy.setWrapTitle(false);

		lastEditDate = new StaticTextItem("lastEditDate");
		lastEditDate.setTitle(MESSAGES.settingsLatestChangeWhen());
		lastEditDate.setWidth(FORMITEM_WIDTH);
		lastEditDate.setWrapTitle(false);

		active = new CheckboxItem();
		active.setTitle(MESSAGES.blueprintSettingsActiv());
		active.setWrapTitle(false);
		active.setTooltip(MESSAGES.blueprintSettingsActivTooltip());

		geodesksActive = new CheckboxItem();
		geodesksActive.setTitle(MESSAGES.blueprintAttributeGeodesksActiv());
		geodesksActive.setWrapTitle(false);
		geodesksActive.setTooltip(MESSAGES.blueprintAttributeGeodesksActivTooltip());

		publiek = new CheckboxItem();
		publiek.setTitle(MESSAGES.blueprintSettingsPublic());
		publiek.setWrapTitle(false);
		publiek.setPrompt(MESSAGES.blueprintAttributePublicTooltip());
		publiek.addChangeHandler(new ChangeHandler() {

			public void onChange(ChangeEvent event) {
				if (containsNonPublicLayers) {
					SC.warn(MESSAGES.blueprintSettingsWarnCannotBePublic());
					event.cancel();
				}
			}
		});
		publiek.addChangedHandler(new com.smartgwt.client.widgets.form.fields.events.ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				boolean val = publiek.getValueAsBoolean();
				limitToLoketTerritory.setDisabled(!val);
				limitToUserTerritory.setDisabled(val);
			}
		});

		limitToLoketTerritory = new CheckboxItem();
		limitToLoketTerritory.setTitle(MESSAGES.settingsLimitToTerritoryAdministrator());
		limitToLoketTerritory.setWrapTitle(false);
		limitToLoketTerritory.setPrompt(MESSAGES.settingsLimitToTerritoryAdministratorTooltip());

		limitToUserTerritory = new CheckboxItem();
		limitToUserTerritory.setTitle(MESSAGES.settingsLimitToTerritoryUser());
		limitToUserTerritory.setWrapTitle(false);
		limitToLoketTerritory.setPrompt(MESSAGES.settingsLimitToTerritoryUserTooltip());

		// ----------------------------------------------------------

		form.setTitleOrientation(TitleOrientation.LEFT);
		form.setFields(blueprintName, active, clientApplicationName, geodesksActive, new SpacerItem(),
				new SpacerItem(), publiek, lastEditBy, limitToLoketTerritory, lastEditDate, limitToUserTerritory);

		VLayout group = new VLayout();
		group.setPadding(10);
		group.setIsGroup(true);
		group.setGroupTitle(MESSAGES.settingsFormGroupSettings());
		group.addMember(form);
		group.setOverflow(Overflow.AUTO);

		addMember(group);
	}

	
	public void onBlueprintSelectionChange(BlueprintEvent bpe) {
		setBlueprint(bpe.getBlueprint());
	}
	
	public void setBlueprint(BlueprintDto blueprint) {
		form.clearValues();
		this.blueprint = blueprint;
		if (blueprint != null) {
			blueprintName.setValue(blueprint.getName());
			clientApplicationName.setValue(blueprint.getUserApplicationName());
			lastEditBy.setValue(blueprint.getLastEditBy());
			lastEditDate.setValue(DATE_FORMATTER.format(blueprint.getLastEditDate()));
			active.setValue(blueprint.isActive());
			geodesksActive.setValue(blueprint.isGeodesksActive());
			publiek.setValue(blueprint.isPublic());
			limitToLoketTerritory.setValue(blueprint.isLimitToCreatorTerritory());
			limitToUserTerritory.setValue(blueprint.isLimitToUserTerritory());
			limitToLoketTerritory.setDisabled(!blueprint.isPublic());
			limitToUserTerritory.setDisabled(blueprint.isPublic());

			//FIXME
//			containsNonPublicLayers = (blueprint.getLayerTree() == null ? false : blueprint.getLayerTree()
//					.containsNonPublicLayers());
		}
		fireChangedHandler();
	}

	// -- SaveButtonBar events --------------------------------------------------------

	public boolean onEditClick(ClickEvent event) {
		form.setDisabled(false);
		return true;
	}

	public boolean onSaveClick(ClickEvent event) {
		if (validate()) {
			blueprint.setName(blueprintName.getValueAsString());
			blueprint.setUserApplicationName(clientApplicationName.getValueAsString());
			blueprint.setActive(active.getValueAsBoolean());
			blueprint.setGeodesksActive(geodesksActive.getValueAsBoolean());
			blueprint.setPublic(publiek.getValueAsBoolean());
			if (blueprint.isPublic()) {
				blueprint.setLimitToLoketTerritory(limitToLoketTerritory.getValueAsBoolean());
				blueprint.setLimitToUserTerritory(false);
			} else {
				blueprint.setLimitToLoketTerritory(false);
				blueprint.setLimitToUserTerritory(limitToUserTerritory.getValueAsBoolean());
			}
			ManagerCommandService.saveBlueprint(blueprint, SaveBlueprintRequest.SAVE_SETTINGS);
			form.setDisabled(true);
			return true;
		} else {
			return false;
		}
	}

	public boolean onCancelClick(ClickEvent event) {
		setBlueprint(blueprint);
		form.setDisabled(true);
		return true;
	}

	public boolean validate() {
		if (!form.validate()) {
			SC.say(MESSAGES.formWarnNotvalid());
			return false;
		}
		return true;
	}


	public boolean onResetClick(ClickEvent event) {
		return false;
	}


	public boolean isDefault() {
		return true;
	}


}
