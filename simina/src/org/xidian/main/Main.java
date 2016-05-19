package org.xidian.main;

import java.io.File;

import org.xidian.model.RGDataModel;
import org.xidian.utils.Constant;
import org.xidian.utils.LoadModel;

/**
 * 程序入口
 * @author HanChun
 * @version 1.0 2016-5-19
 */
public class Main {
	public static void main(String[] args) {
		//1.加载模型,生成可达图
		String path = Constant.rootPath+"resources"+File.separator+"test.pnt"; //for debug
		RGDataModel rg = new RGDataModel(LoadModel.loadResource(path));
		//2.遍历可达图，输出到文件
		rg.traverseRG(Constant.rootPath+"resources"+File.separator+"result.txt");
	}
}
