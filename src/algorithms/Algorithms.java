package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;

import structure.Circle;

public class Algorithms {
	private int epsilon = 500;

	/**
	 * Calcule le cercle minimum d'un ensemble de points
	 * 
	 * @param points  l'ensemble de points
	 * @param algo    l'algorithme utilise (0 pour le naïf, 1 pour Welzl recursif et
	 *                pour tout autre valeur, Welzl iteratif sera utilise
	 * @param display la valur de l'affichage
	 * @return retourne le cercle minimum calcule par les algorithmes et l'affiche
	 *         si display vaut 1
	 */
	public Circle minimumCircleComputing(ArrayList<Point> points, int algo, boolean display) {
		Circle res = null;
		if (points.isEmpty()) {
			return null;
		}
		if (algo == 0) {
			res = naiveMinimumCircleComputing(points);
		}
		if (algo == 1) {
			res = recursive_welzl(points);

		} else {
			res = iterative_welzl(points);
		}
		if (display) {
			System.out.println("Cercle trouvï¿½ : " + res);
		}
		return res;
	}

	/**
	 * Calcule le cercle minimum d'un ensemble de points a partir de l'algorithme
	 * naif
	 * 
	 * @param inputPoints l'ensemble de points
	 * @return le cercle minimum d'un ensemble de points
	 */
	private Circle naiveMinimumCircleComputing(ArrayList<Point> inputPoints) {
		ArrayList<Point> points = (ArrayList<Point>) inputPoints.clone();
		Circle result = null, circle = null;
		boolean containsAll;

		for (Point p : points) {
			containsAll = true;
			for (Point q : points) {
				result = this.TwoPtsCircle(p, q);
			}
			for (Point r : points) {
				if (!this.isInCircle(r, result)) {
					containsAll = false;
					break;
				}
			}
			if (containsAll)
				return result;
		}

		result = new Circle(new Point(0, 0), Integer.MAX_VALUE);

		for (int i = 0; i < points.size(); i++) {
			containsAll = true;
			for (int j = i + 1; j < points.size(); j++) {
				for (int k = j + 1; k < points.size(); k++) {
					Point p = points.get(i);
					Point q = points.get(j);
					Point r = points.get(k);
					circle = this.circumscribedCircleSq(p, q, r);
					double radiusSq = circle.getRadius();
					for (Point s : points) {
						if (s.distanceSq(circle.getCenter()) <= radiusSq) {
							containsAll = false;
							break;
						}
					}
					if (containsAll && circle.getRadius() < result.getRadius())
						result = circle;
				}
			}
		}
		return new Circle(result.getCenter(), (int) Math.sqrt(result.getRadius()));
	}

	/**
	 * Calcule le cercle minimum d'un ensemble de points a partir de l'algorithme
	 * Welzl
	 * 
	 * @param inputPoints l'ensemble de points
	 * @return le cercle minimum d'un ensemble de points
	 */
	public Circle recursive_welzl(ArrayList<Point> inputPoints) {
		ArrayList<Point> points = (ArrayList<Point>) inputPoints.clone();
		if (points == null || points.isEmpty())
			return null;
		Collections.shuffle(points);
		return b_minidisk(points, new ArrayList<Point>());
	}

	/**
	 * Fonction auxiliaire utilisee par recursive_welzl
	 * 
	 * @param points 'ensemble de points
	 * @param r      des points a partir desquels le cercle sera trace
	 * @return le cercle minimum d'un ensemble de points
	 */
	public Circle b_minidisk(ArrayList<Point> points, ArrayList<Point> r) {
		Circle d;
		if (points.isEmpty() || r.size() == 3) {
			return circle(r.toArray(new Point[0]), r.size());
		}
		Point p = points.remove(0);
		d = b_minidisk(points, r);
		if (!isInCircle(p, d)) {
			r.add(p);
			d = b_minidisk(points, r);
			r.remove(p);
			points.add(p);
		}
		return d;

	}

	/**
	 * Calcule le cercle minimum d'un ensemble de points a partir de l'algorithme
	 * Welzl iteratif
	 * 
	 * @param inputPoints l'ensemble de points
	 * @return le cercle minimum d'un ensemble de points
	 */
	public Circle iterative_welzl(ArrayList<Point> inputPoints) {
		ArrayList<Point> points = (ArrayList<Point>) inputPoints.clone();
		Collections.shuffle(points);
		int point_cpt = 0;
		boolean calling = true;
		Point[] r = new Point[3];
		int r_count = 0;
		BitSet second_call = new BitSet(points.size());
		Circle result = null;

		do {
			if (calling) {
				if (point_cpt == 0 || r_count == 3) {
					result = circle(r, r_count);
					point_cpt++;
					calling = false;
				} else {
					point_cpt--;
					calling = true;
				}
			} else {
				boolean second_call_returning = second_call.get(point_cpt);
				if (!second_call_returning) {
					Point p = points.get(point_cpt - 1);
					if (isInCircle(p, result)) {
						point_cpt++;
						calling = false;
					} else {
						r[r_count++] = p;
						second_call.set(point_cpt--);
						calling = true;
					}
				} else {
					second_call.clear(point_cpt);
					points.add(0, points.remove(point_cpt - 1));
					r_count--;
					point_cpt++;
					calling = false;
				}
			}
		} while (point_cpt <= points.size());

		return result;

	}

	/**
	 * Verifie si un point appartient a un cercle
	 * 
	 * @param p le point dont on veut verifier s'il appartient au cercle
	 * @param c le cercle
	 * @return true si p appartient a c, false sinon
	 */
	public boolean isInCircle(Point p, Circle c) {
		if (c == null) {
			return false;
		}
		return p.distanceSq(c.getCenter()) <= c.getRadius() * c.getRadius() + epsilon;
	}

	public Circle circumscribedCircle(Point p, Point q, Point r) {
		int a = q.x - p.x;
		int b = q.y - p.y;
		int c = r.x - p.x;
		int d = r.y - p.y;
		int e = a * (p.x + q.x) + b * (p.y + q.y);
		int f = c * (p.x + r.x) + d * (p.y + r.y);
		int g = 2 * (a * (r.y - q.y) - b * (r.x - q.x));
		double minX, minY, dx, dy;
		double x, y, radius;
		if (Math.abs(g) < 0.000001) {
			minX = Math.min(p.x, Math.min(q.x, r.x));
			minY = Math.min(p.y, Math.min(q.y, r.y));
			dx = (Math.max(p.x, Math.max(q.x, r.x)) - minX) * 0.5;
			dy = (Math.max(p.y, Math.max(q.y, r.y)) - minY) * 0.5;

			x = minX + dx;
			y = minY + dy;
		}

		else {
			x = (d * e - b * f) / g;
			y = (a * f - c * e) / g;
			dx = x - p.x;
			dy = y - p.y;

		}
		radius = Math.sqrt(dx * dx + dy * dy);
		return new Circle(new Point((int) x, (int) y), (int) radius);
	}

	/**
	 * Calcule le cercle circonscrit au triangle pqr
	 * 
	 * @param p un sommet du triangle
	 * @param q le deuxieme sommet du triangle
	 * @param r le troisieme sommet du triangle
	 * @return le cercle circonscrit au triangle pqr
	 */
	public Circle circumscribedCircleSq(Point p, Point q, Point r) {
		int a = q.x - p.x;
		int b = q.y - p.y;
		int c = r.x - p.x;
		int d = r.y - p.y;
		int e = a * (p.x + q.x) + b * (p.y + q.y);
		int f = c * (p.x + r.x) + d * (p.y + r.y);
		int g = 2 * (a * (r.y - q.y) - b * (r.x - q.x));
		double minX, minY, dx, dy;
		double x, y, radius;
		if (Math.abs(g) < 0.000001) {
			minX = Math.min(p.x, Math.min(q.x, r.x));
			minY = Math.min(p.y, Math.min(q.y, r.y));
			dx = (Math.max(p.x, Math.max(q.x, r.x)) - minX) * 0.5;
			dy = (Math.max(p.y, Math.max(q.y, r.y)) - minY) * 0.5;

			x = minX + dx;
			y = minY + dy;
		}

		else {
			x = (d * e - b * f) / g;
			y = (a * f - c * e) / g;
			dx = x - p.x;
			dy = y - p.y;

		}
		radius = dx * dx + dy * dy;
		return new Circle(new Point((int) x, (int) y), (int) radius);
	}

	/**
	 * Construit le cercle miminum a partir d'un tableau d'au plus trois points
	 * 
	 * @param points les points du cercle
	 * @param count  le nombre de points dans le tableau
	 * @return le cercle minimum contenant les points de points
	 */
	public Circle circle(Point[] points, int count) {
		switch (count) {
		case 0:
			return null;
		case 1:
			return new Circle(points[0], 0);
		case 2:
			return TwoPtsCircle(points[0], points[1]);
		default:
			return circumscribedCircle(points[0], points[1], points[2]);
		}
	}

	/**
	 * Construit le cercle minimum a partir de deux points
	 * 
	 * @param p le premier point
	 * @param q le deuxieme point
	 * @return le cercle minimum contenant p et q
	 */
	public Circle TwoPtsCircle(Point p, Point q) {
		double cX = (p.getX() + q.getX()) / 2;
		double cY = (p.getY() + q.getY()) / 2;
		Point c = new Point((int) cX, (int) cY);
		double rayon = c.distance(p);
		return new Circle(c, (int) rayon);
	}

}
