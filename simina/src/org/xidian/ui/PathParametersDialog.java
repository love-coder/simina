package org.xidian.ui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * 动态路径规划参数对话框 
 *
 */
@SuppressWarnings("serial")
public class PathParametersDialog extends JFrame{
	
	private static PathParametersDialog dynamicPathParametersDialog;
	private JTextField path;
	
	public static PathParametersDialog getInstance(){
		if (dynamicPathParametersDialog == null)
			dynamicPathParametersDialog = new PathParametersDialog();		
			return dynamicPathParametersDialog;		
		}
	
	private PathParametersDialog(){		
		setLayout(null);
		
		setSize(400, 350);
		JLabel pathLabel = new JLabel(UIContants.UI_GRAPH_PARAMETERS_PATH_NAME);		
		path = new JTextField();
		JButton ensure = new JButton(UIContants.CONFIRM);
		JButton cancel = new JButton(UIContants.CANCEL);
		//设置控件大小位置
		pathLabel.setBounds(60, 80, 90, 35);		
		path.setBounds(140, 80, 190, 35);
		ensure.setBounds(100, 250, 90, 40);
		cancel.setBounds(210, 250, 90, 40);
        //添加监听器
		PathListener dynamicPathListener = new PathListener(path,cancel);
		ensure.addActionListener(dynamicPathListener);
		cancel.addActionListener(dynamicPathListener);
		add(pathLabel);		
		add(path);
		add(ensure);
		add(cancel);
		setLocationRelativeTo(null);
	}
	
    public void open() {	
    	//TODO
		path.setText("步长分析对话框默认设置！！！");
		setVisible(true);		
    }
    
}
