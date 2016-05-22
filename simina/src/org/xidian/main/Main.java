package org.xidian.main;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.xidian.model.RGDataModel;
import org.xidian.utils.FileUtil;
import org.xidian.utils.LoadModel;

/**
 * 程序入口
 * @author HanChun
 * @version 1.0 2016-5-19
 */
public class Main {
	
	public static void init() {
	    final JFrame f  = new JFrame();
		JButton resource = new JButton("导入模型");
		resource.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				if(jfc.showOpenDialog(f)==JFileChooser.APPROVE_OPTION ){
					if(jfc.getSelectedFile().getName().contains("pnt")){
						createRG(jfc.getSelectedFile().getAbsolutePath(), jfc.getSelectedFile().getParentFile().getPath()+File.separator+
								jfc.getSelectedFile().getName().substring(0, jfc.getSelectedFile().getName().indexOf('.'))+".gra");
					}
				}
			}
		});
		
		f.add(resource);
		f.setLayout(new FlowLayout());
		f.setSize(240, 180);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
	
	public static void createRG(String resourcePath, String destPath) {
		Long start = System.currentTimeMillis();
		new RGDataModel(LoadModel.loadResource(resourcePath), destPath);  
		FileUtil.write(destPath, "\n分析耗时： "+String.valueOf(System.currentTimeMillis()-start)+"ms", true);
	}
	
	public static void main(String[] args) throws InterruptedException {
		init();
	}
	
}
