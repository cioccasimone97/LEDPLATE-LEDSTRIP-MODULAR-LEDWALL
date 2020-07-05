package Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.common.io.Files;
import com.google.common.io.RecursiveDeleteOption;

import AllFunction.*;
import Elements.*;

public class Start {

	// VARIABILI GLOBALI
	private static String file_input; // img originale
	private static String file_output; // img resized
	private static int height = 0; // led in altezza
	private static int width = 0; // led in larghezza

	private static short led_pannello_riga = 10; // da richiedere #
	private static short led_pannello_colonna = 10; // da richiedere #
	private static short num_pannelli_riga = 2; // da richiedere #
	private static short num_pannelli_colonna = 2; // da richiedere #
	private static String imagePath; // path cartella di salvataggio temp

	public static ArrayList<Integer[][]> array_di_matrix = new ArrayList<Integer[][]>(); // OUTPUT matrix
	public static ArrayList<Integer[][]> matrix_x4 = new ArrayList<Integer[][]>(); // OUTPUT matrix_x4
	public static int vector_color_ordered[];
	public static Integer framerate;
	
	// MAIN
	public static void main(String[] args) throws IOException {
		// VARIABILI LOCALI
		String splitted[];
		String formato;
		Scanner input;
		boolean success = false; // determina la corretta creazione dell'output
		framerate = 0;
		
		// creo la path per l'app
		System.out.println(System.getProperty("os.name"));
		File tempDir = Files.createTempDir();
		imagePath = tempDir.toPath().toString();
		System.out.println(imagePath);
		// ----------------------

		input = new Scanner(System.in);
		System.out.println("Inserire percorso e nome del file (./cartella/file.formato) (img,gif,...): ");
		file_input = input.next();
		splitted = file_input.split("/"); // "/"

		// istanzio un file di uscita (così non modifico l'originale)
		file_output = imagePath + "/resized_" + splitted[splitted.length - 1];
		// -----------------------

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		while (height <= 0 || width <= 0) {
			// ALTEZZA
			System.out.print("Inserire altezza: ");
			checkUserInseredHeight(in);

			// LARGHEZZA
			System.out.print("Inserire larghezza: ");
			checkUserInseredWidth(in);

			// NEGATIVO
			if (height <= 0 || width <= 0) {
				System.out.println("Dimensioni pannello LED non valide");
			}
		}
		input.close(); // non spostare da qui

		// ------------------------------
		formato = file_input.substring(file_input.length() - 3); // prelevo il formato del file
		System.out.println("FORMATO: " + formato);

		switch (formato) {
		// IMG
		case "png":
		case "jpg":
		case "jpeg":
			System.out.println("IMG");
			success = Img.create_img();
			break;

		// GIF
		case "gif":
			System.out.println("GIF");
			success = Gif.create_gif();
			break;
		// MP4 | video
		case "mp4":
			System.out.println("MP4 | video");
			success = MovieToImage.convertMovietoJPG(file_input, file_output, imagePath, "png", 0);
			break;

		default:
			System.err.println("FORMAT NOT RECOGNIZED");
			// come gestirlo?
		}
		// ------------------------------

		// scrittura finale
		if (success == true) {
			System.out.println();
			System.out.println("-array_di_matrix- ARRAYLIST<INTEGER [][]> CREATO CON SUCCESSO");
			System.out.println("-matrix_x4- ARRAYLIST<INTEGER [][]> CREATO CON SUCCESSO");
		} else {
			System.err.println("ERRORE CREAZIONE ARRAYLIST<INTEGER [][]>");
		}

		System.out.println("matrix_x4 SIZE: " + matrix_x4.size());

		// METODO DI ORDINAMENTO
		Screen schermo = new Screen(num_pannelli_riga, num_pannelli_colonna, led_pannello_colonna, led_pannello_riga);
		success = schermo.Riordina(matrix_x4, schermo.indirizzi);
		printVector(vector_color_ordered);

		// scrittura finale
		if (success == true) {
			System.out.println("-vector_color_ordered- INT[] CREATO CON SUCCESSO");
		} else {
			System.err.println("ERRORE CREAZIONE -vector_color_ordered- INT[]");
		}

		// elimino la path temporanea (da implementare all'uscita del programma)
		System.out.println("\n CANCELLAZIONE DI TUTTO");
		com.google.common.io.MoreFiles.deleteRecursively(tempDir.toPath(), RecursiveDeleteOption.ALLOW_INSECURE);

	}

	/**
	 * La funzione controlla il corretto inserimento (da utente) del valore di
	 * LARGHEZZA dell'immagine
	 **/
	private static void checkUserInseredWidth(BufferedReader in) {
		boolean flag_width = false;
		while (!flag_width) {
			try {
				Integer i = Integer.parseInt(in.readLine());
				width = i.intValue();
				flag_width = true;
			} catch (Exception e) {
				System.out.print("RE-Inserire larghezza: ");
				flag_width = false;
			}
		}
	}

	/**
	 * La funzione controlla il corretto inserimento (da utente) del valore di
	 * ALTEZZA dell'immagine
	 **/
	private static void checkUserInseredHeight(BufferedReader in) {
		boolean flag_height = false;
		while (!flag_height) {
			try {
				Integer i = Integer.parseInt(in.readLine());
				height = i.intValue();
				flag_height = true;
			} catch (Exception e) {
				System.out.print("RE-Inserire altezza: ");
				flag_height = false;
			}
		}
	}

	/**
	 * La funzione esegue il print di un vettore fornitogli come parametro
	 * 
	 * @throws IOException
	 **/
	private static void printVector(int v[]) throws IOException {
		File file = new File(Paths.get("").toString() + "FILE_funzionante.txt");
		
		FileWriter w;
		w = new FileWriter(file);
		w.write(framerate.toString());

		for (int i = 0; i < v.length; i++) {
			//System.out.print(v[i] + ",");
			w.write(","+ v[i]);
		}
	
		w.flush();

		System.out.println();
		System.out.println("vector_color_ordered lenght: " + v.length);
	}

	// ------------------------
	// GETTER AND SETTER

	public static short getLed_pannello_riga() {
		return led_pannello_riga;
	}

	public static short getLed_pannello_colonna() {
		return led_pannello_colonna;
	}

	public static short getNum_pannelli_riga() {
		return num_pannelli_riga;
	}

	public static short getNum_pannelli_colonna() {
		return num_pannelli_colonna;
	}

	public static String getFile_input() {
		return file_input;
	}

	public static String getFile_output() {
		return file_output;
	}

	public static String getImagPath() {
		return imagePath;
	}

}
