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
import org.geomajas.plugin.deskmanager.command.manager.dto.GetRasterLayerConfigurationRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetRasterLayerConfigurationResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetVectorCapabilitiesRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetVectorCapabilitiesResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetVectorLayerConfigurationRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetVectorLayerConfigurationResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetWmsCapabilitiesRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetWmsCapabilitiesResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.RasterCapabilitiesInfo;
import org.geomajas.plugin.deskmanager.command.manager.dto.RasterLayerConfiguration;
import org.geomajas.plugin.deskmanager.command.manager.dto.VectorCapabilitiesInfo;
import org.geomajas.plugin.deskmanager.command.manager.dto.VectorLayerConfiguration;

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
		GetVectorCapabilitiesRequest request = new GetVectorCapabilitiesRequest();
		request.setConnectionProperties(connectionProps);
		GwtCommand command = new GwtCommand(GetVectorCapabilitiesRequest.COMMAND);
		command.setCommandRequest(request);

		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new CommandCallback<GetVectorCapabilitiesResponse>() {

					public void execute(GetVectorCapabilitiesResponse response) {
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
			final DataCallback<VectorLayerConfiguration> onFinish, final DataCallback<String> onError) {
		GetVectorLayerConfigurationRequest request = new GetVectorLayerConfigurationRequest();
		request.setConnectionProperties(connectionProps);
		request.setLayerName(layerTypeName);
		GwtCommand command = new GwtCommand(GetVectorLayerConfigurationRequest.COMMAND);
		command.setCommandRequest(request);

		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new CommandCallback<GetVectorLayerConfigurationResponse>() {

					public void execute(GetVectorLayerConfigurationResponse response) {
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
			final DataCallback<RasterLayerConfiguration> onFinish, final DataCallback<String> onError) {
		GetRasterLayerConfigurationRequest request = new GetRasterLayerConfigurationRequest();
		request.setRasterCapabilitiesInfo(info);
		request.setConnectionProperties(connectionProps);
		GwtCommand command = new GwtCommand(GetRasterLayerConfigurationRequest.COMMAND);
		command.setCommandRequest(request);

		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new CommandCallback<GetRasterLayerConfigurationResponse>() {

					public void execute(GetRasterLayerConfigurationResponse response) {
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
