package simpleChatGUI;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class ChatConnectionGUI {
	
	static float scale;
	
	static Shell shell;
	private Text txtUsername;
	private Text txtPort;
	private Button btnAccedi;
	
	private Socket socket;
	
	public String host = "";
	public String username = "";
	public int port;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ChatConnectionGUI window = new ChatConnectionGUI();
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
		scale();
		
		shell.open();
		shell.layout();
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
		shell.setSize(409, 439);
		shell.setText("Chat Connection Data");
		shell.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) {
					
		        }
			}
		});

		Label lblInserireNome = new Label(shell, SWT.NONE);
		lblInserireNome.setBounds(10, 73, 94, 15);
		lblInserireNome.setText("Inserire Username");

		txtUsername = new Text(shell, SWT.BORDER);
		txtUsername.setBounds(10, 94, 373, 38);

		Label lblInserireIndirizzoIp = new Label(shell, SWT.NONE);
		lblInserireIndirizzoIp.setBounds(10, 183, 169, 15);
		lblInserireIndirizzoIp.setText("Inserire indirizzo IP o \"localhost\"");

		Combo cmbHost = new Combo(shell, SWT.NONE);
		cmbHost.setBounds(10, 204, 373, 38);
		cmbHost.setText("localhost");
		cmbHost.add("localhost");
		
		Label lblInserirePorta = new Label(shell, SWT.NONE);
		lblInserirePorta.setBounds(268, 183, 31, 15);
		lblInserirePorta.setText("Porta:");
		
		txtPort = new Text(shell, SWT.BORDER);
		txtPort.setText("5000");
		txtPort.setBounds(305, 180, 78, 23);

		btnAccedi = new Button(shell, SWT.NONE);
		btnAccedi.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!txtUsername.getText().isBlank() && !cmbHost.getText().isBlank() && !txtPort.getText().isBlank()) {
					username = txtUsername.getText();
					host = cmbHost.getText();
					try {
						port = Integer.parseInt(txtPort.getText());
					} catch (NumberFormatException e1) {
						port = -1;
					}
					if((host.equals("localhost") || isValidIPv4(host)) && (port>=0 && port<65536)) {
						try {
							socket = new Socket(host, port);
							
							ChatGUI c = new ChatGUI ();
							c.createClient(socket, username);
							
							shell.setVisible(false);
							
							c.open();
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
			}
		});
		btnAccedi.setBounds(10, 287, 373, 38);
		btnAccedi.setText("Accedi");
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
            c.setBounds(
                s(r.x),
                s(r.y),
                s(r.width),
                s(r.height)
            );

            if (c instanceof Composite)
                scaleRecursive((Composite) c);
        }
    }
	
	// Method to scale everything at once so it is easy to comment and work on the design page
	public static void scale() {
		Display display = Display.getDefault();
		Point dpi = display.getDPI();
        scale = dpi.x / 96f;
		scaleRecursive(shell);
		
		Rectangle r = shell.getBounds();
		shell.setBounds(
            s(r.x),
            s(r.y),
            s(r.width),
            s(r.height)
        );
    }
}
