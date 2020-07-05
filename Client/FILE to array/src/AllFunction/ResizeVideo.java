package AllFunction;

import java.io.File;

import Main.Start;
import ws.schild.jave.*;

public class ResizeVideo {

	private static int width = Start.getLed_pannello_riga()*Start.getNum_pannelli_riga();
	private static int height = Start.getLed_pannello_colonna()*Start.getNum_pannelli_colonna();

	public static void without_AUDIO(File src, File target) {
		MultimediaObject source = new MultimediaObject(src);

		long inizio = System.currentTimeMillis();

		System.out.println("[without_AUDIO] - JOB STARTED");

		VideoAttributes video = new VideoAttributes();
		video.setCodec("mpeg4"); // CODEC

		try {
			video.setBitRate(new Integer(source.getInfo().getVideo().getBitRate()));
		} catch (EncoderException e) {
			System.err.println(e);
		}

		try {
			video.setFrameRate((int) source.getInfo().getVideo().getFrameRate());
		} catch (EncoderException e) {
			System.err.println(e);
		}

		if (width % 2 != 0 || height % 2 != 0) {
			System.out.println("moltiplico *2 perche' dispari");
			// video.setSize(new VideoSize(width * 2, height * 2)); // mi da problemi con
			// banda nera a dx
			video.setSize(new VideoSize(width * 20, height * 20)); // FUNZIONANTE SENZA PROBLEMI (segna riduzione di
																	// slicer thread)
		} else {
			video.setSize(new VideoSize(width, height));
		}
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("mp4");
		attrs.setVideoAttributes(video);
		Encoder encoder = new Encoder();

		try {
			encoder.encode(source, target, attrs);
		} catch (IllegalArgumentException | EncoderException e) {
			System.err.println(e);
		}

		long fine = System.currentTimeMillis();
		System.out.println("[without_AUDIO] - JOB COMPLETED in " + (fine - inizio) / 1000 + "s");
	}
}
