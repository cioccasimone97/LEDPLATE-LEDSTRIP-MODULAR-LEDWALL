package AllFunction;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.Start;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarStyle;

public class Img {

	private static short led_pannello_riga = Start.getLed_pannello_riga();
	private static short led_pannello_colonna = Start.getLed_pannello_colonna();
	private static int width = Start.getLed_pannello_riga() * Start.getNum_pannelli_riga();
	private static int height = Start.getLed_pannello_colonna() * Start.getNum_pannelli_colonna();
	private static String file_input = Start.getFile_input();
	private static String file_output = Start.getFile_output();

	// FUNZIONE CHE CREA IMG
	/**
	 * La funzione crea un immagine partendo dalla sua matrice di colori associata
	 **/
	public static boolean create_img() throws IOException {

		ProgressBar pb = new ProgressBar("create_img", 100, ProgressBarStyle.ASCII);

		Function.resize(file_input, file_output, width, height);

		File f = new File(file_output);
		BufferedImage bi = ImageIO.read(f);

		Integer app_m[][] = new Integer[width][height];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color c = new Color(bi.getRGB(x, y));

				pb.step();

				// array di matrix
				app_m[x][y] = Function.getIntFromColor(c.getRed(), c.getGreen(), c.getBlue());
			}
		}

		// associo matrice
		Start.array_di_matrix.add(app_m);

		Integer app_x4[][];

		// matrix_x4
		for (int y = 0; y < height; y = y + led_pannello_colonna) {
			for (int x = 0; x < width; x = x + led_pannello_riga) {

				pb.step();

				app_x4 = new Integer[led_pannello_riga][led_pannello_colonna];
				// creo la matricetta
				for (int i_y = 0; i_y < led_pannello_colonna; i_y++) {
					for (int i_x = 0; i_x < led_pannello_riga; i_x++) {
						Color c = new Color(bi.getRGB(x + i_x, y + i_y));
						app_x4[i_x][i_y] = Function.getIntFromColor(c.getRed(), c.getGreen(), c.getBlue());
					}
				}

				Start.matrix_x4.add(app_x4);
			}
		}

		// distruggo IMG creata
		if (f.exists()) {
			f.delete();
		}
		pb.stop();

		//Function.visualizza_MATRIX_x4();
		return true;
	}
}
