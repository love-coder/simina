package org.xidian.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MenuPanel extends JPanel {	
	
	public MenuPanel() {		
		//设置布局
		setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));		    
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension((int)(screen.getWidth()*0.17), (int)(screen.getHeight()*0.8)));
		this.setOpaque(false);		
		//设置按钮样式
		ImageButton basicPropertyButton = new ImageButton( "menu",UIContants.UI_BISIC_PROPERTY,16);
		ImageButton reachabilityGraphButton = new ImageButton( "menu",UIContants.UI_REACHABILITY_GRAPH_BUTTON_NAME,16);
		ImageButton localReachabilityGraphButton = new ImageButton("menu",UIContants.UI_LOCAL_REACHABILITY_GRAPH_BUTTON_NAME,16);
		ImageButton pathButton = new ImageButton("menu",UIContants.UI_PATH_BUTTON_NAME,16);
		ImageButton beanconAnalysisButton = new ImageButton("menu",UIContants.UI_BEACON_ANALYSIS,16);
		ImageButton inequationButton = new ImageButton("menu",UIContants.UI_INEQUATION,16);
		ImageButton dynamicPathButton = new ImageButton("menu",UIContants.UI_DYNAMIC_PATH_ANALYSIS,16);	
		
		//设置按钮大小
		int width = (int) (screen.getWidth()*0.15);
		int height = (int) (screen.getHeight()*0.07);
		basicPropertyButton.setPreferredSize(new Dimension(width, height));
		reachabilityGraphButton.setPreferredSize(new Dimension(width, height));
		localReachabilityGraphButton.setPreferredSize(new Dimension(width, height));
		pathButton.setPreferredSize(new Dimension(width, height));
		beanconAnalysisButton.setPreferredSize(new Dimension(width, height));
		inequationButton.setPreferredSize(new Dimension(width, height));
		dynamicPathButton.setPreferredSize(new Dimension(width, height));
		//添加监听器		
		MenuListener menuListener = new MenuListener(basicPropertyButton, reachabilityGraphButton, localReachabilityGraphButton, pathButton, beanconAnalysisButton, inequationButton);
		basicPropertyButton.addActionListener(menuListener);
		reachabilityGraphButton.addActionListener(menuListener);
		localReachabilityGraphButton.addActionListener(menuListener);
		pathButton.addActionListener(menuListener);
		beanconAnalysisButton.addActionListener(menuListener);	
		inequationButton.addActionListener(menuListener);	
		dynamicPathButton.addActionListener(menuListener);	
		//设置按钮默认背景边框可见
		basicPropertyButton.setBorderPainted(false);	
		basicPropertyButton.setContentAreaFilled(false);
		reachabilityGraphButton.setBorderPainted(false);	
		reachabilityGraphButton.setContentAreaFilled(false);
		localReachabilityGraphButton.setBorderPainted(false);	
		localReachabilityGraphButton.setContentAreaFilled(false);
		pathButton.setBorderPainted(false);	
		pathButton.setContentAreaFilled(false);
		beanconAnalysisButton.setBorderPainted(false);	
		beanconAnalysisButton.setContentAreaFilled(false);
		inequationButton.setBorderPainted(false);	
		inequationButton.setContentAreaFilled(false);
		dynamicPathButton.setBorderPainted(false);	
		dynamicPathButton.setContentAreaFilled(false);		
		//添加按钮
		add(basicPropertyButton);
		add(reachabilityGraphButton);
		add(localReachabilityGraphButton);
		add(pathButton);
		add(beanconAnalysisButton);
		add(inequationButton);
		add(dynamicPathButton);
		//设置可见性		
		setVisible(true);
	}

}
