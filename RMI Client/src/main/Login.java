//admin login. Username and password are admin & 123   

package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login {

	private JFrame frame;
	private JTextField txtUsername;
	private JPasswordField pwdPassword;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
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
	public Login() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setBounds(189, 10, 63, 19);
		frame.getContentPane().add(lblLogin);
		
		JLabel lblUserName = new JLabel("User Name");
		lblUserName.setBounds(81, 65, 63, 13);
		frame.getContentPane().add(lblUserName);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(81, 119, 63, 13);
		frame.getContentPane().add(lblPassword);
		
		txtUsername = new JTextField();
		//txtUsername.setText(");
		txtUsername.setBounds(189, 62, 96, 19);
		frame.getContentPane().add(txtUsername);
		txtUsername.setColumns(10);
		
		pwdPassword = new JPasswordField();
		//pwdPassword.setText("Password");
		pwdPassword.setBounds(189, 116, 96, 19);
		frame.getContentPane().add(pwdPassword);
		
		JButton btnSubmit = new JButton("Login");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(txtUsername.getText().toString().equalsIgnoreCase("admin") && pwdPassword.getText().toString().equalsIgnoreCase("123") ) {
					ClientView cv = new ClientView();
					cv.setVisible(true);
					frame.setVisible(false);
				}else {
					JOptionPane.showMessageDialog(null, "Invalid Login Detais","Login Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnSubmit.setBounds(167, 171, 85, 21);
		frame.getContentPane().add(btnSubmit);
	}
}
