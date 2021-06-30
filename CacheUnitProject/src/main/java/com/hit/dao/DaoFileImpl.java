package com.hit.dao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import com.hit.dm.DataModel;

public class DaoFileImpl<T> extends java.lang.Object implements IDao<java.lang.Long,DataModel<T>>
{
	private String file_path;
	
	public DaoFileImpl(java.lang.String filePath)
	{
		file_path = filePath;
	}
	
	@Override
	public void save(DataModel<T> t)
	{
		
		//reading the file into array of DataModel
		//writing the array back to the same file - including the parameter it suppose to save
		
		Deque<DataModel<T>> dm = new LinkedList<>();	//list of the DataModel except the one we want to delete
		DataModel<T> dm_temp = null;					//temp DataModel will hold a spesific Data from the hard-disc in each iteration
		
		try {

			//streams to file
			FileOutputStream		fos = null;
			ObjectOutputStream	oos = null;
			
			FileInputStream		fis = new FileInputStream(file_path);
				
			//if file is empty - add new DataModel with no extra checkings
			if (fis.getChannel().size() == 0)
			{
				System.out.println("*****Empty file*****");
				fis.close();
				fos = new FileOutputStream(file_path);
				oos = new ObjectOutputStream(fos);
				oos.writeObject(t);
				oos.close();
				fos.close();

			} else {	
				ObjectInputStream	ois = new ObjectInputStream(fis);
				while (fis.available()>0)
				{
					dm_temp = (DataModel<T>) ois.readObject();

					//found a match - do not save it in the array
					if ((dm_temp.getDataModelId()).equals(t.getDataModelId()))
					{
						//add the old datamodel but with the new content
						dm.addLast(t);
					}
					//other DataModel - need to save to the array
					else {
						
						dm.addLast(dm_temp);
					}
				}
				
				//closing streams
				ois.close();
				fis.close();
				
				//open streams for writing
				fos = new FileOutputStream(file_path);
				oos = new ObjectOutputStream(fos);
				Iterator<DataModel<T>> it = dm.iterator();
				
				//looping through the list of DataModel
				while (it.hasNext())
				{
					dm_temp = it.next();
					//writing the current DataModel into the file
					oos.writeObject(dm_temp);
				}
				
				//closing streams
				oos.close();
				ois.close();
			
			}	 
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void delete(DataModel<T> t)
	{
		//reading the file into array of DataModel
		//skip the DataModel to delete
		//writing the array back to the same file
		
		Deque<DataModel<T>> dm = new LinkedList<>();	//list of the DataModel except the one we want to delete
		DataModel<T> dm_temp = null;					//temp DataModel will hold a spesific Data from the file in each iteration
		
		try {
			//streams to file
			FileInputStream		fis = new FileInputStream(file_path);
			FileOutputStream		fos = null;
			ObjectInputStream	ois = new ObjectInputStream(fis);
			ObjectOutputStream	oos = null;
			
			while (fis.available()>0)
			{
				dm_temp = (DataModel<T>) ois.readObject();
				
				//found a match - do not save it in the array
				if (dm_temp.equals(t))
				{
					//do nothing
				}
				//other DataModel - need to save to the array
				else {
					dm.addFirst(dm_temp);
				}
			}
			
			//closing streams
			ois.close();
			fis.close();
			
			//open streams for writing
			fos = new FileOutputStream(file_path);
			oos = new ObjectOutputStream(fos);
			Iterator<DataModel<T>> it = dm.iterator();
			
			//looping through the list of DataModel
			while (it.hasNext())
			{
				dm_temp = it.next();
				System.out.println(dm_temp);
				//writing the current DataModel into the file
				oos.writeObject(dm_temp);
			}
			
			//closing streams
			oos.close();
			ois.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public DataModel<T> find(java.lang.Long id)
	{
		DataModel<T> dm = null;
		
		try {
			FileInputStream		fis = new FileInputStream(file_path);
			ObjectInputStream	ois = new ObjectInputStream(fis);
			
			while (fis.available()>0)
			{
				dm = (DataModel<T>) ois.readObject();
				
				//found a match
				if (dm.getDataModelId().equals(id))
				{
					//closing connections
					ois.close();
					fis.close();
					
					//return the DataModel
					return dm;
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}