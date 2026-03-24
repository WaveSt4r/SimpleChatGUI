package simpleChatGUI;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ThServer extends Thread {
	public static ArrayList<Socket> elenco;
	public static ServerSocket server;
	private int port;

	public ThServer(int port) {
		this.port = port;
	}

	public void run() {
		elenco = new ArrayList<Socket>();

		System.out.println("Server partito");

		try {
			server = new ServerSocket(port);
			System.out.println("Server in ascolto...");

			while (ServerGUI.serverIsOn) {
				Socket s = server.accept();
				System.out.println("Client connesso.");
				ThClientManager gc = new ThClientManager(s);
				elenco.add(s);
				
				ServerGUI.nHosts++;
				ServerGUI.display.asyncExec(() -> {
					ServerGUI.refreshHostsCount();
				});
				
				gc.start();
			}
		} catch (IOException e) {
		}
	}

	public static void stopServer() {
		try {
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
