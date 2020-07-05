package Sender;

import java.io.*;
import java.net.*;
import java.util.Random;

public class client {

	Socket socketclient = null;
	int porta = 5005;
	// String ip = "10.0.0.1";
	String ip = "10.3.141.1"; // ip su CASA
	short x = 7, y = 14;

	DataInputStream in;
	DataOutputStream out;

	BufferedReader tastiera;

	// SAMPLE
	// ----------------------------------------------------------------------------------------
	String a14x7_v2 = "0,3787281,3787281,3787281,3787281,3787281,3787281,3787281,3787281,3787281,3787281,16712194,16712194,3787281,3787281,3787281,16712194,3787281,16712194,3787281,3787281,3787281,3787281,3787281,3787281,16712194,3787281,3787281,3787281,3787281,3787281,3787281,16712194,3787281,3787281,3787281,3787281,3787281,16712194,16712194,16712194,3787281,3787281,3787281,3787281,3787281,3787281,3787281,3787281,16645629,16645629,1187529,1187529,1187529,1187529,1187529,1187529,1187529,16712194,16712194,16712194,16712194,1187529,1187529,1187529,1187529,1187529,1187529,1187529,16712194,1187529,1187529,16712194,16712194,16712194,16712194,1187529,1187529,1187529,1187529,16712194,1187529,1187529,1187529,1187529,1187529,16712194,16712194,16712194,16712194,1187529,1187529,1187529,1187529,1187529,1187529,1187529,1187529,1187529";
	String a14x7 = "0,16769539,16769539,16769539,16769539,16769539,16769539,16769539,650231,650231,7408375,7408375,7408375,7408375,7408375,7408375,16777215,16777215,16777215,7408375,650231,650231,210431,7408375,7408375,16777215,16777215,16777215,7408375,7408375,16777215,16777215,16777215,131331,7408375,7408375,261899,7408375,131331,16777215,16777215,16777215,7408375,7408375,7408375,7408375,7408375,7408375,7408375,7408375,16712451,16712451,16712451,16712451,16712451,16712451,16712451,7408375,7408375,7408375,7408375,7408375,7408375,7408375,7408375,7408375,131331,16777215,16777215,16777215,7408375,7408375,16777215,16777215,16777215,7408375,7408375,210431,650231,650231,7408375,16777215,16777215,16777215,7408375,7408375,16777215,16777215,16777215,7408375,650231,261899,650231,650231,7408375,7408375,7408375,7408375,7408375";
	// -----------------------------------------------------------------------------------------------

	static String s;
	
	public static void main(String[] args) throws IOException {
		// leggo da SENDER (ricorda di sistemare il TXT per la formattazione in lettura)
		
		FileReader f;
		f = new FileReader("./SENDER/FILE_funzionante.txt");
		BufferedReader b;
		b = new BufferedReader(f);
		s = b.readLine();
		System.out.println("S: " + s);
		// ---------------

		client c = new client();
		c.connetti(); // creo connessione
		c.invia_vettori(); // invio vettori

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

			String vett = s; // associo il FILE al vettore di invio

			System.err.println("last at 2 (first vett number): " + vett.charAt(2));
			System.err.println("last at " + (vett.length() - 1) + ": " + vett.charAt(vett.length() - 1));
			out.write(vett.getBytes("utf-8"));

			Thread.sleep(100);
			System.err.println("send END");
			out.write("END".getBytes("utf-8"));

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
