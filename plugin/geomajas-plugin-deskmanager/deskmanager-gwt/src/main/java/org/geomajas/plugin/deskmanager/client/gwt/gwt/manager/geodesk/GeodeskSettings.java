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
package org.geomajas.plugin.deskmanager.client.gwt.manager.geodesk;

import java.util.LinkedHashMap;
import java.util.List;

import org.geomajas.plugin.deskmanager.client.gwt.manager.ManagerApplicationLoader;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.AbstractConfigurationLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.SaveButtonBar;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.GeodeskEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.GeodeskSelectionHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.ManagerCommandService;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveGeodeskRequest;
import org.geomajas.plugin.deskmanager.domain.dto.BlueprintDto;
import org.geomajas.plugin.deskmanager.domain.dto.GeodeskDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.form.validator.Validator;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * 
 * @author Oliver May
 * 
 */
public class GeodeskSettings extends AbstractConfigurationLayout implements GeodeskSelectionHandler {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);
	
	private static final DateTimeFormat DATE_FORMATTER = DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_SHORT);

	private static final int FORMITEM_WIDTH = 300;

	private GeodeskDto geodesk;

	private DynamicForm form;

	private TextItem geodeskName;

	private FormItem geodeskId;

	private SelectItem blueprints;

	private StaticTextItem lastEditBy;

	private StaticTextItem lastEditDate;

	private StaticTextItem geodeskAdministrator;

	private CheckboxItem active;

	private CheckboxItem publicGeodesk;

	private CheckboxItem limitToCreatorTerritory;

	private CheckboxItem limitToUserTerritory;

	private boolean geodeskIdValid;

	private boolean containsNonPublicLayers;

	public GeodeskSettings() {
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

		geodeskName = new TextItem();
		geodeskName.setTitle(MESSAGES.settingsNameGeodesk());
		geodeskName.setRequired(true);
		geodeskName.setWidth(FORMITEM_WIDTH);
		geodeskName.setWrapTitle(false);

		blueprints = new SelectItem();
		blueprints.setWidth(FORMITEM_WIDTH);
		blueprints.setTitle(MESSAGES.geodeskSettingsNameBlueprint());
		blueprints.setDisabled(true); // ter info
		blueprints.setTooltip("<nobr>" + MESSAGES.settingsNameBlueprintTooltip() + "</nobr>");

		if (Role.ADMINISTRATOR.equals(ManagerApplicationLoader.getInstance().getUserProfile().getRole())) {
			geodeskId = new TextItem();
			geodeskId.setRequired(true);
			geodeskId.addChangedHandler(new com.smartgwt.client.widgets.form.fields.events.ChangedHandler() {
				public void onChanged(ChangedEvent event) {
					String val = ((TextItem) geodeskId).getValueAsString();
					if (val != null && !"".equals(val)) {
						ManagerCommandService.checkGeodeskIdExists(val, new DataCallback<Boolean>() {

							public void execute(Boolean exists) {
								geodeskIdValid = !exists;
								geodeskId.validate();
								if (exists) {
									SC.warn(MESSAGES.settingGeodeskWarnGeodeskIdNotUnique());
								}
							}
						});
					}
				}
			});
			Validator v = new CustomValidator() {

				protected boolean condition(Object value) {
					return geodeskIdValid;
				}
			};
			v.setErrorMessage(MESSAGES.validatorWarnGeodeskIdNotUnique());
			geodeskId.setValidators(v);
		} else {
			geodeskId = new StaticTextItem();
		}

		geodeskId.setTitle(MESSAGES.geodeskSettingsId());
		geodeskId.setWidth(FORMITEM_WIDTH);
		geodeskId.setWrapTitle(false);
		geodeskId.setTooltip("<nobr>" + MESSAGES.geodeskSettingsIdTooltip() + "</nobr>");

		geodeskAdministrator = new StaticTextItem("geodeskAdministrator");
		geodeskAdministrator.setTitle(MESSAGES.geodeskSettingsAdmin());
		geodeskAdministrator.setWidth(FORMITEM_WIDTH);
		geodeskAdministrator.setWrapTitle(false);

		lastEditBy = new StaticTextItem("lastEditBy");
		lastEditBy.setTitle(MESSAGES.settingsLatestChangeBy());
		lastEditBy.setWidth(FORMITEM_WIDTH);
		lastEditBy.setWrapTitle(false);

		lastEditDate = new StaticTextItem("lastEditDate");
		lastEditDate.setTitle(MESSAGES.settingsLatestChangeWhen());
		lastEditDate.setWidth(FORMITEM_WIDTH);
		lastEditDate.setWrapTitle(false);

		active = new CheckboxItem();
		active.setTitle(MESSAGES.geodeskSettingsActiv());
		active.setWrapTitle(false);
		active.setTooltip(MESSAGES.geodeskActivTooltip());

		publicGeodesk = new CheckboxItem();
		publicGeodesk.setTitle(MESSAGES.geodeskSettingsPublic());
		publicGeodesk.setWrapTitle(false);
		publicGeodesk.setPrompt(MESSAGES.geodeskPublicTooltip());
		publicGeodesk.addChangeHandler(new com.smartgwt.client.widgets.form.fields.events.ChangeHandler() {

			public void onChange(ChangeEvent event) {
				if (containsNonPublicLayers) {
					SC.warn(MESSAGES.geodeskSettingsWarnCannotBePublic());
					event.cancel();
				}
			}
		});
		publicGeodesk.addChangedHandler(new com.smartgwt.client.widgets.form.fields.events.ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				boolean val = publicGeodesk.getValueAsBoolean();
				if (geodesk.getBlueprint().isLimitToCreatorTerritory()) {
					limitToCreatorTerritory.setDisabled(true);
				} else {
					limitToCreatorTerritory.setDisabled(!val);
				}
				if (geodesk.getBlueprint().isLimitToUserTerritory()) {
					limitToUserTerritory.setDisabled(true);
				} else {
					limitToUserTerritory.setDisabled(val);
				}
			}
		});

		limitToCreatorTerritory = new CheckboxItem();
		limitToCreatorTerritory.setTitle(MESSAGES.settingsLimitToTerritoryAdministrator());
		limitToCreatorTerritory.setWrapTitle(false);
		limitToCreatorTerritory.setPrompt(MESSAGES.settingsLimitToTerritoryAdministratorTooltip());

		limitToUserTerritory = new CheckboxItem();
		limitToUserTerritory.setTitle(MESSAGES.settingsLimitToTerritoryUser());
		limitToUserTerritory.setWrapTitle(false);
		limitToUserTerritory.setPrompt(MESSAGES.settingsLimitToTerritoryUserTooltip());

		// ----------------------------------------------------------

		form.setTitleOrientation(TitleOrientation.LEFT);
		form.setFields(geodeskName, active, blueprints, publicGeodesk, geodeskId, limitToCreatorTerritory, 
				geodeskAdministrator, limitToUserTerritory, lastEditBy, new SpacerItem(), lastEditDate,
				new SpacerItem());

		VLayout group = new VLayout();
		group.setPadding(10);
		group.setIsGroup(true);
		group.setGroupTitle(MESSAGES.settingsFormGroupSettings());
		group.addMember(form);
		group.setOverflow(Overflow.AUTO);

		addMember(group);

		// -- get data for dropdownbox
		ManagerCommandService.getBlueprints(new DataCallback<List<BlueprintDto>>() {

			public void execute(List<BlueprintDto> result) {
				if (result.size() > 0) {
					LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
					for (BlueprintDto blueprintDto : result) {
						if (blueprintDto.isActive()) {
							valueMap.put(blueprintDto.getId(), blueprintDto.getName());
						}
					}
					blueprints.setValueMap(valueMap);
				} else {
					SC.logWarn(MESSAGES.geodeskSettingsWarnNoBlueprints());
				}
			}
		});
	}

	private void setGeodesk(GeodeskDto loket) {
		form.clearValues();
		this.geodesk = loket;
		geodeskIdValid = true;
		if (loket != null) {
			geodeskName.setValue(loket.getName());
			geodeskId.setValue(loket.getGeodeskId());
			blueprints.setValue(loket.getBlueprint().getId());
			geodeskAdministrator.setValue(loket.getCreationBy());
			lastEditBy.setValue(loket.getLastEditBy());
			lastEditDate.setValue(DATE_FORMATTER.format(loket.getLastEditDate()));
			active.setValue(loket.isActive());
			publicGeodesk.setValue(loket.isPublic());
			limitToCreatorTerritory.setValue(loket.isLimitToCreatorTerritory());
			limitToUserTerritory.setValue(loket.isLimitToUserTerritory());
			limitToCreatorTerritory.setDisabled(!loket.isPublic());
			limitToUserTerritory.setDisabled(loket.isPublic());

			// FIXME
			// containsNonPublicLayers = (geodesk.getLayerTree() == null ? false : geodesk.getLayerTree()
			// .containsNonPublicLayers());

			// -- constraints from blueprint --
			publicGeodesk.setDisabled(!loket.getBlueprint().isPublic());
			if (!limitToUserTerritory.isDisabled()) {
				limitToUserTerritory.setDisabled(loket.getBlueprint().isLimitToUserTerritory());
			}
			if (!limitToCreatorTerritory.isDisabled()) {
				limitToCreatorTerritory.setDisabled(loket.getBlueprint().isLimitToCreatorTerritory());
			}
			if (!loket.getBlueprint().isGeodesksActive()) {
				active.setDisabled(true);
				active.setHint("<nobr>" + MESSAGES.geodeskSettingsWarnGeodeskInactivedByBlueprint() + "</nobr>");
			} else {
				active.setDisabled(false);
				active.setHint("");
			}
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
			geodesk.setName(geodeskName.getValueAsString());
			if (geodeskId instanceof TextItem) {
				geodesk.setGeodeskId(((TextItem) geodeskId).getValueAsString());
			}
			geodesk.setActive(active.getValueAsBoolean());
			geodesk.setPublic(publicGeodesk.getValueAsBoolean());
			if (geodesk.isPublic()) {
				geodesk.setLimitToLoketTerritory(limitToCreatorTerritory.getValueAsBoolean());
			} else {
				geodesk.setLimitToUserTerritory(limitToUserTerritory.getValueAsBoolean());
			}
			ManagerCommandService.saveGeodesk(geodesk, SaveGeodeskRequest.SAVE_SETTINGS);
			form.setDisabled(true);
			return true;
		} else {
			return false;
		}
	}

	public boolean onCancelClick(ClickEvent event) {
		setGeodesk(geodesk);
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

	public void onGeodeskSelectionChange(GeodeskEvent geodeskEvent) {
		setGeodesk(geodeskEvent.getGeodesk());
	}
	

	public boolean onResetClick(ClickEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isDefault() {
		return true;
	}
}
