import java.awt.EventQueue;
import java.text.ParseException;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.*;
import java.net.*;

import org.jitsi.hammer.utils.*;

public class ConnectGUI {

	private JFrame frmTest;
	private JTextField XMPPdomain;
	private JTextField MUCdomain;
	private JTextField roomName;
	private JFormattedTextField XMPPhost;
	private JFormattedTextField port;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConnectGUI window = new ConnectGUI();
					window.frmTest.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ConnectGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTest = new JFrame();
		frmTest.setTitle("Test Harness GUI");
		frmTest.setBounds(100, 100, 450, 300);
		frmTest.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTest.getContentPane().setLayout(null);

		try {
			JLabel lblNewLabel = new JLabel(" XMPP domain");
			lblNewLabel.setBounds(22, 46, 94, 16);
			frmTest.getContentPane().add(lblNewLabel);

			XMPPdomain = new JTextField();
			XMPPdomain.setBounds(22, 62, 130, 26);
			frmTest.getContentPane().add(XMPPdomain);
			XMPPdomain.setColumns(10);

			JLabel lblXmppHost = new JLabel(" XMPP host");
			lblXmppHost.setBounds(22, 100, 94, 16);
			frmTest.getContentPane().add(lblXmppHost);

			MaskFormatter formatter = new MaskFormatter("###.###.###.###");
			JFormattedTextField XMPPhost = new JFormattedTextField(formatter);
			XMPPhost.setText("52.88.34.121");
			XMPPhost.setBounds(22, 114, 130, 26);
			frmTest.getContentPane().add(XMPPhost);

			JLabel lblMucDomain = new JLabel(" MUC domain");
			lblMucDomain.setBounds(22, 152, 94, 16);
			frmTest.getContentPane().add(lblMucDomain);

			MUCdomain = new JTextField();
			MUCdomain.setBounds(22, 169, 130, 26);
			frmTest.getContentPane().add(MUCdomain);
			MUCdomain.setColumns(10);

			JSeparator separator = new JSeparator();
			separator.setForeground(UIManager.getColor("EditorPane.inactiveForeground"));
			separator.setOrientation(SwingConstants.VERTICAL);
			separator.setBounds(164, 6, 12, 239);
			frmTest.getContentPane().add(separator);

			JLabel lblChatRoomName = new JLabel(" Room Name");
			lblChatRoomName.setBounds(183, 25, 94, 16);
			frmTest.getContentPane().add(lblChatRoomName);

			roomName = new JTextField();
			roomName.setText("TestHammer");
			roomName.setBounds(183, 41, 94, 26);
			frmTest.getContentPane().add(roomName);
			roomName.setColumns(10);

			JLabel lblPort = new JLabel(" Port");
			lblPort.setBounds(327, 25, 94, 16);
			frmTest.getContentPane().add(lblPort);

			MaskFormatter portFormatter = new MaskFormatter("#####");
			JFormattedTextField port = new JFormattedTextField(portFormatter);
			port.setText("5222");
			port.setBounds(327, 41, 94, 26);
			frmTest.getContentPane().add(port);

			JLabel lblFakeUsers = new JLabel(" Fake Users");
			lblFakeUsers.setBounds(183, 79, 94, 16);
			frmTest.getContentPane().add(lblFakeUsers);

			SpinnerModel sm = new SpinnerNumberModel(1, 1, null, 1); // default
																		// value,lower
																		// bound,upper
																		// bound,increment
																		// by
			JSpinner numberOfFakeUsers = new JSpinner(sm);
			numberOfFakeUsers.setBounds(183, 95, 94, 26);
			frmTest.getContentPane().add(numberOfFakeUsers);

			JLabel lblTestLength = new JLabel(" Duration(sec.)");
			lblTestLength.setBounds(326, 79, 95, 16);
			frmTest.getContentPane().add(lblTestLength);

			JFormattedTextField length = new JFormattedTextField();
			length.setText("0");
			length.setBounds(326, 95, 95, 26);
			frmTest.getContentPane().add(length);

			JTextArea txtrTest = new JTextArea();
			txtrTest.setLineWrap(true);
			txtrTest.setBounds(183, 152, 238, 79);
			frmTest.getContentPane().add(txtrTest);

			JButton btnConnect = new JButton("CONNECT");
			btnConnect.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					txtrTest.setText(
							"-XMPPdomain " + XMPPdomain.getText() + " -XMPPhost " + XMPPhost.getText() + " -MUCdomain "
									+ MUCdomain.getText() + " -room " + roomName.getText() + " -port " + port.getText()
									+ " -users " + numberOfFakeUsers.getValue() + " -length " + length.getText());

					try {
						processInformation();
					} catch (UnknownHostException ex) {
						ex.printStackTrace();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			});
			btnConnect.setBounds(326, 243, 117, 29);
			frmTest.getContentPane().add(btnConnect);

			JButton btnReset = new JButton("RESET");
			btnReset.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					XMPPdomain.setText("");
					XMPPhost.setText("");
					MUCdomain.setText("");
					roomName.setText("TestHammer");
					port.setText("5222");
					numberOfFakeUsers.setValue(1);
					length.setText("0");
					txtrTest.setText("");
				}
			});
			btnReset.setBounds(214, 243, 117, 29);
			frmTest.getContentPane().add(btnReset);

			JButton btnGenerateReport = new JButton("Generate Report");
			btnGenerateReport.setBounds(22, 243, 130, 29);
			frmTest.getContentPane().add(btnGenerateReport);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		frmTest.setVisible(true);
	}

	public void processInformation() throws UnknownHostException, IOException {
		Socket s = new Socket(XMPPhost.getText(), Integer.parseInt(port.getText()));
		ObjectOutputStream p = new ObjectOutputStream(s.getOutputStream());

		p.writeObject(null);
		p.flush();

		// get the details from server

	}
}
