package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.json.JSONArray;

import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JCheckBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.awt.event.ActionEvent;
import javax.swing.JSpinner;

public class ChangeState extends JFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChangeState frame = new ChangeState();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	static	boolean State;
	Service service = null;
	String ButtonLable;
	
	public ChangeState() {
		
		System.setProperty("java.security.policy", "file:allowall.policy");
        
	       int floorCount = 0;
		  try {
				 service = (Service) Naming.lookup("//localhost/LevelService");
				 floorCount =service.getFloorCount();
				
		  }catch(Exception e) {
					e.printStackTrace();
		}
		  String Floornum[] = new String[floorCount];
		  
		  for(int f=0; f<floorCount ; f++) {
			  Floornum[f] = String.valueOf(f+1);
		  }
		
		getContentPane().setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 21));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 604, 452);
		getContentPane().setLayout(null);
		
		JLabel lblUpdateRoom = new JLabel("Update Room Sensor");
		lblUpdateRoom.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 24));
		lblUpdateRoom.setBounds(200, 10, 249, 41);
		getContentPane().add(lblUpdateRoom);
		
		JLabel lblNewLabel = new JLabel("Floor No.");
		lblNewLabel.setFont(new Font("Tahoma", Font.ITALIC, 18));
		lblNewLabel.setBounds(108, 81, 108, 25);
		getContentPane().add(lblNewLabel);
		
		JLabel lblRoomNo = new JLabel("Room No.");
		lblRoomNo.setFont(new Font("Tahoma", Font.ITALIC, 18));
		lblRoomNo.setBounds(108, 138, 108, 25);
		getContentPane().add(lblRoomNo);
		
		
		JComboBox FloorcomboBox = new JComboBox(Floornum);
		FloorcomboBox.setBounds(273, 86, 132, 21);
		getContentPane().add(FloorcomboBox);
		
		JComboBox RoomcomboBox = new JComboBox();
		RoomcomboBox.setBounds(273, 143, 132, 21);
		getContentPane().add(RoomcomboBox);
		
		JButton SubmitButton = new JButton("Select Floor & Room");
		SubmitButton.setBounds(182, 232, 192, 32);
		getContentPane().add(SubmitButton);
		SubmitButton.setEnabled(false); // Submit button disable till select room and floor
		
		FloorcomboBox.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	String[] Roomnum;
		        try {
		            // Select relevant rooms for selected Floor
					int RoomCount = service.getRoomCount(FloorcomboBox.getSelectedItem().toString());
					Roomnum = new String[RoomCount];
					for(int f=0; f<RoomCount ; f++) {
						Roomnum[f] = String.valueOf(f+1);
					  }
					RoomcomboBox.setModel(new DefaultComboBoxModel(Roomnum));
						
				     }catch(Exception ec) {
						ec.printStackTrace();
					}
		        
		    }
		});
		
		RoomcomboBox.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				 try {
					 //Check state of sensor 
						String alldata = service.Getdata();
						JSONArray jsar = new JSONArray(alldata);
						SubmitButton.setEnabled(true);
						State = jsar.getJSONObject(FloorcomboBox.getSelectedIndex()).getJSONArray("Rooms").getJSONObject(RoomcomboBox.getSelectedIndex()).getBoolean("Active");
						System.out.println(State + "-------------" + FloorcomboBox.getSelectedIndex()+1);
						if(State) {
							ButtonLable = "Switch Off the Sensor";
							SubmitButton.setText("Switch Off the Sensor");
							SubmitButton.setBackground(Color.RED);
						}
						else { 
							ButtonLable = "Switch On the Sensor";
							SubmitButton.setText("Switch On the Sensor");
							SubmitButton.setBackground(Color.GREEN);
						}
						
					     }catch(Exception ec) {
							ec.printStackTrace();
						}
			}
			
		});
		
		
		SubmitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int a = JOptionPane.showConfirmDialog(getContentPane(), "Are You sure to  " + ButtonLable, "Confirm Sensor Change", JOptionPane.YES_NO_OPTION);
				if(a==JOptionPane.YES_OPTION){  
					try {
					
						if(State) 
							service.SensorOff(FloorcomboBox.getSelectedItem().toString(), RoomcomboBox.getSelectedItem().toString());
						else
							service.SensorOn(FloorcomboBox.getSelectedItem().toString(), RoomcomboBox.getSelectedItem().toString());
				     
						ClientView c = new ClientView();
					    c.setVisible(true);
					    setVisible(false);
					}catch(Exception ec) {
						ec.printStackTrace();
					}
				} 
			}
		});
		
		
	}
}
