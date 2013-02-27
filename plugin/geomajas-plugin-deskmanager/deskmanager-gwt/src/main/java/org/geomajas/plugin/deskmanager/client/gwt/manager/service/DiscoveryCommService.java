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
package org.geomajas.plugin.deskmanager.client.gwt.manager.service;

import java.util.List;
import java.util.Map;

import org.geomajas.command.CommandResponse;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.Deferred;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetRasterLayerConfigRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetRasterLayerConfigResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetGeotoolsVectorCapabilitiesRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetGeotoolsVectorCapabilitiesResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetVectorLayerConfigRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetVectorLayerConfigResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetWmsCapabilitiesRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetWmsCapabilitiesResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.RasterCapabilitiesInfo;
import org.geomajas.plugin.deskmanager.command.manager.dto.DynamicRasterLayerConfiguration;
import org.geomajas.plugin.deskmanager.command.manager.dto.VectorCapabilitiesInfo;
import org.geomajas.plugin.deskmanager.command.manager.dto.DynamicVectorLayerConfiguration;

/**
 * Convenience class with helper methods for commands.
 * 
 * @author Kristof Heirwegh
 */
public final class DiscoveryCommService {

	/**
	 * Utility class.
	 */
	private DiscoveryCommService() {}

	// -- Vector ----------------------------------------------------

	public static void getVectorCapabilities(final Map<String, String> connectionProps,
			final DataCallback<List<VectorCapabilitiesInfo>> onFinish, final DataCallback<String> onError) {
		GetGeotoolsVectorCapabilitiesRequest request = new GetGeotoolsVectorCapabilitiesRequest();
		request.setConnectionProperties(connectionProps);
		GwtCommand command = new GwtCommand(GetGeotoolsVectorCapabilitiesRequest.COMMAND);
		command.setCommandRequest(request);

		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new CommandCallback<GetGeotoolsVectorCapabilitiesResponse>() {

					public void execute(GetGeotoolsVectorCapabilitiesResponse response) {
						if (onFinish != null) {
							onFinish.execute(response.getVectorCapabilities());
						}
					}
				});

		def.addCallback(new AbstractCommandCallback<CommandResponse>() {

			public void execute(CommandResponse response) {
			}

			public void onCommandException(CommandResponse response) {
				if (onError != null && response.getErrorMessages().size() > 0) {
					onError.execute(response.getErrorMessages().get(0));
				}
			}
		});
	}

	public static void getRasterCapabilities(final Map<String, String> connectionProps,
			final DataCallback<List<RasterCapabilitiesInfo>> onFinish, final DataCallback<String> onError) {
		GetWmsCapabilitiesRequest request = new GetWmsCapabilitiesRequest();
		request.setConnectionProperties(connectionProps);
		GwtCommand command = new GwtCommand(GetWmsCapabilitiesRequest.COMMAND);
		command.setCommandRequest(request);

		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new CommandCallback<GetWmsCapabilitiesResponse>() {

					public void execute(GetWmsCapabilitiesResponse response) {
						if (onFinish != null) {
							onFinish.execute(response.getRasterCapabilities());
						}
					}
				});

		def.addCallback(new AbstractCommandCallback<CommandResponse>() {

			public void execute(CommandResponse response) {
			}

			public void onCommandException(CommandResponse response) {
				if (onError != null && response.getErrorMessages().size() > 0) {
					onError.execute(response.getErrorMessages().get(0));
				}
			}
		});
	}

	public static void getVectorLayerConfiguration(final Map<String, String> connectionProps, String layerTypeName,
			final DataCallback<DynamicVectorLayerConfiguration> onFinish, final DataCallback<String> onError) {
		GetVectorLayerConfigRequest request = new GetVectorLayerConfigRequest();
		request.setConnectionProperties(connectionProps);
		request.setLayerName(layerTypeName);
		GwtCommand command = new GwtCommand(GetVectorLayerConfigRequest.COMMAND);
		command.setCommandRequest(request);

		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new CommandCallback<GetVectorLayerConfigResponse>() {

					public void execute(GetVectorLayerConfigResponse response) {
						if (onFinish != null) {
							onFinish.execute(response.getVectorLayerConfiguration());
						}
					}
				});

		def.addCallback(new AbstractCommandCallback<CommandResponse>() {

			public void execute(CommandResponse response) {
			}

			public void onCommandException(CommandResponse response) {
				if (onError != null && response.getErrorMessages().size() > 0) {
					onError.execute(response.getErrorMessages().get(0));
				}
			}
		});
	}
	
	public static void getRasterLayerConfiguration(Map<String, String> connectionProps, RasterCapabilitiesInfo info,
			final DataCallback<DynamicRasterLayerConfiguration> onFinish, final DataCallback<String> onError) {
		GetRasterLayerConfigRequest request = new GetRasterLayerConfigRequest();
		request.setRasterCapabilitiesInfo(info);
		request.setConnectionProperties(connectionProps);
		GwtCommand command = new GwtCommand(GetRasterLayerConfigRequest.COMMAND);
		command.setCommandRequest(request);

		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new CommandCallback<GetRasterLayerConfigResponse>() {

					public void execute(GetRasterLayerConfigResponse response) {
						if (onFinish != null) {
							onFinish.execute(response.getRasterLayerConfiguration());
						}
					}
				});

		def.addCallback(new AbstractCommandCallback<CommandResponse>() {

			public void execute(CommandResponse response) {
			}

			public void onCommandException(CommandResponse response) {
				if (onError != null && response.getErrorMessages().size() > 0) {
					onError.execute(response.getErrorMessages().get(0));
				}
			}
		});
	}
}
