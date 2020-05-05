package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;



public class Server extends UnicastRemoteObject implements Service {

	static int delay = 0; // delay for 0 sec. 
	static int period = 15000; // repeat every 15 sec. 
	static Timer timer = new Timer();
	String allData = "[{}]";
	int FloorCount = 0;
	int MaxRoomCount = 0;
	protected Server() throws RemoteException {
		super();
	
		timer.scheduleAtFixedRate(new TimerTask()  // used for reload frame every 15 second
					{ 
						   public void run() 
						    { 
		callApi();
	}}, delay, period);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// set the policy file as the system security policy
		System.setProperty("java.security.policy", "file:allowall.policy");
		
		try {
			Server svr = new Server();
			 // Bind the remote object's stub in the registry
			Registry registry = LocateRegistry.getRegistry();
			registry.bind("LevelService", svr);
			System.out.println("Service Strated........");
		}catch(RemoteException re){
            System.err.println(re.getMessage());
        }
        catch(AlreadyBoundException abe){
            System.err.println(abe.getMessage());
        }

	}  

	@Override
	public String Getdata() throws RemoteException {
		// TODO Auto-generated method stub
		return allData;
			}

	@Override
	public int getFloorCount() throws RemoteException {
		// get number of floors saved in database
	 	return FloorCount;
	}

	@Override
	public int getRoomCount(String floornum) throws RemoteException {
		// get numbers of rooms according to relevant floor number 
		String cnt = "";
		int roomcount = 0;
		try {
			URL url = new URL("http://localhost:5000/getRoomsCount/" + floornum);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Accept", "application/json");

	        if (conn.getResponseCode() != 200) {
	            throw new RuntimeException("Failed : HTTP error code : "
	                    + conn.getResponseCode());
	        }
          //get JSON type data to string
	        Scanner sc = new Scanner(url.openStream());
	        while(sc.hasNext()) {
	            cnt += sc.nextLine();
	        }
	        
	        //create JSON type object by string
	        JSONObject jb = new JSONObject(cnt); 
	        roomcount = jb.getInt("count");
	        conn.disconnect();

	      } catch (MalformedURLException e) {

	        e.printStackTrace();

	      } catch (IOException e) {

	        e.printStackTrace();

	      } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return roomcount;
	}

	@Override
	public void AddRoom(String floornum) throws RemoteException {
		// server generate the next room number according to given floor number and save in db
		
		try {
		
			 HttpClient client = HttpClient.newHttpClient();
		        HttpRequest request = HttpRequest.newBuilder()
		                .uri(URI.create("http://localhost:5000/addRoom/" + floornum ))
		                .POST(HttpRequest.BodyPublishers.ofString(""))
		                .build();

		        HttpResponse<String> response = client.send(request,
		                HttpResponse.BodyHandlers.ofString());

		        System.out.println(response.body());
		     callApi();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}

	@Override
	public void AddFloor() throws RemoteException{
		// server generate the next floor number and save in db
		try {
		
			 HttpClient client = HttpClient.newHttpClient();
		        HttpRequest request = HttpRequest.newBuilder()
		                .uri(URI.create("http://localhost:5000/addFloor"))
		                .POST(HttpRequest.BodyPublishers.ofString(""))
		                .build();

		        HttpResponse<String> response = client.send(request,
		                HttpResponse.BodyHandlers.ofString());

		        System.out.println(response.body());
			    callApi();
	       
	        } catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void callApi() {
		
		try {
			//-------------Get All Data by Server -----------
		
		URL url = new URL("http://localhost:5000/all");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }
        allData = "";
        // assign JSON type array to string
        Scanner sc = new Scanner(url.openStream());
        while(sc.hasNext()) {
            allData += sc.nextLine();
        }
        System.out.println(allData);
        conn.disconnect();
      
        //----------------Get Floors -------------------
        String fcnt = "";
		    URL url1 = new URL("http://localhost:5000/getFloorCount");
	        HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
	        conn1.setRequestMethod("GET");
	        conn1.setRequestProperty("Accept", "application/json");

	        if (conn1.getResponseCode() != 200) {
	            throw new RuntimeException("Failed : HTTP error code : "
	                    + conn1.getResponseCode());
	        }

	        Scanner sc1 = new Scanner(url1.openStream());
	        while(sc1.hasNext()) {
	            fcnt += sc1.nextLine();
	        }
	        
	        JSONObject jb = new JSONObject(fcnt); 
	        FloorCount = jb.getInt("count");
	        conn1.disconnect();
	        
	        //------------------- Get MaxRoom ----------------
	        String maxroomcnt = "";
		    URL url2 = new URL("http://localhost:5000/MaxRoomCount");
	        HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
	        conn2.setRequestMethod("GET");
	        conn2.setRequestProperty("Accept", "application/json");

	        if (conn2.getResponseCode() != 200) {
	            throw new RuntimeException("Failed : HTTP error code : "
	                    + conn2.getResponseCode());
	        }

	        Scanner sc2 = new Scanner(url2.openStream());
	        while(sc2.hasNext()) {
	        	maxroomcnt += sc2.nextLine();
	        }
	        
	        JSONObject jb1 = new JSONObject(maxroomcnt); 
	        MaxRoomCount = jb1.getInt("maximumRoom");
	        conn2.disconnect();
	        
	        //------- Send Email -------------
	        URL url3 = new URL("http://localhost:5000/MailSender");
	        HttpURLConnection conn3 = (HttpURLConnection) url3.openConnection();
	        conn3.setRequestMethod("GET");
	        conn3.setRequestProperty("Accept", "application/json");
	        url3.openStream();
	        conn3.disconnect();
	        

			 //------ Send Message ----------
			 URL url4 = new URL("http://localhost:5000/SMS-Sender");
             HttpURLConnection conn4 = (HttpURLConnection) url4.openConnection();
             conn4.setRequestMethod("GET");
             conn4.setRequestProperty("Accept", "application/json");
             url4.openStream();
             conn4.disconnect();


	        
		} catch (MalformedURLException e) {

        e.printStackTrace();

      } catch (IOException e) {

        e.printStackTrace();

      } catch (JSONException e) {
		e.printStackTrace();
	}
		
	}

	@Override
	public int getMaxRoomCOunt() throws RemoteException {
		return MaxRoomCount;
	}

	@Override
	public void SensorOff(String floornum, String roomnum) throws RemoteException {
		try {
			
			 HttpClient client = HttpClient.newHttpClient();
		        HttpRequest request = HttpRequest.newBuilder()
		                .uri(URI.create("http://localhost:5000/Off/" +floornum + "/" +roomnum))
		                .POST(HttpRequest.BodyPublishers.ofString(""))
		                .build();

		        HttpResponse<String> response = client.send(request,
		                HttpResponse.BodyHandlers.ofString());

		        System.out.println(response.body());
			    callApi();
	       
	        } catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void SensorOn(String floornum, String roomnum) throws RemoteException {
		try {
			
			 HttpClient client = HttpClient.newHttpClient();
		        HttpRequest request = HttpRequest.newBuilder()
		                .uri(URI.create("http://localhost:5000/On/" +floornum + "/" +roomnum))
		                .POST(HttpRequest.BodyPublishers.ofString(""))
		                .build();

		        HttpResponse<String> response = client.send(request,
		                HttpResponse.BodyHandlers.ofString());

		        System.out.println(response.body());
			    callApi();
	       
	        } catch (Exception e) {
			e.printStackTrace();
		}
	}	
}
