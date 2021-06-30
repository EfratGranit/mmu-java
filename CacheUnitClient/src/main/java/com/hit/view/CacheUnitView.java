package com.hit.view;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class CacheUnitView extends java.util.Observable implements IView {
	
	private CacheUnitLayout cur_panel;
	
	public CacheUnitView() {
		super();
		cur_panel = new CacheUnitLayout(this);
	}
	
	public void start() {
		javax.swing.SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				GenerView();
			}
		});
	}
	
	public void GenerView()
	{
		JFrame frame = new JFrame("CacheUnitUI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(cur_panel);
		frame.pack();
	    frame.setVisible(true);
	    frame.setResizable(false);
	}
	
	public <T> void updateUIData(T t) {
		
		if(t.equals("getStatistics") || t.equals(cur_panel.getFilePath()))
		{
			setChanged();
			notifyObservers(t);
		}
		else
		{
			String[] newT = ((String) t).split(";");
			for(int i=0; i< newT.length; i++)
				cur_panel.setInput(newT[i]);
		}
	}
}




//package com.hit.view;
//
//public class CacheUnitView extends java.util.Observable implements IView {
//	public void start()
//	{
//		
//	}
//	
//	public <T> void	 updateUIData(T t)
//	{
//		
//	}
//}
