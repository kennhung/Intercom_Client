package org.kenn.intercom;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.FlowLayout;
import javax.swing.JLabel;

public class IntercomClient {
	AudioSender as;
	Thread ast;
	InformationSocket is;
	Thread ist;

	private JFrame frame;
	private JTextField addrField;
	private JButton btnSpeak;
	private JPanel panelSouth;
	private JButton btnConnect;
	private JPanel panelMain;
	private JPanel panelSpeak;
	private JPanel panelStatus;
	private JLabel lblStatus;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IntercomClient window = new IntercomClient();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public IntercomClient() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panelConnection = new JPanel();
		frame.getContentPane().add(panelConnection, BorderLayout.NORTH);
		panelConnection.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		addrField = new JTextField();
		panelConnection.add(addrField);
		addrField.setColumns(10);

		btnConnect = new JButton("Connect");
		btnConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (as == null||is == null) {
					as = new AudioSender(addrField.getText());
					ast = new Thread(as);
					ast.start();
					is = new InformationSocket(addrField.getText());
					ist = new Thread(is);
					ist.start();
					btnSpeak.setEnabled(true);
					btnConnect.setText("Disconnect");
				} else if (as.isDisconnected()) {
					as = new AudioSender(addrField.getText());
					ast = new Thread(as);
					ast.start();
					//audio sender start
					is = new InformationSocket(addrField.getText());
					ist = new Thread(is);
					ist.start();
					//information socket start
					btnSpeak.setEnabled(true);
					btnConnect.setText("Disconnect");
				} else {
					as.disconnect();
					btnSpeak.setEnabled(false);
					btnConnect.setText("Connect");
				}

			}
		});
		panelConnection.add(btnConnect);

		panelSouth = new JPanel();
		frame.getContentPane().add(panelSouth, BorderLayout.SOUTH);
				panelSouth.setLayout(new BorderLayout(0, 0));
				
				panelSpeak = new JPanel();
				panelSouth.add(panelSpeak, BorderLayout.NORTH);
						panelSpeak.setLayout(new BorderLayout(0, 0));
				
						btnSpeak = new JButton("Speak");
						panelSpeak.add(btnSpeak);
						btnSpeak.setEnabled(false);
						btnSpeak.addMouseListener(new MouseAdapter() {
							@Override
							public void mousePressed(MouseEvent e) {
								if (btnSpeak.isEnabled() && as != null) as.setEnable(true);
							}

							@Override
							public void mouseReleased(MouseEvent e) {
								if (as != null) as.setEnable(false);
							}
						});
				
				panelStatus = new JPanel();
				panelSouth.add(panelStatus, BorderLayout.SOUTH);
				panelStatus.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
				
				lblStatus = new JLabel("Status");
				panelStatus.add(lblStatus);
		
		panelMain = new JPanel();
		frame.getContentPane().add(panelMain, BorderLayout.CENTER);
	}

}
