package org.xidian.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.xidian.alg.ReachabilityGraphAlgorithm;
import org.xidian.alg.StepAlgorithm;
import org.xidian.model.StateNode;

/**
 * 动态路径分析监听类
 */
public class PathListener implements ActionListener {
	
	private JTextField path;
	private JButton cancel;
	private MainPanel mainPanel;
	
	public PathListener(JTextField path, JButton cancel) {		
		this.path = path;
		this.cancel = cancel;
		mainPanel = MainPanel.getInstance();
	}
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if (actionEvent.getSource() == cancel) {
			PathParametersDialog.getInstance().dispose();
		} else { 			
			if (path.getText().equals("")) {
				JOptionPane.showMessageDialog(null, UIContants.UI_NO_IMPORT_PATH_ERROR, UIContants.UI_HINT, JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			//TODO 调运行分析可达图程序 
			String result = StepAlgorithm.getDeadlockPathLength();
			mainPanel.setText(result);
			
			JOptionPane.showMessageDialog(null,UIContants.UI_ANALYSIS_SUCCESS);
			PathParametersDialog.getInstance().dispose();					
		}
	}
				
}
	

