package simpleChatGUI;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;

import javax.swing.JOptionPane;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class ServerGUI {
	
	static float scale;
	
	private Thread server;

	static Display display;
	protected static Shell shlServer;
	private Text txtMessage;
	static List lstChat;
	private Text txtPort;
	private Label lblServerStatus;
	private Button btnToggle;
	private Button btnInvia;
	static Label lblNumberConnectedHosts;

	static int port;
	static boolean serverIsOn = false;
	static int nHosts = 0;

	/**
	 * Launch the application.
	 * 
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
		display = Display.getDefault();
		createContents();
		
		// Comment line below if you want to edit on the design page
		// Scales the window for monitors whose resolution is different than 1080p
		scale();
		
		serverStopGUImod();
		
		shlServer.open();
		shlServer.layout();

		while (!shlServer.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlServer = new Shell(SWT.CLOSE | SWT.MIN | SWT.TITLE);
		shlServer.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				ThServer.stopServer();
				try {
					server.join();
					System.out.println("Server spento");
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			@Override
			public void shellActivated(ShellEvent e) {
				txtMessage.setFocus();
			}
		});
		shlServer.setSize(537, 493);
		shlServer.setText("Server");

		lstChat = new List(shlServer, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		lstChat.setBounds(10, 39, 369, 365);

		txtMessage = new Text(shlServer, SWT.BORDER);
		txtMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) {
					try {
						if (!txtMessage.getText().isBlank()) {
							String message = "Server: " + txtMessage.getText().trim();
							lstChat.add(message);
							for (Socket attuale : ThServer.elenco) {
								if (attuale != null) {
									PrintWriter out = new PrintWriter(attuale.getOutputStream(), true);
									out.println(message);
								}
							}
							
							// Scroll so the last item is visible
							lstChat.setTopIndex(ServerGUI.lstChat.getItemCount() - 1);
							
							txtMessage.setText("");
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		txtMessage.setBounds(10, 410, 283, 34);

		btnInvia = new Button(shlServer, SWT.NONE);
		btnInvia.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					if (!txtMessage.getText().isBlank()) {
						String message = "Server: " + txtMessage.getText().trim();
						lstChat.add(message);
						for (Socket attuale : ThServer.elenco) {
							if (attuale != null) {
								PrintWriter out = new PrintWriter(attuale.getOutputStream(), true);
								out.println(message);
							}
						}
						// Scroll so the last item is visible
						lstChat.setTopIndex(lstChat.getItemCount() - 1);
						
						txtMessage.setText("");
						txtMessage.setFocus();
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnInvia.setBounds(299, 410, 80, 34);
		btnInvia.setText("Invia");

		Label labelServerStatus = new Label(shlServer, SWT.NONE);
		labelServerStatus.setBounds(385, 66, 69, 15);
		labelServerStatus.setText("Server status:");

		lblServerStatus = new Label(shlServer, SWT.NONE);
		lblServerStatus.setBounds(460, 66, 51, 15);
		lblServerStatus.setText("Offline");

		txtPort = new Text(shlServer, SWT.BORDER);
		txtPort.setBounds(423, 39, 88, 21);
		txtPort.setText("5000");

		Label lblPorta = new Label(shlServer, SWT.NONE);
		lblPorta.setBounds(385, 42, 33, 15);
		lblPorta.setText("Porta:");

		btnToggle = new Button(shlServer, SWT.NONE);
		btnToggle.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (serverIsOn == false) {
					if (!txtPort.getText().isBlank()) {
						port = Integer.parseInt(txtPort.getText());
						server = new ThServer(port);
						server.start();
						serverIsOn = true;
						serverStartGUImod();
					} else {
						JOptionPane.showMessageDialog(null, "Compilare tutti i campi", "Errore", 2);
					}
				} else {
					serverIsOn = false;
					ThServer.stopServer();
					try {
						server.join();
						System.out.println("Server spento");
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					serverStopGUImod();
				}
			}
		});
		btnToggle.setBounds(385, 87, 126, 34);
		btnToggle.setText("Start");

		Label lblHostConnected = new Label(shlServer, SWT.NONE);
		lblHostConnected.setBounds(385, 214, 87, 15);
		lblHostConnected.setText("Host connected:");

		lblNumberConnectedHosts = new Label(shlServer, SWT.NONE);
		lblNumberConnectedHosts.setBounds(478, 214, 33, 15);
		lblNumberConnectedHosts.setText("0");

		Label lblStats = new Label(shlServer, SWT.NONE);
		lblStats.setAlignment(SWT.CENTER);
		lblStats.setBounds(385, 187, 126, 21);
		lblStats.setText("Stats");

		Label label = new Label(shlServer, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(385, 179, 126, 2);

		Label lblServerControlPanel = new Label(shlServer, SWT.NONE);
		lblServerControlPanel.setAlignment(SWT.CENTER);
		lblServerControlPanel.setBounds(10, 10, 501, 15);
		lblServerControlPanel.setText("Server Control Panel");

		Label label_1 = new Label(shlServer, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setBounds(10, 31, 501, 2);
	}

	public void serverStartGUImod() {
		btnToggle.setText("Stop");
		lblServerStatus.setText("Online");
		txtPort.setEnabled(false);
		lstChat.setEnabled(true);
		txtMessage.setEnabled(true);
		btnInvia.setEnabled(true);
	}

	public void serverStopGUImod() {
		btnToggle.setText("Start");
		lblServerStatus.setText("Offline");
		txtPort.setEnabled(true);
		lstChat.setEnabled(false);
		txtMessage.setEnabled(false);
		btnInvia.setEnabled(false);
	}
	
	static void refreshHostsCount () {
		lblNumberConnectedHosts.setText(""+ServerGUI.nHosts);
	}
	

	// Scales a value
	public static int s(int v) {
		return Math.round(v * scale);
	}

	// Recursive method to scale every GUI element on the shell
	public static void scaleRecursive(Composite parent) {
		for (Control c : parent.getChildren()) {
			Rectangle r = c.getBounds();
			c.setBounds(s(r.x), s(r.y), s(r.width), s(r.height));

			if (c instanceof Composite)
				scaleRecursive((Composite) c);
		}
	}

	// Method to scale everything at once so it is easy to comment and work on the
	// design page
	public static void scale() {
		Display display = Display.getDefault();
		Point dpi = display.getDPI();
		scale = dpi.x / 96f;
		scaleRecursive(shlServer);

		Rectangle r = shlServer.getBounds();
		shlServer.setBounds(s(r.x), s(r.y), s(r.width), s(r.height));
	}
}
