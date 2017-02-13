package org.xidian.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	public static MainFrame mainFrame;	
	
	public static MainFrame getInstance() {		
		if (mainFrame == null){
			mainFrame = new MainFrame();
		} 
		return mainFrame;
	}
	
	public MainFrame() {
		super(UIContants.UI_SOFTWARE_NAME);
		init();
	}
		 
	public void init() {
		//默认启动界面大小
		Dimension defaultDimension = Toolkit.getDefaultToolkit().getScreenSize();
		setSize((int)(defaultDimension.getWidth()*0.9), (int)(defaultDimension.getHeight()*0.9));	
	    setResizable(false); 
	    setLocationRelativeTo(null);
		//默认最小界面大小
		setMinimumSize(new Dimension((int)(defaultDimension.getWidth()*0.5), (int)(defaultDimension.getHeight()*0.5)));
		Container container = getContentPane();	
		//图标
		setIconImage(new ImageIcon(this.getClass().getResource("/images/icon.png")).getImage());	
		//添加菜单、工具和主显示panel	
		container.add(new MenuPanel(), BorderLayout.WEST);
		container.add(new OptionPanel(), BorderLayout.NORTH);		
		container.add(MainPanel.getInstance(), BorderLayout.CENTER);		
		//添加背景图片
		((JPanel) this.getContentPane()).setOpaque(false);
		Background background = new Background();
		getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
		setVisible(true);		
	}

}
