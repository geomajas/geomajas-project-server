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

import org.geomajas.plugin.deskmanager.client.gwt.manager.common.SaveButtonBar;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.SaveButtonBar.WoaEventHandler;
import org.geomajas.plugin.deskmanager.domain.dto.LayerModelDto;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Kristof Heirwegh
 */
public class DatalayerStyle extends VLayout implements WoaEventHandler {

	private LayerModelDto lmd;

	public DatalayerStyle() {
		super(5);
		setWidth100();

		SaveButtonBar buttonBar = new SaveButtonBar(this);
		addMember(buttonBar);

		VLayout group = new VLayout();
		group.setPadding(10);
		group.setIsGroup(true);
		group.setGroupTitle("Stijl");
		group.setOverflow(Overflow.AUTO);

		// group.addMember(form);

		// TODO

		addMember(group);
	}

	public void setLayerModel(LayerModelDto lmd) {
	}

	// -- SaveButtonBar events --------------------------------------------------------

	public boolean onEditClick(ClickEvent event) {
		return true;
	}

	public boolean onSaveClick(ClickEvent event) {
		return validate();
	}

	public boolean onCancelClick(ClickEvent event) {
		setLayerModel(lmd);
		return true;
	}

	public boolean validate() {
		// if (!form.validate()) {
		// SC.say("Niet alle gegevens werden correct ingevuld.");
		// return false;
		// }
		return true;
	}
}
