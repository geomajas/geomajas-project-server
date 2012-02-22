/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.sld.client;

/**
 * The central location of all name tokens for the application. All {@link ProxyPlace} classes get their tokens from
 * here. This class also makes it easy to use name tokens as a resource within UIBinder xml files.
 * <p />
 * The public static final String is used within the annotation {@link NameToken}, which can't use a method and the
 * method associated with this field is used within UiBinder which can't access static fields.
 * <p />
 * Also note the exclamation mark in front of the tokens, this is used for search engine crawling support.
 * 
 * @author Jan De Moerloose
 */
public final class NameTokens {

	private NameTokens() {

	}

	public static final String ABOUT_US_PAGE = "!aboutUsPage";

	public static final String CONTACT_PAGE = "!contactPage";

	public static final String HOME_PAGE = "!homePage";

	public static String getAboutUsePage() {
		return ABOUT_US_PAGE;
	}

	public static String getContactPage() {
		return CONTACT_PAGE;
	}

	public static String getHomePage() {
		return HOME_PAGE;
	}
}