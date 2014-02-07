/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.reporting.command.dto;

import org.geomajas.command.CommandResponse;

/**
 * Response object for {@link org.geomajas.plugin.reporting.command.reporting.PrepareReportingCommand}.
 *
 * @author Jan De Moerloose
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
//@Api(allMethods = true) don't know about api, all this caching stuff causes problems cfr lazy loading etc
public class PrepareReportingResponse extends CommandResponse {

	private static final long serialVersionUID = 100L;

	/** Report name placeholder in relative URL. */
	public static final String REPORT_NAME = "${reportName}";

	/** Report format placeholder in relative URL. */
	public static final String FORMAT = "${format}";

	private String key;
	private String cacheCategory;
	private String relativeUrl;

	/**
	 * Key which can be used to obtain the {@link org.geomajas.plugin.reporting.data.ReportingCacheContainer}.
	 *
	 * @return cache key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Key which can be used to obtain the {@link org.geomajas.plugin.reporting.data.ReportingCacheContainer}.
	 *
	 * @param key cache key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Cache category where the {@link org.geomajas.plugin.reporting.data.ReportingCacheContainer} can be found.
	 *
	 * @return cache category
	 */
	public String getCacheCategory() {
		return cacheCategory;
	}

	/**
	 * Cache category where the {@link org.geomajas.plugin.reporting.data.ReportingCacheContainer} can be found.
	 *
	 * @param cacheCategory cache category
	 */
	public void setCacheCategory(String cacheCategory) {
		this.cacheCategory = cacheCategory;
	}

	/**
	 * Url for the report, relative to the dispatcher base. Placeholders are used for the report name
	 * and report format.
	 *
	 * @return report url
	 */
	public String getRelativeUrl() {
		return relativeUrl;
	}

	/**
	 * Url for the report, relative to the dispatcher base. Placeholders are used for the report name
	 * and report format.
	 *
	 * @param relativeUrl report url
	 */
	public void setRelativeUrl(String relativeUrl) {
		this.relativeUrl = relativeUrl;
	}

	@Override
	public String toString() {
		return "PrepareReportingResponse{" +
				super.toString() +
				", key='" + key + '\'' +
				", cacheCategory='" + cacheCategory + '\'' +
				", relativeUrl='" + relativeUrl + '\'' +
				'}';
	}
}
