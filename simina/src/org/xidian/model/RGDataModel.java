package org.xidian.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 可达图（广义）数据模型
 * @author HanChun
 * @version 1.0 version 2016-5-18
 */
public class RGDataModel {
	
	public StateNode rootState; //初始状态
	public PetriModel model;
	public static Map<Integer,StateNode> preStatesMap = new HashMap<Integer,StateNode>(1000); //初始化1000个状态
	
	/**
	 * @param rootState 初始状态
	 * @param model PetriModel
	 */
	public RGDataModel(PetriModel model) {
		this.model = model;
		this.rootState = new StateNode(model, model.getIninmarking().getMarking(), null, 1, 1); //初始初始状态
	}

	/**
	 * 生成可达图
	 */
	public void createRG(){
		
	}
	
	
	/**
	 * 遍历可达图，输出到文件中
	 * @param destPath 输出目录
	 */
	public void traverseRG(String destPath){
		
	}
	
	/**
	 * 检查当前状态是否已经发生过
	 * @param node 待检查状态
	 * @return boolean true:发生过
	 */
	public boolean ifOccured(StateNode node) {
		if(preStatesMap.containsKey(node.hashCode())){
			return true;
		}else{
			preStatesMap.put(node.hashCode(),node);
			return false;
		}
	}

}
