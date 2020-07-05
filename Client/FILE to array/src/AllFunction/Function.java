package AllFunction;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;

import org.w3c.dom.Node;

import Main.Start;

public class Function {

	private static short led_pannello_riga = Start.getLed_pannello_riga();
	private static short led_pannello_colonna = Start.getLed_pannello_colonna();
	private static int width = Start.getLed_pannello_riga()*Start.getNum_pannelli_riga();
	private static int height = Start.getLed_pannello_colonna()*Start.getNum_pannelli_colonna();
	private static String file_input = Start.getFile_input();

	private static int n_per_frame = (width * height) / (led_pannello_riga * led_pannello_colonna);

	// FUNZIONI CHE FANNO RESIZE
	/**
	 * La funzione effettua resize dell'immagine fornita tramite passaggio di
	 * parametri di scala su entrambe le dimensioni (altezza/larghezza)
	 **/
	protected static void resize(String inputImagePath, String outputImagePath, int scaledWidth, int scaledHeight)
			throws IOException {
		// reads input image
		File inputFile = new File(inputImagePath);
		BufferedImage inputImage = ImageIO.read(inputFile);

		// creates output image
		BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());

		// scales the input image to the output image
		Graphics2D g2d = outputImage.createGraphics();
		g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
		g2d.dispose();

		// extracts extension of output file
		String formatName = outputImagePath.substring(outputImagePath.lastIndexOf(".") + 1);

		// writes to output file
		ImageIO.write(outputImage, formatName, new File(outputImagePath));

		//System.out.println("IMAGE resized " + outputImagePath);
	}

	// INT from COLOR
	/**
	 * La funzione restituisce un unico intero partendo dalla composizione RGB dello
	 * stesso
	 **/
	protected static int getIntFromColor(int Red, int Green, int Blue) {
		Red = (Red << 16) & 0x00FF0000; // Shift red 16-bits and mask out other stuff
		Green = (Green << 8) & 0x0000FF00; // Shift Green 8-bits and mask out other stuff
		Blue = Blue & 0x000000FF; // Mask out anything not blue.

		// log.debug("getIntFromColor (RGB: " + Red + " - " + Green + " - " + Blue +
		// ")");

		return Red | Green | Blue; // Bitwise OR everything together.
	}

	/**
	 * La funzione restituisce uno short che rappresenta la frequenza con cui
	 * eseguire la gif
	 **/
	protected static short getGifFramerate() throws IOException {
		File file = new File(file_input);
		ArrayList<Float> wait_frame = new ArrayList<Float>(); // vettore di attese tra frame

		// Get GIF reader
		ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
		// Give it the stream to decode from
		reader.setInput(ImageIO.createImageInputStream(file));

		int numImages = reader.getNumImages(true);

		// Get 'metaFormatName'. Need first frame for that.
		IIOMetadata imageMetaData = reader.getImageMetadata(0);
		String metaFormatName = imageMetaData.getNativeMetadataFormatName();

		// Prepare streams for image encoding
		for (int i = 0; i < numImages; i++) {
			// Get input metadata
			IIOMetadataNode root = (IIOMetadataNode) reader.getImageMetadata(i).getAsTree(metaFormatName);

			// Find GraphicControlExtension node
			int nNodes = root.getLength();
			for (int j = 0; j < nNodes; j++) {
				Node node = root.item(j);
				if (node.getNodeName().equalsIgnoreCase("GraphicControlExtension")) {
					// Get delay value
					String delay = ((IIOMetadataNode) node).getAttribute("delayTime");
					wait_frame.add(Float.parseFloat(delay));
					// log.debug("DELAY: " + delay + " 100/s");
				}
			}
		}

		// somma wait_frame
		float sum = 0;
		for (int i = 0; i < wait_frame.size(); i++) {
			sum = sum + wait_frame.get(i) / 100;
		}

		short delay = (short) (wait_frame.size() / sum);
		System.out.println("FRAMERATE: " + delay);
		if (delay!=0)
			return delay; // abbastanza corretto pur essendo uan media
		else return 15;
	}

	/**
	 * La funzione visualizza l'arraylist di sottomatrici associate a ciascun frame
	 **/
	protected static void visualizza_MATRIX_x4() throws IOException {
		File file = new File(Paths.get("").toAbsolutePath().toString() + File.separator + "Output" + File.separator
				+ "matrix_x4.txt");
		FileWriter fw = new FileWriter(file);

		for (int i = 0; i < Start.matrix_x4.size(); i++) {
			Integer app[][] = Start.matrix_x4.get(i);
			if (i % n_per_frame == 0) {
				int frame = i / n_per_frame + 1;
				System.err.println("FRAME: " + frame);
				fw.write("FRAME: " + frame + "\r\n");
			}
			for (int y = 0; y < app.length; y++) {
				for (int x = 0; x < app.length; x++) {
					System.out.print(app[x][y] + " ");
					fw.write(app[x][y] + " ");
				}
				fw.write("\r\n");
				System.out.println();
			}
			fw.write("\r\n");
			System.out.println();
		}

		fw.flush();
		fw.close();

		System.out.println("IMG NUMBER: " + Start.matrix_x4.size() / n_per_frame);
		System.out.println("SIZE: " + Start.matrix_x4.size());
		System.out.println("X: " + Start.matrix_x4.get(0).length);
		System.out.println();
	}

	/**
	 * La funzione visualizza l'arraylist associata a ciascun frame dell'intera
	 * matrice
	 **/
	public static void visualizza_VECTOR_DI_MATRIX() throws IOException {
		File file = new File(Paths.get("").toAbsolutePath().toString() + File.separator + "Output" + File.separator
				+ "vector_di_matrix.txt");
		FileWriter fw = new FileWriter(file);

		for (int i = 0; i < Start.array_di_matrix.size(); i++) {
			Integer app[][] = Start.array_di_matrix.get(i);
			if (i % n_per_frame == 0) {
				int frame = i / n_per_frame + 1;
				System.err.println("FRAME: " + frame);
				fw.write("FRAME: " + frame + "\r\n");
			}
			// System.out.println(app.length);
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					System.out.print(app[x][y] + ",");
					fw.write(app[x][y] + ",");
				}
			}
		}
		fw.flush();
		fw.close();
	}
}
