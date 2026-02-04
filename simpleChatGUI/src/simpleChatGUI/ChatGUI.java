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

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class ChatGUI {

	static float scale;

	static Shell shell;
	private Text txtMessage;
	private PrintWriter out;
	private String username;
	private Socket socket;
	private List lstChat;

	public void createClient (Socket socket, String username) {
		this.socket = socket;
		this.username = username;
	}
	
	/**
	 * Launch the application.
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
		scale();
				
		shell.open();
		shell.layout();
		
		System.out.println("Client partito");
		
		// Avviamo il thread per ricevere i messaggi
		ThClientMessageListener r;
		try {
			
			r = new ThClientMessageListener(socket.getInputStream(), lstChat);
			r.start();
			
			out = new PrintWriter(socket.getOutputStream(), true);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		lstChat.add(" ----- " + username + " si è unito alla chat!" + " ----- ");
		out.println(" ----- " + username + " si è unito alla chat!" + " ----- ");
		
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
				ChatConnectionGUI.shell.setVisible(true);
				ChatConnectionGUI.shell.setActive();

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
		shell.setSize(405, 493);
		shell.setText(username);
		
		lstChat = new List(shell, SWT.BORDER | SWT.V_SCROLL);
		lstChat.setBounds(10, 10, 369, 394);
		
		txtMessage = new Text(shell, SWT.BORDER);
		txtMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) {
					if(!txtMessage.getText().isBlank()) {
						String message = username + ": " + txtMessage.getText().trim();
						out.println(message);
						lstChat.add(message);
						txtMessage.setText("");
					}
		        }
			}
		});
		txtMessage.setBounds(10, 410, 283, 34);
		
		Button btnInvia = new Button(shell, SWT.NONE);
		btnInvia.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!txtMessage.getText().isBlank()) {
					String message = username + ": " + txtMessage.getText().trim();
					out.println(message);
					lstChat.add(message);
					txtMessage.setText("");
					txtMessage.setFocus();
				}
			}
		});
		btnInvia.setBounds(299, 410, 80, 34);
		btnInvia.setText("Invia");
		
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
