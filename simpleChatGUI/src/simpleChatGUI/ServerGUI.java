package simpleChatGUI;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import java.net.*;

public class ServerGUI {

	protected Shell shlServer;
	private Text txtMessage;
	//private PrintWriter out;
	//private BufferedReader in;
	
	private ServerSocket serverSocket;
	
	public static Socket elenco[];
	public static final int MAX = 10;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ServerGUI window = new ServerGUI();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlServer.open();
		shlServer.layout();
		
		System.out.println("Server partito");
		try {
			elenco = new Socket[MAX];
			int n = 0;
			
			serverSocket = new ServerSocket(5000);
			System.out.println("Server in ascolto...");
			
			while (true) {
				Socket s = serverSocket.accept();

				if (n < MAX) {
					ClientManager gc = new ClientManager(s);
					elenco[n] = s;
					gc.start();
					n++;
				} else {
					System.out.println("Server in FULL...");
				}
				
				if(!shlServer.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				} else {
					break;
				}
			}
		} catch (Exception e) {
			
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlServer = new Shell();
		shlServer.setSize(405, 493);
		shlServer.setText("Server");
		
		List lstChat = new List(shlServer, SWT.BORDER);
		lstChat.setBounds(10, 10, 369, 394);
		
		txtMessage = new Text(shlServer, SWT.BORDER);
		txtMessage.setBounds(10, 410, 283, 34);
		
		Button btnInvia = new Button(shlServer, SWT.NONE);
		btnInvia.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!txtMessage.getText().isBlank()) {
					//String message = username + ": " + txtMessage.getText();
					//out.println(message);
					//lstChat.add(message);
				}
			}
		});
		btnInvia.setBounds(299, 410, 80, 34);
		btnInvia.setText("Invia");

	}
}
