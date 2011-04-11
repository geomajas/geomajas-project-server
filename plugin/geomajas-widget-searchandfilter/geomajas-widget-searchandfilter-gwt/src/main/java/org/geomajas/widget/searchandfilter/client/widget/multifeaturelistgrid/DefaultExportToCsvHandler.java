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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.SearchFeatureRequest;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.Deferred;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.layer.feature.SearchCriterion;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.client.util.Callback;
import org.geomajas.widget.searchandfilter.command.dto.ExportToCsvRequest;
import org.geomajas.widget.searchandfilter.command.dto.ExportToCsvResponse;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.UrlBuilder;
import com.smartgwt.client.core.Function;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.Window;

/**
 * TODO refactor so can be used for feature & location search.
 *
 * The default export handler. This will use the featureIds from the grid to
 * requeste the CSV. This means that if the resultset was cut (eg. there were
 * more features than the grid accepts (default 100) they will also not be in
 * the CSV.
 *
 * @author Kristof Heirwegh
 */
public class DefaultExportToCsvHandler implements ExportToCsvHandler {

	private List<Feature> features;
	private VectorLayer layer;
	private MapModel model;

	private SearchAndFilterMessages messages = GWT.create(SearchAndFilterMessages.class);

	public DefaultExportToCsvHandler(MapModel model, VectorLayer layer) {
		if (model == null || layer == null) {
			throw new IllegalArgumentException("All parameters are required.");
		}
		this.layer = layer;
		this.model = model;
	}

	public void setFeatures(List<Feature> features) {
		this.features = features;
	}

	public void execute(final VectorLayer vlayer) {
		execute(vlayer, null);
	}

	public void execute(VectorLayer vlayer, final Callback onFinished) {
		if (this.layer.equals(vlayer)) {
			ExportToCsvRequest request = new ExportToCsvRequest();
			SearchFeatureRequest featReq = new SearchFeatureRequest();
			request.setSearchFeatureRequest(featReq);
			request.setEncoding(messages.exportToCsvEncoding());
			request.setLocale(messages.exportToCsvLocale());
			request.setSeparatorChar(messages.exportToCsvSeparatorChar());
			request.setQuoteChar(messages.exportToCsvQuoteChar());
			request.setLayerId(layer.getServerLayerId());
			featReq.setCriteria(buildCriteria());
			featReq.setBooleanOperator("OR");
			featReq.setCrs(model.getCrs());
			featReq.setLayerId(layer.getServerLayerId());
			featReq.setFilter(layer.getFilter());
			featReq.setFeatureIncludes(GwtCommandDispatcher.getInstance().getLazyFeatureIncludesSelect());

			GwtCommand command = new GwtCommand("command.searchandfilter.ExportToCsv");
			command.setCommandRequest(request);
			Deferred deferred = GwtCommandDispatcher.getInstance().execute(command, new CommandCallback() {
				private static final String CONTENT_PRE = "<div style='margin-top: 20px; width: 200px; text-align: ce" +
						"nter'><b>";
				private static final String CONTENT_POST = "</b><br />";
				private static final String LINK_POST = "</div>";

				public void execute(CommandResponse response) {
					if (response instanceof ExportToCsvResponse) {
						ExportToCsvResponse resp = (ExportToCsvResponse) response;
						if (resp.getDocumentId() != null) {
							UrlBuilder ub = new UrlBuilder();
							ub.setPath("d/fileDownload");
							ub.setParameter("id", resp.getDocumentId());
							String link = ub.buildString().replaceFirst("http:///", GWT.getHostPageBaseURL());

							Window window = new Window();
							window.setTitle(messages.exportToCsvWindowTitle());
							String content = CONTENT_PRE + messages.exportToCsvContentReady() + CONTENT_POST;
							String linktocsv = messages.exportToCsvDownloadLink(link) + LINK_POST;
							window.addItem(new HTMLFlow(content + linktocsv));
							window.centerInPage();
							window.setAutoSize(true);
							window.show();
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

	private SearchCriterion[] buildCriteria() {
		List<SearchCriterion> critters = new ArrayList<SearchCriterion>();
		String idField = layer.getLayerInfo().getFeatureInfo().getIdentifier().getName();
		if (features != null) {
			for (Feature feat : features) {
				critters.add(new SearchCriterion(idField, "=", feat.getId()));
			}
		}
		return critters.toArray(new SearchCriterion[0]);
	}
}
