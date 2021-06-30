package com.hit.memory;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.dao.DaoFileImpl;
import com.hit.dao.IDao;
import com.hit.dm.DataModel;

class CacheUnitTest {

	DataModel<String> dm1,dm2,dm3,dm4,dm5;
	Long[] ln;
	String file_path;
	
	@BeforeEach
	void setUp() throws Exception {
		
		//setting 5 DataModel
		ln = new Long[5];
		for (int i=0;i<5;i++)
		{
			ln[i] = new Long(i);
		}
		
		dm1 = new DataModel<String>(ln[0],"hello");
		dm2 = new DataModel<String>(ln[1],"world");
		dm3 = new DataModel<String>(ln[2],"testing");
		dm4 = new DataModel<String>(ln[3],"dao");
		dm5 = new DataModel<String>(ln[4],"datamodfdf");
		
		//the file we use as the Hard Drive
		file_path = "./src/main/resources/datasource.txt";
	}

	@Test
	public void getDataModelsTest() {

		//LRU with capacity = 5
	    IAlgoCache<Long, DataModel<String>> algo = new LRUAlgoCacheImpl<>(3);

	    //setting the Idao
	    IDao<Long, DataModel<String>> dao = new DaoFileImpl<>(file_path);
	    	
	    
	    
	    //setting the CacheUnit using DataModel<String>
	    CacheUnit<DataModel<String>> cu = new CacheUnit(algo,dao);
	   
	    //setting example array of id's
	    Long[] longs = new Long[5];
	    for (int i=0;i<longs.length;i++)
	    {
	    		longs[i] = new Long(i);
	    }
	    
	    longs[4] = new Long(0);
	    
	    //will hold the array of DataModels
	    DataModel<DataModel<String>>[] dm=null;
	    
	    //setting array of DataModel with the specified id's
	    try {
			dm = cu.getDataModels(longs);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    //printing array of DataModels
	    for (int i=0;i<dm.length;i++)
	    {
	    		System.out.println(i + ". " + dm[i]);
	    }
	    
	    
	    /* TEST OF DAO
	     * 
	    dao.save(dm1);
	    dao.save(dm2);
	    dao.save(dm3);
	    dao.save(dm4);
	    dao.save(dm5);
	    
	    //dao.delete(dm2);

	    System.out.println(dao.find(new Long(1)));
	    */

	}

}
