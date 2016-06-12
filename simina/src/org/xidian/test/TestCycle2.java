package org.xidian.test;

import java.io.File;
import java.util.regex.Pattern;

import org.junit.Test;
import org.xidian.alg.CyclePath;
import org.xidian.alg.CyclePath2;
import org.xidian.model.GraphModel;
import org.xidian.utils.FileUtil;

/**
 * 测试深度优先遍历非递归写法
 * 遍历完其中的所有点和边
 * @author HanChun
 *
 */
public class TestCycle2 {
	
	GraphModel graphModel = new GraphModel(8);
	CyclePath2 cyclePath;
	
	@Test
	public void testDTS() {
		
		String resource = FileUtil.read(System.getProperty("user.dir") + File.separator  + "resources"+File.separator  + "Graph.txt",null);
		Pattern pattern = Pattern.compile("\r|\n");
    	String[] strs = pattern.split(resource);
    	String[] temp;
    	for(int i = 0; i < strs.length; i++) {
    		temp = strs[i].split(" ");
    		for(int j = 1; j < temp.length; j++) {
    			graphModel.costMatrix.getMatrix()[Integer.parseInt(temp[0])][Integer.parseInt(temp[j])] = 1;
    		}
    	}
    	
    	graphModel.costMatrix.printMatrix(graphModel.costMatrix.getMatrix());
    	
    	cyclePath = new CyclePath2(graphModel);
    	
	}
	
	
	
}
