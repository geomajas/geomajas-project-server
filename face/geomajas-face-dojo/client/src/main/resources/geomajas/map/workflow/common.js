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

dojo.provide("geomajas.map.workflow.common");
dojo.platformRequire({
    common: ["geomajas.map.workflow.Activity",
             "geomajas.map.workflow.Workflow",
             "geomajas.map.workflow.WorkflowFactory",
             "geomajas.map.workflow.DefaultWorkflowFactory",
             "geomajas.map.workflow.WorkflowHandler",
             "geomajas.map.workflow.activities.CommitActivity",
             "geomajas.map.workflow.activities.LayerValidationActivity",
             "geomajas.map.workflow.activities.EditAttributesActivity"
            ],
    browser: []
});