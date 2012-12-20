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
package org.geomajas.plugin.deskmanager.command.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.command.Command;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.plugin.deskmanager.command.manager.dto.LayerModelResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveLayerModelRequest;
import org.geomajas.plugin.deskmanager.domain.LayerModel;
import org.geomajas.plugin.deskmanager.domain.MailAddress;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.plugin.deskmanager.service.common.LayerModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command that saves changes to a layer model in the database. The layermodel is passed as dto.
 * 
 * @author Oliver May
 * @author Kristof Heirwegh
 * 
 */
@Component(SaveLayerModelRequest.COMMAND)
@Transactional(rollbackFor = { Exception.class })
public class SaveLayerModelCommand implements Command<SaveLayerModelRequest, LayerModelResponse> {

	private final Logger log = LoggerFactory.getLogger(SaveLayerModelCommand.class);

	@Autowired
	private DtoConverterService dtoService;

	@Autowired
	private LayerModelService layerModelService;

	/** {@inheritDoc} */
	public void execute(SaveLayerModelRequest request, LayerModelResponse response) throws Exception {
		try {
			if (request.getLayerModel() == null) {
				response.getErrorMessages().add("Geen Datalaag opgegeven ??");
			} else if (request.getLayerModel().getLayerConfiguration() == null) {
				response.getErrorMessages().add("Geen Laag configuratie opgegeven ??");
			} else {
				LayerModel target = layerModelService.getLayerModelById(request.getLayerModel().getId());
				if (target == null) {
					response.getErrorMessages().add(
							"Geen Datalaag gevonden voor id: " + request.getLayerModel().getId()
									+ " (Nieuwe Datalaag?)");
				} else {
					LayerModel source = dtoService.fromDto(request.getLayerModel());
					if (!target.isReadOnly()) {
						if (target.getLayerConfiguration() == null) {
							target.setName(source.getName());
							target.setActive(source.isActive());
							target.setDefaultVisible(source.isDefaultVisible());
							target.setPublic(source.isPublic());

						} else {
							ClientLayerInfo cli = source.getLayerConfiguration().getClientLayerInfo();
							org.geomajas.plugin.deskmanager.configuration.client.ClientLayerInfo ud = (org.geomajas.plugin.deskmanager.configuration.client.ClientLayerInfo) cli.getUserData();
							target.setName(cli.getLabel());
							target.setActive(ud.isActive());
							target.setClientLayerId(cli.getId());
							target.setDefaultVisible(cli.isVisible());
							target.setMaxScale(cli.getMaximumScale());
							target.setMinScale(cli.getMinimumScale());
							target.setPublic(ud.isPublic());
							target.setLayerType(source.getLayerConfiguration().getServerLayerInfo().getLayerType()
									.getGeometryType());
							target.setLayerConfiguration(source.getLayerConfiguration());
						}
					}
					copyNotifications(source, target);

					layerModelService.saveOrUpdateLayerModel(target);
					response.setLayerModel(dtoService.toDto(target, false));
				}
			}
		} catch (Exception e) {
			response.getErrorMessages().add("Fout bij opslaan Datalaag: " + e.getMessage());
			log.error("fout bij opslaan datalaag.", e);
		}
	}

	/** {@inheritDoc} */
	public LayerModelResponse getEmptyCommandResponse() {
		return new LayerModelResponse();
	}

	// -------------------------------------------------

	private void copyNotifications(LayerModel source, LayerModel target) throws Exception {
		List<MailAddress> toDelete = new ArrayList<MailAddress>();
		List<MailAddress> toAdd = new ArrayList<MailAddress>();
		Map<Long, MailAddress> sourcemap = toMap(source.getMailAddresses());

		// remove deleted records
		for (MailAddress m : target.getMailAddresses()) {
			if (!source.getMailAddresses().contains(m)) {
				toDelete.add(m);
			}
		}
		if (toDelete.size() > 0) {
			target.getMailAddresses().removeAll(toDelete);
		}

		// update existing
		for (MailAddress upd : target.getMailAddresses()) {
			MailAddress src = sourcemap.get(upd.getId());
			if (src != null) {
				upd.setEmail(src.getEmail());
				upd.setName(src.getName());
			} else {
				log.warn("MailAddress not found !? (id: " + upd.getId() + ")");
			}
		}

		// add new
		for (MailAddress m : source.getMailAddresses()) {
			if (!target.getMailAddresses().contains(m)) {
				toAdd.add(m);
			}
		}
		if (toAdd.size() > 0) {
			target.getMailAddresses().addAll(toAdd);
		}
	}

	private Map<Long, MailAddress> toMap(List<MailAddress> list) {
		Map<Long, MailAddress> res = new HashMap<Long, MailAddress>();
		for (MailAddress m : list) {
			if (m.getId() != null) {
				res.put(m.getId(), m);
			}
		}

		return res;
	}
}
