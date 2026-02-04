package simpleChatGUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;

//Classe separata per ascoltare il server senza bloccare la tastiera
public class ThClientMessageListener extends Thread {
	private BufferedReader in;
	private List lstChat;
	private Display display;

	public ThClientMessageListener(InputStream in, List lstChat) {
		this.in = new BufferedReader(new InputStreamReader(in));
		this.lstChat = lstChat;
		this.display = lstChat.getDisplay();
	}

	@Override
	public void run() {
		try {
			String s;
			while ((s = in.readLine()) != null) {
				System.out.println(s);
				String finalMsg = s;
				
				display.asyncExec(() -> {
				    if (!lstChat.isDisposed()) {
				    	lstChat.add(finalMsg);
				    }
				});
			}
		} catch (IOException e) {
			System.out.println("Connessione interrotta.");
		}
	}
}