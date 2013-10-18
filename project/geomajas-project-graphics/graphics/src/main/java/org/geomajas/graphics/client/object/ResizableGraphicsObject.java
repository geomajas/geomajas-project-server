/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.graphics.client.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.graphics.client.object.anchor.Anchored;
import org.geomajas.graphics.client.object.labeler.ResizableLabeler;
import org.geomajas.graphics.client.object.role.Labeled;
import org.geomajas.graphics.client.object.role.Renderable;
import org.geomajas.graphics.client.object.role.RoleType;
import org.geomajas.graphics.client.util.FlipState;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.VectorObject;

/**
 * Base class for graphical objects that are resizable.
 * 
 * @author Jan De Moerloose
 * 
 */
public abstract class ResizableGraphicsObject extends BaseGraphicsObject implements Resizable {

	private SortedGroup rootGroup;

	private Map<RoleType<?>, ResizableAwareRole<?>> roles = new HashMap<RoleType<?>, ResizableAwareRole<?>>();

	private Resizable resizable;

	private RoleType<?>[] renderOrder = new RoleType<?>[] { 
			Anchored.TYPE, 
			Bordered.TYPE, 
			Renderable.TYPE, 
			Labeled.TYPE };

	protected ResizableGraphicsObject(Resizable r) {
		this(r, null);
	}

	protected ResizableGraphicsObject(Resizable r, String text) {
		resizable = r;
		rootGroup = new SortedGroup();
		addRole(new ResizableRenderer());
		if (text != null) {
			addRole(new ResizableLabeler());
			getRole(Labeled.TYPE).getTextable().setLabel(text);
		}
		addRole(Draggable.TYPE, this);
		addRole(Resizable.TYPE, this);
	}

	public void setRenderOrder(RoleType<?>... renderOrder) {
		this.renderOrder = renderOrder;
		rootGroup.clearRoles();
		for (ResizableAwareRole<?> role : roles.values()) {
			rootGroup.add(role.getType(), role.asObject());
		}
	}

	public <T> void addRole(ResizableAwareRole<T> role) {
		if (roles.containsKey(role.getType())) {
			removeRole(role.getType());
		}
		role.setResizable(resizable);
		roles.put(role.getType(), role);
		rootGroup.add(role.getType(), role.asObject());
		update();
		super.addRole(role.getType(), role.asRole());
	}

	public void removeRole(RoleType<?> type) {
		ResizableAwareRole<?> role = roles.remove(type);
		if (role != null) {
			rootGroup.remove(role.getType(), role.asObject());
		}
		super.removeRole(type);
	}

	@SuppressWarnings("unchecked")
	public <T> T asRole(RoleType<T> type) {
		return (T) roles.get(type).asRole();
	}

	public Resizable getResizable() {
		return resizable;
	}

	@Override
	public void flip(FlipState state) {
		// symmetric
		resizable.flip(state);
		update();
	}

	@Override
	public boolean isPreserveRatio() {
		return resizable.isPreserveRatio();
	}
	
	

	public boolean isAutoHeight() {
		return resizable.isAutoHeight();
	}

	@Override
	public void setUserBounds(Bbox bounds) {
		resizable.setUserBounds(bounds);
		update();
	}

	@Override
	public Bbox getUserBounds() {
		return resizable.getUserBounds();
	}

	@Override
	public Bbox getBounds() {
		return resizable.getBounds();
	}

	@Override
	public void setPosition(Coordinate position) {
		resizable.setPosition(position);
		update();
	}

	public void update() {
		roles.get(Renderable.TYPE).onUpdate();
		for (ResizableAwareRole<?> role : roles.values()) {
			if (role.getType() != Renderable.TYPE) {
				role.onUpdate();
			}
		}
	}

	@Override
	public Coordinate getPosition() {
		return resizable.getPosition();
	}

	public VectorObject asObject() {
		return rootGroup;
	}

	public void setOpacity(double opacity) {
		rootGroup.setOpacity(opacity);
	}

	protected void copyTo(ResizableGraphicsObject to) {
		for (ResizableAwareRole<?> role : roles.values()) {
			ResizableAwareRole<?> clone = role.cloneRole(to.getResizable());
			to.addRole(clone);				
		}
		to.update();
	}

	public void setColor(String color) {
		getRole(Labeled.TYPE).getTextable().setFontColor(color);
	}

	public String getColor() {
		return getRole(Labeled.TYPE).getTextable().getFontColor();
	}
	
	public void setSize(int size) {
		getRole(Labeled.TYPE).getTextable().setFontSize(size);
	}

	public int getSize() {
		return getRole(Labeled.TYPE).getTextable().getFontSize();
	}

	public String getFont() {
		return getRole(Labeled.TYPE).getTextable().getFontFamily();
	}

	/**
	 * Group the sorts role by rendering order.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class SortedGroup extends Group {

		private List<VectorObject> backGroundObjects = new ArrayList<VectorObject>();

		private Map<RoleType<?>, ObjectRef> typedRefs = new LinkedHashMap<RoleType<?>, ObjectRef>();

		public SortedGroup() {
			initRenderOrder();
		}

		private void initRenderOrder() {
			for (int i = 0; i < renderOrder.length; i++) {
				typedRefs.put(renderOrder[i], new ObjectRef());
			}
		}

		public <T> VectorObject add(RoleType<T> type, VectorObject vo) {
			if (typedRefs.containsKey(type)) {
				typedRefs.get(type).setObject(vo);
			} else {
				backGroundObjects.add(vo);
			}
			clear();
			for (VectorObject object : backGroundObjects) {
				add(object);
			}
			for (ObjectRef object : typedRefs.values()) {
				if (!object.isEmpty()) {
					add(object.getObject());
				}
			}
			return vo;
		}

		public VectorObject remove(RoleType<?> type, VectorObject vo) {
			if (typedRefs.containsKey(type)) {
				typedRefs.get(type).clear();
			} else {
				backGroundObjects.remove(vo);
			}
			return super.remove(vo);
		}
		
		public void clearRoles() {
			backGroundObjects.clear();
			typedRefs.clear();
			clear();
			initRenderOrder();
		}

		/**
		 * Nullable reference to object.
		 * 
		 * @author Jan De Moerloose
		 * 
		 */
		class ObjectRef {

			private VectorObject object;

			public VectorObject getObject() {
				return object;
			}

			public void clear() {
				object = null;
			}

			public boolean isEmpty() {
				return object == null;
			}

			public void setObject(VectorObject object) {
				this.object = object;
			}

		}

	}

}
