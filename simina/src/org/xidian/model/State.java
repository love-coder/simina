package org.xidian.model;

/**
 * marking 类
 * @author HanChun
 * @version 1.0 2016-5-16
 */
public class State {
	
	private int[] state;
	
	public State(int[] state) {
		this.state = state;
	}
	
	/**
	 * 这个方法视乎效率很低
	 * @param temState
	 * @return
	 */
	
//	public boolean equals(State temState){
//		int[] teststate = temState.getState();
//	    if (teststate.length != state.length) {
//	    	return false;
//	    }
//	    for (int index = 0; index < state.length; index++) {
//	        if (state[index] != teststate[index]) {
//	           return false;
//	        }
//	    }
//	    return true;
//	}
	   
	   
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
	
	public void setState(int[] state) {
		this.state = state;
	}

}
