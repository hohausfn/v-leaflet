package org.vaadin.addon.leaflet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.vaadin.addon.leaflet.client.LeafletLayerGroupState;

import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Component;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public class LLayerGroup extends AbstractComponentContainer implements
		LeafletLayer {

	private List<Component> components = new ArrayList<Component>();
	private Boolean active;

	public LLayerGroup(Boolean active) {
		this.active = active;
	}

	public LLayerGroup() {
		this(true);
	}

	@Override
	public void replaceComponent(Component oldComponent, Component newComponent) {

	}

	@Override
	public void addComponent(Component c) {
		if ((!(c instanceof AbstractLeafletLayer))
				&& (!(c instanceof LLayerGroup))) {
			throw new IllegalArgumentException(
					"only instances of AbstractLeafletLayer or LLayerGroup allowed");
		}
		super.addComponent(c);
		components.add(c);
		markAsDirty();
	}

	@Override
	public void removeComponent(Component c) {
		super.removeComponent(c);
		components.remove(c);
		markAsDirty();
	}

	@Override
	public void beforeClientResponse(boolean initial) {
		super.beforeClientResponse(initial);
		getState().active = active;
	}

	@Override
	protected LeafletLayerGroupState getState() {
		return (LeafletLayerGroupState) super.getState();
	}

	@Override
	public int getComponentCount() {
		return components.size();
	}

	@Override
	public Iterator<Component> iterator() {
		return components.iterator();
	}

	public void setActive(Boolean active) {
		this.active = active;
		getState().active = active;
	}

	@Override
	public Geometry getGeometry() {
		ArrayList<Geometry> gl = new ArrayList<Geometry>();
		for (Component c : this) {
			LeafletLayer l = (LeafletLayer) c;
			gl.add(l.getGeometry());
		}
		return new GeometryFactory().buildGeometry(gl);
	}
}
