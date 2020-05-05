//main data view jframe 
package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.json.JSONArray;
import org.json.JSONException;
import javax.swing.JButton;
import javax.swing.JScrollPane;

public class ClientView extends JFrame {

	private JPanel contentPane;
	static int delay = 0; // delay for 0 sec. 
	static int period = 30000; // repeat every 30 sec. 
	static Timer timer = new Timer();
	 static ClientView frame ;
	 int mxrm = 10;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					 frame = new ClientView();
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
	JSONArray temparray = null;	// used for check data changes after refresh
	JSONArray jsonarry = null;
	public ClientView() {
	
		
		timer.scheduleAtFixedRate(new TimerTask()  // used for reload frame every 15 second
		{ 
		   public void run() 
		    { 
		        
		    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setBounds(20, 20, 1325, 732);
				contentPane = new JPanel();
				contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
				setContentPane(contentPane);
				contentPane.setLayout(null);
				
				   
				JButton btnAddFloor = new JButton("Add"); // button to go to add floor or add room frame
				btnAddFloor.setBounds(805, 10, 95, 27);
				getContentPane().add(btnAddFloor);
				btnAddFloor.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						FloorAdd fa = new FloorAdd();
						fa.setVisible(true);
						setVisible(false);
						
					}
				});
				
				JButton btnChangeState = new JButton("Change State"); // button to change state of sensors
				btnChangeState.setBounds(920, 10, 125, 27);
				getContentPane().add(btnChangeState);
				btnChangeState.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ChangeState cs = new ChangeState();
						cs.setVisible(true);
						setVisible(false);
						
					}
				});
				int p = 185;
		   		for(int v=0; v<mxrm; v++) {  // create Room number labels 
		   		JLabel lblNewLabel_1 = new JLabel("Room " + (v+1));
		   		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15));
		   		lblNewLabel_1.setBounds(p, 42, 81, 20);
		   		contentPane.add(lblNewLabel_1);
		   		p= p + 112;
		   		}
				
		System.setProperty("java.security.policy", "file:allowall.policy");
	        
	        Service service = null;
		
	        try {
	        	
	            service = (Service) Naming.lookup("//localhost/LevelService");
	            mxrm = service.getMaxRoomCOunt();
	            String alldata = service.Getdata(); //call get all data method by rmi server
		         jsonarry = new JSONArray(alldata); // assign return string value to  JSON array 
	            
	           int y = 72;
	           
	           for(int j=0; j<jsonarry.length(); j++) {
	   		
	        	   JLabel jmain = new JLabel(); // create rows according to floor numbers 
	        	   jmain.setFont(new Font("Tahoma", Font.PLAIN, 15));
	        	   jmain.setBorder(new LineBorder(new Color(0, 0, 0)));
	        	   jmain.setBounds(25, y, 132, 72);
	        	   contentPane.add(jmain);
	       		   jmain.setText("FLOOR    " +  String.valueOf(jsonarry.getJSONObject(j).getInt("FloorNo")));
	       		   int x = 167;
	        	   
	       		   for(int i=0; i<jsonarry.getJSONObject(j).getJSONArray("Rooms").length(); i++) {
	       			 // create a label to view  smoke level and co2 level, position of this label change with floor and room of each floor
	        		   JLabel jlbSmLvl = new JLabel("");  
	        		   jlbSmLvl.setFont(new Font("Tahoma", Font.PLAIN, 14));
	        		   jlbSmLvl.setBorder(new LineBorder(new Color(0, 0, 0)));
	        		   jlbSmLvl.setBounds(x, y, 102, 32);
	        	       contentPane.add(jlbSmLvl);
	   			
	        		    JLabel jlCLvl = new JLabel("");
	        		    jlCLvl.setFont(new Font("Tahoma", Font.PLAIN, 14));
	        		    jlCLvl.setBorder(new LineBorder(new Color(0, 0, 0)));
	        		    jlCLvl.setBounds(x, y+42, 102, 32);
	        		    contentPane.add(jlCLvl);
	        		     	//get values for relevant room by JSON array 		
	   			        int smokelevel = jsonarry.getJSONObject(j).getJSONArray("Rooms").getJSONObject(i).getInt("SmokeLevel");
	   			        int c02level = jsonarry.getJSONObject(j).getJSONArray("Rooms").getJSONObject(i).getInt("CO2Level");
	   			
	   			     jlbSmLvl.setText("Smoke Level:" + String.valueOf(smokelevel));
	   			  jlCLvl.setText("Co2 Level  : " +String.valueOf(c02level));
	   			     
	   			     boolean active = jsonarry.getJSONObject(j).getJSONArray("Rooms").getJSONObject(i).getBoolean("Active");
	   			     //check sensor activation
	   			     if(!active) {
	   			    	jlbSmLvl.setText(null);
	   			    	jlCLvl.setText(null);
	   			    	jlbSmLvl.setBorder(new LineBorder(Color.RED));
	   			    	jlCLvl.setBorder(new LineBorder(Color.RED));
	   			     }
	   			    
	   			  if(smokelevel >= 5) jlbSmLvl.setForeground(Color.RED); else jlbSmLvl.setForeground(Color.GREEN);
	   			if(c02level >= 5) jlCLvl.setForeground(Color.RED); else jlCLvl.setForeground(Color.GREEN);
	   			   x = x +112;
	   			   
	   			   if(temparray != null && active) {
	   				   if(temparray.getJSONObject(j).getJSONArray("Rooms").getJSONObject(i).getInt("SmokeLevel") != smokelevel)
	   					 jlCLvl.setBorder(new LineBorder(Color.BLUE));
	   				   if(temparray.getJSONObject(j).getJSONArray("Rooms").getJSONObject(i).getInt("CO2Level") != c02level)
	   					   jlCLvl.setBorder(new LineBorder(Color.BLUE));
	   			   }
	   			     
	   		}
	       		    y= y+86;
	           }
	           
	           
	         } catch (NotBoundException ex) {
	            System.err.println(ex.getMessage());
	        } catch (MalformedURLException ex) {
	            System.err.println(ex.getMessage());
	        } catch (RemoteException ex) {
	            System.err.println(ex.getMessage());
	        } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        temparray = jsonarry;
		    }}, delay, period);
		
		
	}
	
	
}
