package org.xidian.model;

import java.util.List;

/**
 * State状态节点
 * @author HanChun
 * @version 1.0 2016-5-18
 */
public class StateNode {
	
	public PetriModel model;
	public int[] state; 
	public List<StateNode> ChildNodes;
	public int deepth;  
	public int stateNo;  
	public boolean ifDeadlock = false;  //是否死锁状态
	
	/**
	 * @param model PetriModel
	 * @param state 状态
	 * @param childNodes 孩子节点
	 * @param deepth //状态深度
	 * @param stateNo //状态编号
	 */
	public StateNode(PetriModel model, int[] state, List<StateNode> childNodes, int deepth, int stateNo) {
		this.model = model;
		this.state = state;
		ChildNodes = childNodes;
		this.deepth = deepth;
		this.stateNo = stateNo;
	}
	
	public void addChildNodes(StateNode newChildNode) {
		
		
	}
	
	 
	 /**
	  * hashCode()
	  * 计算hashcode，用来比较两个对象是否相等
	  */
	 @Override
	 public int hashCode(){
		 int total = 0;
		 for (int offset = 0; offset < state.length; offset++) {
			 total = (2 * total);
			 for (int index = 0; index < (state.length - offset); index++) {
				total += state[index];
			 }
		 }
		 //处理hashcode过大的情况，
		 if (total < 0) {
			 total = Integer.MAX_VALUE + total;
		 }      
		 return total;
	}

	public int[] getState() {
		return state;
	}

}
