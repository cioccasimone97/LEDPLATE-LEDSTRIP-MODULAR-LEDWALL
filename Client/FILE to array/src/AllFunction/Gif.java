package AllFunction;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Main.Start;
import me.tongfei.progressbar.*;

public class Gif {

	private static short led_pannello_riga = Start.getLed_pannello_riga();
	private static short led_pannello_colonna = Start.getLed_pannello_colonna();
	private static int width = Start.getLed_pannello_riga() * Start.getNum_pannelli_riga();
	private static int height = Start.getLed_pannello_colonna() * Start.getNum_pannelli_colonna();
	private static String file_input = Start.getFile_input();
	private static String imagePath = Start.getImagPath();

	// FUNZIONE CHE CREA GIF
	/**
	 * La funzione, eseguendo ricorsivamente la funzione"crea_img()", prende una GIF
	 * ne crea i rispettivi frame e crea la matrice di colori associata
	 **/
	public static boolean create_gif() throws IOException {
		// getting gif framerate
		Start.framerate = Math.round(Function.getGifFramerate()); // VALORE DA PASSARE COME PARAMETRO 0
		System.out.print(Start.framerate);

		// begin GIF
		String[] imageatt = new String[] { "imageLeftPosition", "imageTopPosition", "imageWidth", "imageHeight" };

		ImageReader reader = (ImageReader) ImageIO.getImageReadersByFormatName("gif").next();
		ImageInputStream ciis = ImageIO.createImageInputStream(new File(file_input));
		reader.setInput(ciis, false);

		int noi = reader.getNumImages(true);
		BufferedImage master = null;

		ProgressBar pb = new ProgressBar("create_gif", noi * 2, ProgressBarStyle.ASCII);

		for (int i = 0; i < noi; i++) {
			BufferedImage image = reader.read(i);
			IIOMetadata metadata = reader.getImageMetadata(i);

			Node tree = metadata.getAsTree("javax_imageio_gif_image_1.0");
			NodeList children = tree.getChildNodes();

			pb.step();

			for (int j = 0; j < children.getLength(); j++) {
				Node nodeItem = children.item(j);

				if (nodeItem.getNodeName().equals("ImageDescriptor")) {
					Map<String, Integer> imageAttr = new HashMap<String, Integer>();

					for (int k = 0; k < imageatt.length; k++) {
						NamedNodeMap attr = nodeItem.getAttributes();
						Node attnode = attr.getNamedItem(imageatt[k]);
						imageAttr.put(imageatt[k], Integer.valueOf(attnode.getNodeValue()));
					}
					if (i == 0) {
						master = new BufferedImage(imageAttr.get("imageWidth"), imageAttr.get("imageHeight"),
								BufferedImage.TYPE_INT_ARGB);
					}
					master.getGraphics().drawImage(image, imageAttr.get("imageLeftPosition"),
							imageAttr.get("imageTopPosition"), null);
				}
			}

			String outpath = imagePath + "/" + i + ".png";
			//System.out.println("OUTPATH: " + outpath); // outpath
			ImageIO.write(master, "PNG", new File(outpath));
		}

		for (int i = 0; i < noi; i++) {
			
			pb.step();
			
			String image = imagePath + "/" + Integer.toString(i) + ".png";
			File f_in = new File(image); // leggo IMG

			Function.resize(image, image, width, height);

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
