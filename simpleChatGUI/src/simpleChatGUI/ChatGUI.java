package simpleChatGUI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;

import javax.swing.JOptionPane;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;

public class ChatGUI {

	static float scale;

	static Shell shell;
	private Text txtMessage;
	private List lstChat;
	private Text txtUsername;
	private Text txtPort;
	private Button btnConnettiti;
	private Button btnInvia;
	
	private PrintWriter out;
	private Socket socket;
	static boolean isConnected = false;
	
	public String host = "";
	public String username = "";
	public int port;

	public void createClient(Socket socket, String username) {
		this.socket = socket;
		this.username = username;
	}

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ChatGUI window = new ChatGUI();
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

		// Comment line below if you want to edit on the design page
		// Scales the window for monitors whose resolution is different than 1080p
		scale();
		
		hostDisconnectedGUImod();
		
		shell.open();
		shell.layout();

		System.out.println("Client partito");

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell(SWT.CLOSE | SWT.MIN | SWT.TITLE);
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				try {
					out.println(" ----- " + username + " ha abbandonato la chat" + " ----- ");
					socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			@Override
			public void shellActivated(ShellEvent e) {
				txtMessage.setFocus();
			}
		});
		shell.setSize(405, 627);
		shell.setText("Chat");
		
		Label lblUsername = new Label(shell, SWT.NONE);
		lblUsername.setBounds(10, 13, 56, 15);
		lblUsername.setText("Username:");
		
		txtUsername = new Text(shell, SWT.BORDER);
		txtUsername.setBounds(72, 10, 307, 21);
		
		Label lblInserireIndirizzoIp_1 = new Label(shell, SWT.NONE);
		lblInserireIndirizzoIp_1.setText("Inserire indirizzo IP o \"localhost\":");
		lblInserireIndirizzoIp_1.setBounds(10, 40, 188, 15);
		
		Combo cmbHost = new Combo(shell, SWT.NONE);
		cmbHost.setBounds(10, 61, 369, 23);
		cmbHost.setText("localhost");
		
		Label lblInserirePorta = new Label(shell, SWT.NONE);
		lblInserirePorta.setText("Porta:");
		lblInserirePorta.setBounds(262, 40, 31, 15);
		
		txtPort = new Text(shell, SWT.BORDER);
		txtPort.setText("5000");
		txtPort.setBounds(301, 37, 78, 23);
		
		btnConnettiti = new Button(shell, SWT.NONE);
		btnConnettiti.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isConnected == false) {
					if (!txtUsername.getText().isBlank() && !cmbHost.getText().isBlank() && !txtPort.getText().isBlank()) {
						username = txtUsername.getText();
						host = cmbHost.getText();
						try {
							port = Integer.parseInt(txtPort.getText());
						} catch (NumberFormatException e1) {
							port = -1;
						}
						
						if ((host.equals("localhost") || isValidIPv4(host)) && (port >= 0 && port < 65536)) {
							try {
								socket = new Socket(host, port);
								
								// Avviamo il thread per ricevere i messaggi
								ThClientMessageListener r;
	
								r = new ThClientMessageListener(socket.getInputStream(), lstChat);
								r.start();
	
								out = new PrintWriter(socket.getOutputStream(), true);
								
								isConnected = true;
								hostConnectedGUImod();
	
								lstChat.add(" ----- " + username + " si è unito alla chat!" + " ----- ");
								out.println(" ----- " + username + " si è unito alla chat!" + " ----- ");
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								JOptionPane.showMessageDialog(null, "Errore di connessione", "Errore", 0);
							}
						} else {
							JOptionPane.showMessageDialog(null, "Parametri non validi", "Errore", 0);
						}
					} else {
						JOptionPane.showMessageDialog(null, "Compilare tutti i campi", "Errore", 2);
					}
				} else {
					isConnected = false;
					hostDisconnectedGUImod();
					
					out.println(" ----- " + username + " ha abbandonato la chat" + " ----- ");
					try {
						socket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		btnConnettiti.setBounds(10, 90, 369, 34);
		btnConnettiti.setText("Connettiti");
		
		Label label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 130, 369, 2);

		lstChat = new List(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		lstChat.setBounds(10, 138, 369, 400);

		txtMessage = new Text(shell, SWT.BORDER);
		txtMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) {
					if (!txtMessage.getText().isBlank()) {
						String message = username + ": " + txtMessage.getText().trim();
						out.println(message);
						lstChat.add(message);
						
						// Scroll so the last item is visible
						lstChat.setTopIndex(lstChat.getItemCount() - 1);
						
						txtMessage.setText("");
					}
				}
			}
		});
		txtMessage.setBounds(10, 544, 283, 34);

		btnInvia = new Button(shell, SWT.NONE);
		btnInvia.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!txtMessage.getText().isBlank()) {
					String message = username + ": " + txtMessage.getText().trim();
					out.println(message);
					lstChat.add(message);
					
					// Scroll so the last item is visible
					lstChat.setTopIndex(lstChat.getItemCount() - 1);
					
					txtMessage.setText("");
					txtMessage.setFocus();
				}
			}
		});
		btnInvia.setBounds(299, 544, 80, 34);
		btnInvia.setText("Invia");

	}
	
	public void hostConnectedGUImod() {
		btnConnettiti.setText("Disconnettiti");
		lstChat.setEnabled(true);
		txtMessage.setEnabled(true);
		btnInvia.setEnabled(true);
	}

	public void hostDisconnectedGUImod() {
		btnConnettiti.setText("Connettiti");
		lstChat.setEnabled(false);
		lstChat.removeAll();
		txtMessage.setEnabled(false);
		btnInvia.setEnabled(false);
	}
	
	public static boolean isValidIPv4(String ip) {
		try {
			// Try to resolve the address (if it’s not valid, it will throw an exception)
			InetAddress address = InetAddress.getByName(ip);

			// Check if the address is an instance of Inet4Address
			return address instanceof Inet4Address;
		} catch (UnknownHostException e) {
			// This exception means the address is invalid
			return false;
		}
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
		scaleRecursive(shell);

		Rectangle r = shell.getBounds();
		shell.setBounds(s(r.x), s(r.y), s(r.width), s(r.height));
	}
}
