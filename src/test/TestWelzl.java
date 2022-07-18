package test;

import algorithms.Algorithms;
import main.Main;
import structure.Circle;

import java.awt.Point;
import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestWelzl {
	private Algorithms a = new Algorithms();
	
	@Test
	public void test() {
		Circle naive, iterativeWelzl, recursiveWelzl;
		String path = "samples/test-";
		int nbTestsRecOK = 0;
		int nbTestsItOK = 0;
		ArrayList<Point> points;
		for (int i = 2; i <= 1664; i++) {
			points = Main.read(path + i + ".points");
			naive = a.minimumCircleComputing((ArrayList<Point>)points.clone(), 0, false);
			iterativeWelzl = a.minimumCircleComputing((ArrayList<Point>)points.clone(), 2, false);
			recursiveWelzl = a.minimumCircleComputing((ArrayList<Point>)points.clone(), 1, false);
			if (this.compareCircle(naive, recursiveWelzl, 5)) {
				nbTestsRecOK ++;
			}
			if (this.compareCircle(naive, iterativeWelzl, 5)) {
				nbTestsItOK ++;
			}
			else {
				System.out.println("Test " + i + "KO");
				System.out.println("naif " + naive.getRadius());
				System.out.println("ite " + iterativeWelzl.getRadius());
				System.out.println("rec " + recursiveWelzl.getRadius());
			}
				
		}
		System.out.println("Nombre des tests pass�s pour la version r�cursive : " + nbTestsRecOK);
		System.out.println("Nombre des tests pass�s pour la version it�rative : " + nbTestsItOK);
				
	}
	
	private boolean compareCircle(Circle c1, Circle c2, int threshold) {
		int radius1 = c1.getRadius();
		int radius2 = c2.getRadius();
		return Math.abs(radius1 - radius2) <= threshold;
	}

}
