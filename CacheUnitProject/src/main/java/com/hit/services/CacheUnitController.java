package com.hit.services;

import com.hit.dm.DataModel;

public class CacheUnitController<T> extends java.lang.Object
{
	
	private CacheUnitService<T> cache_unit_service = null;
	
	public CacheUnitController()
	{
		//TODO remove printing code
		System.out.println("-- In CacheUnitController constructor\n");
		cache_unit_service = new CacheUnitService<T>();
	}
	
	public boolean update(DataModel<T>[] dm)
	{
		//TODO remove printing code
		System.out.println("updating\n");
		
		return cache_unit_service.update(dm);
	}
	
	public boolean delete(DataModel<T>[] dm)
	{
		//TODO remove printing code
		System.out.println("deleting\n");
		return cache_unit_service.delete(dm);
	}
	
	public DataModel<T>[] get(DataModel<T>[] dm)
	{
		//TODO remove printing code
		System.out.println("retriving data\n");
		return cache_unit_service.get(dm);
	}
	
	public String statistics()
	{
		//TODO remove printing code
		System.out.println("retriving statistics\n");
		
		return cache_unit_service.statistics();
	}
}