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
package org.geomajas.plugin.jmeter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.geomajas.command.CommandRequest;
import org.geomajas.command.dto.GetRasterTilesRequest;
import org.geomajas.command.dto.GetRasterTilesResponse;
import org.geomajas.command.dto.GetVectorTileRequest;
import org.geomajas.command.dto.GetVectorTileResponse;

/**
 * Geomajas sampler that randomly fetches tiles.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GeomajasSamplerClient extends AbstractJavaSamplerClient implements Serializable {

	private static final Logger LOG = LoggingManager.getLoggerForClass();

	private static final long serialVersionUID = 1L;

	private GeomajasSamplerSetup setUp;

	// set up default arguments for the JMeter GUI
	@Override
	public Arguments getDefaultParameters() {
		Arguments defaultParameters = new Arguments();
		defaultParameters.addArgument("baseUrl", "http://localhost:8888/d/performance");
		defaultParameters.addArgument("appId", "sampleApp");
		defaultParameters.addArgument("mapId", "sampleMap");
		defaultParameters.addArgument("layerIds", "");
		defaultParameters.addArgument("runMode", "random");
		defaultParameters.addArgument("tileCount", "100");
		defaultParameters.addArgument("saveTiles", "false");
		defaultParameters.addArgument("username", null);
		defaultParameters.addArgument("password", null);
		defaultParameters.addArgument("userToken", null);
		defaultParameters.addArgument("tileFolder", "/tmp/jmeter/tiles");
		return defaultParameters;
	}

	@Override
	public void setupTest(JavaSamplerContext context) {
		LOG.info("Set up GeomajasSamplerClient");
		setUp = GeomajasSamplerSetup.getInstance(context);
	}

	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		LOG.info("Starting GeomajasSamplerClient in thread " + Thread.currentThread().getName());
		SampleResult result = new SampleResult();
		result.sampleStart(); // start stopwatch
		if (setUp.getErrorLabel() != null) {
			result.setSampleLabel(setUp.getErrorLabel());
			result.setSuccessful(false);
			result.sampleEnd();
			return result;
		}
		int successCount = 0;
		int failurecount = 0;
		Map<String, Integer> layerCount = new HashMap<String, Integer>();
		CommandRequest request;
		while ((request = setUp.getNextRequest()) != null) {
			if (request instanceof GetVectorTileRequest) {
				GetVectorTileRequest vectorRequest = (GetVectorTileRequest) request;
				LOG.info("Executing vector request for layer " + vectorRequest.getLayerId());
				GetVectorTileResponse response = (GetVectorTileResponse) setUp.execute(GetVectorTileRequest.COMMAND,
						vectorRequest);
				try {
					if (setUp.consumeTile(vectorRequest.getLayerId(), response)) {
						LOG.info("Succesfully consumed vector tile ");
						String layerId = vectorRequest.getLayerId();
						if (!layerCount.containsKey(layerId)) {
							layerCount.put(layerId, 0);
						}
						layerCount.put(layerId, layerCount.get(layerId) + 1);
					} else {
						LOG.info("Vector tile not consumed ");
					}
					successCount++;
				} catch (Exception e) {
					failurecount++;
				}
			} else if (request instanceof GetRasterTilesRequest) {
				GetRasterTilesRequest rasterRequest = (GetRasterTilesRequest) request;
				LOG.info("Executing raster request for layer " + rasterRequest.getLayerId());
				GetRasterTilesResponse response = (GetRasterTilesResponse) setUp.execute(GetRasterTilesRequest.COMMAND,
						rasterRequest);
				try {
					if (setUp.consumeTile(rasterRequest.getLayerId(), response)) {
						LOG.info("Succesfully consumed raster tile ");
						String layerId = rasterRequest.getLayerId();
						if (!layerCount.containsKey(layerId)) {
							layerCount.put(layerId, 0);
						}
						layerCount.put(layerId, layerCount.get(layerId) + 1);
					} else {
						LOG.info("Raster tile not consumed ");
					}
					successCount++;
				} catch (Exception e) {
					failurecount++;
				}
			}
		}
		if (successCount > 0) {
			result.sampleEnd();
			result.setSuccessful(true);
			LOG.info("Found results for " + layerCount.size() + " layers");
			result.setSampleLabel(successCount + "/" + (failurecount + successCount) + " tiles successfull "
					+ getLayerStatistics(layerCount));
		} else {
			result.setSampleLabel("No tile results");
			result.sampleEnd();
			result.setSuccessful(false);
		}
		return result;
	}

	@Override
	public void teardownTest(JavaSamplerContext context) {
		GeomajasSamplerSetup.tearDown();
	}

	protected String getLayerStatistics(Map<String, Integer> layerCount) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (String layerId : layerCount.keySet()) {
			if (sb.length() > 1) {
				sb.append(",");
			}
			sb.append(layerId);
			sb.append("(").append(layerCount.get(layerId)).append(")");
		}
		sb.append("]");
		return sb.toString();
	}

}