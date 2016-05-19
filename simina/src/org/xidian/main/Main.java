package org.xidian.main;

import java.io.File;

import org.xidian.calculation.ReachabilityGraph;
import org.xidian.utils.Constant;
import org.xidian.utils.LoadModel;

public class Main {

	public static void main(String[] args) {

		//1.加载模型,生成可达图
		String path = Constant.rootPath+"resources"+File.separator+"test.pnt"; //for debug
		ReachabilityGraph rg = new ReachabilityGraph(LoadModel.loadResource(path));
		
		
		//3.遍历可达图，输出到文件
		
		
	}

}
