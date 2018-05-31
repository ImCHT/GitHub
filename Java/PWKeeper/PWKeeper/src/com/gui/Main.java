package com.gui;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class Main
{
	public static void main(String[] args)
	{
//		System.out.println(System.getProperty("user.dir"));
		//程序入口，显示登陆界面
		new Login().init();
//		new MainWindow().init();
		
		//设置UI风格
		try {
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//			UIManager.setLookAndFeel(
//					"javax.swing.plaf.metal.MetalLookAndFeel");
//			UIManager.setLookAndFeel(
//					"javax.swing.plaf.nimbus.NimbusLookAndFeel");
			UIManager.setLookAndFeel(
					"com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
//			UIManager.setLookAndFeel(
//					"com.sun.java.swing.plaf.motif.MotifLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
