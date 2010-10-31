import java.awt.*;

public abstract class Shape {
	int state;
	Color conturColor, fillColor;
	boolean selected;

	abstract public void draw(Graphics g_main);
	abstract void paint(Graphics g);
	abstract void changeLineThickness(Graphics g_main);

	public void changeFillColor(Color givenColor) {
		this.fillColor = givenColor;
	}

	public void changeConturColor(Color givenColor) {
		System.out.println("CONTORCOLOR:= " + givenColor);
		this.conturColor = givenColor;
	}

	public void scale(){}

	public void translate() {
	}

	/* Sees if the shapes can be further moved */ 
	public boolean shapesStayInside() {
		return true;
	}
}