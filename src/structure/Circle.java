package structure;
import java.awt.Point;

public class Circle {
	private int radius;
	private Point center;
	
	/**
	 * Accesseur sur le rayon
	 * @return le rayon
	 */
	public int getRadius() {
		return radius;
	}
	
	/**
	 * Accesseur sur le centre
	 * @return le centre
	 */
	public Point getCenter() {
		return center;
	}
	
	/**
	 * Constructeur d'un cercle
	 * @param center le centre
	 * @param radius le rayon
	 */
	public Circle (Point center, int radius) {
		this.radius = radius;
		this.center = center;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Circle)) return false;
		if (o == this) return true;
		Circle other = (Circle) o;
		return other.radius == this.radius && other.center.equals(this.center);
	}
	
	@Override
    public String toString() {
		return "centre : [" + center.x + "; " + center.y + "] , rayon : " + radius;
	}
}
