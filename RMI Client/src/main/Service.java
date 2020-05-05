package main;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Service  extends Remote{
	
	public String Getdata() throws RemoteException;
	public int getFloorCount() throws RemoteException;
	public int getRoomCount(String floornum) throws RemoteException;
	public void AddRoom(String floornum) throws RemoteException;
	public void AddFloor() throws RemoteException;
	public int getMaxRoomCOunt() throws RemoteException;
	public void SensorOff(String floornum, String roomnum) throws RemoteException;
	public void SensorOn(String floornum, String roomnum) throws RemoteException;

}
