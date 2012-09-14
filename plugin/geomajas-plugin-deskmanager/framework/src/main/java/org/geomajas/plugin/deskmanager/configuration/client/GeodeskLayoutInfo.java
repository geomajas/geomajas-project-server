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
package org.geomajas.plugin.deskmanager.configuration.client;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ClientWidgetInfo;

/**
 * @author Balder
 * @author Oliver May
 * @author Kristof Heirwegh
 * @since 1.0.0
 */
@Api
public class GeodeskLayoutInfo implements ClientWidgetInfo {

	private static final long serialVersionUID = 1L;
	
	public static final String IDENTIFIER = "MagdageoGeodeskLayout";

	private String title;

	private String bannerUrl;

	private String titleColor;

	private String bgColor;

	private String logoUrl;

	private String logoAlt;

	private String areaType; // A, B, C

	private String areaId;

	private String municipality;

	private String titleBarColor;

	private String borderColor;

	private String logoHref;

	public String getTitle() {
		return title;
	}

	/**
	 * @param title de titel van het loket.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleColor() {
		return titleColor;
	}

	/**
	 * @param titleColor de kleur van de titel van het loket.
	 */
	public void setTitleColor(String titleColor) {
		this.titleColor = titleColor;
	}

	public String getBgColor() {
		return bgColor;
	}

	/**
	 * @param bgColor de achtergrondkleur van het loket.
	 */
	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	/**
	 * @param logoUrl de url naar het logo.
	 */
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getLogoAlt() {
		return logoAlt;
	}

	/**
	 * @param logoAlt alt tekst van het logo.
	 */
	public void setLogoAlt(String logoAlt) {
		this.logoAlt = logoAlt;
	}

	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}

	public String getAreaType() {
		return areaType;
	}

	public void setAreaId(String areaName) {
		this.areaId = areaName;
	}

	public String getAreaId() {
		return areaId;
	}

	/**
	 * @param value achtergrondkleur van de titelbalk.
	 */
	public void setTitleBarColor(String value) {
		this.titleBarColor = value;
	}

	public String getTitleBarColor() {
		return this.titleBarColor;
	}

	public void setBorderColor(String value) {
		this.borderColor = value;
	}

	public String getBorderColor() {
		return this.borderColor;
	}

	public void setLogoHref(String value) {
		this.logoHref = value;
	}

	public String getLogoHref() {
		return this.logoHref;
	}

	public String getMunicipality() {
		return municipality;
	}

	public void setMunicipality(String municipality) {
		this.municipality = municipality;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

}
