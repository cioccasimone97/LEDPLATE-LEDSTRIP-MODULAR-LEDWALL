package Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import Main.Start;

/**
 * A screen is composed of more than one plate. It can have multiple
 * configurations: for example 4x4, 3x5, 9x4 and others. The variables plates_x
 * and plates_y indicate the number of plates that the screen is built of.
 */
public class Screen {
	
	// attributes
	private String id_screen;
	private int plates_x, plates_y, x, y;
	private int led_number; // number of leds in a plate
	private int plates_number; // number of plates in the screen
	private int position;
	public ArrayList<Integer[][]> indirizzi;
	int colori[];
	Plate[][] plates; // matrix that represents the screen

	Vector<Plate> screen = new Vector<Plate>(); // list of plates

	// constructor
	/**
	 * @param plates_x Number of led panels on the x axis
	 * @param plates_y Number of led panels on the y axis
	 * @param x        number of leds on a raw in a panel
	 * @param y        number of leds on a column in a panel
	 * 
	 *                 The constructor initializes the id_screen as
	 *                 "S"+plates_x+plates_y. So S56 indicates a screen composed of
	 *                 5 panels along the x axis and 6 panels along the y axis.
	 * 
	 * 
	 */
	public Screen(int plates_x, int plates_y, int x, int y) {
		this.plates_x = plates_x;
		this.plates_y = plates_y;
		this.x = x;
		this.y = y;
		this.id_screen = "S" + plates_x + plates_y; // this is an invented equation to have a unique id for every screen
		led_number = x * y * plates_x * plates_y; // total number of leds in a screen
		plates_number = plates_x * plates_y; // total number of panels in a screen
		ArrayList<Integer[][]> indirizzi = new ArrayList<Integer[][]>();
		Plate[][] plates = new Plate[plates_x][plates_y];

		// here we populate the matrix
		int count = 0;
		int j = 0;
		int i = 0;
		boolean pari = true; // inizializzo a random il valore effettivo verrà assunto poi
		for (i = 0; i < plates_y; i++) {
			if (i % 2 == 0) // plates sulle righe pari dello schermo
			{
				pari = true;
				for (j = 0; j < plates_x; j++) {
					plates[j][i] = new Plate(count, x, y, pari);
					count++;
				}
			} else // plates sulle righe dispari dello schermo
			{
				pari = false;
				for (j = plates_x - 1; j >= 0; j--) {
					plates[j][i] = new Plate(count, x, y, pari);
					count++;
				}
			}
		}
		for (int i2 = 0; i2 < plates_y; i2++) {
			for (int j2 = 0; j2 < plates_x; j2++) {
				indirizzi.add(plates[j2][i2].sottomatrice);
			}
		}
		this.indirizzi = indirizzi;
	}

	// METHODS
	/**
	 * Returns the list of every Led Plate's details and the details of the screen
	 */
	@Override
	public String toString() {

		System.out.println("PLATES DETAILS:");
		for (int t = 0; t < plates_y; t++) {
			for (int s = 0; s < plates_x; s++) {

				System.out.println(plates[s][t].toString());

			}
		}

		return "\nSCREEN DETAILS: \nid_screen=" + id_screen + "\nplates_x=" + plates_x + ", plates_y=" + plates_y
				+ ",\nx=" + x + ", y=" + y + ", led_number=" + led_number;

	}

	/**
	 * La funzione riceve una lista di sottomatrici e una matrice di indirizzi, e
	 * crea una corrispondenza tra le due. Ciò permette il corretto ordinamento
	 * all'interno del vettore finale da disegnare a schermo
	 * 
	 * @throws IOException
	 **/
	public boolean Riordina(ArrayList<Integer[][]> matrix_x4, ArrayList<Integer[][]> indirizzi) throws IOException {
		Start.vector_color_ordered = new int[plates_x * plates_x * x
		 * y * matrix_x4.size() / indirizzi.size()];
		Iterator<Integer[][]> myIterator = matrix_x4.iterator();
		Iterator<Integer[][]> myIterator2 = indirizzi.iterator();
		short frame_number = 0;
		int ind = 0;

		while (myIterator.hasNext()) {
			// System.out.println("myIterator: " + myIterator.hasNext());
			// System.out.println("myIterator2: " + myIterator2.hasNext());

			Integer[][] i = myIterator.next();
			if (!myIterator2.hasNext()) {
				myIterator2 = indirizzi.iterator();
				frame_number++;
			}
			;
			Integer[][] j = myIterator2.next();

			for (int r = 0; r < y; r++) {
				for (int c = 0; c < x; c++) {
					// System.out.println(j[c][r]);
					// System.out.println(i[c][r]);
					ind = frame_number * led_number + j[c][r];
					Start.vector_color_ordered[ind] = i[c][r];
				}
			}
		}
		return true;
	}
}
