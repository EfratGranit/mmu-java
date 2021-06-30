package com.hit.dm;

public class DataModel<T> extends java.lang.Object implements java.io.Serializable
{
	private java.lang.Long	id;
	private T				content;
	
	public DataModel(java.lang.Long id,T content)
	{
		this.id		= id;
		this.content	= content;
	}
	
	public int hashCode()
	{
		return (Integer) ((Number) this.id);
	}
	
	public boolean equals(java.lang.Object obj)
	{
		if (this==obj) return true;
		if (this == null) return false;
		if (this.getClass() != obj.getClass())
		{
			return false;	
		}
		DataModel<T> dm = (DataModel<T>) obj;
		if ((this.getDataModelId().equals(dm.getDataModelId()))&&(this.getContent().equals(dm.getContent())))
		{
			return true;
		}
		return false;
	}
	
	public java.lang.String toString()
	{
		return id + " " + content;
	}
	
	public java.lang.Long getDataModelId()
	{
		return this.id;
	}
	
	public void setDataModelId(java.lang.Long id)
	{
		this.id = id;
	}
	
	public T getContent()
	{
		return this.content;
	}
	
	public void setContent(T content)
	{
		this.content = content;
	}
}
