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

dojo.provide("geomajas.widget.LayerTree");
dojo.require("dijit._Templated");
dojo.require("dijit.layout.LayoutContainer");
dojo.require("dijit.Tree");
dojo.require("dojox.collections.ArrayList");
dojo.require("dojo.data.ItemFileWriteStore");

dojo.require("geomajas.widget.LTButton");
dojo.require("geomajas.widget.LTToggleButton");

/**
 * <p>
 * This widget represents a certain view on the MapModel class, just like the
 * MapWidget does. Represented here are the layers in a tree, just like they
 * are configured. Accompanied with this view, there are buttons that define
 * certain actions on these layers.<br/>
 * Originally there are no buttons in this widget, so they have to be added
 * manually or through configuration. These buttons can be either single
 * actions, or activesettables (a bit like in the DynamicToolbar widget). But
 * We're not talking about ordinairy actions and tools.
 * At this time only activesettables are supported, and they should all 
 * implement LayerTreeTool. For each of these added to this widget,
 * a LTButton widget is presented with unique layer manipulation posiibilities.
 * </p>
 * <p>
 * Also it is possible to select a single layer in this tree representation. By
 * selecting a Layer, the LTButtons will enable. That is because these buttons
 * define actions on a single layer: the selected one.<br/>
 * One possible example is the LayerVisibleTool. This implementation
 * of LayerTreeTool determines the visibility of the selected layer.
 * By activating/deactivating the accompanied ASButton, the selected layer will
 * be visible or not.
 * </p>
 * @author Pieter De Graef
 */
dojo.declare(
	"geomajas.widget.LayerTree",
	[dijit.layout.LayoutContainer, dijit._Templated],
	{
		widgetsInTemplate : true,
		templatePath : dojo.moduleUrl("geomajas.widget", "html/LayerTree.html"),

		mapModel : null,
		tree : null,
		selected : null,
		selectedNode : null,
		buttonsEl : null,
		buttons : null,

		layerVisibleIcon : "layerIsVisibleIcon16",
		layerInvisibleIcon : "layerIsNotVisibleIcon16",

		// Connection handles:
		topicHandle : null,
		rebuildHandle : null,
		layerHandles : null,
		actionHandles : null,
		toolHandles : null,

		/**
		 * Initialization function for this widget. Initializes somes arrays.
		 */
		constructor : function () {
			this.buttons = new dojox.collections.ArrayList ();
			this.layerHandles = [];
			this.actionHandles = [];
			this.toolHandles = [];
		},

		startup : function () {
			this.buttonsEl = document.getElementById (this.id+":buttons");
		},

		/**
		 * Initialize this widget with a MapModel object. As said in the class
		 * definition, this widget represents a certain view of a MapModel
		 * object, showing it's layers in a tree.
		 * @param mapModel : The MapModel behind the MapWidget you want to
		 *                   manipulate.
		 */
		init : function (/*MapModel*/mapModel) {
			this.mapModel = mapModel;
			this._buildTree(); // Build the tree. (view on the mapmodel)

			// Subscribe to the node onclick topic (for layer selection):
			if (this.topicHandle != null) {
				dojo.unsubscribe(this.topicHandle);
			}
			this.topicHandle = dojo.subscribe(this.id + ":tree", this, "_onExecute"); // is actually an onclick.

			// When the mapmodel has a new rootnode, we need to rebuild the tree:
			if (this.rebuildHandle != null) {
				dojo.disconnect(this.rebuildHandle);
			}
			this.rebuildHandle = dojo.connect (this.mapModel, "setRootNode", this, "_buildTree");

			// Add connections to the layer's visibility status:
			while (this.layerHandles.length > 0) {
				dojo.disconnect(this.layerHandles.pop());
			}
			var layers = this.mapModel.getLayerList();
			for (var i=0; i<layers.count; i++) {
				var layer = layers.item(i);
				this._onLayerCheckVisibility(layer);
				this.layerHandles[i] = dojo.connect(layer, "onCheckVisibility", this, "_onLayerCheckVisibility");
			}
			this._recursiveSetExpanded(this.mapModel.getRootNode(), this.tree.rootNode.getChildren()[0]);
		},

		/**
		 * Destructor for this widget. Cleans up all the connects, and the
		 * topic subscriptions before destroying itself.
		 */
		destroy : function (finalize) {
			log.info ("LayerTree.destroy");

			dojo.unsubscribe(this.topicHandle);
			dojo.disconnect(this.rebuildHandle);

			while (this.layerHandles.length > 0) {
				dojo.disconnect(this.layerHandles.pop());
			}
			while (this.actionHandles.length > 0) {
				dojo.disconnect(this.actionHandles.pop());
			}
			while (this.toolHandles.length > 0) {
				dojo.disconnect(this.toolHandles.pop());
			}

			this.inherited(arguments);
		},

		/**
		 * Function the returns the selected Layer, or null if there is no
		 * selection.
		 * @return : The Layer object behind the selected tree-node.
		 */
		getSelected : function () {
			return this.selected;
		},

		/**
		 * Not implemented yet!
		 */
		addAction : function (action) {
			if (!this.buttons.contains(action.getId())) {
				var button = new geomajas.widget.LTButton({name:action.getId(), id:action.getId(), iconClass:action.getImage(), label:action.getTooltip(), showLabel:false});
				button.setAction (action);
				this.actionHandles.push (dojo.connect(button, "onClick", dojo.hitch(this, "_checkButtons"))); // Check all buttons again after every click on one of them.
				this.buttonsEl.appendChild (button.domNode);
				button.applyLayer(null);
				this.buttons.add (button);
				return true
			}
			return false;
		},

		/**
		 * Adds a new LayerTreeTool to the list. This tool
		 * will be presented be a LTButton widget. When the user clicks this
		 * button, the "LTButton:onClick" function will be called.
		 * @param tool LayerTreeTool implementation to be added to the list.
		 */
		addTool : function (tool) {
			if (!this.buttons.contains(tool.getId())) {
				var button = new geomajas.widget.LTToggleButton({name:tool.getId(), id:tool.getId(), iconClass:tool.getImage(), label:tool.getTooltip(), showLabel:false});
				button.setTool (tool);
				this.toolHandles.push (dojo.connect(button, "onClick", dojo.hitch(this, "_checkButtons"))); // Check all buttons again after every click on one of them.
				this.buttonsEl.appendChild (button.domNode);
				button.applyLayer(null);
				this.buttons.add (button);
				return true;
			}
			return false;
		},

		/**
		 * Retrieve the MapModel object on which this LayerTree operates.
		 * @return A reference to the MapModel object.
		 */
		getMapModel : function () {
			return this.mapModel;
		},



		//---------------------------------------------------------------------
		// Private methods. Mainly for treebuilding and handling.
		//---------------------------------------------------------------------

		/**
		 * @private
		 * Executed when the user click on one of the nodes in the tree. This
		 * can change the selected layer. Also, css styles need changing to
		 * reflect this selection.
		 * Last but not least, the buttons need updating as well. This because
		 * the newly selected layer has it's own status which should be
		 * reflected in the buttons.
		 */
		_onExecute : function(message) {
			if (message.event == "execute") {
				var layerId = message.item.id;
				if (this.selected == null) { // First selection
					this.selected = this.mapModel.getLayerById (layerId);
					if (this.selected != null) {
						this.selected.setSelected (true);
						dojo.addClass(message.node.labelNode, "dijitTreeLabelSelected");
						this.selectedNode = message.node;
					} else {
						layerId = null;
						this.selected = null;
						this.selectedNode = null;
					}
				} else {
					if (this.selected.getId() == layerId) { // Was already selected, so deselect
						this.selected.setSelected (false);
						this.selected = null;
						this.selectedNode = null;
						dojo.removeClass(message.node.labelNode, "dijitTreeLabelSelected");
					} else { // Deselect previous, then select current:
						this.selected.setSelected (false);
						dojo.removeClass(this.selectedNode.labelNode, "dijitTreeLabelSelected");
						this.selected = this.mapModel.getLayerById (layerId);
						if (this.selected != null) {
							this.selected.setSelected (true);
							this.selectedNode = message.node;
							dojo.addClass(message.node.labelNode, "dijitTreeLabelSelected");
						} else {
							layerId = null;
							this.selected = null;
							this.selectedNode = null;
						}						
					}
				}
				if (this.selected == null) {
					this.mapModel.setSelectedLayer (null);
				} else {
					this.mapModel.setSelectedLayer (layerId);
				}
				// Redraw the buttons:
				this._checkButtons();
			}
		},

		/**
		 * @private
		 */
		_addNodeToJSON : function (layerTreeNode, jsonNode) {
			jsonNode.name = layerTreeNode.getLabel();
			jsonNode.id = layerTreeNode.getId();
			jsonNode.type = "layerType";
			if (jsonNode.id == null || jsonNode.id == "") {
				jsonNode.id = this.id+":rootNode";
			}

			var children = layerTreeNode.getChildren();
			if (children) {
				jsonNode.children = [];
			}
			for (var i=0; i<children.count; i++) {
				jsonNode.children.push({});
				this._addNodeToJSON (children.item(i), jsonNode.children[i]);
			}
		},

		/**
		 * @private
		 * Check status off all buttons.
		 */
		_checkButtons : function () {
			for (var i=0; i<this.buttons.count; i++) {
				var button = this.buttons.item(i);
				button.applyLayer(this.selected);
			}
		},

		/**
		 * @private
		 * Rebuild the tree using the current MapModel. Basically this tree is
		 * a view on the MapModel, displaying the layers.
		 */
		_buildTree : function () {
			if (this.tree) {
				this.tree.destroy();
			}
			var jsonData = { identifier: "id", items: [], label: "name" }; // unique by id, displayed as name
			var root = { };
			this._addNodeToJSON(this.mapModel.getRootNode(), root);
			jsonData.items.push( root );
			var treeStore = new dojo.data.ItemFileWriteStore( { data: jsonData } );
			var params = { id: this.id+":tree", store: treeStore, typeAttr: "type", query: { type: "layerType" }, persist:false };
			if (dojo.byId(this.id+":treePage") == null) {
				var pane = dojo.byId(this.id+":treePagePane");
				var div = document.createElement("div");
				div.setAttribute("id", this.id+":treePage");
				pane.appendChild(div);
			}
			this.tree = new dijit.Tree(params, dojo.byId(this.id+":treePage"));
			this._recursiveExpandAll(this.tree.rootNode);
		},

		/**
		 * @private
		 * Changes treenode icons according to a layer's visibility status.
		 */
		_onLayerCheckVisibility : function (layer) {
			if (layer.isVisible() && layer.checkScaleVisibility()) {
				var node = this._fetchNodeByIdentifier(layer.getId());
				this._removeIconFromNode(node, this.layerInvisibleIcon);
				this._addIconToNode(node, this.layerVisibleIcon);
			} else {
				var node = this._fetchNodeByIdentifier(layer.getId());
				this._removeIconFromNode(node, this.layerVisibleIcon);
				this._addIconToNode(node, this.layerInvisibleIcon);
			}
			if (layer == this.selected) {
				this._checkButtons();
			}
		},

		/**
		 * @private
		 * Fetches a TreeNode widget by it's identifier. In this widget,
		 * the identifiers are the layer-ID's.
		 */
		_fetchNodeByIdentifier : function (id, node) {
			if (node == null){
				return this._fetchNodeByIdentifier(id, this.tree.rootNode);
			} else {
				if (node.item && node.item.id == id) {
					return node;
				}
				var children = node.getChildren();
				if (children != null) {
					for (var i=0; i<children.length; i++) {
						var temp = this._fetchNodeByIdentifier(id, children[i]);
						if (temp != null) {
							return temp;
						}
					}
				}
			}
			return null;
		},

		/**
		 * @private
		 * Adds a CSS icon style to a TreeNode widget.
		 */
		_addIconToNode : function (node, iconClassName) {
			if (node != null && iconClassName != null && node.iconNode != null) {
				dojo.removeClass(node.iconNode, "dijitLeaf");
				dojo.addClass(node.iconNode, iconClassName);
			}
		},

		/**
		 * @private
		 * Removes a CSS icon style from a TreeNode widget.
		 */
		_removeIconFromNode : function (node, iconClassName) {
			if (node != null && iconClassName != null && node.iconNode != null) {
				dojo.removeClass(node.iconNode, "dijitLeaf");
				dojo.removeClass(node.iconNode, iconClassName);
			}
		},

		/**
		 * @private
		 * Go through the tree, and set the subnodes on expanded or collapsed
		 * so determined in the server-side XML configuration.
		 */
		_recursiveSetExpanded : function (layerTreeNode, treeNode) {
			var children = treeNode.getChildren();
			if (children != null) {
				if (layerTreeNode.isExpanded()) {
					this.tree._expandNode(treeNode);
				} else {
					this.tree._collapseNode(treeNode);
				}
				for (var i=0; i<children.length; i++) {
					this._recursiveSetExpanded(layerTreeNode.getChildren().item(i), children[i]);
				}
			}
		},

		/**
		 * @private
		 * This function exists to set all the visibility icons. To do this, we
		 * need to expand the entire tree. Problem is this: when this function
		 * is called, the subnodes aren't fully loaded yet. So we can't
		 * immediatly get the children. But calling the private "_expandNode"
		 * first will inherently load it's chilren. Too bad i could not find
		 * a public function to do this....but that's Dojo
		 */
		_recursiveExpandAll : function (treeNode) {
			if (treeNode != null && treeNode.isExpandable) {
				this.tree._expandNode(treeNode);
				var children = treeNode.getChildren();
				//log.debug ("Expanding node : " + treeNode.label + ", found " + children.length + " children");
				for (var i=0; i<children.length; i++) {
					this._recursiveExpandAll(children[i]);
				}
			}
		}
	}
);