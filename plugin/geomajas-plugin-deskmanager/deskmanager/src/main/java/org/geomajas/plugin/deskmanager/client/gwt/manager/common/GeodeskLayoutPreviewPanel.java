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
package org.geomajas.plugin.deskmanager.client.gwt.manager.common;

import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.util.UrlBuilder;
import org.geomajas.plugin.deskmanager.configuration.client.GeodeskLayoutInfo;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * FIXME : Move to MGapp?
 * @author Kristof Heirwegh
 */
public class GeodeskLayoutPreviewPanel extends VLayout {

	private GeodeskLayoutInfo layout;

	private HTMLPane content;

	private String box = "<div style='width:340px; height:100%;'>"
			+ "<div style='position:absolute; left: 10px; top:10px; width: 270px; " +
			"height:150px; border: medium solid ${bordercolor}; background-color: ${bgcolor};' >";

	private String partTitle = "<div class=\"previewtitle\" "
			+ "style='background-color: ${titlebarcolor}; color: ${titlecolor}' >${title}</div></div>";

	private String link = "<div style='position:absolute; top:43px; left: 25px; "
			+ "vertical-align:middle; text-align:center; height:110px; width:250px; " +
			"overflow:hidden; border: thin dashed black'>"
			+ "<div style='position:absolute; z-index:9999; font-size:0.9em; "
			+ "font-family: sans serif;'>Logo: 250x110</div>"
			+ "<a href='${link}' title=\"${linkalt}\" target='_blank' >";

	private String logo = "<img src='${imgurl}' style='height:100%; z-index:10; "
			+ "border: thin dotted blue;' /></a></div>";

	private String banner = "<div style='position:absolute; top:170px; left: 25px; "
			+ "vertical-align:middle; text-align:center; height:226px; width:500px; overflow:hidden;" +
			" border: thin dashed black'>"
			+ "<div style='position:absolute; z-index:9999; font-size:0.9em; font-family: "
			+ "sans serif;'>Opstart banner: 500x226</div>"
			+ "<img src='${bannerurl}' style='height:100%; z-index:10; border: thin dotted blue;' /></div>";

	private String boxEnd = "</div>";

	public GeodeskLayoutPreviewPanel() {

		// --- fancy border ---
		setIsGroup(true);
		setGroupTitle("Voorbeeld layout");

		// --- main content ---
		content = new HTMLPane();
		content.setWidth(345);
		content.setHeight(270);
		content.setContents("");
		content.setOverflow(Overflow.HIDDEN);
		addMember(content);
	}

	public void refresh() {
		update();
	}

	// ------------------------------------------------------------------

	private void update() {
		if (layout == null) {
			content.setContents("");
		} else {
			try {
				String part = box.replaceFirst("\\$\\{bordercolor\\}", layout.getBorderColor());
				String result = part.replaceFirst("\\$\\{bgcolor\\}", layout.getBgColor());

				part = partTitle.replaceFirst("\\$\\{title\\}", layout.getTitle());
				part = part.replaceFirst("\\$\\{titlecolor\\}", layout.getTitleColor());
				result += part.replaceFirst("\\$\\{titlebarcolor\\}", layout.getTitleBarColor());

				part = link.replaceFirst("\\$\\{link\\}", layout.getLogoHref());
				result += part.replaceFirst("\\$\\{linkalt\\}", layout.getLogoAlt());

				UrlBuilder logoUrl = new UrlBuilder(Geomajas.getDispatcherUrl() + layout.getLogoUrl());
				result += logo.replaceFirst("\\$\\{imgurl\\}", logoUrl.toString());

				UrlBuilder bannerUrl = new UrlBuilder(Geomajas.getDispatcherUrl() + layout.getBannerUrl());
				result += banner.replaceFirst("\\$\\{bannerurl\\}", bannerUrl.toString());

				result += boxEnd;

				// SC.say(result.replaceAll("<", "&lt;"));
				content.setContents(result);
			} catch (Exception e) {
				content.setContents("[#Fout bij genereren voorbeeld: " + e.getMessage() + "]");
			}
		}
	}

	// ------------------------------------------------------------------

	public void setLoketLayout(GeodeskLayoutInfo layout) {
		this.layout = layout;
		update();
	}
}
