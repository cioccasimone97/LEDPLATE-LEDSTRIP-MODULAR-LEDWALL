package AllFunction;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.Java2DFrameConverter;

import Main.Start;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarStyle;

public class MovieToImage {
	private static int size;

	/**
	 * La funzione ricevuto un video ne estrane e salva i suoi N frame come immagini
	 **/
	public static boolean convertMovietoJPG(String f_in, String f_resized, String imagePath, String imgType,
			int frameJump) {

		File f_res = new File(f_resized); // lo elimino alla fine

		// lancio resize del video
		ResizeVideo.without_AUDIO(new File(f_in), f_res);
		// -----------------------

		Java2DFrameConverter converter = new Java2DFrameConverter();
		FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(f_resized);
		try {
			frameGrabber.start();
		} catch (Exception e1) {
			System.err.println("framegrabber.start()");
			System.err.println(e1);
		}

		ProgressBar pb = new ProgressBar("convertMovietoJPG", frameGrabber.getLengthInFrames(), ProgressBarStyle.ASCII);

		Frame frame;
		Start.framerate = (int) Math.round(frameGrabber.getFrameRate());
		int imgNum = 0;
		System.out.println("Video has " + frameGrabber.getLengthInFrames() + " frames and has frame rate of " + Start.framerate);

		size = frameGrabber.getLengthInFrames();
		System.out.println("LENGHT: " + size);

		try {
			for (int ii = 1; ii <= frameGrabber.getLengthInFrames(); ii++) {

				pb.step();

				imgNum++;
				// System.out.println("IMG: " + imgNum);
				frameGrabber.setFrameNumber(ii);
				frame = frameGrabber.grab();
				BufferedImage bi = converter.convert(frame);
				String path = imagePath + "/" + imgNum + ".png";
				//System.out.println(path);
				ImageIO.write(bi, imgType, new File(path));
				ii += frameJump;
			}
			frameGrabber.stop();
		} catch (Exception | IOException e) {
			System.err.println(e);
		}

		pb.stop();

		boolean s = false;
		try {
			s = Video.create_imgVideo(size);
		} catch (IOException e) {
			System.err.println(e);
		}

		// distruggo VIDEO resized
		if (f_res.exists()) {
			f_res.delete();
		}

		if (s == true) {
			return true;
		} else {
			return false;
		}
	}
}