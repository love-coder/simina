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
		createRG();
	}

	/**
	 * 生成可达图(非递归方法)
	 */
	public void createRG(){
		boolean[] canFire = getEnabledTrans(rootState.getState());
		
//		while(1==1) {
//			
//		}
	}
	
	/**
	 * 遍历可达图，输出到文件中
	 * @param destPath 输出目录
	 */
	public void traverseRG(String destPath){
		
	}
	
	
	
	/**
	 * @param transIndex 发射变迁编号
	 * @param currentState 待发射状态
	 * @return  
	 */
	public int[] fire(int transIndex, int[] currentState) {
	    int[] newMarking = new int[currentState.length];  //发射之后的marking
	    
	    System.out.print("==t" + (transIndex+1) + "==>");  //for debug
	
	    for (int i = 0; i < currentState.length; i++) {
	    	newMarking[i] = currentState[i] + model.getPreMatrix().getValue(i, transIndex) - 
	        		model.getPosMatrix().getValue(i, transIndex);	
	    }
	    return newMarking;
	}
		   
		   
	 /**
	  * 得到当前状态下能够发射的变迁
	  * @return boolean[] 
	  */
	 public boolean[] getEnabledTrans(int[] currentState) {
	      //记录变迁是否能发射结果
	      boolean[] result = new boolean[model.transCount];  
	      for (int i = 0; i < model.transCount ;i++) {
	         for (int j = 0; j < model.placesCount; j++) {
	            if ((currentState[j] >= model.getPosMatrix().getValue(j, i))) {  
	               result[i] = true;
	               break;
	            }
	         }
	      }
	      return result;
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
