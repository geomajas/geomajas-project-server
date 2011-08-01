/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.searchandfilter.client.widget.multifeaturelistgrid;

import org.geomajas.command.CommandRequest;
import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.command.dto.SearchFeatureRequest;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.Deferred;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.widget.featureinfo.client.widget.DockableWindow;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.client.util.Callback;
import org.geomajas.widget.searchandfilter.client.util.SearchCommService;
import org.geomajas.widget.searchandfilter.command.dto.ExportToCsvRequest;
import org.geomajas.widget.searchandfilter.command.dto.ExportToCsvResponse;
import org.geomajas.widget.searchandfilter.command.dto.FeatureSearchRequest;
import org.geomajas.widget.searchandfilter.search.dto.Criterion;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.UrlBuilder;
import com.smartgwt.client.core.Function;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.Window;

/**
 * Export the results of a search to CSV, supported searches are
 * SearchFeatureRequest, SearchByLocationRequest and Criterion.
 * 
 * @author Kristof Heirwegh
 */
public class ExportSearchToCsvHandler implements ExportToCsvHandler {

	protected VectorLayer layer;
	protected MapModel model;
	protected CommandRequest request;

	private SearchAndFilterMessages messages = GWT.create(SearchAndFilterMessages.class);

	/**
	 * @param model
	 * @param layer
	 * @param searchRequest
	 *            the search to use to retrieve features.
	 */
	public ExportSearchToCsvHandler(MapModel model, VectorLayer layer, CommandRequest searchRequest) {
		this(model, layer);
		setRequest(searchRequest);
	}

	public ExportSearchToCsvHandler(MapModel model, VectorLayer layer, Criterion criterion) {
		this(model, layer);
		FeatureSearchRequest req = new FeatureSearchRequest();
		req.setCriterion(criterion);
		req.setMapCrs(model.getCrs());
		req.setLayerFilters(SearchCommService.getLayerFiltersForCriterion(criterion, model));
		this.request = req;
	}

	public ExportSearchToCsvHandler(MapModel model, VectorLayer layer) {
		if (model == null || layer == null) {
			throw new IllegalArgumentException("All parameters are required.");
		}
		this.layer = layer;
		this.model = model;
	}

	public void execute(VectorLayer vlayer) {
		execute(vlayer, null);
	}

	public void execute(final VectorLayer vlayer, final Callback onFinished) {
		if (this.layer.equals(vlayer)) {
			ExportToCsvRequest exportRequest = new ExportToCsvRequest();
			exportRequest.setSearchFeatureRequest(getSearchFeatureRequest());
			exportRequest.setSearchByLocationRequest(getSearchByLocationRequest(vlayer));
			exportRequest.setSearchByCriterionRequest(getSearchCriterionRequest());
			exportRequest.setEncoding(messages.exportToCsvEncoding());
			exportRequest.setLocale(messages.exportToCsvLocale());
			exportRequest.setSeparatorChar(messages.exportToCsvSeparatorChar());
			exportRequest.setQuoteChar(messages.exportToCsvQuoteChar());
			exportRequest.setLayerId(layer.getServerLayerId());

			GwtCommand command = new GwtCommand(ExportToCsvRequest.COMMAND);
			command.setCommandRequest(exportRequest);
			Deferred deferred = GwtCommandDispatcher.getInstance().execute(command, new CommandCallback() {
				private static final String CONTENT_PRE = "<div style='margin-top: 20px; width: 200px; text-align: "
						+ "center'><b>";
				private static final String CONTENT_POST = "</b><br />";
				private static final String LINK_POST = "</div>";

				public void execute(CommandResponse response) {
					if (response instanceof ExportToCsvResponse) {
						ExportToCsvResponse resp = (ExportToCsvResponse) response;
						if (resp.getDocumentId() != null) {
							UrlBuilder ub = new UrlBuilder();
							ub.setPath("d/csvDownload");
							ub.setParameter("id", resp.getDocumentId());
							String link = ub.buildString().replaceFirst("http:///", GWT.getHostPageBaseURL());

							Window window = new DockableWindow();
							window.setTitle(messages.exportToCsvWindowTitle());
							String content = CONTENT_PRE + messages.exportToCsvContentReady() + CONTENT_POST;
							String linktocsv = messages.exportToCsvDownloadLink(link) + LINK_POST;
							window.addItem(new HTMLFlow(content + linktocsv));
							window.centerInPage();
							window.setAutoSize(true);
							window.show();
							window.setKeepInParentRect(true);
						}
						if (onFinished != null) {
							onFinished.execute();
						}
					}
				}
			});
			deferred.addErrorCallback(new Function() {
				public void execute() {
					if (onFinished != null) {
						onFinished.execute();
					}
				}
			});
		}
	}

	// ----------------------------------------------------------

	protected SearchByLocationRequest getSearchByLocationRequest(VectorLayer layer) {
		if (request != null && request instanceof SearchByLocationRequest) {
			SearchByLocationRequest req = (SearchByLocationRequest) request;
			SearchByLocationRequest clone = new SearchByLocationRequest();
			clone.setBuffer(req.getBuffer());
			clone.setCrs(req.getCrs());
			clone.setFilter(req.getFilter());
			clone.setFeatureIncludes(req.getFeatureIncludes());
			clone.setLocation(req.getLocation());
			clone.setQueryType(req.getQueryType());
			clone.setRatio(req.getRatio());
			clone.setSearchType(req.getSearchType());
			// not bothering to include the other layers, we won't use the
			// result anyway
			clone.setLayerIds(new String[] { layer.getServerLayerId() });
			return clone;
		} else {
			return null;
		}
	}

	protected FeatureSearchRequest getSearchCriterionRequest() {
		if (request != null && request instanceof FeatureSearchRequest) {
			return (FeatureSearchRequest) request;
		} else {
			return null;
		}
	}

	protected SearchFeatureRequest getSearchFeatureRequest() {
		if (request != null && request instanceof SearchFeatureRequest) {
			return (SearchFeatureRequest) request;
		} else {
			return null;
		}
	}

	protected CommandRequest getRequest() {
		return request;
	}

	protected void setRequest(CommandRequest request) {
		if (request == null) {
			this.request = null;
		} else if (request instanceof SearchFeatureRequest || request instanceof SearchByLocationRequest) {
			this.request = request;
		} else {
			throw new IllegalArgumentException(
					"Please provide a request (SearchFeatureRequest or SearchByLocationRequest)");
		}
	}
}
