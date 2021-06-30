package com.hit.memory;

import com.hit.algorithm.IAlgoCache;
import com.hit.dao.IDao;
import com.hit.dm.DataModel;

public class CacheUnit<T> extends java.lang.Object {
	
	private IAlgoCache<java.lang.Long,DataModel<T>>	algo;
	private IDao<java.lang.Long,DataModel<T>>		dao;
	private static int								m_count_swap = 0;
	
	public CacheUnit(com.hit.algorithm.IAlgoCache<java.lang.Long,DataModel<T>> algo, IDao<java.lang.Long,DataModel<T>> dao) 
	{
		this.algo	= algo;
		this.dao		= dao;
	}
	
	//
	public DataModel<T>[] getDataModels(java.lang.Long[] ids) throws java.lang.ClassNotFoundException, java.io.IOException
	{
		//array of DataModel
		DataModel<T>[] dm = (DataModel<T>[]) new DataModel[ids.length];
		
		//index of the new array
		int i = 0;
		DataModel<T> dm_temp = null;
		for (Long id : ids)
		{
			//data model not found in cache
			if (algo.getElement(id)==null)
			{	
				//page found in disc - page fault - count swaps - +1
				if ((dm_temp=(DataModel<T>)dao.find(id))!=null)
				{
					//add DataModel to the array if not already exists in it
					if (!inArray(dm,id))
					{
						dm[i] = dm_temp;

						i++;
					}
					
					//add DataModel to the cache
					algo.putElement(id, dao.find(id));
					
					//increment count_swap (page fault)
					m_count_swap++;
				} else {
					//do nothing - page not in disc and not in cache
					//TODO
					//check if I want to add it to the hard disc and the cache in this situation
				}		
			//data model in cache
			} else {
				//we already add this id to cache and array of DataModels
				//we update algo with the element, so it updates, using the algorithm we use (in this case - LRU)
				//and we don't update the array of DataModels so we don't get duplicate elements in the array
				
				//add DataModel to the array if not already exists in it
				if (!inArray(dm,id))
				{
					dm[i] = algo.getElement(id);

					i++;
					
				}
				
				//update cache
				algo.putElement(id, algo.getElement(id));
			}
		}
		
		//return the array of DataModel
		return dm;
	}
	
	private boolean inArray(DataModel<T>[] dm, Long id)
	{
		int i=0;
		while (dm[i]!=null)
		{
			if (dm[i].getDataModelId().equals(id))
				return true;
			
			i++;
		}
		return false;
	}
	
	
	//TODO delete this method
	public void updateCache(DataModel<T>[] dm)
	{
		for (DataModel<T> tmp : dm)
		{
			//found in cache
			if (algo.getElement(tmp.getDataModelId())!=null)
			{
				algo.putElement(tmp.getDataModelId(), tmp);
				System.out.println(tmp + " || " + algo.getElement(tmp.getDataModelId()));
				System.out.println("let's see if it has new val: "+algo.getElement((tmp.getDataModelId())).getContent());
			}
		}
	}
	
	public IAlgoCache<java.lang.Long,DataModel<T>> getAlgo()
	{
		return algo;
	}
	
	public IDao<java.lang.Long,DataModel<T>> getDao()
	{
		return dao;
	}
	
	public static int getSwapCount()
	{
		return m_count_swap;
	}
}
