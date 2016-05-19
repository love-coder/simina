package org.xidian.model;

import java.util.List;

/**
 * State状态节点
 * @author HanChun
 * @version 1.0 2016-5-18
 */
public class StateNode {
	
	PetriModel model;
	public int[] state; //节点
	public List<State> ChildNodes;//孩子状态节点
	int deepth;  //状态深度
	int stateNo;  //节点编号
	StateNode preNode;  //之前
	boolean ifDeadlock = false;  //是否死锁状态
	
	public StateNode(int[] state, List<State> childNodes, int deepth, int stateNo) {
		this.state = state;
		ChildNodes = childNodes;
		this.deepth = deepth;
		this.stateNo = stateNo;
	}

	/**
    * @param transIndex 发射变迁编号
    * @param markup 待发射状态
    * @return  
    */
	private int[] fire(int transIndex, int[] markup) {
	    int[] newMarking = new int[markup.length];  //发射之后的marking
	    System.out.print("==t" + (transIndex+1) + "==>");  //for debug
	    for (int i = 0; i < markup.length; i++) {
	    	newMarking[i] = markup[i] + model.getPreMatrix().getValue(i, transIndex) - 
	        		model.getPosMatrix().getValue(i, transIndex);	
	    }
	    return newMarking;
	}
	   
	   
	 /**
	  * 得到当前状态下能够发射的变迁
	  * @return boolean[]
	  */
	 private boolean[] getEnabledTrans() {
	      //记录变迁是否能发射结果
	      boolean[] result = new boolean[model.transCount];  
	      
	      for (int i = 0; i < model.transCount ;i++) {
	         for (int j = 0; j < model.placesCount; j++) {
	        	 //【重要】当前某个位置marking若小于后置矩阵对应位置的数字，则不能发射！
	            if ((state[j] >= model.getPosMatrix().getValue(j, i))) {  
	               result[i] = true;
	               break;
	            }
	         }
	      }
	      return result;
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

}
