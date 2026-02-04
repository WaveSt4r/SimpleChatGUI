package simpleChatGUI;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	public static ArrayList<Socket> elenco;

	public static void main(String[] args) {
		elenco = new ArrayList<Socket>();

		System.out.println("Server partito");

		try {
			ServerSocket server = new ServerSocket(5000);
			if(server.equals(server)) {}					// Toglie il warning
			System.out.println("Server in ascolto...");

			while (true) {
				Socket s = server.accept();
				System.out.println("Client connesso.");
				ClientManager gc = new ClientManager(s);
				elenco.add(s);
				gc.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
