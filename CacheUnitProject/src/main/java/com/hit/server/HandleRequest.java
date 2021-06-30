package com.hit.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hit.dm.DataModel;
import com.hit.services.CacheUnitController;

public class HandleRequest<T> extends java.lang.Object implements java.lang.Runnable
{
	private Socket client;
	private CacheUnitController<T> cache_unit_controller;
	
	public HandleRequest(java.net.Socket s, CacheUnitController<T> controller)
	{
		this.client = s;
		this.cache_unit_controller = controller;
		
		//TODO remove this line of code
		//tells me I got to HandleRequest
		System.out.println("-- HandleRequest Object - in Constructor\n");
	}

	@Override
	public void run() {
		
		String request_str = null;
		
		try {
			ObjectOutputStream os = new ObjectOutputStream(client.getOutputStream());
			ObjectInputStream is = new ObjectInputStream(client.getInputStream());
				
			try {
				request_str = (String)is.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//getting statistics
			if(request_str.equals("getStatistics"))
			{
				String stats = cache_unit_controller.statistics();
				os.writeObject(stats);
			}
			//getting json file
			else {
				Type ref = new TypeToken<Request<DataModel<T>[]>>(){}.getType();
				Request<DataModel<T>[]> request = new Gson().fromJson(request_str, ref); 
				
				if (request.getHeaders().get("action").equals("UPDATE"))
				{
					//TODO remove printing code - check if should use another field for success
					System.out.println("-- in HandleRequest - updating\n");
					
					if (cache_unit_controller.update(request.getBody()))
					{
						System.out.println("updated successfully!\n");
						os.writeObject("true");
						os.flush();
					} else {
						System.out.println("failed to update!\n");
						os.writeObject("false");
						os.flush();
					}
						
				} else if (request.getHeaders().get("action").equals("DELETE")) {
					System.out.println("-- in HandleRequest - deleting\n");
					
					if (cache_unit_controller.delete(request.getBody()))
					{
						System.out.println("deleted successfully!\n");
						os.writeObject("true");
						os.flush();	
					} else {
						System.out.println("failed to delete\n");
						os.writeObject("false");
						os.flush();
					}
				} else if (request.getHeaders().get("action").equals("GET"))
				{
					System.out.println("-- in HandleRequest - getting\n");
					
					DataModel<T>[] dm_array = null;
					dm_array = cache_unit_controller.get(request.getBody());
					
					if (dm_array.length>0)
					{
						System.out.println("get data ended successfully!\n");
						String msg = new Gson().toJson(dm_array);
						os.writeObject(msg);
						os.flush();	
					} else {
						os.writeObject("failed to retrieve data\n");
						os.flush();
					}
				}
			}
			
			is.close();
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
