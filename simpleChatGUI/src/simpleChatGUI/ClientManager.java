package simpleChatGUI;

import java.io.*;
import java.net.*;

public class ClientManager extends Thread {
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;

	public ClientManager(Socket s) {
		this.socket = s;
	}

	@Override
	public void run() {
		try {
			String mess;
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// Legge i messaggi in arrivo da questo client
			while ((mess = in.readLine()) != null) {
				// Invia a tutti i client nell'ArrayList del Server tranne se stesso
				for (Socket attuale : Server.elenco) {
					if (attuale!=socket && attuale != null) {
						out = new PrintWriter(attuale.getOutputStream(), true);
						out.println(mess);
					}
				}
			}
		} catch (IOException e) {
		}
		System.out.println("Client disconnesso.");
	}
}