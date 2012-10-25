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

import org.geomajas.plugin.deskmanager.client.gwt.manager.common.AbstractConfigurationLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.SaveButtonBar;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.themeconfig.ThemeConfigurationPanel;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.GeodeskEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.GeodeskSelectionHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.Whiteboard;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.ManagerCommandService;
import org.geomajas.plugin.deskmanager.client.gwt.manager.util.GeodeskDtoUtil;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveBlueprintRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveGeodeskRequest;
import org.geomajas.plugin.deskmanager.domain.dto.GeodeskDto;
import org.geomajas.widget.advancedviews.configuration.client.ThemesInfo;

import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Panel to allow theme configuration on the geodesk.
 * 
 * @author Oliver May
 * 
 */
public class GeodeskThemeConfig extends AbstractConfigurationLayout implements GeodeskSelectionHandler {

	private GeodeskDto geodesk;

	private ThemeConfigurationPanel themePanel;

	public GeodeskThemeConfig() {
		super();
		setMargin(5);

		SaveButtonBar buttonBar = new SaveButtonBar(this);
		addMember(buttonBar);

		// -------------------------------------------------

		themePanel = new ThemeConfigurationPanel();
		// themePanel.setDisabled(true);
		themePanel.setWidth100();
		themePanel.setHeight100();
		// themePanel.setOverflow(Overflow.AUTO);
		themePanel.setDisabled(true);

		addMember(themePanel);
	}

	public void onGeodeskSelectionChange(GeodeskEvent geodeskEvent) {
		setGeodesk(geodeskEvent.getGeodesk());
	}

	private void setGeodesk(GeodeskDto geodesk) {
		this.geodesk = geodesk;
		if (geodesk != null) {
			themePanel.setMainMap(GeodeskDtoUtil.getMainMap(geodesk));

			if (GeodeskDtoUtil.getMainMapClientWidgetInfo(geodesk).get(ThemesInfo.IDENTIFIER) != null) {
				themePanel.setThemeConfig((ThemesInfo) GeodeskDtoUtil.getMainMapClientWidgetInfo(geodesk).get(
						ThemesInfo.IDENTIFIER));
			} else {
				themePanel.setThemeConfig(new ThemesInfo());
			}
		}
		fireChangedHandler();
	}

	// -- SaveButtonBar events --------------------------------------------------------

	public boolean onEditClick(ClickEvent event) {
		themePanel.setDisabled(false);
		return true;
	}

	public boolean onSaveClick(ClickEvent event) {
		// If we save and the clientwidgetinfo was not yet defined on the geodesk, overwrite it.
		geodesk.getMainMapClientWidgetInfos().put(ThemesInfo.IDENTIFIER, themePanel.getThemeConfig());

		ManagerCommandService.saveGeodesk(geodesk, SaveGeodeskRequest.SAVE_CLIENTWIDGETINFO);
		themePanel.setDisabled(true);
		return true;
	}

	public boolean onCancelClick(ClickEvent event) {
		setGeodesk(geodesk);
		themePanel.setDisabled(true);
		// Reload the geodesk
		ManagerCommandService.getGeodesk(geodesk.getId(), new DataCallback<GeodeskDto>() {

			public void execute(GeodeskDto result) {
				Whiteboard.fireEvent(new GeodeskEvent(result));
			}
		});
		return true;
	}

	public boolean onResetClick(ClickEvent event) {
		geodesk.getMainMapClientWidgetInfos().remove(ThemesInfo.IDENTIFIER);
		ManagerCommandService.saveGeodesk(geodesk, SaveBlueprintRequest.SAVE_CLIENTWIDGETINFO);
		return true;
	}

	public boolean isDefault() {
		return !geodesk.getMainMapClientWidgetInfos().containsKey(ThemesInfo.IDENTIFIER);
	}
}
