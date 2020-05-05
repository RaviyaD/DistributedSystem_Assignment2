// add new room or floor 
package main;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;
import java.awt.Component;
import javax.swing.Box;

public class FloorAdd extends JFrame {

	private JPanel contentPane;
	static FloorAdd frame;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					 frame = new FloorAdd();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	Service service = null;
	public FloorAdd() {
       System.setProperty("java.security.policy", "file:allowall.policy");
        
       int fct = 0;
	  try {
			 service = (Service) Naming.lookup("//localhost/LevelService");
		     fct =service.getFloorCount();  // get number of floor by RMI SERVER
			
	  }catch(Exception e) {
				e.printStackTrace();
			}
		
	  String Floornum[] = new String[fct];
	  //create string array by number of floors
	  for(int f=0; f<fct ; f++) {
		  Floornum[f] = String.valueOf(f+1);
	  }
	  
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 654, 464);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		//create button to add new floor
		JButton btnNewButton = new JButton("Add New Floor");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int a = JOptionPane.showConfirmDialog(contentPane, "Are You sure to add new floor ", "Confirm Add Floor", JOptionPane.YES_NO_OPTION);
				if(a==JOptionPane.YES_OPTION){  
					try {
					 service.AddFloor();
				     
				    ClientView c = new ClientView();
				    c.setVisible(true);
				    setVisible(false);
				     
					}catch(Exception ec) {
						ec.printStackTrace();
					}
				} 
			}
		});
		btnNewButton.setFont(new Font("Ubuntu Mono", Font.BOLD | Font.ITALIC, 20));
		btnNewButton.setBackground(Color.RED);
		btnNewButton.setBounds(174, 26, 292, 45);
		contentPane.add(btnNewButton);
		//---------------add new Room ---------------------
		JLabel lblNewLabel = new JLabel("Add New Room ");
		lblNewLabel.setFont(new Font("Tw Cen MT Condensed", Font.ITALIC, 21));
		lblNewLabel.setForeground(Color.BLUE);
		lblNewLabel.setBounds(65, 122, 226, 45);
		contentPane.add(lblNewLabel);
		
		JLabel lblSelectFloor = new JLabel("Select Floor");
		lblSelectFloor.setFont(new Font("Serif", Font.ITALIC, 16));
		lblSelectFloor.setBounds(174, 199, 92, 25);
		contentPane.add(lblSelectFloor);
		
		JComboBox comboBox = new JComboBox(Floornum);
		comboBox.setBounds(298, 201, 168, 25);
		contentPane.add(comboBox);
		
		JButton btnAddNewRoom = new JButton("Add New Room");
		btnAddNewRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fnumber = comboBox.getSelectedItem().toString();
				int a = JOptionPane.showConfirmDialog(contentPane, "Are You sure to add new Room to Floor " + fnumber, "Confirm Add Floor", JOptionPane.YES_NO_OPTION);
				if(a==JOptionPane.YES_OPTION){  
					try {
				     service.AddRoom(fnumber);
				     ClientView c = new ClientView();
				     c.setVisible(true);
					 setVisible(false);
					     
					}catch(Exception ec) {
						ec.printStackTrace();
					}
				} 
			}
		});
		btnAddNewRoom.setForeground(Color.GREEN);
		btnAddNewRoom.setFont(new Font("Ubuntu Mono", Font.BOLD | Font.ITALIC, 20));
		btnAddNewRoom.setBackground(Color.RED);
		btnAddNewRoom.setBounds(233, 263, 204, 45);
		contentPane.add(btnAddNewRoom);
	}
}
