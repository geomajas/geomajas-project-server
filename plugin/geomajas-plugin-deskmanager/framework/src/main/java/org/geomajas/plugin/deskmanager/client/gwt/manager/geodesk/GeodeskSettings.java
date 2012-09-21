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
package org.geomajas.plugin.deskmanager.client.gwt.manager.geodesk;

import java.util.LinkedHashMap;
import java.util.List;

import org.geomajas.plugin.deskmanager.client.gwt.manager.ManagerApplication;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.SaveButtonBar;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.SaveButtonBar.WoaEventHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.CommService;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveGeodeskRequest;
import org.geomajas.plugin.deskmanager.domain.dto.BlueprintDto;
import org.geomajas.plugin.deskmanager.domain.dto.GeodeskDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;

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
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.form.validator.Validator;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * 
 * @author Oliver May
 *
 */
public class GeodeskSettings extends VLayout implements WoaEventHandler {

	private static final DateTimeFormat DATE_FORMATTER = DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_SHORT);

	private static final int FORMITEM_WIDTH = 300;

	private GeodeskDto geodesk;

	private DynamicForm form;

	private TextItem geodeskName;

	private FormItem geodeskId;

	private SelectItem blueprints;

	private StaticTextItem lastEditBy;

	private StaticTextItem lastEditDate;

	private StaticTextItem loketBeheerder;

	private CheckboxItem active;

	private CheckboxItem publiek;

	private CheckboxItem limitToCreatorTerritory;

	private CheckboxItem limitToUserTerritory;

	private boolean geodeskIdValid;

	private boolean containsNonPublicLayers;

	public GeodeskSettings() {
		super(5);

		SaveButtonBar buttonBar = new SaveButtonBar(this);
		addMember(buttonBar);

		// ----------------------------------------------------------

		form = new DynamicForm();
		form.setAutoWidth();
		form.setAutoFocus(true); /* Set focus on first field */
		form.setNumCols(4);
		form.setDisabled(true);

		geodeskName = new TextItem();
		geodeskName.setTitle("Naam geodesk");
		geodeskName.setRequired(true);
		geodeskName.setWidth(FORMITEM_WIDTH);
		geodeskName.setWrapTitle(false);

		blueprints = new SelectItem();
		blueprints.setWidth(FORMITEM_WIDTH);
		blueprints.setTitle("Blauwdruk");
		blueprints.setDisabled(true); // ter info
		blueprints.setTooltip("<nobr>De blauwbruk die als basis gebruikt wordt voor dit geodesk.</nobr>");

		if (Role.ADMINISTRATOR.equals(ManagerApplication.getInstance().getUserProfile()
				.getRole())) {
			geodeskId = new TextItem();
			geodeskId.setRequired(true);
			geodeskId.addChangedHandler(new ChangedHandler() {

				public void onChanged(ChangedEvent event) {
					String val = ((TextItem) geodeskId).getValueAsString();
					if (val != null && !"".equals(val)) {
						CommService.checkGeodeskIdExists(val, new DataCallback<Boolean>() {

							public void execute(Boolean exists) {
								geodeskIdValid = !exists;
								geodeskId.validate();
								if (exists) {
									SC.warn("Geodesk Id bestaat reeds! Gelieve een ander Geodesk Id te kiezen.");
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
			v.setErrorMessage("Geodesk Id bestaat reeds!");
			geodeskId.setValidators(v);
		} else {
			geodeskId = new StaticTextItem();
		}

		geodeskId.setTitle("Geodesk Id");
		geodeskId.setWidth(FORMITEM_WIDTH);
		geodeskId.setWrapTitle(false);
		geodeskId.setTooltip("<nobr>Naam gebruikt in URL om geodesk op te roepen.</nobr>");

		loketBeheerder = new StaticTextItem("loketBeheerder");
		loketBeheerder.setTitle("Geodesk-Beheerder");
		loketBeheerder.setWidth(FORMITEM_WIDTH);
		loketBeheerder.setWrapTitle(false);

		lastEditBy = new StaticTextItem("lastEditBy");
		lastEditBy.setTitle("Laatste wijziging door");
		lastEditBy.setWidth(FORMITEM_WIDTH);
		lastEditBy.setWrapTitle(false);

		lastEditDate = new StaticTextItem("lastEditDate");
		lastEditDate.setTitle("Laatste wijziging op");
		lastEditDate.setWidth(FORMITEM_WIDTH);
		lastEditDate.setWrapTitle(false);

		active = new CheckboxItem();
		active.setTitle("Geodesk actief");
		active.setWrapTitle(false);
		active.setTooltip("Aan: geodesk kan geraadpleegd worden.");

		publiek = new CheckboxItem();
		publiek.setTitle("Publiek");
		publiek.setWrapTitle(false);
		publiek.setPrompt("Aan: geodesk kan geraadpleegd worden zonder aanmelden.<br />"
				+ "Uit: geodesk kan enkel geraadpleegd worden na aanmelden (LB of VO).");
		publiek.addChangeHandler(new ChangeHandler() {

			public void onChange(ChangeEvent event) {
				if (containsNonPublicLayers) {
					SC.warn("U kan dit geodesk niet publiek maken daar deze niet publieke lagen bevat!");
					event.cancel();
				}
			}
		});
		publiek.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				boolean val = publiek.getValueAsBoolean();
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
		limitToCreatorTerritory.setTitle("Beperk tot grondgebied Loketbeheerder");
		limitToCreatorTerritory.setWrapTitle(false);
		limitToCreatorTerritory
				.setPrompt("Aan: betekent dat beveiliging geldt voor het grondgebied" +
						" van de entiteit loketbeheerder.<br />Uit: geen beveiliging.");

		limitToUserTerritory = new CheckboxItem();
		limitToUserTerritory.setTitle("Beperk tot grondgebied Gebruiker");
		limitToUserTerritory.setWrapTitle(false);
		limitToCreatorTerritory.setPrompt("Aan: betekent dat beveiliging geldt voor het grondgebied"
				+ " van de entiteit gebruiker.<br />Uit: geen beveiliging.");

		// ----------------------------------------------------------

		form.setTitleOrientation(TitleOrientation.LEFT);
		form.setFields(geodeskName, active, blueprints, publiek, geodeskId, limitToCreatorTerritory, loketBeheerder,
				limitToUserTerritory, lastEditBy, new SpacerItem(), lastEditDate, new SpacerItem());

		VLayout group = new VLayout();
		group.setPadding(10);
		group.setIsGroup(true);
		group.setGroupTitle("Instellingen");
		group.addMember(form);
		group.setOverflow(Overflow.AUTO);

		addMember(group);

		// -- get data for dropdownbox
		CommService.getBlueprints(new DataCallback<List<BlueprintDto>>() {

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
					SC.logWarn("Er zijn geen blauwdrukken beschikbaar!");
				}
			}
		});
	}

	public void setGeodesk(GeodeskDto loket) {
		form.clearValues();
		this.geodesk = loket;
		geodeskIdValid = true;
		if (loket != null) {
			geodeskName.setValue(loket.getName());
			geodeskId.setValue(loket.getGeodeskId());
			blueprints.setValue(loket.getBlueprint().getId());
			loketBeheerder.setValue(loket.getCreationBy());
			lastEditBy.setValue(loket.getLastEditBy());
			lastEditDate.setValue(DATE_FORMATTER.format(loket.getLastEditDate()));
			active.setValue(loket.isActive());
			publiek.setValue(loket.isPublic());
			limitToCreatorTerritory.setValue(loket.isLimitToCreatorTerritory());
			limitToUserTerritory.setValue(loket.isLimitToUserTerritory());
			limitToCreatorTerritory.setDisabled(!loket.isPublic());
			limitToUserTerritory.setDisabled(loket.isPublic());

			//FIXME
//			containsNonPublicLayers = (geodesk.getLayerTree() == null ? false : geodesk.getLayerTree()
//					.containsNonPublicLayers());

			// -- constraints from blueprint --
			publiek.setDisabled(!loket.getBlueprint().isPublic());
			if (!limitToUserTerritory.isDisabled()) {
				limitToUserTerritory.setDisabled(loket.getBlueprint().isLimitToUserTerritory());
			}
			if (!limitToCreatorTerritory.isDisabled()) {
				limitToCreatorTerritory.setDisabled(loket.getBlueprint().isLimitToCreatorTerritory());
			}
			if (!loket.getBlueprint().isGeodesksActive()) {
				active.setDisabled(true);
				active.setHint("<nobr> Geodesk gedesactiveerd door blauwdruk.</nobr>");
			} else {
				active.setDisabled(false);
				active.setHint("");
			}
		}
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
			geodesk.setPublic(publiek.getValueAsBoolean());
			if (geodesk.isPublic()) {
				geodesk.setLimitToLoketTerritory(limitToCreatorTerritory.getValueAsBoolean());
			} else {
				geodesk.setLimitToUserTerritory(limitToUserTerritory.getValueAsBoolean());
			}
			CommService.saveGeodesk(geodesk, SaveGeodeskRequest.SAVE_SETTINGS);
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
			SC.say("Niet alle gegevens werden correct ingevuld.");
			return false;
		}
		return true;
	}
}
