package org.xidian.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

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
		this.rootState = new StateNode(model, model.getIninmarking().getMarking(), 1); //初始初始状态
		try {
			createRG();
		} catch (CloneNotSupportedException e) {
			System.err.println("生成可达图失败！");
			e.printStackTrace();
		}
	}

	/**
	 * 生成可达图(非递归方法)
	 * @throws CloneNotSupportedException 
	 */
	public void createRG() throws CloneNotSupportedException{
		
		int stateCount = 1;	//状态数
		Stack<Integer> nextTrans = new Stack<Integer>(); //当前状态下，能够发射的变迁
		Queue<StateNode> stateQueue = new LinkedList<StateNode>();  //状态队列
		Marking temState = null;
		StateNode currentState = rootState;
		StateNode duringState = null;  //过程中探索到的状态
		preStatesMap.put(currentState.hashCode(), currentState);
		stateQueue.add(rootState); //根状态为起始状态
		boolean[] canFire = null;
		
		while(!stateQueue.isEmpty()) {
			currentState = stateQueue.poll();	//每次取出队列中最前面的状态作为当前状态（注意该状态可能不是新状态）
			canFire = getEnabledTrans(currentState);
			for(int i = 0; i < canFire.length; i++) {
				if(canFire[i]) nextTrans.push(i);
			}
			while(!nextTrans.isEmpty()) {
				temState = fire(currentState, nextTrans.pop());
				//新状态
				if(!ifOccured(temState)) {
					stateCount++;
					duringState = new StateNode(model, temState.marking, stateCount);
					preStatesMap.put(temState.hashCode(), duringState);
					stateQueue.add((StateNode) duringState.clone());
				//旧状态
				}else{
					//currentState = preStatesMap.get(temState.hashCode());
				}
			}
			System.out.println(stateCount);
		}
		
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
	public Marking fire(StateNode currentState, int transIndex) {
	    Marking newMarking = new Marking(model.placesCount);;  //发射之后的marking
	    for (int i = 0; i < currentState.state.length; i++) {
	    	newMarking.marking[i] = currentState.state[i] + model.getPreMatrix().getValue(i, transIndex) - 
	        		model.getPosMatrix().getValue(i, transIndex);	
	    }
	    return newMarking;
	}
		   
	 /**
	  * 得到当前状态下能够发射的变迁
	  * @return boolean[] 
	  */
	 public boolean[] getEnabledTrans(StateNode currentState) {
	      //记录变迁是否能发射结果
	      boolean[] result = new boolean[model.transCount];  
	      for(int i = 0; i < result.length; i++) {
	    	  result[i] = true;
	      }
	      for (int i = 0; i < model.transCount ;i++) {
	         for (int j = 0; j < model.placesCount; j++) {
	        	 //必须指定变迁全部满足
	            if ((currentState.state[j] < model.getPosMatrix().getValue(j, i))) {  
	               result[i] = false;
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
	public boolean ifOccured(Marking node) {
		if(preStatesMap.containsKey(node.hashCode())){
			return true;
		}else{
			return false;
//			preStatesMap.put(node.hashCode(),node);
//			return false;
		}
	}

}
