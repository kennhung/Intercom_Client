import java.awt.EventQueue;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.FlowLayout;

public class IntercomClient {
	AudioSender as;
	Thread ast;
	
	private JFrame frame;
	private JTextField addrField;
	private JButton btnSpeak;

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
		Scanner in = new Scanner(System.in);
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		addrField = new JTextField();
		panel.add(addrField);
		addrField.setColumns(10);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				as = new AudioSender(addrField.getText());
				
				ast = new Thread(as);
				ast.start();
				as.setSpeak(true);
			}
		});
		panel.add(btnConnect);
		
		btnSpeak = new JButton("Speak");
		btnSpeak.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(!as.getSpeak()) as.setSpeak(true);
				
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if(as.getSpeak()) as.setSpeak(false);
			}
		});
		frame.getContentPane().add(btnSpeak, BorderLayout.CENTER);
	}

}
