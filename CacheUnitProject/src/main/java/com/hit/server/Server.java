package com.hit.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import com.hit.services.CacheUnitController;
import com.hit.util.CLI;

public class Server implements Observer{

	private ServerSocket		m_socket;
	private	boolean			m_server_stat;
	private	String			m_command;
	private final boolean	OFFLINE;
	private final boolean	ONLINE;
	
	private CacheUnitController<String> cuc;
	

	
	//TODO leave an empty constructor and create the serverSocket in start() method
	public Server()
	{
		this.ONLINE = true;
		this.OFFLINE = false;
		cuc = new CacheUnitController<String>();
		
		try {
			//creating a server socket and setting server state to online
			m_socket = new ServerSocket(12345);
			m_server_stat = OFFLINE;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: Cannot connect to the server. check if the port is already in used");
			e.printStackTrace();
		}
	}
	
	public void start()
	{
		//output stream
		ObjectOutputStream os = null;
		
		while (m_server_stat)
		{
			try {
				Socket client = m_socket.accept();	//waiting for client to ask for connection
				System.out.println("New client just connected\n");
				if (m_server_stat)
				{
					new Thread(new HandleRequest<String>(client, cuc)).start(); //starting HandleRequest using new thread
				} 
				//in case server logged out while processing
				else {
					os = new ObjectOutputStream(client.getOutputStream());
					os.writeObject("Server OFFLINE\n");
				}				

			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
		
		try {
			m_socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//observer got notified from its subject (CLI)
	@Override
	public void update(Observable o, Object arg) {
		
		m_command = (String) arg;

		//works with lower case only
		if (m_command.equals("start") && (m_server_stat == OFFLINE))
		{
			m_server_stat = ONLINE;
			System.out.println("Server is now online");
			Runnable runnable_th = new Runnable() {
				
				@Override
				public void run() {
					//calling start() method of this class
					start();
				}
			};
			
			//starting the new thread
			new Thread(runnable_th).start();
			
		} else if ( ((m_command.equals("stop"))||(m_command.equals("close")) ) && (m_server_stat == ONLINE))
		{
			m_server_stat = OFFLINE;
			System.out.println("Server is now shutdown");

		}

	}

}
