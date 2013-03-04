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

package org.geomajas.plugin.wmsclient.server.proxy;

/**
 * <p>
 * Configuration that determines how the requests should be manipulated before responding. A first option determines
 * whether or not to copy all headers from the original request. If this value is set to false, there is the option to
 * determines by means of identifiers which requests should be cached and which shouldn't.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class RequestConfiguration {

	private static final String PARAMETER_SPLIT_REGEX = "[\\s,]+";

	private boolean copyHeaders;

	private String[] cacheIdentifiers = new String[] {};

	public RequestConfiguration() {
	}

	public boolean isCopyHeaders() {
		return copyHeaders;
	}

	public void setCopyHeaders(boolean copyHeaders) {
		this.copyHeaders = copyHeaders;
	}

	public String[] getCacheIdentifiers() {
		return cacheIdentifiers;
	}

	public void setCacheIdentifiers(String[] cacheIdentifiers) {
		this.cacheIdentifiers = cacheIdentifiers;
	}

	public void setCacheIdentifiers(String cacheIdentifiers) {
		this.cacheIdentifiers = cacheIdentifiers.split(PARAMETER_SPLIT_REGEX);
	}
}