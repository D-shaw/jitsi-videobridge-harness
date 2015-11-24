package org.jitsi.hammer;

import java.awt.EventQueue;

import javax.media.Format;
import javax.media.format.AudioFormat;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import org.jitsi.hammer.Credential;
import org.jitsi.hammer.Hammer;
import org.jitsi.hammer.HostInfo;
import org.jitsi.hammer.utils.MediaDeviceChooser;
import org.jitsi.impl.neomedia.jmfext.media.protocol.greyfading.VideoGreyFadingMediaDevice;
import org.jitsi.impl.neomedia.jmfext.media.protocol.rtpdumpfile.RtpdumpMediaDevice;
import org.jitsi.service.libjitsi.LibJitsi;
import org.jitsi.service.neomedia.MediaService;
import org.jitsi.service.neomedia.codec.Constants;
import org.jitsi.service.neomedia.device.MediaDevice;
import org.jitsi.service.neomedia.format.MediaFormatFactory;
import org.jitsi.videobridge.AudioSilenceMediaDevice;

import net.java.sip.communicator.launcher.ChangeJVMFrame;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JSpinner;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.awt.event.ItemEvent;
import java.awt.Font;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class NetworkMonitor implements ItemListener, ActionListener, FocusListener {

	public static StringBuffer GUILog = new StringBuffer("");

	private JFrame frmTestHarness;
	private JTextField txtXMPPdomain;
	private JTextField txtXMPPhost;
	private JTextField txtMUCdomain;
	private JTextField txtRoomName;
	private JTextField txtPort;
	private JTextField txtLength;
	private JTextArea txtrAudioFile;
	private JTextArea txtrVideoFile;

	private JRadioButton rdbtnTcpIp;
	private JRadioButton rdbtnUdp;

	private JButton btnApply;
	private JButton btnReset;
	private JButton btnOutputLog;
	private JButton btnAssist;
	private JButton btnServerMonitor;
	private JButton AudioFileButton;
	private JButton VideoFileButton;
	private JButton btnLoginCredentials;

	private JSpinner spnFakeUsers;
	private JSpinner spnStatsPolling;
	private JSpinner spnAddInterval;

	private JCheckBox chckbxAudioStream;
	private JCheckBox chckbxVideoStream;
	private JCheckBox chckbxOverallStats;
	private JCheckBox chckbxAllStats;
	private JCheckBox chckbxSummaryStats;
	private JCheckBox chckbxNoStats;
	private JScrollPane scrollPane;
	private JTextArea txtrReserved;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		// code from jitsi-hammer
		String version = System.getProperty("java.version");
		String vmVendor = System.getProperty("java.vendor");
		String osName = System.getProperty("os.name");

		Main.setSystemProperties(osName);
		Main.setScHomeDir(osName);

		// this needs to be set before any DNS lookup is run
		File f = new File(System.getProperty(Main.PNAME_SC_HOME_DIR_LOCATION),
				System.getProperty(Main.PNAME_SC_HOME_DIR_NAME) + File.separator + ".usednsjava");
		if (f.exists()) {
			System.setProperty("sun.net.spi.nameservice.provider.1", "dns,dnsjava");
		}

		if (version.startsWith("1.4") || vmVendor.startsWith("Gnu") || vmVendor.startsWith("Free")) {
			String os = "";

			if (osName.startsWith("Mac"))
				os = ChangeJVMFrame.MAC_OSX;
			else if (osName.startsWith("Linux"))
				os = ChangeJVMFrame.LINUX;
			else if (osName.startsWith("Windows"))
				os = ChangeJVMFrame.WINDOWS;

			ChangeJVMFrame changeJVMFrame = new ChangeJVMFrame(os);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

			changeJVMFrame.setLocation(screenSize.width / 2 - changeJVMFrame.getWidth() / 2,
					screenSize.height / 2 - changeJVMFrame.getHeight() / 2);
			changeJVMFrame.setVisible(true);

			return;
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NetworkMonitor window = new NetworkMonitor();
					window.frmTestHarness.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public NetworkMonitor() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTestHarness = new JFrame();
		frmTestHarness.setResizable(false);
		frmTestHarness.setTitle("Test Harness");
		frmTestHarness.setBounds(100, 100, 700, 550);
		frmTestHarness.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frmTestHarness.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic(KeyEvent.VK_F);
		menuBar.add(mnFile);

		JMenuItem mntmNewMenuItem = new JMenuItem("Exit");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnFile.add(mntmNewMenuItem);

		JMenu mnHelp = new JMenu("Help");
		mnFile.setMnemonic(KeyEvent.VK_H);
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				About dialog = new About();
				dialog.setModal(true);
				dialog.setVisible(true);
			}
		});
		mnHelp.add(mntmAbout);

		JPanel panel = new JPanel();
		frmTestHarness.getContentPane().add(panel, BorderLayout.WEST);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 170, 0 };
		gbl_panel.rowHeights = new int[] { 50, 16, 26, 16, 26, 16, 26, 0, 0, 0, 20 };
		gbl_panel.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JLabel lblNewLabel = new JLabel("XMPP domain");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 14));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		panel.add(lblNewLabel, gbc_lblNewLabel);

		txtXMPPdomain = new JTextField();
		txtXMPPdomain.setText("52.88.34.121");
		GridBagConstraints gbc_txtXMPPdomain = new GridBagConstraints();
		gbc_txtXMPPdomain.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtXMPPdomain.insets = new Insets(0, 0, 5, 0);
		gbc_txtXMPPdomain.gridx = 0;
		gbc_txtXMPPdomain.gridy = 2;
		panel.add(txtXMPPdomain, gbc_txtXMPPdomain);
		txtXMPPdomain.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("XMPP host");
		lblNewLabel_1.setFont(new Font("Lucida Grande", Font.BOLD, 14));
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 3;
		panel.add(lblNewLabel_1, gbc_lblNewLabel_1);

		txtXMPPhost = new JTextField();
		txtXMPPhost.setText("52.88.34.121");
		txtXMPPhost.addFocusListener(this);
		GridBagConstraints gbc_txtXMPPhost = new GridBagConstraints();
		gbc_txtXMPPhost.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtXMPPhost.insets = new Insets(0, 0, 5, 0);
		gbc_txtXMPPhost.gridx = 0;
		gbc_txtXMPPhost.gridy = 4;
		panel.add(txtXMPPhost, gbc_txtXMPPhost);
		txtXMPPhost.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("MUC domain");
		lblNewLabel_2.setFont(new Font("Lucida Grande", Font.BOLD, 14));
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 5;
		panel.add(lblNewLabel_2, gbc_lblNewLabel_2);

		txtMUCdomain = new JTextField();
		txtMUCdomain.setText("conference.52.88.34.121");
		GridBagConstraints gbc_txtMUCdomain = new GridBagConstraints();
		gbc_txtMUCdomain.insets = new Insets(0, 0, 5, 0);
		gbc_txtMUCdomain.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMUCdomain.gridx = 0;
		gbc_txtMUCdomain.gridy = 6;
		panel.add(txtMUCdomain, gbc_txtMUCdomain);
		txtMUCdomain.setColumns(10);

		btnApply = new JButton("APPLY");
		btnApply.addActionListener(this);
		GridBagConstraints gbc_btnApply = new GridBagConstraints();
		gbc_btnApply.anchor = GridBagConstraints.SOUTH;
		gbc_btnApply.insets = new Insets(0, 0, 5, 0);
		gbc_btnApply.gridx = 0;
		gbc_btnApply.gridy = 7;
		panel.add(btnApply, gbc_btnApply);
		
		btnOutputLog = new JButton("OUTPUT LOG");
		btnOutputLog.addActionListener(this);
		GridBagConstraints gbc_btnOutputLog = new GridBagConstraints();
		gbc_btnOutputLog.anchor = GridBagConstraints.SOUTH;
		gbc_btnOutputLog.insets = new Insets(0, 0, 5, 0);
		gbc_btnOutputLog.gridx = 0;
		gbc_btnOutputLog.gridy = 8;
		panel.add(btnOutputLog, gbc_btnOutputLog);

		btnReset = new JButton("RESET");
		GridBagConstraints gbc_btnReset = new GridBagConstraints();
		gbc_btnReset.anchor = GridBagConstraints.SOUTH;
		gbc_btnReset.insets = new Insets(0, 0, 5, 0);
		gbc_btnReset.gridx = 0;
		gbc_btnReset.gridy = 9;
		panel.add(btnReset, gbc_btnReset);

		btnAssist = new JButton("ASSIST ME...");
		GridBagConstraints gbc_btnAssist = new GridBagConstraints();
		gbc_btnAssist.anchor = GridBagConstraints.SOUTH;
		gbc_btnAssist.gridx = 0;
		gbc_btnAssist.gridy = 10;
		panel.add(btnAssist, gbc_btnAssist);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmTestHarness.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel GenPanel = new JPanel();
		tabbedPane.addTab("General", null, GenPanel, null);
		GridBagLayout gbl_GenPanel = new GridBagLayout();
		gbl_GenPanel.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gbl_GenPanel.rowHeights = new int[] { 35, 0, 0, 35, 0, 0, 35, 0, 0, 0, 0 };
		gbl_GenPanel.columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_GenPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0 };
		GenPanel.setLayout(gbl_GenPanel);

		JLabel lblRoomName = new JLabel("Room Name");
		GridBagConstraints gbc_lblRoomName = new GridBagConstraints();
		gbc_lblRoomName.insets = new Insets(0, 0, 5, 5);
		gbc_lblRoomName.gridx = 1;
		gbc_lblRoomName.gridy = 1;
		GenPanel.add(lblRoomName, gbc_lblRoomName);

		JLabel lblPort = new JLabel("Port");
		GridBagConstraints gbc_lblPort = new GridBagConstraints();
		gbc_lblPort.insets = new Insets(0, 0, 5, 0);
		gbc_lblPort.gridx = 3;
		gbc_lblPort.gridy = 1;
		GenPanel.add(lblPort, gbc_lblPort);

		txtRoomName = new JTextField();
		txtRoomName.setText("TestHarness");
		GridBagConstraints gbc_txtRoomName = new GridBagConstraints();
		gbc_txtRoomName.insets = new Insets(0, 0, 5, 5);
		gbc_txtRoomName.gridx = 1;
		gbc_txtRoomName.gridy = 2;
		GenPanel.add(txtRoomName, gbc_txtRoomName);
		txtRoomName.setColumns(10);

		txtPort = new JTextField();
		txtPort.setText("5222");
		GridBagConstraints gbc_txtPort = new GridBagConstraints();
		gbc_txtPort.insets = new Insets(0, 0, 5, 0);
		gbc_txtPort.gridx = 3;
		gbc_txtPort.gridy = 2;
		GenPanel.add(txtPort, gbc_txtPort);
		txtPort.setColumns(10);

		JLabel lblFakeUsers = new JLabel("Fake Users");
		GridBagConstraints gbc_lblFakeUsers = new GridBagConstraints();
		gbc_lblFakeUsers.insets = new Insets(0, 0, 5, 5);
		gbc_lblFakeUsers.gridx = 1;
		gbc_lblFakeUsers.gridy = 4;
		GenPanel.add(lblFakeUsers, gbc_lblFakeUsers);

		JLabel lblDuration = new JLabel("Duration (.sec)");
		GridBagConstraints gbc_lblDuration = new GridBagConstraints();
		gbc_lblDuration.insets = new Insets(0, 0, 5, 0);
		gbc_lblDuration.gridx = 3;
		gbc_lblDuration.gridy = 4;
		GenPanel.add(lblDuration, gbc_lblDuration);

		SpinnerModel smFU = new SpinnerNumberModel(1, 1, null, 1);
		spnFakeUsers = new JSpinner(smFU);
		GridBagConstraints gbc_spnFakeUsers = new GridBagConstraints();
		gbc_spnFakeUsers.ipadx = 50;
		gbc_spnFakeUsers.insets = new Insets(0, 0, 5, 5);
		gbc_spnFakeUsers.gridx = 1;
		gbc_spnFakeUsers.gridy = 5;
		GenPanel.add(spnFakeUsers, gbc_spnFakeUsers);

		txtLength = new JTextField();
		txtLength.setText("0");
		GridBagConstraints gbc_txtLength = new GridBagConstraints();
		gbc_txtLength.insets = new Insets(0, 0, 5, 0);
		gbc_txtLength.gridx = 3;
		gbc_txtLength.gridy = 5;
		GenPanel.add(txtLength, gbc_txtLength);
		txtLength.setColumns(10);

		JLabel lblNetworkProtocol = new JLabel("Transport Protocol");
		GridBagConstraints gbc_lblNetworkProtocol = new GridBagConstraints();
		gbc_lblNetworkProtocol.insets = new Insets(0, 0, 5, 5);
		gbc_lblNetworkProtocol.gridx = 1;
		gbc_lblNetworkProtocol.gridy = 7;
		GenPanel.add(lblNetworkProtocol, gbc_lblNetworkProtocol);

		rdbtnTcpIp = new JRadioButton("TCP/IP");
		rdbtnTcpIp.setSelected(true);
		rdbtnTcpIp.addItemListener(this);
		GridBagConstraints gbc_rdbtnTcpip = new GridBagConstraints();
		gbc_rdbtnTcpip.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnTcpip.gridx = 1;
		gbc_rdbtnTcpip.gridy = 8;
		GenPanel.add(rdbtnTcpIp, gbc_rdbtnTcpip);

		rdbtnUdp = new JRadioButton("UDP");
		GridBagConstraints gbc_rdbtnUdp = new GridBagConstraints();
		gbc_rdbtnUdp.ipadx = 17;
		gbc_rdbtnUdp.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnUdp.gridx = 1;
		gbc_rdbtnUdp.gridy = 9;
		GenPanel.add(rdbtnUdp, gbc_rdbtnUdp);

		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnTcpIp);
		group.add(rdbtnUdp);

		btnServerMonitor = new JButton("Server Monitor");
		btnServerMonitor.addActionListener(this);
		GridBagConstraints gbc_btnServerMonitor = new GridBagConstraints();
		gbc_btnServerMonitor.insets = new Insets(0, 0, 5, 0);
		gbc_btnServerMonitor.gridx = 3;
		gbc_btnServerMonitor.gridy = 9;
		GenPanel.add(btnServerMonitor, gbc_btnServerMonitor);

		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 10;
		GenPanel.add(scrollPane, gbc_scrollPane);

		txtrReserved = new JTextArea();
		scrollPane.setViewportView(txtrReserved);

		JPanel AdvPanel = new JPanel();
		tabbedPane.addTab("Advanced", null, AdvPanel, null);
		GridBagLayout gbl_AdvPanel = new GridBagLayout();
		gbl_AdvPanel.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_AdvPanel.rowHeights = new int[] { 25, 0, 0, 25, 0, 0, 25, 0, 0, 0, 25, 0, 0 };
		gbl_AdvPanel.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0 };
		gbl_AdvPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		AdvPanel.setLayout(gbl_AdvPanel);

		chckbxAudioStream = new JCheckBox("Audio Stream");
		// Register listen for the radio button
		chckbxAudioStream.addItemListener(this);
		GridBagConstraints gbc_chckbxAudioStream = new GridBagConstraints();
		gbc_chckbxAudioStream.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxAudioStream.gridx = 0;
		gbc_chckbxAudioStream.gridy = 1;
		AdvPanel.add(chckbxAudioStream, gbc_chckbxAudioStream);

		AudioFileButton = new JButton("Audio packets");
		AudioFileButton.addActionListener(this);
		AudioFileButton.setEnabled(false);
		GridBagConstraints gbc_AudioFileButton = new GridBagConstraints();
		gbc_AudioFileButton.insets = new Insets(0, 0, 5, 5);
		gbc_AudioFileButton.gridx = 1;
		gbc_AudioFileButton.gridy = 1;
		AdvPanel.add(AudioFileButton, gbc_AudioFileButton);

		txtrAudioFile = new JTextArea("Use Default Silence");
		txtrAudioFile.setBackground(UIManager.getColor("Button.background"));
		GridBagConstraints gbc_txtrAudioFile = new GridBagConstraints();
		gbc_txtrAudioFile.anchor = GridBagConstraints.WEST;
		gbc_txtrAudioFile.gridwidth = 2;
		gbc_txtrAudioFile.insets = new Insets(0, 30, 5, 5);
		gbc_txtrAudioFile.gridx = 0;
		gbc_txtrAudioFile.gridy = 2;
		AdvPanel.add(txtrAudioFile, gbc_txtrAudioFile);

		VideoFileButton = new JButton("Video packets");
		VideoFileButton.addActionListener(this);
		VideoFileButton.setEnabled(false);
		GridBagConstraints gbc_VideoFileButton = new GridBagConstraints();
		gbc_VideoFileButton.insets = new Insets(0, 0, 5, 5);
		gbc_VideoFileButton.gridx = 1;
		gbc_VideoFileButton.gridy = 4;
		AdvPanel.add(VideoFileButton, gbc_VideoFileButton);

		txtrVideoFile = new JTextArea("Use Default Fading From Black To White");
		txtrVideoFile.setBackground(UIManager.getColor("Button.background"));
		GridBagConstraints gbc_txtrVideoFile = new GridBagConstraints();
		gbc_txtrVideoFile.anchor = GridBagConstraints.WEST;
		gbc_txtrVideoFile.gridwidth = 2;
		gbc_txtrVideoFile.insets = new Insets(0, 30, 5, 5);
		gbc_txtrVideoFile.gridx = 0;
		gbc_txtrVideoFile.gridy = 5;
		AdvPanel.add(txtrVideoFile, gbc_txtrVideoFile);

		chckbxVideoStream = new JCheckBox("Video Stream");
		chckbxVideoStream.addItemListener(this);
		GridBagConstraints gbc_chckbxVideoStream = new GridBagConstraints();
		gbc_chckbxVideoStream.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxVideoStream.gridx = 0;
		gbc_chckbxVideoStream.gridy = 4;
		AdvPanel.add(chckbxVideoStream, gbc_chckbxVideoStream);

		chckbxOverallStats = new JCheckBox("Overall Stats");
		chckbxOverallStats.setToolTipText("enable the logging of the overall stats at the end of the run");
		GridBagConstraints gbc_chckbxOverallStats = new GridBagConstraints();
		gbc_chckbxOverallStats.ipadx = 19;
		gbc_chckbxOverallStats.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxOverallStats.gridx = 0;
		gbc_chckbxOverallStats.gridy = 7;
		AdvPanel.add(chckbxOverallStats, gbc_chckbxOverallStats);

		JLabel lblNewLabel_3 = new JLabel("Stats Polling (.sec)");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 1;
		gbc_lblNewLabel_3.gridy = 7;
		AdvPanel.add(lblNewLabel_3, gbc_lblNewLabel_3);

		chckbxAllStats = new JCheckBox("All Stats");
		chckbxAllStats.setToolTipText("enable the logging of all the stats collected during the run");
		GridBagConstraints gbc_chckbxAllStats = new GridBagConstraints();
		gbc_chckbxAllStats.ipadx = 45;
		gbc_chckbxAllStats.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxAllStats.gridx = 0;
		gbc_chckbxAllStats.gridy = 8;
		AdvPanel.add(chckbxAllStats, gbc_chckbxAllStats);

		SpinnerModel smSP = new SpinnerNumberModel(5, 3, null, 1);
		spnStatsPolling = new JSpinner(smSP);
		GridBagConstraints gbc_spnStatsPolling = new GridBagConstraints();
		gbc_spnStatsPolling.ipadx = 30;
		gbc_spnStatsPolling.insets = new Insets(0, 0, 5, 5);
		gbc_spnStatsPolling.gridx = 1;
		gbc_spnStatsPolling.gridy = 8;
		AdvPanel.add(spnStatsPolling, gbc_spnStatsPolling);

		chckbxSummaryStats = new JCheckBox(" Summary Stats");
		GridBagConstraints gbc_chckbxSummaryStats = new GridBagConstraints();
		gbc_chckbxSummaryStats.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxSummaryStats.gridx = 0;
		gbc_chckbxSummaryStats.gridy = 9;
		AdvPanel.add(chckbxSummaryStats, gbc_chckbxSummaryStats);

		chckbxNoStats = new JCheckBox("No Stats");
		chckbxNoStats.addItemListener(this);
		GridBagConstraints gbc_chckbxNoStats = new GridBagConstraints();
		gbc_chckbxNoStats.ipadx = 43;
		gbc_chckbxNoStats.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxNoStats.gridx = 0;
		gbc_chckbxNoStats.gridy = 10;
		AdvPanel.add(chckbxNoStats, gbc_chckbxNoStats);

		JLabel lblAddingUserInterval = new JLabel("Adding User Interval (millisec.)");
		GridBagConstraints gbc_lblAddingUserInterval = new GridBagConstraints();
		gbc_lblAddingUserInterval.insets = new Insets(0, 0, 5, 5);
		gbc_lblAddingUserInterval.gridx = 1;
		gbc_lblAddingUserInterval.gridy = 9;
		AdvPanel.add(lblAddingUserInterval, gbc_lblAddingUserInterval);

		SpinnerModel smIV = new SpinnerNumberModel(2000, 500, null, 100);
		spnAddInterval = new JSpinner(smIV);
		GridBagConstraints gbc_spnAddInterval = new GridBagConstraints();
		gbc_spnAddInterval.ipadx = 30;
		gbc_spnAddInterval.insets = new Insets(0, 0, 5, 5);
		gbc_spnAddInterval.gridx = 1;
		gbc_spnAddInterval.gridy = 10;
		AdvPanel.add(spnAddInterval, gbc_spnAddInterval);

		btnLoginCredentials = new JButton("Login Credentials");
		GridBagConstraints gbc_btnLoginCredentials = new GridBagConstraints();
		gbc_btnLoginCredentials.insets = new Insets(0, 0, 0, 5);
		gbc_btnLoginCredentials.gridx = 0;
		gbc_btnLoginCredentials.gridy = 11;
		AdvPanel.add(btnLoginCredentials, gbc_btnLoginCredentials);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		if (source == rdbtnTcpIp) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				txtXMPPdomain.setText("52.88.34.121");
				txtXMPPhost.setText("52.88.34.121");
				txtMUCdomain.setText("conference.52.88.34.121");
			} else if (e.getStateChange() == ItemEvent.DESELECTED) {
				txtXMPPdomain.setText("52.25.85.82");
				txtXMPPhost.setText("52.25.85.82");
				txtMUCdomain.setText("conference.52.25.85.82");
			}
		} // end of TCP/IP or UDP Selection

		if (source == chckbxAudioStream) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				AudioFileButton.setEnabled(true);
			} else if (e.getStateChange() == ItemEvent.DESELECTED) {
				AudioFileButton.setEnabled(false);
			}
		}

		if (source == chckbxVideoStream) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				VideoFileButton.setEnabled(true);
			} else if (e.getStateChange() == ItemEvent.DESELECTED) {
				VideoFileButton.setEnabled(false);
			}
		}

		if (source == chckbxNoStats) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				chckbxOverallStats.setSelected(false);
				chckbxAllStats.setSelected(false);
				chckbxSummaryStats.setSelected(false);

				chckbxOverallStats.setEnabled(false);
				chckbxAllStats.setEnabled(false);
				chckbxSummaryStats.setEnabled(false);
			} else if (e.getStateChange() == ItemEvent.DESELECTED) {
				chckbxOverallStats.setEnabled(true);
				chckbxAllStats.setEnabled(true);
				chckbxSummaryStats.setEnabled(true);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();

		if (source == btnApply) {
			// We call initialize the Hammer
			Hammer.init();
//			System.out.println("Im done");

			String XMPPdomain = txtXMPPdomain.getText();
			String XMPPhost = txtXMPPhost.getText();
			String MUCdomain = txtMUCdomain.getText();
			String roomName = txtRoomName.getText();
			int port = Integer.parseInt(txtPort.getText());
			int numberOfFakeUsers = (Integer) spnFakeUsers.getValue();
			// how long does the fake conference run in seconds. if 0 or
			// negative, never stops.
			int length = Integer.parseInt(txtLength.getText());

			HostInfo hostInfo = new HostInfo(XMPPdomain, XMPPhost, port, MUCdomain, roomName);
			txtrReserved.append("Host Info Constructed...\n");

			/*
			 * Construct MediaDeviceChooser
			 */
			MediaDeviceChooser mdc = new MediaDeviceChooser();

			MediaService service = LibJitsi.getMediaService();
			MediaFormatFactory factory = service.getFormatFactory();
			// set audio packet
			AudioFormat opusFormat = new AudioFormat(Constants.OPUS_RTP, 48000, Format.NOT_SPECIFIED,
					2 /* channels */) {
				/**
				 * FMJ depends on this value when it calculates the RTP
				 * timestamps on the packets that it sends.
				 *
				 * This limits the supported files to only files with 20ms opus
				 * frames.
				 */
				@Override
				public long computeDuration(long length) {
					return 20L * 1000 * 1000;
				}
			};

			// user select audio stream, if not use default silence stream
			MediaDevice audioMediaDevice = null;
			if (chckbxAudioStream.isSelected() && !txtrAudioFile.getText().equals("Use Default Silence.")) {
				String audioRtpdumpFile = txtrAudioFile.getText();
				audioMediaDevice = RtpdumpMediaDevice.createRtpdumpAudioMediaDevice(audioRtpdumpFile, opusFormat);
			} else {
				audioMediaDevice = new AudioSilenceMediaDevice();
			}

			// set video stream
			MediaDevice videoMediaDevice = null;
			if (chckbxVideoStream.isSelected()
					&& !txtrVideoFile.getText().equals("Use Default Fading From Black To White")) {
				String videoRtpdumpFile = txtrVideoFile.getText();
				videoMediaDevice = RtpdumpMediaDevice.createRtpdumpVideoMediaDevice(videoRtpdumpFile, Constants.VP8_RTP,
						factory.createMediaFormat("vp8", 90000));
			} else {
				videoMediaDevice = new VideoGreyFadingMediaDevice();
			}

			// add audio and video to MediaDeciveChooser mdc
			mdc.setMediaDevice(audioMediaDevice);
			mdc.setMediaDevice(videoMediaDevice);

			List<Credential> credentials = null;
			int interval = (Integer) spnAddInterval.getValue();
			boolean disableStats = chckbxNoStats.isSelected();
			boolean overallStats = chckbxOverallStats.isSelected();
			boolean allStats = chckbxAllStats.isSelected();
			boolean summaryStats = chckbxSummaryStats.isSelected();
			int statsPolling = (Integer) spnStatsPolling.getValue();

			txtrReserved.append("Audio Stream: " + txtrAudioFile.getText() + "\n");
			txtrReserved.append("Video Stream: " + txtrVideoFile.getText() + "\n");

			/*
			 * Construct Hammer
			 */
			Hammer hammer = new Hammer(hostInfo, mdc, "BU-Testers", numberOfFakeUsers);
			txtrReserved.append("Start Testing...\n");

			// Cleanly stop the hammer when the program shutdown
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				public void run() {
					txtrReserved.append("Stopping Test - Harness...\n");

					hammer.stop();

					txtrReserved.append("Exiting the program...\n");
				}
			}));

			// After the initialization we start the Hammer (all its users will
			// connect to the XMPP server and try to setup media stream with it
			// bridge
			hammer.start(interval, disableStats, null, overallStats, allStats, summaryStats, statsPolling);
			txtrReserved.append("\n" + GUILog.toString());

			// open browser
			StringBuffer roomUrl = new StringBuffer("http://");
			roomUrl.append(XMPPhost).append("/").append(roomName);
			openWebpage(roomUrl.toString());

			// Set the fake conference to stop depending on runLength
			/*  This part not working now, just comment
			if (length > 0) {
	
				try {
					Thread.sleep(length * 1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
					hammer.stop();
				
			} else {
				while (true)
					try {
						Thread.sleep(3600000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			} */
			
			if(length>0) {
				try {
					Thread.sleep(length * 1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				finally{
					hammer.stop();
					txtrReserved.append("Test Stop");
					
				}
			}

		} // end of btnApply
		
		 if (source == btnOutputLog){
			try {
				java.awt.Desktop.getDesktop().edit(new File("/Users/Ritika/Desktop/Hammer_Stats"));
				System.out.println("Im done");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}

		else if (source == AudioFileButton) {
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter audioFilter = new FileNameExtensionFilter("Audio Files", "rtpdump");
			chooser.setFileFilter(audioFilter);
			if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(null)) {
				File file = chooser.getSelectedFile();
				txtrAudioFile.setText(file.getAbsolutePath());
			}
		} // end of Audio File chooser

		else if (source == VideoFileButton) {
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter rtpFilter = new FileNameExtensionFilter("Rtpdump Files", "rtpdump");
			FileNameExtensionFilter ivfFilter = new FileNameExtensionFilter("IVF Files", "ivf");
			chooser.addChoosableFileFilter(rtpFilter);
			chooser.addChoosableFileFilter(ivfFilter);
			if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(null)) {
				File file = chooser.getSelectedFile();
				txtrVideoFile.setText(file.getAbsolutePath());
			}
		} // end of Video File chooser

		else if (source == btnServerMonitor) {
			StringBuffer serverPage = new StringBuffer("http://");
			serverPage.append(txtXMPPhost.getText()).append(":3000");
			openWebpage(serverPage.toString());
			// write log
			txtrReserved.append("Open Server Stats Page...\n");
		}
	}

	public void focusLost(FocusEvent e) {
		Object source = e.getSource();
		if (source == txtXMPPhost) {
			txtMUCdomain.setText("conference." + txtXMPPhost.getText());
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * Open browser to the fake room page. Cross platform solution
	 * 
	 * @param roomUrl
	 */
	public static void openWebpage(String roomUrl) {
		URL url;
		try {
			url = new URL(roomUrl);
			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.browse(url.toURI());
				} catch (IOException | URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				Runtime runtime = Runtime.getRuntime();
				try {
					runtime.exec("open " + url);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} catch (MalformedURLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}
}