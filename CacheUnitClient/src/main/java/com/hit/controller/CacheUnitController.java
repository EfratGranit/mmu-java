package com.hit.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Observable;

import com.google.gson.Gson;
import com.hit.model.CacheUnitModel;
import com.hit.model.IModel;
import com.hit.view.CacheUnitView;
import com.hit.view.IView;

public class CacheUnitController extends java.lang.Object implements IController  {

	//will create array of objects of this class to parse json string
	private class dmFromJson
	{
		private String id;
		private String content;
		
		public String getId()
		{
			return id;
		}
		
		public String getContent()
		{
			return content;
		}
	}
	
	private IView	m_view;
	private IModel	m_model;
	
	public CacheUnitController(IModel model,IView view)
	{
		m_view	= view;
		m_model	= model;
	}
	
	
	@Override
	public void update(Observable o, Object arg) {
		
		String request	= (String) arg;

		//TODO place this IF CODE inside of the ELSE IF below
		if ((!(request.equals("getStatistics")) && ((o instanceof CacheUnitView))))
		{
			String json = readAllBytesJava7(request);
			request = new String(json);
		}
		
		//updates from the MODEL - the model want to send some data to the view
		if (o instanceof CacheUnitModel)
		{

			if (isJSONValid(request) && (!request.equals("true")) && (!request.equals("false")))
			{
				//parse to string
				Gson gson = new Gson();
				dmFromJson[] dm_from_json = gson.fromJson(request, dmFromJson[].class);
				
				String[] dm_arr = new String[dm_from_json.length];
				int i =0;
				for (dmFromJson dm_str : dm_from_json)
				{
					dm_arr[i] = new String(dm_str.getId() + " " + dm_str.getContent());
					i++;
				}
				
				request = String.join("\n", dm_arr);
			}
			m_view.updateUIData(request);
		}
		//updates from the VIEW - the client want to get some data from the model
		else if (o instanceof CacheUnitView)
		{
			//1 of 2 options - both strings!
			//String of Json
			//String for asking Statistics of the project

			m_model.updateModelData(request);
		}
		
	}
	
	private static String readAllBytesJava7(String filePath) 
	{
		String content = "";
		try 
		{
			content = new String ( Files.readAllBytes( Paths.get(filePath) ) );
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return content;
	}
	
	  public static boolean isJSONValid(String jsonInString) {
		  Gson gson = new Gson();
	      try {
	          gson.fromJson(jsonInString, Object.class);
	          return true;
	      } catch(com.google.gson.JsonSyntaxException ex) { 
	          return false;
	      }
	  }

}
