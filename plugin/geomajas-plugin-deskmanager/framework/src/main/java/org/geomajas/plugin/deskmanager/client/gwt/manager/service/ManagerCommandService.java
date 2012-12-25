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
import org.geomajas.plugin.deskmanager.command.manager.dto.GetGeodeskRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetGeodeskResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetGeodesksRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetGeodesksResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetLayerModelRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetLayerModelsRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetLayerModelsResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetLayersRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetLayersResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetTerritoriesRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetTerritoriesResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.LayerModelResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveBlueprintRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveGeodeskRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveLayerModelRequest;
import org.geomajas.plugin.deskmanager.domain.dto.BlueprintDto;
import org.geomajas.plugin.deskmanager.domain.dto.DynamicLayerConfiguration;
import org.geomajas.plugin.deskmanager.domain.dto.GeodeskDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerModelDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;

/**
 * Convenience class with helper methods for commands.
 * 
 * @author Kristof Heirwegh
 * @author Oliver May
 */
public final class ManagerCommandService {

	/**
	 * Utility class
	 */
	private ManagerCommandService() {
	}

	// -- Layers ----------------------------------------------------
	/**
	 * {@see org.geomajas.plugin.deskmanager.command.manager.GetLayersCommand}.
	 * 
	 * @param onFinish
	 *            callback called when system layers are retrieved.
	 */
	public static void getLayers(final DataCallback<GetLayersResponse> onFinish) {
		GetLayersRequest request = new GetLayersRequest();
		GwtCommand command = new GwtCommand(GetLayersRequest.COMMAND);
		command.setCommandRequest(request);
		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<GetLayersResponse>() {

					public void execute(GetLayersResponse response) {
						if (onFinish != null) {
							onFinish.execute(response);
						}
					}
				});
	}

	// -- Blueprint --------------------------------------------------------

	/**
	 * {@see org.geomajas.plugin.deskmanager.command.manager.GetBlueprintCommand}.
	 * 
	 * @param uuid
	 *            the uuid of the blueprint to fetch.
	 * @param onFinish
	 *            callback called when the blueprint is fetched.
	 */
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
	}

	/**
	 * {@see org.geomajas.plugin.deskmanager.command.manager.GetBlueprintsCommand}.
	 * 
	 * @param onFinish
	 *            callback called when blueprints are fetched.
	 */
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
	}

	/**
	 * {@see org.geomajas.plugin.deskmanager.command.manager.DeleteBlueprintCommand}.
	 * 
	 * @param blueprint
	 *            the blueprint to delete.
	 */
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
	}

	/**
	 * {@see org.geomajas.plugin.deskmanager.command.manager.CreateBlueprintCommand}.
	 * 
	 * @param userApplicationName
	 *            the user application to base the blueprint on.
	 * @param name
	 *            the preferred name for the blueprint.
	 */
	public static void createNewBlueprint(String userApplicationName, String name) {
		CreateBlueprintRequest request = new CreateBlueprintRequest();
		request.setName(name);
		request.setUserApplicationName(userApplicationName);
		GwtCommand command = new GwtCommand(CreateBlueprintRequest.COMMAND);
		command.setCommandRequest(request);
		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<BlueprintResponse>() {

					public void execute(BlueprintResponse response) {
						Whiteboard.fireEvent(new BlueprintEvent(response.getBlueprint(), false, true));
					}
				});
	}

	/**
	 * {@see org.geomajas.plugin.deskmanager.command.manager.SaveBlueprintCommand}.
	 * 
	 * @param blueprint
	 *            the blueprint to save.
	 * @param bitmask
	 *            which attributes to save.
	 */
	public static void saveBlueprint(BlueprintDto blueprint, int bitmask) {
		SaveBlueprintRequest request = new SaveBlueprintRequest();
		request.setBlueprint(blueprint);
		request.setSaveBitmask(bitmask);
		GwtCommand command = new GwtCommand(SaveBlueprintRequest.COMMAND);
		command.setCommandRequest(request);
		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<BlueprintResponse>() {

					public void execute(BlueprintResponse response) {
						Whiteboard.fireEvent(new BlueprintEvent(response.getBlueprint()));
					}
				});
	}

	// -- LayerModel --------------------------------------------------------

	/**
	 * {@see org.geomajas.plugin.deskmanager.command.manager.GetLayerModelCommand}.
	 * 
	 * @param id
	 *            the id of the layer model to fetch
	 * @param onFinish
	 *            callback called when layermodel is retrieved.
	 */
	public static void getLayerModel(String id, final DataCallback<LayerModelDto> onFinish) {
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
	}

	/**
	 * {@see org.geomajas.plugin.deskmanager.command.manager.GetLayerModelsCommand}.
	 * 
	 * @param onFinish
	 *            callback called when layermodels are retrieved.
	 */
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
	}

	/**
	 * {@see org.geomajas.plugin.deskmanager.command.manager.DeleteLayerModelCommand}.
	 * 
	 * @param layerModel
	 *            the layerModel to delete.
	 */
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
	}

	/**
	 * {@see org.geomajas.plugin.deskmanager.command.manager.CheckLayerModelInUseCommand}.
	 * 
	 * @param layerModel
	 *            the layerModel to check.
	 * @param onFinish
	 *            callback called when answer is recieved.
	 */
	public static void checkLayerModelInUse(final LayerModelDto layerModel, final DataCallback<Boolean> onFinish) {
		CheckLayerModelInUseRequest request = new CheckLayerModelInUseRequest();
		request.setLayerModelId(layerModel.getId());
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
	}

	/**
	 * {@see org.geomajas.plugin.deskmanager.command.manager.CreateLayerModelCommand}.
	 * 
	 * @param configuration
	 *            the layerconfiguration to base the layermodel on.
	 */
	public static void createNewLayerModel(DynamicLayerConfiguration configuration) {
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
	}

	/**
	 * {@see org.geomajas.plugin.deskmanager.command.manager.SaveLayerModelCommand}.
	 * 
	 * @param layerModel
	 *            the layerModel to save.
	 */
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
	}

	/**
	 * {@see org.geomajas.plugin.deskmanager.command.manager.GetTerritoriesCommand}.
	 * 
	 * @param onFinish
	 *            callback called when the groups are retrieved.
	 */
	public static void getGroups(final DataCallback<List<TerritoryDto>> onFinish) {
		GetTerritoriesRequest request = new GetTerritoriesRequest();
		GwtCommand command = new GwtCommand(GetTerritoriesRequest.COMMAND);
		command.setCommandRequest(request);
		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<GetTerritoriesResponse>() {

					public void execute(GetTerritoriesResponse response) {
						if (onFinish != null) {
							onFinish.execute(response.getTerritories());
						}
					}
				});
	}

	/**
	 * {@see org.geomajas.plugin.deskmanager.command.manager.getGeodesksCommand}.
	 * 
	 * @param onFinich
	 *            callback called when the geodesk is retrieved.
	 */
	public static void getGeodesk(String uuid, final DataCallback<GeodeskDto> onFinish) {
		GetGeodeskRequest request = new GetGeodeskRequest();
		request.setUuid(uuid);
		GwtCommand command = new GwtCommand(GetGeodeskRequest.COMMAND);
		command.setCommandRequest(request);
		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<GetGeodeskResponse>() {

					public void execute(GetGeodeskResponse response) {
						if (onFinish != null) {
							onFinish.execute(response.getGeodesk());
						}
					}
				});
	}

	/**
	 * {@see org.geomajas.plugin.deskmanager.command.manager.GetGeodesksCommand}.
	 * 
	 * @param onFinish
	 *            callback called when geodesks are retrieved.
	 */
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
	}

	/**
	 * {@see org.geomajas.plugin.deskmanager.command.manager.DeleteGeodeskCommand}.
	 * 
	 * @param geodesk
	 *            the geodesk to delete.
	 */
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
	}

	/**
	 * {@see org.geomajas.plugin.deskmanager.command.manager.CreateGeodeskCommand}.
	 * 
	 * @param blueprintId
	 *            the id of the dependant blueprint.
	 * @param geodeskName
	 *            the preferred name of the new geodesk.
	 */
	public static void createNewGeodesk(String blueprintId, String geodeskName) {
		CreateGeodeskRequest request = new CreateGeodeskRequest();
		request.setBlueprintId(blueprintId);
		request.setName(geodeskName);
		GwtCommand command = new GwtCommand(CreateGeodeskRequest.COMMAND);
		command.setCommandRequest(request);
		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<GetGeodeskResponse>() {

					public void execute(GetGeodeskResponse response) {
						Whiteboard.fireEvent(new GeodeskEvent(response.getGeodesk(), false, true));
					}
				});
	}

	/**
	 * {@see org.geomajas.plugin.deskmanager.command.manager.SaveGeodeskCommand}.
	 * 
	 * @param geodesk
	 *            the geodesk to save.
	 * @param bitmask
	 *            which attributes to save.
	 */
	public static void saveGeodesk(GeodeskDto geodesk, int bitmask) {
		SaveGeodeskRequest request = new SaveGeodeskRequest();
		request.setGeodesk(geodesk);
		request.setSaveBitmask(bitmask);
		GwtCommand command = new GwtCommand(SaveGeodeskRequest.COMMAND);
		command.setCommandRequest(request);
		Deferred def = GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<GetGeodeskResponse>() {

					public void execute(GetGeodeskResponse response) {
						Whiteboard.fireEvent(new GeodeskEvent(response.getGeodesk()));
					}
				});
	}

	/**
	 * {@see org.geomajas.plugin.deskmanager.command.manager.CheckGeodeskIdExistsCommand}.
	 * 
	 * @param geodeskId
	 *            the id to check.
	 * @param onFinish
	 *            DataCallback will be called when the command is executed.
	 */
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
	}
}
