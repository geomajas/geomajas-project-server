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
package org.geomajas.plugin.deskmanager.client.gwt.geodesk.ribbon;

import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.ScaleSelect;
import org.geomajas.widget.utility.common.client.ribbon.RibbonColumn;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Oliver May
 * 
 */
public class ScaleSelectRibbonColumn extends VLayout implements RibbonColumn {

	public static final String IDENTIFIER = "SclaeSelectRibbonColumn";
	private ScaleSelect scaleSelect;

	public ScaleSelectRibbonColumn(MapWidget mapWidget) {
		super(8);
		setWidth(100);
		scaleSelect = new ScaleSelect(mapWidget);
		scaleSelect.setWidth100();
		scaleSelect.setColWidths("0", "*");
		FormItem combo = scaleSelect.getFields()[0];
		combo.setWidth(100);
		combo.setShowTitle(false);
		addMember(scaleSelect);
	}

	public Widget asWidget() {
		return this;
	}

	public void setShowTitles(boolean showTitles) {
	}

	public boolean isShowTitles() {
		return false;
	}

	public void setTitleAlignment(TitleAlignment titleAlignment) {
	}

	public TitleAlignment getTitleAlignment() {
		return TitleAlignment.BOTTOM;
	}

	public void setButtonBaseStyle(String buttonBaseStyle) {
	}

	/**
	 * Can accept "X and "Y" text values to be printed out.
	 */
	public void configure(String key, String value) {
	}

	// ------------------------------------------------------------------------
	// SmartGWT methods overrides:
	// ------------------------------------------------------------------------

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	// ------------------------------------------------------------------------
	// Private classes:
	// ------------------------------------------------------------------------

	public boolean isEnabled() {
		return !getDisabled();
	}

	public void setEnabled(boolean enabled) {
		setDisabled(!enabled);
	}
}
