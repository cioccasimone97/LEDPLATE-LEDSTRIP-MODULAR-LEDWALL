package Elements;

import java.util.Arrays;

/**
 * This class represents the elementary unit of a screen. Every screen is
 * composed of one or more of this objects. A "led plate" is composed of a led
 * strip that starts and ends with a characteristic led address. A plate has a
 * precise place in the screen.
 */
public class Plate {

	// attributes
	private String id_plate;
	int position;
	int x, y; // row and column
	int led_number; // number of leds in the plate
	int init_address, end_address;
	int[] ledStrip;
	int[] ledStripAdress;
	Integer[][] sottomatrice; // intesa come plate, i nomi in inglese mi incasinano
	boolean pari; // mi serve per distinguere i due diversi tipi di popolamento dei plate
	// volendo lo si potrebbe anche ricavare senza creare questo nuovo parametro ma
	// non avevo voglia
	// contructor

	/**
	 * 
	 * @param position is referred to the number of the plate in the screen
	 *                 structure.
	 * @param x        raw of the led panel
	 * @param y        column of the led panel
	 * 
	 *                 The constructor initializes the id_plate, which is created
	 *                 with a "P" letter + position + x*y. For example: a Plate in
	 *                 the position 4 composed of 7x7 leds would be: P449 (so the
	 *                 last digits also indicate the total number in a plate).
	 * 
	 *                 With these parameters is possible to populate the array
	 *                 ledStrip that represents the led strip.
	 */
	public Plate(int position, int x, int y, boolean pari) {
		this.id_plate = "P" + position + (y * x); // this is an invented equation to have a unique id for every plate
		this.position = position;
		this.x = x;
		this.y = y;
		this.pari = pari;
		led_number = x * y;
		init_address = x * y * (position - 1); // initial address of the plate
		end_address = init_address + led_number - 1; // address of the led which the plate end with
		ledStrip = new int[led_number]; // it contains a number from 0 to 255 that indicates the color the led would
										// have
		ledStripAdress = new int[led_number];
		this.sottomatrice = generaSottomatrice(position); // non ho cambiato nulla se non aggiunto questo metodo
	}

	private Integer[][] generaSottomatrice(int posizione) { // posizione arriva da Screen che man mano genera Plate e
															// gli assegna la posizione
		sottomatrice = new Integer[x][y];
		short count = 0;
		short j = 0;
		short i = 0;
		if (this.pari) { // se il plate si trova su una riga pari eseguo il solito algoritmo
			for (i = 0; i < y; i++) {
				if (i % 2 == 0) {
					for (j = 0; j < x; j++) {
						sottomatrice[j][i] = posizione * x * y + count;
						count++;
					}
				} else {
					for (j = (short) (x - 1); j >= 0; j--) {
						sottomatrice[j][i] = posizione * x * y + count;
						count++;
					}
				}
			}
			return (sottomatrice);
		} else { // se il plate si trova in una riga dispari la matrice viene popolata grazie a
					// questo sofisticato algoritmo frutto di disegnini e tentativi a caso
			for (i = 0; i < y; i++) {
				if (i % 2 == 0) {
					for (j = 0; j < x; j++) {
						sottomatrice[x - 1 - j][y - 1 - i] = posizione * x * y + count; // non cambia molto rispetto
																						// all'altro
						count++;
					}
				} else {
					for (j = (short) (x - 1); j >= 0; j--) {
						sottomatrice[x - 1 - j][y - 1 - i] = posizione * x * y + count;
						count++;
					}
				}
			}
			return (sottomatrice);
		}
	}

	// methods
	/**
	 * Example: P34, Position=3 2x2, init_address=8, end_address=11, ledStrip=[0, 0,
	 * 0, 0]
	 */
	@Override
	public String toString() {
		return id_plate + ", Position=" + position + ", " + x + "x" + y + ", init_address=" + init_address
				+ ", end_address=" + end_address + ",\nledStrip=" + Arrays.toString(ledStrip);
	}
}
