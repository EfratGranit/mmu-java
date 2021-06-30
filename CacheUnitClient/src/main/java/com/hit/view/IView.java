package com.hit.view;

public interface IView {
	void start();
	<T> void updateUIData(T t);
}