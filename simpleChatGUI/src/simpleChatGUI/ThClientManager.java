package simpleChatGUI;

import java.io.*;
import java.net.*;

public class ThClientManager extends Thread {
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;

	public ThClientManager(Socket s) {
		this.socket = s;
	}

	@Override
	public void run() {
		try {
			String msg;
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// Reads incoming messages from this client
			while ((msg = in.readLine()) != null) {
				// Sends the message to the GUI server
				String finalMsg = msg;
				ServerGUI.display.asyncExec(() -> {
					if (!ServerGUI.lstChat.isDisposed()) {
						ServerGUI.lstChat.add(finalMsg);
						// Scroll so the last item is visible
						ServerGUI.lstChat.setTopIndex(ServerGUI.lstChat.getItemCount() - 1);
					}
				});
				
				// Sends the message to all clients in the Server's ArrayList except itself
				for (Socket attuale : ThServer.elenco) {
					if (attuale != socket && attuale != null) {
						out = new PrintWriter(attuale.getOutputStream(), true);
						out.println(msg);
					}
				}
			}
		} catch (IOException e) {
		}
		System.out.println("Client disconnesso.");
		ThServer.elenco.remove(socket);
		ServerGUI.nHosts--;
		ServerGUI.display.asyncExec(() -> {
			ServerGUI.refreshHostsCount();
		});
	}
}