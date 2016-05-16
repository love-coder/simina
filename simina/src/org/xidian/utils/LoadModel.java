package org.xidian.utils;

import java.io.File;

import org.junit.Test;
import org.xidian.model.PetriModel;

/**
 * 加载model
 * @author HanChun
 * @version 1.0 2016-5-16
 */
public class LoadModel {

	PetriModel model;
	
	/**
	 * @param filePath 文件路径
	 */
	public void loadResource(String filePath) {
		String resource = FileUtil.read(filePath, null);
		System.out.println(resource);
		
		model = new PetriModel(1,2);
		
	}
	
	
	
	@Test
	public void testMedel(){
		loadResource(Constant.rootPath+"resources"+File.separator+"test.pnt");
	}
	
	
	
}
