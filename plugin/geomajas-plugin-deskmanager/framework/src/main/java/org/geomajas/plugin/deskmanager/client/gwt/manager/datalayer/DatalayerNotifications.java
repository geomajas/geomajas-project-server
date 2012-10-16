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
package org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer;

import org.geomajas.plugin.deskmanager.client.gwt.geodesk.widget.infowindow.NotificationWindow;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.AbstractConfigurationLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.MailManagePanel;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.SaveButtonBar;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.ManagerCommandService;
import org.geomajas.plugin.deskmanager.domain.dto.LayerModelDto;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
public class DatalayerNotifications extends AbstractConfigurationLayout {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);
	
	private LayerModelDto lmd;

	private MailManagePanel mailManage;

	public DatalayerNotifications() {
		super(5);
		setWidth100();

		SaveButtonBar buttonBar = new SaveButtonBar(this);
		addMember(buttonBar);

		mailManage = new MailManagePanel();
		mailManage.setDisabled(true);
		mailManage.setWidth100();
		mailManage.setHeight100();

		VLayout group = new VLayout();
		group.setPadding(10);
		group.setIsGroup(true);
		group.setGroupTitle(MESSAGES.datalayerNotificationsFormGroup());
		group.addMember(mailManage);
		group.setOverflow(Overflow.AUTO);

		addMember(group);
	}

	public void setLayerModel(final LayerModelDto lmd) {
		this.lmd = lmd;
		if (lmd != null) {
			mailManage.setValues(lmd.getMailAddresses());
		} else {
			mailManage.setValues(null);
		}
		fireChangedHandler();
	}

	// -- SaveButtonBar events --------------------------------------------------------

	public boolean onEditClick(ClickEvent event) {
		mailManage.setDisabled(false);
		return true;
	}

	public boolean onSaveClick(ClickEvent event) {
		if (lmd != null) {
			if (mailManage.hasChanged()) {
				if (mailManage.hasErrors()) {
					SC.warn(MESSAGES.datalayerNotificationsWarnInvalidFormData());
					return false;
				} else {
					lmd.setMailAddresses(mailManage.getValues());
					ManagerCommandService.saveLayerModel(lmd);
				}
			} else {
				NotificationWindow.showInfoMessage(MESSAGES.datalayerNotificationsNoFormChanges());
			}
		}
		mailManage.setDisabled(true);
		return true;
	}

	public boolean onCancelClick(ClickEvent event) {
		setLayerModel(lmd);
		mailManage.setDisabled(true);
		return true;
	}

	public boolean onResetClick(ClickEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isDefault() {
		// TODO Auto-generated method stub
		return true;
	}
}
