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
package org.geomajas.plugin.deskmanager.client.gwt.manager.service;

import java.util.List;

import org.geomajas.command.CommandResponse;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.Deferred;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.widget.infowindow.NotificationWindow;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.BlueprintEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.GeodeskEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.LayerModelEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.Whiteboard;
import org.geomajas.plugin.deskmanager.command.manager.dto.BlueprintResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.CheckGeodeskIdExistsRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.CheckGeodeskIdExistsResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.CheckLayerModelInUseRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.CheckLayerModelInUseResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.CreateBlueprintRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.CreateGeodeskRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.CreateLayerModelRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.DeleteBlueprintRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.DeleteGeodeskRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.DeleteLayerModelRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetBlueprintRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetBlueprintsRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetBlueprintsResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetGeodeskUrlBaseRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetGeodeskUrlBaseResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetGeodesksRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetGeodesksResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetGroupsRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetGroupsResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetLayerModelRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetLayerModelsRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetLayerModelsResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetSystemLayerTreeNodeRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetSystemLayerTreeNodeResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.LayerConfiguration;
import org.geomajas.plugin.deskmanager.command.manager.dto.LayerModelResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.ReadApplicationRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.ReadApplicationResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveBlueprintRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveGeodeskRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveLayerModelRequest;
import org.geomajas.plugin.deskmanager.domain.dto.BlueprintDto;
import org.geomajas.plugin.deskmanager.domain.dto.GeodeskDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerModelDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;

/**
 * Convenience class with helper methods for commands.
 * 
 * @author Kristof Heirwegh
 */
public final class CommService {

	/**
	 * Utility class
	 */
	private CommService() {}

	public static void getGeodeskUrlBase(final DataCallback<GetGeodeskUrlBaseResponse> onFinish) {
		GetGeodeskUrlBaseRequest request = new GetGeodeskUrlBaseRequest();
		GwtCommand command = new GwtCommand(GetGeodeskUrlBaseRequest.COMMAND);
		command.setCommandRequest(request);
		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<GetGeodeskUrlBaseResponse>() {

					public void execute(GetGeodeskUrlBaseResponse response) {
						if (onFinish != null) {
							onFinish.execute(response);
						}
					}
				});
		def.addCallback(NotificationWindow.getInstance());
	}

	// -- LayerTreeNode ----------------------------------------------------

	public static void getSystemLayerTreeNode(final DataCallback<GetSystemLayerTreeNodeResponse> onFinish) {
		GetSystemLayerTreeNodeRequest request = new GetSystemLayerTreeNodeRequest();
		GwtCommand command = new GwtCommand(GetSystemLayerTreeNodeRequest.COMMAND);
		command.setCommandRequest(request);
		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<GetSystemLayerTreeNodeResponse>() {

					public void execute(GetSystemLayerTreeNodeResponse response) {
						if (onFinish != null) {
							onFinish.execute(response);
						}
					}
				});
		def.addCallback(NotificationWindow.getInstance());
	}

	// -- Blueprint --------------------------------------------------------

	public static void getBlueprint(String uuid, final DataCallback<BlueprintDto> onFinish) {
		GetBlueprintRequest request = new GetBlueprintRequest();
		request.setUuid(uuid);
		GwtCommand command = new GwtCommand(GetBlueprintRequest.COMMAND);
		command.setCommandRequest(request);
		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<BlueprintResponse>() {

					public void execute(BlueprintResponse response) {
						if (onFinish != null) {
							onFinish.execute(response.getBlueprint());
						}
					}
				});
		def.addCallback(NotificationWindow.getInstance());
	}

	public static void getBlueprints(final DataCallback<List<BlueprintDto>> onFinish) {
		GetBlueprintsRequest request = new GetBlueprintsRequest();
		GwtCommand command = new GwtCommand(GetBlueprintsRequest.COMMAND);
		command.setCommandRequest(request);
		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<GetBlueprintsResponse>() {

					public void execute(GetBlueprintsResponse response) {
						if (onFinish != null) {
							onFinish.execute(response.getBlueprints());
						}
					}
				});
		def.addCallback(NotificationWindow.getInstance());
	}

	public static void deleteBlueprint(final BlueprintDto blueprint) {
		DeleteBlueprintRequest request = new DeleteBlueprintRequest();
		request.setUuid(blueprint.getId());
		GwtCommand command = new GwtCommand(DeleteBlueprintRequest.COMMAND);
		command.setCommandRequest(request);
		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<CommandResponse>() {

					public void execute(CommandResponse response) {
						Whiteboard.fireEvent(new BlueprintEvent(blueprint, true, false));
					}
				});
		def.addCallback(NotificationWindow.getInstance());
	}

	public static void createNewBlueprint(String userApplicationName) {
		CreateBlueprintRequest request = new CreateBlueprintRequest();
		request.setUserApplicationName(userApplicationName);
		GwtCommand command = new GwtCommand(CreateBlueprintRequest.COMMAND);
		command.setCommandRequest(request);
		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<BlueprintResponse>() {

					public void execute(BlueprintResponse response) {
						Whiteboard.fireEvent(new BlueprintEvent(response.getBlueprint(), false, true));
					}
				});
		def.addCallback(NotificationWindow.getInstance());
	}

	public static void saveBlueprint(BlueprintDto blueprint, int saveWhat) {
		SaveBlueprintRequest request = new SaveBlueprintRequest();
		request.setBlueprint(blueprint);
		request.setSaveWhat(saveWhat);
		GwtCommand command = new GwtCommand(SaveBlueprintRequest.COMMAND);
		command.setCommandRequest(request);
		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<BlueprintResponse>() {

					public void execute(BlueprintResponse response) {
						Whiteboard.fireEvent(new BlueprintEvent(response.getBlueprint()));
					}
				});
		def.addCallback(NotificationWindow.getInstance());
	}

	// -- LayerModel --------------------------------------------------------

	public static void getLayerModel(Long id, final DataCallback<LayerModelDto> onFinish) {
		GetLayerModelRequest request = new GetLayerModelRequest();
		request.setId(id);
		GwtCommand command = new GwtCommand(GetLayerModelRequest.COMMAND);
		command.setCommandRequest(request);
		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<LayerModelResponse>() {

					public void execute(LayerModelResponse response) {
						if (onFinish != null) {
							onFinish.execute(response.getLayerModel());
						}
					}
				});
		def.addCallback(NotificationWindow.getInstance());
	}

	public static void getLayerModels(final DataCallback<List<LayerModelDto>> onFinish) {
		GetLayerModelsRequest request = new GetLayerModelsRequest();
		GwtCommand command = new GwtCommand(GetLayerModelsRequest.COMMAND);
		command.setCommandRequest(request);
		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<GetLayerModelsResponse>() {

					public void execute(GetLayerModelsResponse response) {
						if (onFinish != null) {
							onFinish.execute(response.getLayerModels());
						}
					}
				});
		def.addCallback(NotificationWindow.getInstance());
	}

	public static void deleteLayerModel(final LayerModelDto layerModel) {
		DeleteLayerModelRequest request = new DeleteLayerModelRequest();
		request.setId(layerModel.getId());
		GwtCommand command = new GwtCommand(DeleteLayerModelRequest.COMMAND);
		command.setCommandRequest(request);
		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<CommandResponse>() {

					public void execute(CommandResponse response) {
						Whiteboard.fireEvent(new LayerModelEvent(layerModel, true, false));
					}
				});
		def.addCallback(NotificationWindow.getInstance());
	}

	public static void checkLayerModelInUse(final LayerModelDto layerModel, final DataCallback<Boolean> onFinish) {
		CheckLayerModelInUseRequest request = new CheckLayerModelInUseRequest();
		request.setClientLayerId(layerModel.getClientLayerId());
		GwtCommand command = new GwtCommand(CheckLayerModelInUseRequest.COMMAND);
		command.setCommandRequest(request);
		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<CheckLayerModelInUseResponse>() {

					public void execute(CheckLayerModelInUseResponse response) {
						if (onFinish != null) {
							onFinish.execute(response.isLayerModelInUse());
						}
					}
				});
		def.addCallback(NotificationWindow.getInstance());
	}

	public static void createNewLayerModel(LayerConfiguration configuration) {
		CreateLayerModelRequest request = new CreateLayerModelRequest();
		request.setConfiguration(configuration);
		GwtCommand command = new GwtCommand(CreateLayerModelRequest.COMMAND);
		command.setCommandRequest(request);
		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<LayerModelResponse>() {

					public void execute(LayerModelResponse response) {
						Whiteboard.fireEvent(new LayerModelEvent(response.getLayerModel(), false, true));
					}
				});
		def.addCallback(NotificationWindow.getInstance());
	}

	public static void saveLayerModel(LayerModelDto layerModel) {
		SaveLayerModelRequest request = new SaveLayerModelRequest();
		request.setLayerModel(layerModel);
		GwtCommand command = new GwtCommand(SaveLayerModelRequest.COMMAND);
		command.setCommandRequest(request);
		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<LayerModelResponse>() {

					public void execute(LayerModelResponse response) {
						Whiteboard.fireEvent(new LayerModelEvent(response.getLayerModel()));
					}
				});
		def.addCallback(NotificationWindow.getInstance());
	}

	// -- Territory --------------------------------------------------------

	public static void getGroups(final DataCallback<List<TerritoryDto>> onFinish) {
		GetGroupsRequest request = new GetGroupsRequest();
		GwtCommand command = new GwtCommand(GetGroupsRequest.COMMAND);
		command.setCommandRequest(request);
		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<GetGroupsResponse>() {

					public void execute(GetGroupsResponse response) {
						if (onFinish != null) {
							onFinish.execute(response.getGroups());
						}
					}
				});
		def.addCallback(NotificationWindow.getInstance());
	}

	// -- Geodesk --------------------------------------------------------

	public static void getGeodesk(String uuid, final DataCallback<GeodeskDto> onFinish) {
		ReadApplicationRequest request = new ReadApplicationRequest();
		request.setUuid(uuid);
		GwtCommand command = new GwtCommand(ReadApplicationRequest.COMMAND);
		command.setCommandRequest(request);
		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<ReadApplicationResponse>() {

					public void execute(ReadApplicationResponse response) {
						if (onFinish != null) {
							onFinish.execute(response.getGeodesk());
						}
					}
				});
		def.addCallback(NotificationWindow.getInstance());
	}

	public static void getGeodesks(final DataCallback<List<GeodeskDto>> onFinish) {
		GetGeodesksRequest request = new GetGeodesksRequest();
		GwtCommand command = new GwtCommand(GetGeodesksRequest.COMMAND);
		command.setCommandRequest(request);
		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<GetGeodesksResponse>() {

					public void execute(GetGeodesksResponse response) {
						if (onFinish != null) {
							onFinish.execute(response.getGeodesks());
						}
					}
				});
		def.addCallback(NotificationWindow.getInstance());
	}

	public static void deleteGeodesk(final GeodeskDto geodesk) {
		DeleteGeodeskRequest request = new DeleteGeodeskRequest();
		request.setUuid(geodesk.getId());
		GwtCommand command = new GwtCommand(DeleteGeodeskRequest.COMMAND);
		command.setCommandRequest(request);
		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<CommandResponse>() {

					public void execute(CommandResponse response) {
						Whiteboard.fireEvent(new GeodeskEvent(geodesk, true, false));
					}
				});
		def.addCallback(NotificationWindow.getInstance());
	}

	public static void createNewGeodesk(String blueprintId) {
		CreateGeodeskRequest request = new CreateGeodeskRequest();
		request.setBlueprintId(blueprintId);
		GwtCommand command = new GwtCommand(CreateGeodeskRequest.COMMAND);
		command.setCommandRequest(request);
		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<ReadApplicationResponse>() {

					public void execute(ReadApplicationResponse response) {
						Whiteboard.fireEvent(new GeodeskEvent(response.getGeodesk(), false, true));
					}
				});
		def.addCallback(NotificationWindow.getInstance());
	}

	public static void saveGeodesk(GeodeskDto geodesk, int saveWhat) {
		SaveGeodeskRequest request = new SaveGeodeskRequest();
		request.setGeodesk(geodesk);
		request.setSaveWhat(saveWhat);
		GwtCommand command = new GwtCommand(SaveGeodeskRequest.COMMAND);
		command.setCommandRequest(request);
		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<ReadApplicationResponse>() {

					public void execute(ReadApplicationResponse response) {
						Whiteboard.fireEvent(new GeodeskEvent(response.getGeodesk()));
					}
				});
		def.addCallback(NotificationWindow.getInstance());
	}

	public static void checkGeodeskIdExists(String geodeskId, final DataCallback<Boolean> onFinish) {
		CheckGeodeskIdExistsRequest request = new CheckGeodeskIdExistsRequest();
		request.setGeodeskId(geodeskId);
		GwtCommand command = new GwtCommand(CheckGeodeskIdExistsRequest.COMMAND);
		command.setCommandRequest(request);
		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<CheckGeodeskIdExistsResponse>() {

					public void execute(CheckGeodeskIdExistsResponse response) {
						if (onFinish != null) {
							onFinish.execute(response.getExists());
						}
					}
				});
		def.addCallback(NotificationWindow.getInstance());
	}
}
