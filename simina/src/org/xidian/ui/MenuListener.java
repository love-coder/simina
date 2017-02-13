package org.xidian.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import org.xidian.alg.ReachabilityGraphAlgorithm;

/**
 * 菜单按钮监听类
 * @author HanChun
 */
public class MenuListener implements ActionListener{
	
	private JButton basicPropertyButton, reachabilityGraphButton, localReachabilityGraphButton, 
	                pathButton, beanconAnalysisButton, inequationButton;
	private MainPanel mainPanel;
		
	public MenuListener(JButton basicPropertyButton, JButton reachabilityGraphButton,JButton localReachabilityGraphButton, JButton pathButton, JButton beanconAnalysisButtonJButton, JButton inequationButton) {		
		this.basicPropertyButton = basicPropertyButton;
		this.reachabilityGraphButton = reachabilityGraphButton;
		this.localReachabilityGraphButton = localReachabilityGraphButton;
		this.pathButton = pathButton;
		this.inequationButton = inequationButton;	
		mainPanel = MainPanel.getInstance();
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {	
		if (actionEvent.getSource() == basicPropertyButton) {			
			 //暂时只提供前置后置矩阵
			 //TODO
			 return;		
		}else if (actionEvent.getSource() == reachabilityGraphButton) {
	     	 try {
				String graphResult = new ReachabilityGraphAlgorithm().createReachabilityGraph(null, 0);
				mainPanel.setText(graphResult);
	     	 } catch (CloneNotSupportedException e) {
	     		JOptionPane.showMessageDialog(null,UIContants.UI_ANALYSIS_FAILURE);
	     		 return;
	     	 }  
			 return;
		}else if (actionEvent.getSource() == localReachabilityGraphButton) {
			
		     LocalGraphParametersDialog.getInstance().open();
		     return;
		}else if (actionEvent.getSource() == pathButton) {
			 PathParametersDialog.getInstance().open();	
			 return;
		}else if (actionEvent.getSource() == beanconAnalysisButton) {
			 JOptionPane.showMessageDialog(null, UIContants.UI_IN_DEVELOPMENT);
			 return;
		}else if (actionEvent.getSource() == inequationButton) {
			 JOptionPane.showMessageDialog(null, UIContants.UI_IN_DEVELOPMENT);
			 return;
		}
	    JOptionPane.showMessageDialog(null, UIContants.UI_IN_DEVELOPMENT);
	    return;
	}	
	
}

