package main;

import java.util.ArrayList;

import algorithms.Algorithms;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

	/**
	 * Transforme un fichier de points en une liste de points
	 * 
	 * @param path le chemin du fichier
	 * @return la liste des points contenus dans le fichier
	 */
	public static ArrayList<Point> read(String path) {
		ArrayList<Point> result = new ArrayList<>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(path));
			String line;
			String[] point = new String[2];
			int x, y;
			while ((line = reader.readLine()) != null) {
				point = line.split(" ");
				x = Integer.parseInt(point[0]);
				y = Integer.parseInt(point[1]);
				result.add(new Point(x, y));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Lance le calcul du cercle minimum
	 * 
	 * @param args args[0] doit etre le fichier de points, ars[1] l'algorithme
	 *             utilise et args[2] est optionnel. Il genere un affichage s'il
	 *             vaut 1.
	 */
	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println(
					"Les arguments doivent être de la forme <nom_fichier.points> <numero_algo_utilisé> (<affichage>)"); // affichage
																														// optionnel
			System.exit(1);
		}
		boolean display = false;
		if (args.length == 3) {
			if (args[2].equals("1"))
				display = true;
		}

		ArrayList<Point> points = read(args[0]);
		Algorithms a = new Algorithms();
		a.minimumCircleComputing(points, Integer.parseInt(args[1]), display);
	}

}
