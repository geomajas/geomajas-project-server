package org.geomajas.gwt.example.client.sample.rendering;

import org.geomajas.gwt.client.gfx.HtmlGroup.DivPanel;
import org.geomajas.gwt.client.gfx.TransformableWidget;
import org.vaadin.gwtgraphics.client.Transparent;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;

public class DivRectangle extends DivPanel implements TransformableWidget, Transparent {
	
	private double deltaX;
	
	private double deltaY;
	
	private double scaleX = 1.0;
	
	private double scaleY = 1.0;

	private double userX;

	private double userY;

	private double userWidth;

	private double userHeight;

	public DivRectangle(double userX, double userY, double userWidth, double userHeight) {
		super();
		this.userX = userX;
		this.userY = userY;
		this.userWidth = userWidth;
		this.userHeight = userHeight;
		getElement().getStyle().setPosition(Position.ABSOLUTE);
		getElement().getStyle().setBackgroundColor("red");
	}
	
	public double getUserWidth() {
		return userWidth;
	}

	public void setUserWidth(double userWidth) {
		this.userWidth = userWidth;
		drawTransformed();
	}

	public double getUserHeight() {
		return userHeight;
	}

	public void setUserHeight(double userHeight) {
		this.userHeight = userHeight;
		drawTransformed();
	}

	public double getUserX() {
		return userX;
	}

	
	public void setUserX(double userX) {
		this.userX = userX;
		drawTransformed();
	}

	
	public double getUserY() {
		return userY;
	}

	
	public void setUserY(double userY) {
		this.userY = userY;
		drawTransformed();
	}

	public void setTranslation(double deltaX, double deltaY) {
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		drawTransformed();
	}

	public void setScale(double scaleX, double scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		drawTransformed();
	}

	private void drawTransformed() {
		// transform all points and calculate new bounds
		double x1 = getUserX() * scaleX + deltaX;
		double y1 = getUserY() * scaleY + deltaY;
		double x2 = (getUserX() + getUserWidth()) * scaleX + deltaX;
		double y2 = (getUserY() + getUserHeight()) * scaleY + deltaY;
		getElement().getStyle().setLeft(Math.min(x1, x2), Unit.PX);
		getElement().getStyle().setTop(Math.min(y1, y2), Unit.PX);
		getElement().getStyle().setWidth(Math.abs(x1 - x2), Unit.PX);
		getElement().getStyle().setHeight(Math.abs(y1 - y2), Unit.PX);
	}

	@Override
	public void setFixedSize(boolean fixedSize) {
	}

	@Override
	public boolean isFixedSize() {
		return false;
	}

	@Override
	public void setOpacity(double opacity) {
		getElement().getStyle().setOpacity(opacity);
	}

}
