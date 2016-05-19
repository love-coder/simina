package org.xidian.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 可达图（广义）数据模型
 * @author HanChun
 * @version 1.0 version 2016-5-18
 */
public class RGDataModel {
	
	public State rootState; //初始状态
	public PetriModel model;
	Map<Integer,StateNode> preStatesMap = new HashMap<Integer,StateNode>(1000);  //初始化1000个状态
	
	
	
	
	

	
	/**
	 * 遍历可达图，输出到文件中
	 * @param destPath 输出目录
	 */
	public void traverseRG(String destPath){
		
		
		
	}
	
	
		
	
	
	

}
