dojo.provide("geomajas.map.print.PrintComponentInfo");
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
dojo.declare("PrintComponentInfo", null, {

	/**
	 * @class 
	 * A basic print component (mirror of server object)
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 */
	constructor : function () {
//		private String id;
//		private String tag;
//		private List<PrintComponentInfo> children = new ArrayList<PrintComponentInfo>();
//		private Bbox bounds;
//		private LayoutConstraintInfo layoutConstraint = new LayoutConstraintInfo();

		// abstract class so no 'javaClass' property needed
	
		this.children = {list: [], javaClass : "java.util.ArrayList"};
		this.layoutConstraint = new LayoutConstraintInfo();
	},

	getId : function () {
		return this.id;
	},

	setId : function (id) {
		this.id = id;
	},

	getTag : function () {
		return this.tag;
	},

	setTag : function (tag) {
		this.tag = tag;
	},

	getChildren : function () {
		return this.children;
	},

	setChildren : function (children) {
		this.children = children;
	},

	addChild : function (/*PrintComponentInfo*/ child) {
		if (child != null) {
			this.children.list.push(child);
		}
	},

	getBounds : function () {
		return this.bounds;
	},

	setBounds : function (bounds) {
		this.bounds = bounds;
	},

	getLayoutConstraint : function () {
		return this.layoutConstraint;
	},

	setLayoutConstraint : function (layoutConstraint) {
		this.layoutConstraint = layoutConstraint;
	}
	
});
