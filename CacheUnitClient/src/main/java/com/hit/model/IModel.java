package com.hit.model;

public interface IModel {
	public 		CacheUnitClient	getCacheUnitClient();
	public <T>	void				updateModelData(T t);
}
