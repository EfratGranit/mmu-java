package com.hit.driver;

import com.hit.controller.CacheUnitController;
import com.hit.controller.IController;
import com.hit.model.CacheUnitModel;
import com.hit.model.IModel;
import com.hit.view.CacheUnitView;
import com.hit.view.IView;

public class Driver {
	public static void main(String[] args) {
	    IModel	model			= new CacheUnitModel(); 
	    IView	view				= new CacheUnitView(); 
	    IController controller	= new CacheUnitController(model, view); 
	    
	    ((CacheUnitModel)model).addObserver(controller); 
	    ((CacheUnitView)view).addObserver(controller);
	    
	    view.start();
    } 
}
