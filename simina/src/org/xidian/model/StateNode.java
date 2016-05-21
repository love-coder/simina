package org.xidian.model;

/**
 * State状态节点
 * @author HanChun
 * @version 1.0 2016-5-18
 */
public class StateNode implements Cloneable{
	
	public PetriModel model;
	public int[] state; 
	public int stateNo;  
	public boolean ifDeadlock = false;  //是否死锁状态
	
	/**
	 * @param model PetriModel
	 * @param state 状态
	 * @param childNodes 孩子节点
	 * @param deepth //状态深度
	 * @param stateNo //状态编号
	 */
	public StateNode(PetriModel model, int[] state, int stateNo) {
		this.model = model;
		this.state = state;
		this.stateNo = stateNo;
	}

	public int[] getState() {
		return state;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		 return super.clone();
    }

}
