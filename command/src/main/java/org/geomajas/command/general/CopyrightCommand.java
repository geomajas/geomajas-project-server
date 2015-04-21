/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.command.general;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.geomajas.annotation.Api;
import org.geomajas.command.CommandHasRequest;
import org.geomajas.command.EmptyCommandRequest;
import org.geomajas.command.dto.CopyrightResponse;
import org.geomajas.global.CopyrightInfo;
import org.geomajas.global.PluginInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Command which allows fetching the copyright info for back-end and plug-ins.
 * <p/>
 * It also emits the copyright info in the logs for extra assurance of compliance with the requirements.
 *
 * @author Joachim Van der Auwera
 * @since 1.7.0
 */
@Api
@Component()
public class CopyrightCommand implements CommandHasRequest<EmptyCommandRequest, CopyrightResponse> {

	private final Logger log = LoggerFactory.getLogger(CopyrightCommand.class);

	private Map<String, CopyrightInfo> copyrightMap = new HashMap<String, CopyrightInfo>();

	@Autowired(required = false)
	protected Map<String, PluginInfo> declaredPlugins;

	/**
	 * Build copyright map once.
	 */
	@PostConstruct
	protected void buildCopyrightMap() {
		if (null == declaredPlugins) {
			return;
		}
		// go over all plug-ins, adding copyright info, avoiding duplicates (on object key)
		for (PluginInfo plugin : declaredPlugins.values()) {
			for (CopyrightInfo copyright : plugin.getCopyrightInfo()) {
				String key = copyright.getKey();
				String msg = copyright.getKey() + ": " + copyright.getCopyright() + " : licensed as " +
						copyright.getLicenseName() + ", see " + copyright.getLicenseUrl();
				if (null != copyright.getSourceUrl()) {
					msg += " source " + copyright.getSourceUrl();
				}
				if (!copyrightMap.containsKey(key)) {
					log.info(msg);
					copyrightMap.put(key, copyright);
				}
			}
		}
	}

	@Override
	public EmptyCommandRequest getEmptyCommandRequest() {
		return new EmptyCommandRequest();
	}

	@Override
	public CopyrightResponse getEmptyCommandResponse() {
		return new CopyrightResponse();
	}

	@Override
	public void execute(EmptyCommandRequest request, CopyrightResponse response) throws Exception {
		response.setCopyrights(new ArrayList<CopyrightInfo>(copyrightMap.values()));
	}
}