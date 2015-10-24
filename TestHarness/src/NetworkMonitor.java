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
import org.jitsi.impl.neomedia.jmfext.media.protocol.rtpdumpfile.RtpdumpMediaDevice;
import org.jitsi.service.libjitsi.LibJitsi;
import org.jitsi.service.neomedia.MediaService;
import org.jitsi.service.neomedia.codec.Constants;
import org.jitsi.service.neomedia.device.MediaDevice;
import org.jitsi.service.neomedia.format.MediaFormatFactory;

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
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.List;
import java.awt.event.ItemEvent;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;

public class NetworkMonitor implements ItemListener, ActionListener {

	private JFrame frmTestHarness;
	private JTextField txtXMPPdomain;
	private JTextField txtXMPPhost;
	private JTextField txtMUCdomain;
	private JTextField txtRoomName;
	private JTextField txtPort;
	private JTextField txtLength;

	private JTextArea txtrReserved;

	private JRadioButton rdbtnTcpip;
	private JRadioButton rdbtnUdp;

	private JButton btnApply;
	private JButton btnReset;
	private JButton btnAssist;
	private JButton AudioFileButton;
	private JButton VideoFileButton;
	private JButton btnLoginCredentials;

	private JSpinner spnFakeUsers;
	private JSpinner spnStatsPolling;
	private JSpinner spnAddInterval;

	private JComboBox<String> comboBox;

	private JCheckBox chckbxAudioStream;
	private JCheckBox chckbxVideoStream;
	private JCheckBox chckbxOverallStats;
	private JCheckBox chckbxAllStats;
	private JCheckBox chckbxSummaryStats;
	private JCheckBox chckbxNoStats;

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
		frmTestHarness.setBounds(100, 100, 656, 500);
		frmTestHarness.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frmTestHarness.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmNewMenuItem = new JMenuItem("Exit");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnFile.add(mntmNewMenuItem);

		JMenu mnHelp = new JMenu("Help");
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
		gbl_panel.columnWidths = new int[] { 130, 0 };
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

		btnReset = new JButton("RESET");
		GridBagConstraints gbc_btnReset = new GridBagConstraints();
		gbc_btnReset.anchor = GridBagConstraints.SOUTH;
		gbc_btnReset.insets = new Insets(0, 0, 5, 0);
		gbc_btnReset.gridx = 0;
		gbc_btnReset.gridy = 8;
		panel.add(btnReset, gbc_btnReset);

		btnAssist = new JButton("ASSIST ME...");
		GridBagConstraints gbc_btnAssist = new GridBagConstraints();
		gbc_btnAssist.anchor = GridBagConstraints.SOUTH;
		gbc_btnAssist.gridx = 0;
		gbc_btnAssist.gridy = 9;
		panel.add(btnAssist, gbc_btnAssist);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmTestHarness.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel GenPanel = new JPanel();
		tabbedPane.addTab("General", null, GenPanel, null);
		GridBagLayout gbl_GenPanel = new GridBagLayout();
		gbl_GenPanel.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gbl_GenPanel.rowHeights = new int[] { 35, 0, 0, 35, 0, 0, 35, 0, 0, 0, 0 };
		gbl_GenPanel.columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_GenPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
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
		txtRoomName.setText("TestHammer");
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

		rdbtnTcpip = new JRadioButton("TCP/IP");
		rdbtnTcpip.setSelected(true);
		GridBagConstraints gbc_rdbtnTcpip = new GridBagConstraints();
		gbc_rdbtnTcpip.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnTcpip.gridx = 1;
		gbc_rdbtnTcpip.gridy = 8;
		GenPanel.add(rdbtnTcpip, gbc_rdbtnTcpip);

		rdbtnUdp = new JRadioButton("UDP");
		GridBagConstraints gbc_rdbtnUdp = new GridBagConstraints();
		gbc_rdbtnUdp.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnUdp.gridx = 3;
		gbc_rdbtnUdp.gridy = 8;
		GenPanel.add(rdbtnUdp, gbc_rdbtnUdp);

		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnTcpip);
		group.add(rdbtnUdp);

		txtrReserved = new JTextArea();
		txtrReserved.setLineWrap(true);
		txtrReserved.setBackground(Color.LIGHT_GRAY);
		GridBagConstraints gbc_txtpnReserved = new GridBagConstraints();
		gbc_txtpnReserved.gridwidth = 3;
		gbc_txtpnReserved.gridx = 1;
		gbc_txtpnReserved.gridy = 10;
		GenPanel.add(txtrReserved, gbc_txtpnReserved);
		txtrReserved.setText("Reserved for connection log");

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

		JLabel lblReserveForDisplay = new JLabel("Reserved for audio file path");
		GridBagConstraints gbc_lblReserveForDisplay = new GridBagConstraints();
		gbc_lblReserveForDisplay.insets = new Insets(0, 0, 5, 5);
		gbc_lblReserveForDisplay.gridx = 0;
		gbc_lblReserveForDisplay.gridy = 2;
		AdvPanel.add(lblReserveForDisplay, gbc_lblReserveForDisplay);

		AudioFileButton = new JButton("Audio packets");
		AudioFileButton.addActionListener(this);
		AudioFileButton.setEnabled(false);
		GridBagConstraints gbc_AudioFileButton = new GridBagConstraints();
		gbc_AudioFileButton.insets = new Insets(0, 0, 5, 5);
		gbc_AudioFileButton.gridx = 1;
		gbc_AudioFileButton.gridy = 2;
		AdvPanel.add(AudioFileButton, gbc_AudioFileButton);

		comboBox = new JComboBox<String>();
		comboBox.setEnabled(false);
		comboBox.setModel(
				new DefaultComboBoxModel<String>(new String[] { "---Select format---", "IVF file", "RTPdump file" }));
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 4;
		AdvPanel.add(comboBox, gbc_comboBox);

		JLabel lblReservedForVideo = new JLabel("Reserved for video file path");
		GridBagConstraints gbc_lblReservedForVideo = new GridBagConstraints();
		gbc_lblReservedForVideo.insets = new Insets(0, 0, 5, 5);
		gbc_lblReservedForVideo.gridx = 0;
		gbc_lblReservedForVideo.gridy = 5;
		AdvPanel.add(lblReservedForVideo, gbc_lblReservedForVideo);

		chckbxVideoStream = new JCheckBox("Video Stream");
		chckbxVideoStream.addItemListener(this);
		GridBagConstraints gbc_chckbxVideoStream = new GridBagConstraints();
		gbc_chckbxVideoStream.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxVideoStream.gridx = 0;
		gbc_chckbxVideoStream.gridy = 4;
		AdvPanel.add(chckbxVideoStream, gbc_chckbxVideoStream);

		VideoFileButton = new JButton("Video packets");
		VideoFileButton.setEnabled(false);
		GridBagConstraints gbc_VideoFileButton = new GridBagConstraints();
		gbc_VideoFileButton.insets = new Insets(0, 0, 5, 5);
		gbc_VideoFileButton.gridx = 1;
		gbc_VideoFileButton.gridy = 5;
		AdvPanel.add(VideoFileButton, gbc_VideoFileButton);

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

		JLabel lblAddingUserInterval = new JLabel("Adding User Interval (millisec.)");
		GridBagConstraints gbc_lblAddingUserInterval = new GridBagConstraints();
		gbc_lblAddingUserInterval.insets = new Insets(0, 0, 5, 5);
		gbc_lblAddingUserInterval.gridx = 1;
		gbc_lblAddingUserInterval.gridy = 9;
		AdvPanel.add(lblAddingUserInterval, gbc_lblAddingUserInterval);
		GridBagConstraints gbc_chckbxNoStats = new GridBagConstraints();
		gbc_chckbxNoStats.ipadx = 43;
		gbc_chckbxNoStats.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxNoStats.gridx = 0;
		gbc_chckbxNoStats.gridy = 10;
		AdvPanel.add(chckbxNoStats, gbc_chckbxNoStats);

		SpinnerModel smIV = new SpinnerNumberModel(2000, 1000, null, 100);
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
		if (source == chckbxAudioStream) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				AudioFileButton.setEnabled(true);
			} else if (e.getStateChange() == ItemEvent.DESELECTED) {
				AudioFileButton.setEnabled(false);
			}
		} else if (source == chckbxVideoStream) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				comboBox.setEnabled(true);
				VideoFileButton.setEnabled(true);
			} else if (e.getStateChange() == ItemEvent.DESELECTED) {
				comboBox.setEnabled(false);
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

			// We call initialize the Hammer (registering OSGi bundle for
			// example)
			Hammer.init();

			String XMPPdomain = txtXMPPdomain.getText();
			String XMPPhost = txtXMPPhost.getText();
			String MUCdomain = txtMUCdomain.getText();
			String roomName = txtRoomName.getText();
			int port = Integer.parseInt(txtPort.getText());
			int numberOfFakeUsers = (Integer) spnFakeUsers.getValue();
			int length = Integer.parseInt(txtLength.getText());

			txtrReserved.setText("-XMPPdomain " + XMPPdomain + " -XMPPhost " + XMPPhost + " -MUCdomain " + MUCdomain
					+ " -room " + roomName + " -port " + port + " -users " + numberOfFakeUsers + " -length " + length);

			HostInfo hostInfo = new HostInfo(XMPPdomain, XMPPhost, port, MUCdomain, roomName);

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
			String audioRtpdumpFile = "/Users/andy/git/jitsi-hammer/resources/rtp_opus.rtpdump";
			MediaDevice audioMediaDevice = RtpdumpMediaDevice.createRtpdumpAudioMediaDevice(audioRtpdumpFile,
					opusFormat);
			// set video packet
			String videoRtpdumpFile = "/Users/andy/git/jitsi-hammer/resources/rtp_vp8.rtpdump";
			MediaDevice videoMediaDevice = RtpdumpMediaDevice.createRtpdumpVideoMediaDevice(videoRtpdumpFile,
					Constants.VP8_RTP, factory.createMediaFormat("vp8", 90000));
			// add audio and video to MediaDeciveChooser mdc
			mdc.setMediaDevice(audioMediaDevice);
			mdc.setMediaDevice(videoMediaDevice);

			List<Credential> credentials = null;
			int interval = 2000;
			boolean disableStats = false;
			boolean overallStats = false;
			boolean allStats = false;
			boolean summaryStats = false;
			int statsPolling = 5;
			// how long does the fake conference run in seconds. if 0 or
			// negative,
			// never stops.
			int runLength = 5;

			/*
			 * Construct Hammer
			 */
			final Hammer hammer = new Hammer(hostInfo, mdc, "Jitsi-Hammer", numberOfFakeUsers);

			// Cleanly stop the hammer when the program shutdown
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				public void run() {
					System.out.println("Stopping Jitsi-Hammer...");

					hammer.stop();

					System.out.println("Exiting the program...");
				}
			}));

			// After the initialization we start the Hammer (all its users will
			// connect to the XMPP server and try to setup media stream with it
			// bridge
			hammer.start(interval, disableStats, null, overallStats, allStats, summaryStats, statsPolling);

			// Set the fake conference to stop depending on runLength
			if (runLength > 0) {
				try {
					Thread.sleep(runLength * 1000);
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
			}
		} // end of btnApply
		else if (source == AudioFileButton) {

		}
	}
}
