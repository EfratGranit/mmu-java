package com.hit.model;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class CacheUnitModel extends java.util.Observable implements IModel
{
	private CacheUnitClient cache_unit_client;

	public CacheUnitModel()
	{
		InetAddress server_addr;
		int port;

		try {
			//the server sits on the same machine - otherwise need to save the server's IP
			server_addr = InetAddress.getLocalHost();
			port = 12345;	//the server listens on this port
			
			cache_unit_client = new CacheUnitClient(server_addr,port);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public <T> void updateModelData(T t)
	{
		//why I get T while I have to cast it to String (send method gets String)
		String request = (String) t;
		String response = null;
		
		response = cache_unit_client.send(request);
		
		setChanged();
		notifyObservers(response);
	}

	//will be used to achieve method Send of cacheUnitClient from the controller
	public CacheUnitClient getCacheUnitClient()
	{
		return cache_unit_client;
	}
}
