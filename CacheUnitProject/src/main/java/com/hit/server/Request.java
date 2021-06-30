package com.hit.server;

public class Request<T> extends java.lang.Object implements java.io.Serializable
{
	private java.util.Map<java.lang.String,java.lang.String> headers;
	private T body;
	
	//c'tor
	public Request(java.util.Map<java.lang.String,java.lang.String> headers, T body)
	{
		setBody(body);
		setHeaders(headers);
	}
	
	/*
	 * GETTERS AND SETTERS for request object
	 */
	
	public java.util.Map<java.lang.String, java.lang.String> getHeaders() {
		return headers;
	}

	public void setHeaders(java.util.Map<java.lang.String, java.lang.String> headers) {
		this.headers = headers;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}	
	
	@Override
	public String toString() {
		return body.toString() + " " + headers.toString();
	}
}