package org.xidian.utils;

import java.io.File;
import java.util.regex.Pattern;

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
		
		Pattern pattern = Pattern.compile("\r|\n");
    	String[] strs = pattern.split(resource);

    	for(int i = 1; i < strs.length; i++) {
    		Pattern pattern2 = Pattern.compile("\" \"");
        	String[] strs2 = pattern.split(strs[i]);
    	}
		
		
		
		
		
		
		
	}
	
	
	
	@Test
	public void testMedel(){
		loadResource(Constant.rootPath+"resources"+File.separator+"test.pnt");
	}
	
	
	
}
