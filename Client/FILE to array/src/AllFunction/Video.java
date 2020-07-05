package AllFunction;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.Start;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarStyle;

public class Video {

	private static short led_pannello_riga = Start.getLed_pannello_riga();
	private static short led_pannello_colonna = Start.getLed_pannello_colonna();
	private static int width = Start.getLed_pannello_riga() * Start.getNum_pannelli_riga();
	private static int height = Start.getLed_pannello_colonna() * Start.getNum_pannelli_colonna();
	private static String imagePath = Start.getImagPath();

	// FUNZIONE CHE CREA IMG-VIDEO
	/**
	 * La funzione crea una sequenza di immagini determinata dalla scomposizione del
	 * video in frame
	 **/
	public static boolean create_imgVideo(int noi) throws IOException {

		ProgressBar pb = new ProgressBar("create_imgVideo", noi, ProgressBarStyle.ASCII);

		for (int i = 1; i <= noi; i++) {

			pb.step();

			String image = imagePath + "/" + Integer.toString(i) + ".png";
			File f_in = new File(image); // leggo IMG

			try {
				Function.resize(image, image, width, height);
			} catch (IOException e) {
				System.err.println(e);
			}

			BufferedImage bi = ImageIO.read(f_in);

			Integer app_m[][] = new Integer[width][height];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					Color c = new Color(bi.getRGB(x, y));

					// array di matrix
					app_m[x][y] = Function.getIntFromColor(c.getRed(), c.getGreen(), c.getBlue());
				}
			}

			// associo matrice
			Start.array_di_matrix.add(app_m);

			// matrix_x4
			Integer app_x4[][];
			for (int y = 0; y < height; y = y + led_pannello_colonna) {
				for (int x = 0; x < width; x = x + led_pannello_riga) {

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
			if (f_in.exists()) {
				f_in.delete();
			}
		}
		pb.stop();

		//Function.visualizza_MATRIX_x4();
		return true;
	}
}
