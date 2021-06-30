package com.hit.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class CacheUnitClient extends java.lang.Object
{
	
	//TODO use int to store the port and check if greater than 65535
	
	private InetAddress	m_server_addr;
	private int			m_port;
	private Socket		my_server;
	private ObjectOutputStream	os;
	private ObjectInputStream	is;
	
	public CacheUnitClient(InetAddress server_addr, int port)
	{
		m_server_addr	= server_addr;
		m_port			= port;
		

	}
	
	public String send(String request)
	{
		//creating the socket in the eyes of the client
		try {
			my_server = new Socket(m_server_addr,m_port);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String msg_back_to_view = null;
		try {
			
			os = new ObjectOutputStream(my_server.getOutputStream());
			is = new ObjectInputStream(my_server.getInputStream());

			//asking the server for data
			os.writeObject(request);
			os.flush();

			
			//getting the data from the server
			msg_back_to_view = (String)is.readObject();
			
			
			//TODO remove the comments 
			is.close();
			os.close();
			my_server.close();
			
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
		return msg_back_to_view;
	}

}