package org.xidian.utils;

import java.io.File;

import org.junit.Test;
import org.xidian.model.PetriModel;

/**
 * ������ѧģ��
 * @author HanChun
 * @version 1.0 2016-5-16
 */
public class LoadModel {

	PetriModel model;
	
	/**
	 * @param filePath �ļ�·��
	 */
	public void loadResource(String filePath) {
		String resource = FileUtil.read(filePath, null);
		System.out.println(resource);
		
		
	}
	
	
	
	@Test
	public void testMedel(){
		loadResource(Constant.rootPath+"resources"+File.separator+"test.pnt");
	}
	
	
	
}
