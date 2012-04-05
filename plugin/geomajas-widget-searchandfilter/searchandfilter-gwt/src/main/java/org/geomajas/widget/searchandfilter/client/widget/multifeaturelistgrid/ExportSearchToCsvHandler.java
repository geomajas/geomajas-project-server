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

package org.geomajas.widget.searchandfilter.client.widget.multifeaturelistgrid;

import org.geomajas.command.CommandRequest;
import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.command.dto.SearchFeatureRequest;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
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
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Export the results of a search to CSV, supported searches are
 * SearchFeatureRequest, SearchByLocationRequest and Criterion.
 * 
 * @author Kristof Heirwegh
 * @author Oliver May
 */
public class ExportSearchToCsvHandler implements ExportToCsvHandler {

	protected VectorLayer layer;
	protected MapModel model;
	//TODO: change to FeatureSearchRequest once deprecated methods are removed.
	protected CommandRequest request;

	private SearchAndFilterMessages messages = GWT.create(SearchAndFilterMessages.class);

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
	
	public void setCriterion(Criterion criterion) {
		if (request instanceof FeatureSearchRequest) {
			((FeatureSearchRequest) request).setCriterion(criterion);
		}
	}

	public void execute(VectorLayer vlayer) {
		execute(vlayer, null);
	}

	public void execute(final VectorLayer vectorLayer, final Callback onFinished) {
		if (this.layer.equals(vectorLayer)) {
			ExportToCsvRequest exportRequest = new ExportToCsvRequest();
			exportRequest.setSearchFeatureRequest(getSearchFeatureRequest());
			exportRequest.setSearchByLocationRequest(getSearchByLocationRequest(vectorLayer));
			exportRequest.setSearchByCriterionRequest(getSearchCriterionRequest());
			exportRequest.setEncoding(messages.exportToCsvEncoding());
			exportRequest.setLocale(messages.exportToCsvLocale());
			exportRequest.setSeparatorChar(messages.exportToCsvSeparatorChar());
			exportRequest.setQuoteChar(messages.exportToCsvQuoteChar());
			exportRequest.setLayerId(layer.getServerLayerId());

			GwtCommand command = new GwtCommand(ExportToCsvRequest.COMMAND);
			command.setCommandRequest(exportRequest);
			GwtCommandDispatcher.getInstance().execute(command,
					new AbstractCommandCallback<ExportToCsvResponse>() {

				public void execute(ExportToCsvResponse response) {
					if (response.getDocumentId() != null) {
						UrlBuilder ub = new UrlBuilder();
						ub.setPath("d/csvDownload");
						ub.setParameter("id", response.getDocumentId());
						String link = ub.buildString().replaceFirst("http:///", GWT.getHostPageBaseURL());

						final Window window = new DockableWindow();
						window.setTitle(messages.exportToCsvWindowTitle());
						window.setWidth(200);

						String content = "<div style='margin-top: 20px; width: 200px; text-align: center'>";
						content += "<b>" + messages.exportToCsvContentReady() + "</b><br />";
						content += messages.exportToCsvDownloadLinkDescription() + "</b></div>";
						HTMLFlow infoSpinnerCanvas = new HTMLFlow(content);
						infoSpinnerCanvas.setAutoHeight();
						Anchor anchor = new Anchor(messages.exportToCsvDownloadLinkName(), link);
						anchor.setHeight("20px");
						anchor.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
						anchor.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
							public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
								window.hide();
								window.destroy();
							}
						});
						VerticalPanel panel = new VerticalPanel();
						panel.setHeight("22px");
						panel.setWidth("198px");
						panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
						panel.add(anchor);

						VLayout lay = new VLayout(0);
						lay.setWidth100();
						lay.setLayoutAlign(Alignment.CENTER);
						lay.addMember(infoSpinnerCanvas);
						lay.addMember(panel);
						lay.setAutoHeight();
						window.addItem(lay);
						window.centerInPage();
						window.setAutoSize(true);
						window.show();
						window.setKeepInParentRect(true);
					}
					if (onFinished != null) {
						onFinished.execute();
					}
				}

				@Override
				public void onCommunicationException(Throwable error) {
					if (onFinished != null) {
						onFinished.execute();
					}
					super.onCommunicationException(error);
				}

				@Override
				public void onCommandException(CommandResponse response) {
					if (onFinished != null) {
						onFinished.execute();
					}
					super.onCommandException(response);
				}
			});
		}
	}

	// ----------------------------------------------------------

	protected SearchByLocationRequest getSearchByLocationRequest(VectorLayer layer) {
		if (request instanceof SearchByLocationRequest) {
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
		if (request instanceof FeatureSearchRequest) {
			return (FeatureSearchRequest) request;
		} else {
			return null;
		}
	}

	protected SearchFeatureRequest getSearchFeatureRequest() {
		if (request instanceof SearchFeatureRequest) {
			return (SearchFeatureRequest) request;
		} else {
			return null;
		}
	}

	protected CommandRequest getRequest() {
		return request;
	}

	protected void setRequest(CommandRequest request) {
		if (request instanceof SearchFeatureRequest || request instanceof SearchByLocationRequest) {
			this.request = request;
		} else {
			throw new IllegalArgumentException(
					"Please provide a request (SearchFeatureRequest or SearchByLocationRequest)");
		}
	}
}
