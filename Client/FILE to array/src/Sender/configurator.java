package Sender;
import java.io.*;
import java.net.*;
import java.util.Random;

public class configurator {

	Socket socketclient = null;
	int porta = 5006;
	// String ip = "10.0.0.1";
	String ip = "10.3.141.1"; // ip su CASA

	DataInputStream in;
	DataOutputStream out;

	BufferedReader tastiera;

	public static void main(String[] args) {
		// ciclalo

		configurator c = new configurator();
		c.connetti(); // creo connessione
		c.invia_vettori(); // invio vettori
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {

		}
	}

	public Socket connetti() {
		try {
			System.out.println("[1] - Provo a connettermi...");
			Socket socketclient = new Socket(ip, porta);
			System.out.println("[2] - Connesso - " + ip + " - " + porta);
			in = new DataInputStream(socketclient.getInputStream());
			out = new DataOutputStream(socketclient.getOutputStream());
		} catch (Exception e) {
			System.err.println("ERRORE CLIENT");
		}
		return socketclient;
	}

	public void invia_vettori() {
		try {
			System.out.println("[4] - Invio vettori al server...");

			// invio vettore random di colori (TIPO: IMG)
			String vett = "2,1,7,7";
			out.write(vett.getBytes("utf-8"));
			// --------------------------


			System.out.println("[5] - Attesa risposta...");
			String ricevuta = in.readLine();
			System.out.println("[6] - Echo server: " + ricevuta);

			String back[] = ricevuta.split(",");
			for (int i = 0; i < back.length; i++) {
				System.out.print(back[i] + " ");
			}
			System.out.println();
			System.out.println("[6] - Echo server: " + back.length);

		} catch (Exception e) {
			System.err.println("ERRORE CLIENT [invia_vettori]");
		}

	}

}
