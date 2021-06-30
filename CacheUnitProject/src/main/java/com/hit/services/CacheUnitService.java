package com.hit.services;

import java.io.IOException;

import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.algorithm.SecondChance;
import com.hit.dao.DaoFileImpl;
import com.hit.dao.IDao;
import com.hit.dm.DataModel;
import com.hit.memory.CacheUnit;

public class CacheUnitService<T> {
	
	private CacheUnit<T>		cache_unit;
	private boolean			stat;
	private DataModel<T>[]	dm_array;
	private Long[]			ids;					//holding the array of id's in each request
	private int				m_capacity;			//cache capacity
	private String			algo_in_use;	
	private static int		count;				//counting how many times the project has been used
	private int				count_data_models;	//count number of affected datamodels
	private int				count_swap;			//count page faults
	
	public CacheUnitService()
	{
		//TODO remove printing code
		System.out.println("-- In CacheUnitService constructor\n");
		
		stat					= false;		//operation succeeded
		dm_array				= null;		
		ids					= null;		
		count				= 0;			
		count_data_models	= 0;			
		count_swap			= 0;			
		m_capacity			= 10;
		algo_in_use			= new String("LRU");
		cache_unit			= new CacheUnit<>(new LRUAlgoCacheImpl<>(m_capacity), new DaoFileImpl<>("src/main/resources/datasource.txt"));

	}
	
	//getting DataModels to update
	//updating cache in case the datamodel is in the cache
	//and the hard disc
	//with the new content
	public boolean update(DataModel<T>[] dm)
	{
		//increment count
		incCount();

		//create array of keys
		ids = new Long[dm.length];
		int i=0;
		
		for (DataModel<T> data_models_temp : dm)
		{
			ids[i] = data_models_temp.getDataModelId();
			i++;
		}
	
		//get the datamodels array from the RAM and if not found, get them from the hard disc
		//if it's not anywhere - do nothing
		//TODO check if I want to add another condition to getDataModels and add to hard disc and ram in case
		//I don't find it anywhere
		try {	
			dm_array = cache_unit.getDataModels(ids);
			incCountDataModels(dm_array.length);
			DataModel<T> dm_tmp = null;
			
			IDao<java.lang.Long,DataModel<T>> dao = cache_unit.getDao();
			IAlgoCache<java.lang.Long,DataModel<T>> algo = cache_unit.getAlgo();
			
			for (i=0 ; i<dm_array.length;i++)
			{
				dm_tmp = dm_array[i];
				dm_tmp.setContent(dm[i].getContent());
				
				//save to the hard disc (update if already exists)
				dao.save(dm[i]);
				
				//update cache using its algorithm
				algo.putElement(dm[i].getDataModelId(), dm[i]);
			}
			
			stat = true;
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		incCountSwap();
			
		return stat;
	}
	
	//getting dDataModels to delete
	//deleting (null content) datamodels from the cache in case they are there
	//and the hard disc
	//with null content
	public boolean delete(DataModel<T>[] dm)
	{
		//increment count
		incCount();

		String null_content = new String("null");
		
		//create array of keys
		ids = new Long[dm.length];
		int i=0;
		
		for (DataModel<T> data_models_temp : dm)
		{
			ids[i] = data_models_temp.getDataModelId();
			i++;
		}
	
		//delete the datamodels array from the RAM and if not found, get them from the hard disc
		//if it's not anywhere - do nothing
		try {
			
			dm_array = cache_unit.getDataModels(ids);
			incCountDataModels(dm_array.length);
			DataModel<T> dm_tmp = null;
			
			//will be used a datamodel with null value to set the dm_tmp
			DataModel<T> dm_tmp_null = (DataModel<T>) new DataModel<String>(new Long(1),new String("null"));
			
			IDao<java.lang.Long,DataModel<T>> dao = cache_unit.getDao();
			IAlgoCache<java.lang.Long,DataModel<T>> algo = cache_unit.getAlgo();
			

			
			for (i=0 ; i<dm_array.length;i++)
			{
				dm_tmp = dm_array[i];

				dm_tmp.setContent(dm_tmp_null.getContent());
				
				//save to the hard disc (update if already exists)
				dao.save(dm_tmp);
				
				//update cache using its algorithm
				algo.putElement(dm_tmp.getDataModelId(), dm_tmp);
				
			}
			
			stat = true;
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return stat;
	}

	//returning the spesifed data models after adding them to the RAM
	public DataModel<T>[] get(DataModel<T>[] dm)
	{
		ids = new Long[dm.length];
		
		int i = 0;
		
		for (DataModel<T> data_model_temp : dm)
		{
			ids[i] = data_model_temp.getDataModelId();
			i++;
		}
		
		try {
			dm_array = cache_unit.getDataModels(ids);
			incCountDataModels(dm_array.length);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		incCount();
		
		return dm_array;
	}
	
	public String statistics()
	{		
		String stat_result = new String(
				"Capacity: " + getCapacity()
				+ "\n"
				+ "Algorithm: " + this.algo_in_use
				+ "\n"
				+ "Total number of requests:" + getCount()
				+ "\n"
				+ "Total number of DataModels (GET/UPDATE/DELETE):" + getCountDataModels()
				+ "\n"
				+ "Total number of DataModels swap: " + getCountSwap()
				);
		
		return stat_result;
	}
	
	public static synchronized void incCount()
	{
		count++;
	}
	
	public synchronized void incCountDataModels(int how_many)
	{
		count_data_models+=how_many;
	}
	
	public synchronized void incCountSwap()
	{
		count_swap = cache_unit.getSwapCount();
	}
	
	public static int getCount()
	{
		return count;
	}
	
	public int getCapacity()
	{
		return this.m_capacity;
	}
	
	public int getCountDataModels()
	{
		return this.count_data_models;
	}
	
	public int getCountSwap()
	{
		return count_swap;
	}
}
